package com.bafagroupe.christab_security.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials, Authorization ");
        httpServletResponse.addHeader("Access-Control-Allow_Methods", "GET, POST, PUT, DELETE, PATCH");

        if(httpServletRequest.getMethod().equals("OPTIONS")) { // si une requête est envoyée avec OPTIONS, c'est ok
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        }
        else if(httpServletRequest.getRequestURI().equals("/login")  || httpServletRequest.getRequestURI().equals("/api/login")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        else {
            String jwt = httpServletRequest.getHeader(SecurityParams.JWT_HEADER_NAME);
            /*System.out.println("*********** Début ************");
            System.out.println("Token= "+jwt);*/
            if(jwt == null || !jwt.startsWith(SecurityParams.TOKEN_PREFIX)) { //si le jwt est nul ou ne commence pas par le préfixe
                filterChain.doFilter(httpServletRequest, httpServletResponse); //on passe au filtre suivant il va rejeter l'accès à l'application
                return;
            }
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET.getBytes())).build();
            DecodedJWT decodedJWT = verifier.verify(JWT.decode(jwt.substring(SecurityParams.TOKEN_PREFIX.length()))); // , jwt.length())));
            /*System.out.println("*********** Décodage ************");
            System.out.println("JWT= "+decodedJWT);*/
            // String username = decodedJWT.getSubject();
            String email = decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaims().get(SecurityParams.CLAIM).asList(String.class);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(r ->{
                authorities.add(new SimpleGrantedAuthority(r));
            });
            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(email, null, authorities); // (username, null, authorities);
				    SecurityContextHolder.getContext().setAuthentication(user);
            filterChain.doFilter(httpServletRequest, httpServletResponse); //passe au filtre suivant après avoir identifié l'utilisateur
        }

    }
}
