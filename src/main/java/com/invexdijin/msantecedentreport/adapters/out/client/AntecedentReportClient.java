package com.invexdijin.msantecedentreport.adapters.out.client;

import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentesdisciplinarios.ResponseAntecedentesDisciplinarios;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentespoliciales.ResponseAntecedentesPoliciales;
import com.invexdijin.msantecedentreport.application.core.domain.response.contraloria.ResponseContraloria;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(value = "contraloria",
        url = "https://869bdfdf-bfe3-4ec5-b41c-e9cb3287bfe3.mock.pstmn.io")
public interface AntecedentReportClient {

    @RequestMapping(method = RequestMethod.GET, value = "/v2/co/contraloria/certificado")
    ResponseContraloria getContraloria(@RequestHeader("Authorization") String bearerToken,
                                       @RequestParam("documentType") String documentType,
                                       @RequestParam("documentNumber") String documentNumber);

    @RequestMapping(method = RequestMethod.GET, value = "/v2/co/procuraduria/antecedentes")
    ResponseAntecedentesDisciplinarios getAntececentesDisciplinarios(@RequestHeader("Authorization") String bearerToken,
                                                                     @RequestParam("documentType") String documentType,
                                                                     @RequestParam("documentNumber") String documentNumber);

    @RequestMapping(method = RequestMethod.GET, value = "/v2/co/policia/consultar")
    ResponseAntecedentesPoliciales getAntececentesPoliciales(@RequestHeader("Authorization") String bearerToken,
                                                             @RequestParam("documentType") String documentType,
                                                             @RequestParam("documentNumber") String documentNumber);


}
