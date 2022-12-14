package com.bafagroupe.christab_security.dto;

import lombok.Data;

@Data
public class UserFormRequestDto {
    public String email;
    public String password;
    public String passwordConfirmed;
    public int code;
    // Collection<String> roles;
}
