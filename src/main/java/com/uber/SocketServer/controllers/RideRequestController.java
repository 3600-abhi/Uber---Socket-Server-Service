package com.uber.SocketServer.controllers;

import com.google.gson.Gson;
import com.uber.SocketServer.api.BookingServiceApi;
import com.uber.SocketServer.constant.AppConstant;
import com.uber.SocketServer.constant.BookingStatus;
import com.uber.SocketServer.dtos.*;
import com.uber.SocketServer.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/socket/")
public class RideRequestController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private BookingServiceApi bookingServiceApi;

    @PostMapping(AppConstant.RAISE_RIDE_REQUEST)
    public void raiseRideRequest(@RequestBody RideRequestDto rideRequestDto) {
        System.out.println("RideRequestDt ::: " + new Gson().toJson(rideRequestDto));
        sendRideRequestToDrivers(rideRequestDto);
    }


    public void sendRideRequestToDrivers(RideRequestDto rideRequestDto) {
        if (rideRequestDto.getDriverIdList() != null) {
            for (Long driverId : rideRequestDto.getDriverIdList()) {
                System.out.println("driverId = " + driverId);
                String destination = "/topic/rideRequest/" + String.valueOf(driverId);

                RideResponseDto rideResponseDto = RideResponseDto.builder()
                                                                 .bookingId(rideRequestDto.getBookingId())
                                                                 .passengerId(rideRequestDto.getPassengerId())
                                                                 .driverId(driverId)
                                                                 .startLocation(rideRequestDto.getStartLocation())
                                                                 .endLocation(rideRequestDto.getEndLocation())
                                                                 .build();

                System.out.println("rideResponseDto :: " + new Gson().toJson(rideResponseDto));

                simpMessagingTemplate.convertAndSend(destination, rideResponseDto);
            }
        }
    }

    @MessageMapping("/rideResponse")
    public synchronized void rideResponseHandler(DriverRideResponseDto driverRideResponseDto) {
        System.out.println("Ride accepted by driverId : " + driverRideResponseDto.getDriverId());

        UpdateBookingDto updateBookingDto = UpdateBookingDto.builder()
                                                            .bookingId(driverRideResponseDto.getBookingId())
                                                            .driverId(driverRideResponseDto.getDriverId())
                                                            .bookingStatus(BookingStatus.SCHEDULED)
                                                            .build();

        Call<UpdateBookingDto> callUpdateBooking = bookingServiceApi.updateBooking(updateBookingDto);

        try {
            Response<UpdateBookingDto> resUpdateBooking = callUpdateBooking.execute();

            if (resUpdateBooking.isSuccessful()) {
                UpdateBookingDto updatedBooking = resUpdateBooking.body();
                System.out.println(updatedBooking.getBookingStatus());

                RideAcceptanceRequestDto rideAcceptanceRequestDto = RideAcceptanceRequestDto.builder()
                                                                                            .passengerId(driverRideResponseDto.getPassengerId())
                                                                                            .bookingId(driverRideResponseDto.getBookingId())
                                                                                            .driverId(driverRideResponseDto.getDriverId())
                                                                                            .build();

                sendRideAcceptanceToPassenger(rideAcceptanceRequestDto);
            }
        } catch (IOException e) {
            throw new AppException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendRideAcceptanceToPassenger(RideAcceptanceRequestDto rideAcceptanceRequestDto) {
        System.out.println("rideAcceptanceRequestDto :: " + new Gson().toJson(rideAcceptanceRequestDto));

        System.out.println("Notifying Ride Acceptance to passengerId :: " + rideAcceptanceRequestDto.getPassengerId());
        String destination = "/topic/sendRideRequestAcceptanceToPassenger/" + String.valueOf(rideAcceptanceRequestDto.getPassengerId());
        simpMessagingTemplate.convertAndSend(destination, rideAcceptanceRequestDto);
    }
}