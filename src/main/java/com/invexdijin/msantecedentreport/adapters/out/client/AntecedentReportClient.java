package com.invexdijin.msantecedentreport.adapters.out.client;

import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "antecedents",
        url = "https://fad475a9-af39-4b22-b51d-e83540dea461.mock.pstmn.io")
public interface AntecedentReportClient {

    @RequestMapping(method = RequestMethod.GET, value = "/contraloriaOk")
    ApiResponse getInfoPublicSpendingWatchdog(@RequestHeader("Authorization") String bearerToken,
                               @RequestParam("documentType") String documentType,
                               @RequestParam("documentNumber") String documentNumber);

    @RequestMapping(method = RequestMethod.GET, value = "/procuraduriaOk")
    ApiResponse getInfoDisciplinaryAntecedents(@RequestHeader("Authorization") String bearerToken,
                                                                     @RequestParam("documentType") String documentType,
                                                                     @RequestParam("documentNumber") String documentNumber);

    @RequestMapping(method = RequestMethod.GET, value = "/policiaOk")
    ApiResponse getInfoPoliceAntecedents(@RequestHeader("Authorization") String bearerToken,
                                                             @RequestParam("documentType") String documentType,
                                                             @RequestParam("documentNumber") String documentNumber);


}
