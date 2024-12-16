package com.capstone.shop.core.domain.repository;

import com.capstone.shop.core.domain.entity.ChatRoom;
import com.capstone.shop.core.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatRoomIdOrderByCreatedAtAsc(Long roomId);

    Message findTopByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);

}
