package com.lemonSoju.blog.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MemberLoginRequestDto {

    @NotEmpty(message = "회원 id는 필수입니다.")
    private String uid;
    @NotEmpty(message = "회원 비밀번호는 필수입니다.")
    private String pwd;
}