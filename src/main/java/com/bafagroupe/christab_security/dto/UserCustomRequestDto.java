package com.bafagroupe.christab_security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class UserCustomRequestDto {
    public String email;
    public String rolename;
}
