package com.bafagroupe.christab_security.openFeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "http://localhost:8082", name = "CHRISTAB-RES", configuration = FeignClientConfiguration.class)
public interface UtilisateurRestClient {
    @PostMapping("/api/checkUtilisateurByEmail")
    Boolean checkUtilisateurByEmail(@RequestBody String email);
}
