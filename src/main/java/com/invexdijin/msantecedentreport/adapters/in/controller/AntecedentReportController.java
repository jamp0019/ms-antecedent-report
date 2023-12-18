package com.invexdijin.msantecedentreport.adapters.in.controller;

import com.invexdijin.msantecedentreport.application.core.domain.request.RequestSearch;
import com.invexdijin.msantecedentreport.application.core.domain.response.ConsolidatedResponse;
import com.invexdijin.msantecedentreport.application.ports.in.CreateReportInputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invexdijin")
public class AntecedentReportController {

    @Autowired
    private CreateReportInputPort createReportInputPort;

    @PostMapping("/search-person-report")
    public ResponseEntity<?> requestSearchPerson(@RequestBody RequestSearch requestSearch) throws Exception {
        ConsolidatedResponse response = createReportInputPort.generateSearchPersonReport(requestSearch);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping("/antecedent-report")
    public ResponseEntity<?> requestAntecedentReport(@RequestBody RequestSearch requestSearch) throws Exception {
        ConsolidatedResponse response = createReportInputPort.generateAntecedentReport(requestSearch);
        return ResponseEntity.ok().body(response);
    }

}
