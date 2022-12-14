package com.bafagroupe.christab_security.mappers;

import com.bafagroupe.christab_security.dto.AppRoleRequestDto;
import com.bafagroupe.christab_security.dto.AppRoleResponseDto;
import com.bafagroupe.christab_security.dto.AppUserRequestDto;
import com.bafagroupe.christab_security.entities.AppRole;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppRoleMapper {
    AppRoleResponseDto appRoleToAppRoleDto(AppRole appRole);
    AppRole appRoleRequestDtoToAppRole(AppRoleRequestDto appRoleRequestDto);
    AppRoleRequestDto appRoleToAppRoleRDto(AppRole appRole);
    AppRoleResponseDto appRoleResponseToAppRoleRequest(AppRoleRequestDto appRoleRequestDto);
}
