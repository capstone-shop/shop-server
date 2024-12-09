package com.capstone.shop.user.v1.controller.dto.chat;

import com.capstone.shop.core.domain.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class MessageResponse {
    private Long id;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt; // created_at -> createdAt으로 변경
    private Long senderId;          // 보낸 사람 ID 추가
    private Long chatRoomId;        // 채팅방 ID 추가

    // Entity를 DTO로 변환하는 정적 메서드
    public static MessageResponse fromEntity(Message message) {
        return MessageResponse.builder()
                .id(message.getId())
                .content(message.getContent())
                .isRead(message.isRead())
                .createdAt(message.getCreatedAt())
                .senderId(message.getSender() != null ? message.getSender().getId() : null) // null 처리
                .chatRoomId(message.getChatRoom() != null ? message.getChatRoom().getId() : null) // null 처리
                .build();
    }
}
