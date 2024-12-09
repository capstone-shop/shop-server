package com.capstone.shop.core.domain.repository;

import com.capstone.shop.core.domain.entity.ChatRoom;
import com.capstone.shop.core.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findBySellerAndBuyer(User seller, User buyer);

}
