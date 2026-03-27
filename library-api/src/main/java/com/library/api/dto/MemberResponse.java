package com.library.api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MemberResponse {
    private Integer id;
    private String name;
    private String email;
    private String phone;

    public MemberResponse(Integer id, String name, String email, String phone){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
