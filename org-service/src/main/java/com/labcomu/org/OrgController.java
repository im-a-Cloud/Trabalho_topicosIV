package com.labcomu.org;

import com.labcomu.faultinjection.annotation.Delay;
import com.labcomu.org.resource.ResourceOrganization;
import com.labcomu.org.resource.ResourceResearcher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("api/v1/org")
@Validated
@RequiredArgsConstructor
public class OrgController {
  private static final int CONFLICT = 409;
  private final OrgService service;
  //delay missão 3
  @Delay(value=5, threshold=0.9) 
  @GetMapping("organization/{url}")
  public ResponseEntity<ResourceOrganization> getOrganization(@NotNull @PathVariable String url) {
    return ResponseEntity.of(service.getOrganization(url));
  }

  @PostMapping("organization/researcher/{url}")
  public ResponseEntity<ResourceResearcher> createResearcher(@NotNull @PathVariable String url, @NotNull @RequestBody ResourceResearcher researcher) {
      return service.createResearcher(url, researcher).map(ResponseEntity::ok).orElse(ResponseEntity.status(CONFLICT).build());
  }

}
