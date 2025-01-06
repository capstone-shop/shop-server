package com.capstone.shop.user.v1.controller.dto.chat;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyChatRoomResponse {
    private Long chatRoomId;
    private String otherUserName;
    private String otherUserProfileImage;
    private String lastMessage;
    private LocalDateTime lastMessageSendTime;
}
