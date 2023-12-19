package com.invexdijin.msantecedentreport.adapters.out.client;

import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "antecedents",
        url = "https://api.verifik.co")
public interface AntecedentReportClient {

    @RequestMapping(method = RequestMethod.GET, value = "/v2/co/contraloria/certificado")
    ApiResponse getInfoPublicSpendingWatchdog(@RequestHeader("Authorization") String bearerToken,
                               @RequestParam("documentType") String documentType,
                               @RequestParam("documentNumber") String documentNumber);

    @RequestMapping(method = RequestMethod.GET, value = "/v2/co/procuraduria/antecedentes")
    ApiResponse getInfoDisciplinaryAntecedents(@RequestHeader("Authorization") String bearerToken,
                                                                     @RequestParam("documentType") String documentType,
                                                                     @RequestParam("documentNumber") String documentNumber);

    @RequestMapping(method = RequestMethod.GET, value = "/v2/co/policia/consultar")
    ApiResponse getInfoPoliceAntecedents(@RequestHeader("Authorization") String bearerToken,
                                                             @RequestParam("documentType") String documentType,
                                                             @RequestParam("documentNumber") String documentNumber);


}
