package com.knubisoft.testapi.security;

import com.knubisoft.testapi.model.postgresUsers.User;
import com.knubisoft.testapi.repository.postgresUsers.UserRepo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
@ConditionalOnExpression("${jwt.postgres.users.enabled:true} || ${basic.postgres.users.enabled:true}")
public class AuthUserService implements UserDetailsService {

    public static final String USER_NOT_FOUND = "User with username <%s> not found";
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthUserService(@Qualifier("users") final UserRepo userRepo,
                           final PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(format(USER_NOT_FOUND, username)));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }
}
