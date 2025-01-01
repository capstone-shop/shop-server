package com.capstone.shop.core.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER_REFRESH_TOKEN")
public class UserRefreshToken {
    @JsonIgnore
    @Id
    @Column(name = "REFRESH_TOKEN_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenSeq;

    @OneToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "id", nullable = false)
    private User user;

//    @Column(name = "USER_ID", length = 64, unique = true)
//    @NotNull
//    @Size(max = 64)
//    private Long userId;

    @Column(name = "REFRESH_TOKEN", length = 256)
    @NotNull
    @Size(max = 256)
    private String refreshToken;

    public UserRefreshToken(
            @NotNull @Size(max = 64) User user,
            @NotNull @Size(max = 256) String refreshToken
    ) {
        this.user = user;
        this.refreshToken = refreshToken;
    }
}