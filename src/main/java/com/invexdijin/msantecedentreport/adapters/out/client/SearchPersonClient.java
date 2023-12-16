package com.invexdijin.msantecedentreport.adapters.out.client;

import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "search-person",
        url = "https://fad475a9-af39-4b22-b51d-e83540dea461.mock.pstmn.io")
public interface SearchPersonClient {
    @RequestMapping(method = RequestMethod.GET, value = "/contraloriaNotFound")
    ApiResponse getInfoPersonVoting(@RequestHeader("Authorization") String bearerToken,
                                              @RequestParam("documentType") String documentType,
                                              @RequestParam("documentNumber") String documentNumber);
}
