package com.uber.SocketServer.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideResponseDto {
    private Long bookingId;
    private Long passengerId;
    private Long driverId;
    private ExactLocationDto startLocation;
    private ExactLocationDto endLocation;
}
