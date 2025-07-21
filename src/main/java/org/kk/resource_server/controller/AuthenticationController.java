package org.kk.resource_server.controller;


import ch.qos.logback.core.util.TimeUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.kk.resource_server.vm.LoginVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DurationFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Autowired
    private JwtEncoder jwtEncoder;

    @RequestMapping("/login")
    public ResponseEntity<JWTToken> login(@Valid @RequestBody LoginVM loginVM){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginVM.getUsername(),
                loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = this.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/current-user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> currentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return ResponseEntity.ok(springSecurityUser.getUsername());
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return ResponseEntity.ok(jwt.getSubject());
        } else if (authentication.getPrincipal() instanceof String s) {
            return ResponseEntity.ok(s);
        }
        return null;
    }

    public String createToken(Authentication authentication, boolean rememberMe) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        Instant now = Instant.now();
        Instant validity;
        if (rememberMe) {
            validity = now.plus(60000, ChronoUnit.SECONDS);
        } else {
            validity = now.plus(60000, ChronoUnit.SECONDS);
        }

        // @formatter:off
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim("scp", authorities);
//        if (authentication.getPrincipal() instanceof UserWithId user) {
//            builder.claim(USER_ID_CLAIM, user.getId());
//        }

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS512).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
    }



    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
