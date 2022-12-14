package com.bafagroupe.christab_security.service;

import com.bafagroupe.christab_security.entities.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@AllArgsConstructor
public class UserDetailsServiceI implements UserDetailsService {

    private AccountService accountService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = accountService.loadUserByEmailF(email);
        /*System.out.println("******************* UserDetailsService ******************");
        System.out.println(user.getEmail());*/
        if(user == null) throw new UsernameNotFoundException("Email does not exist");
        if(!user.isValidated()) throw new RuntimeException("Account does not validated ");
        if(!user.getActivated()) throw new UsernameNotFoundException("Account does not activated"); //pour interdire l'accès de l'application aux comptes désactivés
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(r ->{
			authorities.add(new SimpleGrantedAuthority(r.getRolename()));
		});
        if(authorities.size() <= 0) throw new RuntimeException("Roles list empty");
        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
