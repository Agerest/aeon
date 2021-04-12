package com.aeon.rest;

import com.aeon.domain.UserDetailsImpl;
import com.aeon.dto.JwtResponseDTO;
import com.aeon.dto.LoginRequestDTO;
import com.aeon.dto.RegistrationRequestDTO;
import com.aeon.service.BlackTokenService;
import com.aeon.service.JwtService;
import com.aeon.service.UserService;
import com.aeon.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthResource {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final BlackTokenService blackTokenService;

    @PostMapping("/login")
    public JwtResponseDTO authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponseDTO(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles);
    }

    @PostMapping("/signup")
    public Long registerUser(@Valid @RequestBody RegistrationRequestDTO registrationRequestDTO) {
        return userService.createNewUser(registrationRequestDTO);
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request) {
        blackTokenService.addNewBlackToken(TokenUtils.parseJwt(request));
    }
}
