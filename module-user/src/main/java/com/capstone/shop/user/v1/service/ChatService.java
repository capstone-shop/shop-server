package com.capstone.shop.user.v1.service;

import com.capstone.shop.core.domain.entity.User;
import com.capstone.shop.user.v1.controller.dto.chat.ChatRoomResponse;
import com.capstone.shop.user.v1.controller.dto.chat.MessageResponse;
import com.capstone.shop.user.v1.controller.dto.chat.MyChatRoomResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ChatService {

    ChatRoomResponse createChatRoom(User seller, User buyer);
    // 채팅 히스토리 조회
    List<MessageResponse> getChatHistory(Long roomId);
    List<MyChatRoomResponse> getChatRooms(Long currentUserId);
    // 메시지 저장
    MessageResponse saveMessage(MessageResponse message, Long roomId);

    // 메시지 읽음 상태로 변경
    MessageResponse markMessageAsRead(Long messageId);
//
//    List<ChatR>
    boolean isUserInRoom(Long userId, Long roomId);
}
