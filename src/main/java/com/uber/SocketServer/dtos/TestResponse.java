package com.uber.SocketServer.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TestResponse {
    private String data;
}
