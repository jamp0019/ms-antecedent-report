package com.invexdijin.msantecedentreport.adapters.out.client;

import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "search-person",
        url = "https://api.verifik.co")
public interface SearchPersonClient {
    @RequestMapping(method = RequestMethod.GET, value = "/v2/co/registraduria/votacion")
    ApiResponse getInfoPersonVoting(@RequestHeader("Authorization") String bearerToken,
                                              @RequestParam("documentType") String documentType,
                                              @RequestParam("documentNumber") String documentNumber);
}
