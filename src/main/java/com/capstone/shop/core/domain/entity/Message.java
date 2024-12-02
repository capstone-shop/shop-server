package com.capstone.shop.core.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;


    private String content;

    private boolean isRead; // 메시지 읽음 상태

    public void markAsRead() {
        this.isRead = true;
    }

//    public void setChatRoom(Long roomId){
//        this.chatRoom = new ChatRoom(roomId);
//    }

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
