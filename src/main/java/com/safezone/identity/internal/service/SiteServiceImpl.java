package com.safezone.identity.internal.service;

import com.safezone.identity.internal.domain.Site;
import com.safezone.identity.internal.repository.SiteRepository;
import com.safezone.shared.web.NotFoundException;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
class SiteServiceImpl implements SiteService {

    private final SiteRepository siteRepository;

    @Override
    public Site create(UUID id, UUID organizationId, String name) {
        return siteRepository.findById(id)
                .orElseGet(() -> siteRepository.save(new Site(id, organizationId, name)));
    }

    @Override
    @Transactional(readOnly = true)
    public Site get(UUID id) {
        return siteRepository.findById(id).orElseThrow(() -> new NotFoundException("Site not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Site> listByOrganization(UUID organizationId) {
        return siteRepository.findByOrganizationId(organizationId);
    }
}
