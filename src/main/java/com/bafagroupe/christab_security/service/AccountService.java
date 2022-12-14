package com.bafagroupe.christab_security.service;

import com.bafagroupe.christab_security.dto.*;
import com.bafagroupe.christab_security.entities.AppRole;
import com.bafagroupe.christab_security.entities.AppUser;
import com.bafagroupe.christab_security.entities.AppUserRoles;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collection;
import java.util.List;

public interface AccountService {

    // Fonctions de base
    public AppUserResponseDto saveUser(AppUserRequestDto appUserRequestDto);
    public String  confirmUserAccount(String ctoken) throws Exception; // , final RedirectAttributes redirectAttributes) throws Exception;
    public Boolean checkUserByEmail(String email);
    public Boolean checkUserByEmailAndUtilisateur(String email);
    public AppUser loadUserByEmailF(String email);
    public AppUserResponseDto loadUserByEmail(String email);
    public AppRoleResponseDto saveRole(AppRoleRequestDto appRoleRequestDto);
    public void addRoleToUser(UserCustomRequestDto userCustomRequestDto);
    public boolean recupPassword(String email);
    public boolean verifCode(String email, int code);
    public AppUserResponseDto reInitPassword(UserFormRequestDto userFormRequestDto);

    // Ajout de fonctions supplémentaires
    public AppUserResponseDto activatedUser(AppUserRequestDto appUserRequestDto);
    public AppUserResponseDto reactivatedUser(AppUserRequestDto appUserRequestDto);
    public AppUserResponseDto deactivatedUser(AppUserRequestDto appUserRequestDto);
    public AppUserResponseDto updateUserPassword(UserRequestDto userRequestDto);
    public AppUserResponseDto updateUser(AppUserRequestDto appUserRequestDto);
    public AppRoleResponseDto updateRole(AppRoleRequestDto appRoleRequestDto);
    public void deleteUser(AppUserRequestDto appUserRequestDto);
    public void disableUser(AppUserRequestDto appUserRequestDto);
    public void deleteUserRole(AppUserRequestDto appUserRequestDto);
    public void deleteRole(Long id);
    public AppRoleResponseDto loadRole(long id);
    public AppUserResponseDto loadUser(long id);
    public List<AppRoleResponseDto> loadRoles();
    public List<AppUserResponseDto> loadUsers();

    // public AppUserRole saveRoleUser(AppUserRole userRole);
    // public AppUserRole updateRoleUser(Long idUser, Long idRole);
    // public void deleteRoleUser(Long idUser);
    // public AppUserRole loadRoleUser(long idR, long idU);
    // public List<AppUserRole> loadRoleUsers();
}
