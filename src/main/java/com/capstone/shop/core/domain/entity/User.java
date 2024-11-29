package com.capstone.shop.core.domain.entity;

import com.capstone.shop.core.domain.enums.AuthProvider;
import com.capstone.shop.core.domain.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "user")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 12)
    @NotNull
    private String name;

    @Column(name = "email", length = 100, unique = true)
    @NotNull
    @Size(max = 100)
    private String email;

    @JsonIgnore
    @Column(name = "password", length = 128)
    @NotNull
    private String password;

    @Column(name = "address", length = 128)
    private String address;

    @Column(name = "phone_number", length = 15)
    @Pattern(regexp = "^[0-9]{7,15}$", message = "전화번호는 7자 이상 15자 이하의 숫자만 포함해야 합니다.")
    private String phoneNumber;

    @Column(name = "dealing_count")
    private int dealingCount = 0;

    @Column(name = "reputation")
    private int reputation = 30;

    @Column(name = "role", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @Column(name = "auth_provider", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private AuthProvider authProvider;

    @Column(name = "profile_Images", length = 512)
    private String profileImages;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserRefreshToken userRefreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Wish> wishes;

    public User(
            @NotNull @Size(max = 12) String name,
            @NotNull @Size(max = 100) String email,
            @NotNull AuthProvider authProvider,
            @NotNull Role role
    ) {
        this.name = name;
        this.password = "NO_PASS"; //소셜로그인은 패스워드가 없음
        this.email = email != null ? email : "NO_EMAIL";
        this.authProvider = authProvider;
        this.role = role;
    }
}
