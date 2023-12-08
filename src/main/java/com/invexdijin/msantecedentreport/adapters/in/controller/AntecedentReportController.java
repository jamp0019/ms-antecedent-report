package com.invexdijin.msantecedentreport.adapters.in.controller;

import com.invexdijin.msantecedentreport.application.core.domain.request.RequestAntecedentReport;
import com.invexdijin.msantecedentreport.application.ports.in.CreateAntecedentReportInputPort;
import com.itextpdf.text.DocumentException;
import net.sf.jasperreports.engine.JRException;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/v1/antecedent")
public class AntecedentReportController {

    @Autowired
    private CreateAntecedentReportInputPort createAntecedentReportInputPort;
    @PostMapping("/report")
    public ResponseEntity<?> requestAntecedentReport(@RequestBody RequestAntecedentReport requestAntecedentReport) throws DocumentException, IOException, URISyntaxException, JRException, ParseException {
        String response = createAntecedentReportInputPort.generateAntecedentReport(requestAntecedentReport);
        return ResponseEntity.ok().body(response);
    }

}
