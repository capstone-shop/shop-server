package com.capstone.shop.user.v1.controller;
import com.capstone.shop.core.domain.entity.ChatRoom;
import com.capstone.shop.core.domain.entity.Message;
import com.capstone.shop.core.domain.entity.User;
import com.capstone.shop.core.domain.repository.UserRepository;
import com.capstone.shop.user.v1.controller.dto.chat.ChatRoomResponse;
import com.capstone.shop.user.v1.controller.dto.chat.MessageResponse;
import com.capstone.shop.user.v1.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<ChatRoomResponse> createChatRoom(@RequestParam Long sellerId, @RequestParam Long buyerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("판매자를 찾을 수 없습니다."));
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("구매자를 찾을 수 없습니다."));

        ChatRoom chatRoom = chatService.createChatRoom(seller, buyer);

        ChatRoomResponse response = ChatRoomResponse.fromEntity(chatRoom);
        return ResponseEntity.ok(response);
    }

    //채팅 히스토리 조회
    @GetMapping("/{roomId}/messages")
    public List<MessageResponse> getChatHistory(@PathVariable Long roomId) {
        return chatService.getChatHistory(roomId);
    }
    //채팅보내기
    // 채팅방 입장 후 메시지 보내기
    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/messages/{roomId}")
    public MessageResponse sendMessage(@DestinationVariable Long roomId, Message message) {
        return chatService.saveMessage(message, roomId);
    }
    @MessageMapping("/markAsRead")
    @SendTo("/topic/messages/{roomId}")
    public MessageResponse markAsRead(Long messageId) {
        return chatService.markMessageAsRead(messageId);
    }

}
