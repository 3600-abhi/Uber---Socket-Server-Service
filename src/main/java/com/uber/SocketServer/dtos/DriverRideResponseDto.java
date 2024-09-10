package com.uber.SocketServer.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DriverRideResponseDto {
    private Long driverId;
    private Long bookingId;
}
