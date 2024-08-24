package com.uber.SocketServer.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivateChatDto {
    private String senderUid;
    private String receiverUid;
    private String messageContent;
}
