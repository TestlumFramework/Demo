package com.knubisoft.testapi.api.impl;

import com.knubisoft.testapi.api.BasicAuthApi;
import com.knubisoft.testapi.dto.LoginDto;
import com.knubisoft.testapi.dto.UserDto;
import com.knubisoft.testapi.model.postgresUsers.User;
import com.knubisoft.testapi.repository.postgresUsers.UserRepo;
import com.knubisoft.testapi.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@ConditionalOnExpression("${basic.postgres.users.enabled:true}")
public class BasicAuthController implements BasicAuthApi {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<LoginDto> basicLogin(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(loginDto);
    }

    public ResponseEntity<User> createUser(UserDto dto) {
        User user = MapperUtil.convertFrom(dto, new User());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        User savedUser = userRepo.save(user);
        return ResponseEntity.ok(savedUser);
    }

    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }
}
