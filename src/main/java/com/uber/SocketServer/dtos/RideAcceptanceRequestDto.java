package com.uber.SocketServer.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RideAcceptanceRequestDto {
    private Long bookingId;
    private Long passengerId;
    private Long driverId;
}
