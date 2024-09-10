package com.uber.SocketServer.controllers;

import com.uber.SocketServer.api.BookingServiceApi;
import com.uber.SocketServer.constant.AppConstant;
import com.uber.SocketServer.constant.BookingStatus;
import com.uber.SocketServer.dtos.DriverRideResponseDto;
import com.uber.SocketServer.dtos.RideRequestDto;
import com.uber.SocketServer.dtos.UpdateBookingDto;
import org.springframework.beans.factory.annotation.Autowired;
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
        sendRideRequestToDrivers(rideRequestDto);
    }


    public void sendRideRequestToDrivers(RideRequestDto rideRequestDto) {
        if (rideRequestDto.getDriverIdList() != null) {
            for (Long driverId : rideRequestDto.getDriverIdList()) {
                System.out.println("driverId = " + driverId);
                String destination = "/topic/rideRequest/" + String.valueOf(driverId);
                simpMessagingTemplate.convertAndSend(destination, rideRequestDto);
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
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}