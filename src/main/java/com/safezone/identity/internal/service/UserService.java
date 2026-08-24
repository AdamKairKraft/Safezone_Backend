package com.safezone.identity.internal.service;

import com.safezone.identity.internal.domain.AppUser;
import java.util.List;
import java.util.UUID;

public interface UserService {

    AppUser get(UUID id);

    List<AppUser> listByOrganization(UUID organizationId);
}
