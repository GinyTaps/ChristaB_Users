package com.bafagroupe.christab_security.mappers;

import com.bafagroupe.christab_security.dto.AppUserRequestDto;
import com.bafagroupe.christab_security.dto.AppUserResponseDto;
import com.bafagroupe.christab_security.dto.UserCustomRequestDto;
import com.bafagroupe.christab_security.entities.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AppUserMapper {
    AppUserResponseDto appuserToAppUserDto(AppUser appUser);
    AppUserRequestDto appUserToAppserRDto(AppUser appUser);
    AppUser appUserRequestDtoToAppUser(AppUserRequestDto appUserRequestDto);
    AppUser appUserResponseDtoToAppUser(AppUserResponseDto appUserResponseDto);
    UserCustomRequestDto userRequestDtoToUserCustom(AppUserRequestDto appUserRequestDto);
}
