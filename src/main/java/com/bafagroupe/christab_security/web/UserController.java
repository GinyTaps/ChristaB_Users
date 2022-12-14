package com.bafagroupe.christab_security.web;

import com.bafagroupe.christab_security.dto.*;
import com.bafagroupe.christab_security.entities.AppRole;
import com.bafagroupe.christab_security.entities.AppUser;
import com.bafagroupe.christab_security.service.AccountService;
import com.bafagroupe.christab_security.web.util.HeaderUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private final AccountService accountService;
    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private static final String ENTITY_NAME = "User";
    private  final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/login")
    public UserDetails login(@RequestBody AppUserRequestDto appUserRequestDto) {
        return userDetailsService.loadUserByUsername(appUserRequestDto.getEmail());
    }

    @PostMapping("/register")
    public AppUserResponseDto saveUserWMail(@RequestBody AppUserRequestDto appUserRequestDto){
        return accountService.saveUser(appUserRequestDto);
    }

    @GetMapping("/confirmAccount")
    public String confirmAccount(@RequestParam("token")String confirmationToken) throws Exception {
        return accountService.confirmUserAccount(confirmationToken);
    }

    @PostMapping("/users/saveRole")
    public AppRoleResponseDto saveRole(@RequestBody AppRoleRequestDto r) {
        return accountService.saveRole(r);
    }


    @PostMapping("/saveRoleUser")
    public void addRoleToUser(@RequestBody UserCustomRequestDto u ) {
        accountService.addRoleToUser(u);
    }

    @PostMapping("/users/validateAccount")
    public AppUserResponseDto validatedAccount(@RequestBody AppUserRequestDto u) {
        return accountService.activatedUser(u);
    }

    @PostMapping("/users/deactivateAccount")
    public AppUserResponseDto deactivatedAccount(@RequestBody AppUserRequestDto u) {
        return accountService.deactivatedUser(u);
    }


    @PostMapping("/users/updateUserPass")
    public AppUserResponseDto updateUserPassword(@RequestBody UserRequestDto u) {
        return accountService.updateUserPassword(u);
    }


    @PostMapping("/users/updateUser")
    public AppUserResponseDto updateUser(@RequestBody AppUserRequestDto appUserRequestDto) {
        return accountService.updateUser(appUserRequestDto);
    }

    @PostMapping("/users/updateRole")
    public AppRoleResponseDto updateRole(@RequestBody AppRoleRequestDto r) {
        return accountService.updateRole(r);
    }


    @PostMapping("/findUser")
    public AppUserResponseDto loadUser(@RequestBody long id) {
        return accountService.loadUser(id);
    }


    @PostMapping("/users/findRole")
    public AppRoleResponseDto loadRole(@RequestBody long id) {
        return accountService.loadRole(id);
    }


    @PostMapping("/findUserByEmail")
    public AppUserResponseDto loadUserByEmail(@RequestBody String email) {
        return accountService.loadUserByEmail(email);
    }

    @GetMapping("/checkUserByEmailV2/{email}")
    public ResponseEntity<Boolean> checkUserByEmailUtil(@PathVariable String email) {
        return ResponseEntity.ok(accountService.checkUserByEmailAndUtilisateur(email));
    }

    @GetMapping("/checkUserByEmail/{email}")
    public ResponseEntity<Boolean> checkUByEmail(@PathVariable String email) {
        // System.out.println(email);
        return ResponseEntity.ok(accountService.checkUserByEmail(email));
    }

    @GetMapping("/findUsers")
    public List<AppUserResponseDto> loadUsers() {
        return accountService.loadUsers();
    }


    @GetMapping("/users/findRoles")
    public List<AppRoleResponseDto> loadRoles() {
        return accountService.loadRoles();
    }


	@PostMapping("/users/deleteUser")
    public void deleteUser(@RequestBody AppUserRequestDto u) {
        accountService.deleteUser(u);
    }

    @PostMapping("/users/reactivateUser")
    public void reactivateUser(@RequestBody AppUserRequestDto u) {
        accountService.reactivatedUser(u);
    }

    @PostMapping("/users/disableUser")
    public void disableUser(@RequestBody AppUserRequestDto u) {
        accountService.disableUser(u);
    }


    @PostMapping("/users/deleteRole")
    public void deleteRole(@RequestBody long id) {
        accountService.deleteRole(id);
    }


    @PostMapping("/reInitPassword")
    public AppUserResponseDto reInitPassword(@RequestBody UserFormRequestDto user) {
        return accountService.reInitPassword(user);
    }


    @PostMapping("/recupPassword")
    public boolean recupPassword(@RequestBody String email) {
        return accountService.recupPassword(email);
    }

    @PostMapping("/verifCode/{code}")
    public boolean verifCode(@RequestBody String email, @PathVariable int code) {
        return accountService.verifCode(email, code);
    }

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception e) {
        log.debug("REST request to show exception : {}", e);
        return e.getMessage();
    }
}
