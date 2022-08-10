package com.example.security_2fa.model.entity;

import com.example.security_2fa.model.enums.RoleAuthority;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String username;

    private String password;

    private String email;

    private String phone;

    @Column(columnDefinition = "integer default 0")
    private Integer passwordFailCount;

    @Column(columnDefinition = "boolean default false")
    private Boolean isLocked;

    private LocalDateTime lockedDate;

    private String otpKey;

    // 스프링시큐리티에서 권한은 EAGER 여야한다.
    @ElementCollection(fetch = FetchType.EAGER, targetClass = RoleAuthority.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_authorities")
    private Set<RoleAuthority> authorities = new HashSet<>();

    @Builder
    public User(Long idx, String username, String password, String email, String phone, Integer passwordFailCount, Boolean isLocked,
                 LocalDateTime lockedDate, String otpKey, Set<RoleAuthority> authorities) {
        this.idx = idx;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.passwordFailCount = passwordFailCount;
        this.isLocked = isLocked;
        this.lockedDate = lockedDate;
        this.otpKey = otpKey;
        this.authorities = authorities;
    }

    public void encodePassword(String password) {
        this.password = password;
    }

    public void changeOtpSecretKey(String otpKey) {
        this.otpKey = otpKey;
    }
}
