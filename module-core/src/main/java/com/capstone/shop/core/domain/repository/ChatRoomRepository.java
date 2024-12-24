package com.capstone.shop.core.domain.repository;

import com.capstone.shop.core.domain.entity.ChatRoom;
import com.capstone.shop.core.domain.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    ChatRoom findBySellerAndBuyer(User seller, User buyer);

    @Query("SELECT c FROM ChatRoom c WHERE c.seller = :user OR c.buyer = :user")
    List<ChatRoom> findChatRoomsByUser(@Param("user") User user);

    List<ChatRoom> findBySellerOrBuyer(User seller, User buyer);

}
