package com.capstone.shop.user.v1.controller.dto.chat;

import com.capstone.shop.core.domain.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ChatRoomResponse {
    private Long id;
    private Long sellerId;
    private Long buyerId;
    private LocalDateTime createdAt;

    public static ChatRoomResponse fromEntity(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .sellerId(chatRoom.getSeller() != null ? chatRoom.getSeller().getId() : null)
                .buyerId(chatRoom.getBuyer() != null ? chatRoom.getBuyer().getId() : null)
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}
