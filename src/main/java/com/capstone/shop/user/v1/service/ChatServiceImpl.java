package com.capstone.shop.user.v1.service;

import com.capstone.shop.core.domain.entity.ChatRoom;
import com.capstone.shop.core.domain.entity.Message;
import com.capstone.shop.core.domain.entity.User;
import com.capstone.shop.core.domain.repository.ChatRoomRepository;
import com.capstone.shop.core.domain.repository.MessageRepository;
import com.capstone.shop.user.v1.controller.dto.chat.MessageResponse;
import com.capstone.shop.user.v1.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final MessageRepository messageRepository;

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom createChatRoom(User seller, User buyer) {
        ChatRoom existingRoom = chatRoomRepository.findBySellerAndBuyer(seller, buyer);
        if (existingRoom != null) {
            return existingRoom;
        }

        // 새로운 방 생성
        ChatRoom chatRoom = ChatRoom.create(seller, buyer);
        return chatRoomRepository.save(chatRoom);
    }

    // 채팅 히스토리 조회
    @Override
    public List<MessageResponse> getChatHistory(Long roomId) {
        List<Message> messages= messageRepository.findByIdOrderByCreatedAtAsc(roomId);
        return messages.stream()
                .map(MessageResponse::fromEntity).collect(Collectors.toList());
    }

    // 메시지 저장
    @Override
    public MessageResponse saveMessage(Message message, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(()->new IllegalArgumentException("채팅방이 없음"));

        Message savedMessage = messageRepository.save(
                Message.builder()
                        .chatRoom(chatRoom)
                        .sender(message.getSender())
                        .content(message.getContent())
                        .isRead(false)
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        return MessageResponse.fromEntity(savedMessage);
    }

    // 메시지 읽음 상태로 변경
    @Override
    public MessageResponse markMessageAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message 찾을 수 없음"));
        message.markAsRead();
        Message updatedMessage = messageRepository.save(message);

        return MessageResponse.fromEntity(updatedMessage);
    }
}
