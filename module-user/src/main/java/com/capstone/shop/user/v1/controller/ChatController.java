package com.capstone.shop.user.v1.controller;

import com.capstone.shop.core.domain.entity.User;
import com.capstone.shop.core.domain.repository.UserRepository;
import com.capstone.shop.core.security.CurrentUser;
import com.capstone.shop.core.security.UserPrincipal;
import com.capstone.shop.user.v1.controller.dto.chat.ChatRoomResponse;
import com.capstone.shop.user.v1.controller.dto.chat.MessageResponse;
import com.capstone.shop.user.v1.controller.dto.chat.MyChatRoomResponse;
import com.capstone.shop.user.v1.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;
    private final UserRepository userRepository;

    @PostMapping("/create")
    public ResponseEntity<ChatRoomResponse> createChatRoom(@RequestParam Long sellerId, @CurrentUser UserPrincipal userPrincipal) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("판매자를 찾을 수 없습니다."));
        User buyer = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new IllegalArgumentException("구매자를 찾을 수 없습니다."));

        ChatRoomResponse response = chatService.createChatRoom(seller, buyer);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-chat-rooms")
    public ResponseEntity<List<MyChatRoomResponse>> getChatRooms(@CurrentUser UserPrincipal userPrincipal) {
        List<MyChatRoomResponse> chatRooms = chatService.getChatRooms(userPrincipal.getId());
        return ResponseEntity.ok(chatRooms);
    }
    @GetMapping("/{roomId}")
    public List<MessageResponse> getChatHistory(@PathVariable Long roomId, @CurrentUser UserPrincipal userPrincipal) {
        if (!chatService.isUserInRoom(userPrincipal.getId(), roomId)) {
            throw new IllegalArgumentException("이 채팅방에 접근할 권한이 없습니다.");
        }
        return chatService.getChatHistory(roomId);
    }
    //채팅보내기
    // 채팅방 입장 후 메시지 보내기
    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/messages/{roomId}")
    public MessageResponse sendMessage(@DestinationVariable Long roomId, MessageResponse message, @CurrentUser UserPrincipal userPrincipal) {
        if (!chatService.isUserInRoom(userPrincipal.getId(), roomId)) {
            throw new IllegalArgumentException("이 채팅방에 접근할 권한이 없습니다.");
        }
        return chatService.saveMessage(message, roomId);
    }
    @MessageMapping("/markAsRead")
    @SendTo("/topic/messages/{roomId}")
    public MessageResponse markAsRead(Long messageId) {
        return chatService.markMessageAsRead(messageId);
    }



}