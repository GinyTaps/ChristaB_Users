package com.bafagroupe.christab_security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AppUserResponseDto {
    private Long idAppUser;
    private String email;
    private boolean validated;
    private boolean activated;
}
