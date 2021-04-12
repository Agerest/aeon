package com.aeon.service;

import com.aeon.domain.Payment;
import com.aeon.domain.User;
import com.aeon.dto.RegistrationRequestDTO;
import com.aeon.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder encoder;

    @Transactional
    public Long createNewUser(RegistrationRequestDTO registrationRequestDTO) {
        if (userRepository.existsByUsername(registrationRequestDTO.getUsername())) {
            throw new IllegalStateException("Username is already taken");
        }
        if (userRepository.existsByEmail(registrationRequestDTO.getEmail())) {
            throw new IllegalStateException("Email is already in use");
        }

        User user = new User(
                registrationRequestDTO.getUsername(),
                registrationRequestDTO.getEmail(),
                encoder.encode(registrationRequestDTO.getPassword()));
        user.setRoles(roleService.getRoleSet(registrationRequestDTO.getRoles()));
        return userRepository.save(user).getId();
    }

    @Transactional(readOnly = true)
    public User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Could not find user by username " + username));
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void doPayment(String username, Payment payment) {
        User user = getUser(username);
        user.setBalance(user.getBalance().subtract(payment.getSum()));
        userRepository.save(user);
    }
}
