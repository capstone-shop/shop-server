//package com.capstone.shop.user.v1.controller;
//import com.capstone.shop.core.domain.entity.Message;
//import com.capstone.shop.user.v1.controller.dto.chat.MessageResponse;
//import com.capstone.shop.user.v1.service.ChatService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/chat")
//public class ChatController {
//    private final ChatService chatService;
//
//    //채팅 히스토리 조회
//    @GetMapping("/{roomId}/messages")
//    public List<MessageResponse> getChatHistory(@PathVariable Long roomId) {
//        return chatService.getChatHistory(roomId);
//    }
//    //채팅보내기
//    // 채팅방 입장 후 메시지 보내기
//    @MessageMapping("/sendMessage/{roomId}")
//    @SendTo("/topic/messages/{roomId}")
//    public Message sendMessage(@PathVariable Long roomId, Message message) {
//        return chatService.saveMessage(message, roomId); // 채팅방에 메시지 저장 후 반환
//    }
//
//    // 채팅방에 메시지 읽음 처리
//    @MessageMapping("/markAsRead/{roomId}")
//    @SendTo("/topic/messages/{roomId}")
//    public Message markAsRead(@PathVariable Long roomId, Long messageId) {
//        return chatService.markMessageAsRead(messageId, roomId); // 읽음 상태로 업데이트 후 메시지 반환
//    }
//}
