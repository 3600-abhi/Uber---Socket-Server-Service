package com.uber.SocketServer.dtos;

import com.uber.SocketServer.constant.BookingStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBookingDto {
    private Long bookingId;
    private Long driverId;
    private BookingStatus bookingStatus;
}
