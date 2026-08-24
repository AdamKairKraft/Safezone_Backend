package com.safezone.identity.internal.service;

import com.safezone.identity.internal.domain.AppUser;
import com.safezone.identity.internal.repository.AppUserRepository;
import com.safezone.shared.web.NotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;

    @Override
    public AppUser get(UUID id) {
        return appUserRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found: " + id));
    }

    @Override
    public List<AppUser> listByOrganization(UUID organizationId) {
        return appUserRepository.findByOrganizationId(organizationId);
    }
}
