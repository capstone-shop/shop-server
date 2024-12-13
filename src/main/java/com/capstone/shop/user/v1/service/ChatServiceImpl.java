package com.capstone.shop.user.v1.service;

import com.capstone.shop.core.domain.entity.ChatRoom;
import com.capstone.shop.core.domain.entity.Message;
import com.capstone.shop.core.domain.entity.User;
import com.capstone.shop.core.domain.repository.ChatRoomRepository;
import com.capstone.shop.core.domain.repository.MessageRepository;
import com.capstone.shop.core.domain.repository.UserRepository;
import com.capstone.shop.user.v1.controller.dto.chat.ChatRoomResponse;
import com.capstone.shop.user.v1.controller.dto.chat.MessageResponse;
import com.capstone.shop.user.v1.controller.dto.chat.MyChatRoomResponse;
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
    private final UserRepository userRepository;

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoomResponse createChatRoom(User seller, User buyer) {
        ChatRoom existingRoom = chatRoomRepository.findBySellerAndBuyer(seller, buyer);
        if (existingRoom != null) {
            return ChatRoomResponse.fromEntity(existingRoom);
        }
        // 새로운 방 생성
        ChatRoom chatRoom = ChatRoom.create(seller, buyer);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return ChatRoomResponse.fromEntity(savedChatRoom);
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
    public List<MyChatRoomResponse> getChatRooms(Long currentUserId) {
        User currentUser = userRepository.findById(currentUserId).orElseThrow(()->new IllegalArgumentException("유저를 찾을 수 없음"));
        List<ChatRoom> chatRooms = chatRoomRepository.findBySellerOrBuyer(currentUser, currentUser);

        return chatRooms.stream().map(chatRoom -> {
            // 상대방 사용자 결정
            User otherUser = chatRoom.getSeller().equals(currentUser) ?
                    chatRoom.getBuyer() :
                    chatRoom.getSeller();

            // 마지막 메시지 조회
            Message lastMessage = messageRepository.findTopByChatRoomOrderByCreatedAtDesc(chatRoom);

            // DTO 생성
            return MyChatRoomResponse.builder()
                    .chatRoomId(chatRoom.getId())
                    .otherUserName(otherUser.getName())
                    .otherUserProfileImage(otherUser.getProfileImages())
                    .lastMessage(lastMessage != null ? lastMessage.getContent() : "")
                    .build();
        }).collect(Collectors.toList());
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
