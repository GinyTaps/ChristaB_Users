package com.bafagroupe.christab_security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class AppUserRequestDto {
    private String email;
    private String password;
    private String passwordConfirmed;
    private String emailOld;
    private boolean validated;
    private boolean activated;
}
