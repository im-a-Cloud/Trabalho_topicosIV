package com.labcomu.orcid;

import com.labcomu.orcid.resource.Researcher;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/v1/orcid")
@Validated
@RequiredArgsConstructor
public class OrcidController {
    private final OrcidService service;

    @GetMapping("active")
    public boolean isActive() {
        return service.isActive();
    }
    //missão 2
    @Throw(exception=RuntimeException.class, threshold=0.5)
    @GetMapping("researcher/{orcid}")
    public Researcher getResearcher(@NotNull @PathVariable String orcid) {
        return service.getResearcher(orcid);
    }
}
