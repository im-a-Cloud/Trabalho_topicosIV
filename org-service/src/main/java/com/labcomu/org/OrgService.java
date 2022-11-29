package com.labcomu.org;

import com.labcomu.org.domain.Organization;
import com.labcomu.org.domain.mapper.ResearcherMapper;
import com.labcomu.org.domain.repository.OrganizationRepository;
import com.labcomu.org.domain.repository.ResearcherRepository;
import com.labcomu.org.resource.ResourceOrganization;
import com.labcomu.org.resource.ResourceResearcher;
import com.labcomu.org.resource.mapper.ResourceOrganizationMapper;
import com.labcomu.org.resource.mapper.ResourceResearcherMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class OrgService {
    private final ResearcherMapper researcherMapper;
    private final ResourceResearcherMapper resourceResearcherMapper;
    private final ResourceOrganizationMapper resourceOrganizationMapper;
    private final OrganizationRepository organizationRepository;
    private final ResearcherRepository researcherRepository;


    // Criando log
    private Logger logger = LoggerFactory.getLogger(OrgService.class);


    // Falback missão 3
    @Retry(name = "retryWithFallback", fallbackMethod = "getOrganization")
    public Optional<ResourceOrganization> fallback(@NotNull final String url) {
        Logger.erro("Handle Orgservice")

        organization.ifPresent(Organization::getResearchers);

        return organization.map(resourceOrganizationMapper::map); 
    }

    @Delay(value=5, threshold=0.9)
    public Optional<ResourceOrganization> getOrganization(@NotNull final String url, RuntimeException re) {
        Optional<Organization> organization = organizationRepository.findByUrl(url);

        // load researchers (lazy)
        organization.ifPresent(Organization::getResearchers);

        return organization.map(resourceOrganizationMapper::map);
    }


    //Fallback missão 1

    @Retry(name = "retryWithFallback", fallbackMethod = "createResearcher")
    public Optional<ResourceOrganization> fallback(@NotNull final String url) {

        // Log
        Logger.erro("Handle Orgservice")

        if (researcherRepository.existsByOrcid(resourceResearcher.getOrcid()))
            return Optional.empty();

        organizationRepository.findByUrl(url).map(resourceOrganizationMapper::map).ifPresent(resourceResearcher::setOrganization);

        return Optional.of(resourceResearcherMapper.map(researcherRepository.save(researcherMapper.map(resourceResearcher))));
    }

    public Optional<ResourceResearcher> createResearcher(@NotNull final String url, @NotNull ResourceResearcher resourceResearcher, RuntimeException re) {
        if (researcherRepository.existsByOrcid(resourceResearcher.getOrcid()))
            return Optional.empty();

        organizationRepository.findByUrl(url).map(resourceOrganizationMapper::map).ifPresent(resourceResearcher::setOrganization);

        return Optional.of(resourceResearcherMapper.map(researcherRepository.save(researcherMapper.map(resourceResearcher))));
    }

}