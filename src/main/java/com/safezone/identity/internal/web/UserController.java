package com.safezone.identity.internal.web;

import com.safezone.identity.internal.service.UserService;
import com.safezone.identity.internal.web.dto.UserResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
class UserController {

    private final UserService userService;

    // X-User-Id is set by JwtAuthenticationFilter from the verified access token, never
    // from a client-supplied header - this reliably means "whoever is logged in".
    @GetMapping("/me")
    public UserResponse me(@RequestHeader("X-User-Id") UUID userId) {
        return UserResponse.from(userService.get(userId));
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable UUID id) {
        return UserResponse.from(userService.get(id));
    }

    @GetMapping
    public List<UserResponse> listByOrganization(@RequestParam UUID organizationId) {
        return userService.listByOrganization(organizationId).stream().map(UserResponse::from).toList();
    }
}
