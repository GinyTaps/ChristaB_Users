package com.bafagroupe.christab_security.service;

import com.bafagroupe.christab_security.dao.AppRoleRepository;
import com.bafagroupe.christab_security.dao.AppUserRepository;
import com.bafagroupe.christab_security.dao.ConfirmationTokenRepository;
import com.bafagroupe.christab_security.dto.*;
import com.bafagroupe.christab_security.entities.AppRole;
import com.bafagroupe.christab_security.entities.AppUser;
import com.bafagroupe.christab_security.entities.Utilisateur;
import com.bafagroupe.christab_security.mappers.AppRoleMapper;
import com.bafagroupe.christab_security.mappers.AppUserMapper;
import com.bafagroupe.christab_security.openFeign.UtilisateurRestClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AccountServiceITest {
    /*@Mock
    private AppUserRepository userRepository;
    @Mock
    private AppRoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private ConfirmationTokenRepository tokenRepository;
    @Mock
    private AppUserMapper userMapper;
    @Mock
    private AppRoleMapper roleMapper;
    @Mock
    private UtilisateurRestClient restClient;
    @Captor
    private ArgumentCaptor<AppUser> userArgumentCaptor;
    @Captor
    private ArgumentCaptor<AppRole> roleArgumentCaptor;

    *//*@BeforeEach
     public void setUp(){
        AccountServiceI accountServiceI = new AccountServiceI(userRepository, roleRepository, passwordEncoder, tokenRepository, userMapper, roleMapper, restClient);
    }*//*

    @Test
    void saveUser() {
        AccountServiceI accountServiceI = new AccountServiceI(userRepository, roleRepository, passwordEncoder, tokenRepository, userMapper, roleMapper, restClient);
        AppRole role = new AppRole((long) 1, "ADMIN");
        AppRole role1 = new AppRole((long) 2, "USER");
        // Mockito.when(roleRepository.findByRolename("ADMIN")).thenReturn(role);

        List<AppRole> roles = new ArrayList<>();
        Collections.addAll(roles, role, role1);

        AppUser user = new AppUser((long) 1, "wendyataps@gmail.com", "1234", 0, null, false, false, roles);
        AppUserRequestDto userRequestDto = new AppUserRequestDto("wendyataps@gmail.com", "1234", "1234", "", false, false);
        accountServiceI.saveUser(userRequestDto);

        UserCustomRequestDto customRequestDto = new UserCustomRequestDto("wendyataps@gmail.com", "USER");
        Mockito.when(userMapper.userRequestDtoToUserCustom(userRequestDto)).thenReturn(customRequestDto);
        accountServiceI.addRoleToUser(customRequestDto);


        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
        Assertions.assertThat(userArgumentCaptor.getValue().getEmail()).isEqualTo("wendyataps@gmail.com");
        Assertions.assertThat(userArgumentCaptor.getValue().getPassword()).isEqualTo("1234");
    }

    @Test
    void activatedUser() {
        AccountServiceI accountServiceI = new AccountServiceI(userRepository, roleRepository, passwordEncoder, tokenRepository, userMapper, roleMapper, restClient);
        AppRole role = new AppRole(1L, "ADMIN");
        AppRole role1 = new AppRole(2L, "USER");
        List<AppRole> roles = new ArrayList<>();
        Collections.addAll(roles, role, role1);

        AppUser user = new AppUser(1L, "wendyataps@gmail.com", "1234", 0, null, false, false, roles);
        Mockito.when(userRepository.findUByEmail(user.getEmail())).thenReturn(user);
        AppUserRequestDto userRequestDto = new AppUserRequestDto("wendyataps@gmail.com", "1234", "1234", "", false, true);
        AppUserResponseDto userResponseDto = new AppUserResponseDto("wendyataps@gmail.com", "1234", false, true);

        Mockito.when(userRepository.findUByEmail("wendyataps@gmail.com")).thenReturn(user);
        Mockito.when(userMapper.appuserToAppUserDto(Mockito.any(AppUser.class))).thenReturn(userResponseDto);

        AppUserResponseDto responseDto = accountServiceI.activatedUser(userRequestDto);

        Assertions.assertThat(responseDto.getEmail()).isEqualTo("wendyataps@gmail.com");
        Assertions.assertThat(responseDto.isActivated()).isEqualTo(true);

    }

    @Test
    void checkUserByEmail() {
        AccountServiceI accountServiceI = new AccountServiceI(userRepository, roleRepository, passwordEncoder, tokenRepository, userMapper, roleMapper, restClient);
        AppRole role = new AppRole(1L, "ADMIN");
        AppRole role1 = new AppRole(2L, "USER");
        List<AppRole> roles = new ArrayList<>();
        Collections.addAll(roles, role, role1);

        AppUser user = new AppUser( 1L, "wendyataps@gmail.com", "1234", 0, null, false, false, roles);
        Mockito.when(userRepository.findUByEmail("wendyataps@gmail.com")).thenReturn(user);
        Utilisateur utilisateur = new Utilisateur(1, 1, "wendyataps@gmail.com", null, null, 0L, null, null,null,null,null,null,null,null, 0L );
        Mockito.when(restClient.checkUtilisateurByEmail(utilisateur.getEmail())).thenReturn(true);

        Boolean response = accountServiceI.checkUserByEmail(user.getEmail());
        Assertions.assertThat(response).isEqualTo(true); // isTrue();
    }

    @Test
    void confirmUserAccount() {
    }

    @Test
    void loadUserByEmail() {
        AccountServiceI accountServiceI = new AccountServiceI(userRepository, roleRepository, passwordEncoder, tokenRepository, userMapper, roleMapper, restClient);
        AppRole role = new AppRole(1L, "ADMIN");
        AppRole role1 = new AppRole(2L, "USER");
        List<AppRole> roles = new ArrayList<>();
        Collections.addAll(roles, role, role1);

        AppUser user = new AppUser( 1L, "wendyataps@gmail.com", "1234", 0, null, false, false, roles);
        Mockito.when(userRepository.findUByEmail("wendyataps@gmail.com")).thenReturn(user);

        AppUserResponseDto userResponseDto = new AppUserResponseDto("wendyataps@gmail.com", "1234", false, true);

        Mockito.when(userRepository.findUByEmail("wendyataps@gmail.com")).thenReturn(user);
        Mockito.when(userMapper.appuserToAppUserDto(Mockito.any(AppUser.class))).thenReturn(userResponseDto);

        AppUserResponseDto responseDto = accountServiceI.loadUserByEmail("wendyataps@gmail.com");

        Assertions.assertThat(responseDto.getEmail()).isNotNull();
    }

    @Test
    void loadUserByEmailF() {
        AccountServiceI accountServiceI = new AccountServiceI(userRepository, roleRepository, passwordEncoder, tokenRepository, userMapper, roleMapper, restClient);
        AppRole role = new AppRole(1L, "ADMIN");
        AppRole role1 = new AppRole(2L, "USER");
        List<AppRole> roles = new ArrayList<>();
        Collections.addAll(roles, role, role1);

        AppUser user = new AppUser( 1L, "wendyataps@gmail.com", "1234", 0, null, false, false, roles);
        Mockito.when(userRepository.findByEmail("wendyataps@gmail.com")).thenReturn(user);

        Mockito.when(userRepository.findByEmail("wendyataps@gmail.com")).thenReturn(user);

        AppUser response = accountServiceI.loadUserByEmailF("wendyataps@gmail.com");

        Assertions.assertThat(response.getEmail()).isNotNull();
    }

    @Test
    void saveRole() {
        AccountServiceI accountServiceI = new AccountServiceI(userRepository, roleRepository, passwordEncoder, tokenRepository, userMapper, roleMapper, restClient);
        AppRole role = new AppRole((long) 1, "ADMIN");
        AppRoleRequestDto roleRequestDto = new AppRoleRequestDto((long) 1, "ADMIN");
        AppRoleResponseDto roleResponseDto = new AppRoleResponseDto("ADMIN");
        Mockito.when(roleMapper.appRoleRequestDtoToAppRole(roleRequestDto)).thenReturn(role);
        Mockito.when(roleMapper.appRoleToAppRoleDto(role)).thenReturn(roleResponseDto);

        accountServiceI.saveRole(roleRequestDto);

        Mockito.verify(roleRepository, Mockito.times(1)).save(roleArgumentCaptor.capture());
        Assertions.assertThat(roleArgumentCaptor.getValue().getIdAppRole()).isEqualTo(1);
        Assertions.assertThat(roleArgumentCaptor.getValue().getRolename()).isEqualTo("ADMIN");
    }

    @Test
    void addRoleToUser() {
        AccountServiceI accountServiceI = new AccountServiceI(userRepository, roleRepository, passwordEncoder, tokenRepository, userMapper, roleMapper, restClient);
        AppRole role = new AppRole((long) 1, "ADMIN");
        AppRole role1 = new AppRole((long) 2, "USER");
        List<AppRole> roles = new ArrayList<>();
        Collections.addAll(roles, role, role1);

        AppUser user = new AppUser((long) 1, "wendyataps@gmail.com", "1234", 0, null, false, false, roles);
        Mockito.when(userRepository.findUByEmail(user.getEmail())).thenReturn(user);
        UserCustomRequestDto userCustomRequestDto = new UserCustomRequestDto("wendyataps@gmail.com", "ADMIN");
        Mockito.when(userRepository.findUByEmail("wendyataps@gmail.com")).thenReturn(user);
        Mockito.when(roleRepository.findByRolename("ADMIN")).thenReturn(role);

        accountServiceI.addRoleToUser(userCustomRequestDto);

    }

    @Test
    void recupPassword() {
    }

    @Test
    void verifCode() {
    }

    @Test
    void reInitPassword() {
    }

    @Test
    void reactivatedUser() {
    }

    @Test
    void deactivatedUser() {
    }

    @Test
    void disableUser() {
    }

    @Test
    void updateUserPassword() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void updateRole() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void deleteUserRole() {
    }

    @Test
    void deleteRole() {
    }

    @Test
    void loadRole() {
    }

    @Test
    void loadUser() {
    }

    @Test
    void loadRoles() {
    }

    @Test
    void loadUsers() {
    }*/
}
