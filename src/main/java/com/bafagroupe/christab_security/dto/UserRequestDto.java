package com.bafagroupe.christab_security.dto;

import lombok.Data;

@Data
public class UserRequestDto {
    public long idUser;
    public String email;
    public String emailOld;
    public String password;
    public String passwordConfirmed;
    public String oldPassword;
}
