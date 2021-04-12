package com.aeon.service;

import com.aeon.domain.ERole;
import com.aeon.domain.Role;
import com.aeon.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Set<Role> getRoleSet(Set<String> roles) {
        Set<Role> result = new HashSet<>();
        for (String role : roles) {
            if ("admin".equals(role)) {
                Role adminRole = getRole(ERole.ROLE_ADMIN);
                result.add(adminRole);
            } else {
                Role userRole = getRole(ERole.ROLE_USER);
                result.add(userRole);
            }
        }
        return result;
    }

    @Transactional(readOnly = true)
    public Role getRole(ERole eRole) {
        return roleRepository.findByName(eRole)
                .orElseThrow(() -> new IllegalStateException("Could not find role " + eRole.name()));
    }
}
