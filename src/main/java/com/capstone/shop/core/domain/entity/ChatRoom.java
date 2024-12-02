package com.capstone.shop.core.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom { //채팅방을 찾는건 사용자들의 이메일 페어(pair)로 찾음.
                        //ex: sadf@asdf.asdfasdf@sadf.asdf 이런식으로 두 이메일을 조합해서 식별
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 채팅방 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user1_id")
    private User user1; // 사용자 1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user2_id")
    private User user2; // 사용자 2

    @OneToMany(mappedBy = "chatRoom")
    private List<Message> messages = new ArrayList<>();


    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
