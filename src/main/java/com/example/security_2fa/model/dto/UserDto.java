package com.example.security_2fa.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class UserDto {
    private Long idx;

    private String username;

    private String password;

    private String email;

    private String phone;

    private LocalDateTime lockedDate;

    @Builder
    public UserDto(Long idx, String username, String password, String email, String phone, LocalDateTime lockedDate) {
        this.idx = idx;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.lockedDate = lockedDate;
    }
}
