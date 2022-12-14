package com.bafagroupe.christab_security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AppRoleRequestDto {
    private Long idAppRole;
    private String rolename;
}
