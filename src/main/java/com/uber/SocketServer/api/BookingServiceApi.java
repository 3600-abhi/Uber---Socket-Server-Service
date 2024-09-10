package com.uber.SocketServer.api;

import com.uber.SocketServer.dtos.UpdateBookingDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

public interface BookingServiceApi {
    @POST("/api/v1/booking/updateBooking")
    Call<UpdateBookingDto> updateBooking(@Body UpdateBookingDto updateBookingDto);
}
