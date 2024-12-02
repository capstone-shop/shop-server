package com.capstone.shop.user.v1.controller.dto.chat;

import com.capstone.shop.core.domain.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MessageResponse{
    private Long id;
    private String content;
    private boolean isRead;
    private LocalDateTime created_at;

    public static MessageResponse fromEntity(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getContent(),
                message.isRead(),
                message.getCreatedAt()
        );
    }
}
