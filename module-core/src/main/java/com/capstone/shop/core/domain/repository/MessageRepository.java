package com.capstone.shop.core.domain.repository;

import com.capstone.shop.core.domain.entity.ChatRoom;
import com.capstone.shop.core.domain.entity.Message;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatRoomIdOrderByCreatedAtAsc(Long roomId);
    List<Message> findByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);

    Message findTopByChatRoomOrderByCreatedAtDesc(ChatRoom chatRoom);

}
