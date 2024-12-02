//package com.capstone.shop.user.v1.service;
//
//import com.capstone.shop.core.domain.entity.Message;
//import com.capstone.shop.core.domain.repository.MessageRepository;
//import com.capstone.shop.user.v1.controller.dto.chat.MessageResponse;
//import com.capstone.shop.user.v1.service.ChatService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ChatServiceImpl implements ChatService {
//
//    private final MessageRepository messageRepository;
//
//    // 채팅 히스토리 조회
//    @Override
//    public List<MessageResponse> getChatHistory(Long roomId) {
//        List<Message> messages= messageRepository.findByIdOrderByCreatedAtAsc(roomId);
//        return messages.stream()
//                .map(MessageResponse::fromEntity).collect(Collectors.toList());
//    }
//
//    // 메시지 저장
//    @Override
//    public Message saveMessage(Message message, Long roomId) {
//        message.setRoomId(roomId);
//        return messageRepository.save(message);
//    }
//
//    // 메시지 읽음 상태로 변경
//    @Override
//    public Message markMessageAsRead(Long messageId) {
//        Message message = messageRepository.findById(messageId)
//                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
//        message.markAsRead();  // 메시지의 읽음 상태를 true로 설정
//        return messageRepository.save(message);
//    }
//}
