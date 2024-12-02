package com.capstone.shop.user.v1.service;

import com.capstone.shop.core.domain.entity.Message;
import com.capstone.shop.user.v1.controller.dto.chat.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatService {
    // 채팅 히스토리 조회
    List<MessageResponse> getChatHistory(Long roomId);

    // 메시지 저장
    Message saveMessage(Message message);

    // 메시지 읽음 상태로 변경
    Message markMessageAsRead(Long messageId);
}
