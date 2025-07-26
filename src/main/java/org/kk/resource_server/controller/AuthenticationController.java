package org.kk.resource_server.controller;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import org.kk.resource_server.domain.vm.JWTToken;
import org.kk.resource_server.security.DaoUserDetailService;
import org.kk.resource_server.security.SysUser;
import org.kk.resource_server.vm.LoginVM;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private long tokenInvalidSeconds = 15 * 60;
    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private DaoUserDetailService userDetailService;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @RequestMapping("/login")
    public ResponseEntity<JWTToken> login(@Valid @RequestBody LoginVM loginVM) throws Exception {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginVM.getUsername(),
                loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token  = this.createToken(authentication, tokenInvalidSeconds);
        String refreshToken = this.createToken(authentication,  7 * 24 * 60 * 60);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(token);
        return new ResponseEntity<>(new JWTToken(token,refreshToken), httpHeaders, HttpStatus.OK);
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


    @PostMapping("/refresh/token")
    public ResponseEntity<JWTToken> refreshToken(@RequestBody JWTToken jwtToken){
        if(!StringUtils.hasText(jwtToken.getRefreshToken())) {
            return ResponseEntity.badRequest().build();
        }

        Jwt refreshToken = jwtDecoder.decode(jwtToken.getRefreshToken());
        String username = refreshToken.getSubject();
        if(!StringUtils.hasText(username)) {
            return ResponseEntity.badRequest().build();
        }
        UserDetails userdetails = userDetailService.loadUserByUsername(username);
        String idToken = this.createToken(new UsernamePasswordAuthenticationToken(username, userdetails.getPassword(),
                userdetails.getAuthorities()), tokenInvalidSeconds);
        return ResponseEntity.ok(new JWTToken(idToken, ""));
    }

    public String createToken(Authentication authentication, long second) {
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        Instant now = Instant.now(Clock.system(ZoneOffset.ofHours(8)));
        Instant validity;
        validity = now.plus(second, ChronoUnit.SECONDS);

        String username = "";
        Class<?> aClass = authentication.getPrincipal().getClass();
        if (aClass.equals(UsernamePasswordAuthenticationToken.class)) {
            username = authentication.getPrincipal().toString();
        } else if (aClass.equals(SysUser.class)) {
            username = ((SysUser) authentication.getPrincipal()).getUsername();
        }

        // @formatter:off
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(username)
                .claim("scp", authorities);
//        if (authentication.getPrincipal() instanceof UserWithId user) {
//            builder.claim(USER_ID_CLAIM, user.getId());
//        }

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS512).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
    }




}
