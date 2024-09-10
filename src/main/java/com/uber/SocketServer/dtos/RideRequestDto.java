package com.uber.SocketServer.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RideRequestDto {
    private Long bookingId;
    private Long passengerId;
    private ExactLocationDto startLocation;
    private ExactLocationDto endLocation;
    private List<Long> driverIdList;
}
