package com.uber.SocketServer.controllers;

import com.uber.SocketServer.dtos.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/ping") // map incoming messages to a specific method.
    @SendTo("/topic/ping")
    // used to specify the destination where the method's return value should be sent after processing the incoming message.
    public String pingCheck(TestRequest message) {
        System.out.println("Received message from client : " + message.getData());
        return "Hello Client";
    }


//    @Scheduled(fixedDelay = 2000)
//    public void sendPeriodicMessage() {
//        System.out.println("Inside scheduled fn");
//        simpMessagingTemplate.convertAndSend("/topic/scheduled", "periodic message from server : " + System.currentTimeMillis());
//    }

    @MessageMapping("/chat")
    @SendTo("/topic/message")
    public ChatResponse chatMessage(ChatRequest chatRequest) {
        return ChatResponse.builder()
                           .name(chatRequest.getName())
                           .message(chatRequest.getMessage())
                           .timestamp("" + System.currentTimeMillis())
                           .build();
    }

    @MessageMapping("/privateChat")
    public void privateChat(PrivateChatDto privateChatDto) {
        String destination = "/topic/privateChat/" + privateChatDto.getReceiverUid();
        simpMessagingTemplate.convertAndSend(destination, privateChatDto);
    }
}
