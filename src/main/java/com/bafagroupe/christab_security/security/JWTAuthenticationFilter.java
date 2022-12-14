package com.bafagroupe.christab_security.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bafagroupe.christab_security.entities.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AppUser u = null;
        try {
            u = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
            /*System.out.println("******* Utilisateur connecté *******");
            System.out.println(u.getEmail());
            System.out.println(u.getPassword());
            System.out.println("**************");*/
        } catch (IOException e) {
            e.printStackTrace();
        }
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(u.getEmail(), u.getPassword())); //ici on travaille avec l'email et non le username
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        User user = (User)authResult.getPrincipal(); //retourne l'objet utilisateur authentifié
        List<String> roles = new ArrayList<>();
        authResult.getAuthorities().forEach(r ->{
            roles.add(r.getAuthority());
        });

        String jwt = JWT.create()
                .withIssuer(request.getRequestURI()) // l'url de l'api
                .withSubject( user.getUsername()) // le nom de l'utilisateur mais ici l'email
                .withArrayClaim(SecurityParams.CLAIM, roles.toArray(new String[roles.size()])) // les rôles de l'utilisateur
                .withExpiresAt(new Date(System.currentTimeMillis()+ SecurityParams.TIME)) // date d'expiration
                .sign(Algorithm.HMAC256(SecurityParams.SECRET.getBytes())); // signature composé du secret encodé
        response.addHeader(SecurityParams.JWT_HEADER_NAME, SecurityParams.TOKEN_PREFIX+jwt);
    }
}
