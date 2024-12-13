package com.capstone.shop.user.v1.controller.dto.chat;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyChatRoomResponse {
    private Long chatRoomId;
    private String otherUserName;
    private String otherUserProfileImage;
    private String lastMessage;
}
