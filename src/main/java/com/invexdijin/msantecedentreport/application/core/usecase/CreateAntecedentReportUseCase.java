package com.invexdijin.msantecedentreport.application.core.usecase;

import com.invexdijin.msantecedentreport.adapters.out.client.AntecedentReportClient;
import com.invexdijin.msantecedentreport.application.core.domain.request.RequestAntecedentReport;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import com.invexdijin.msantecedentreport.application.ports.in.CreateAntecedentReportInputPort;
import com.invexdijin.msantecedentreport.application.ports.out.CreateFormatPdfOutputPort;
import com.itextpdf.text.DocumentException;
import net.sf.jasperreports.engine.JRException;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class CreateAntecedentReportUseCase implements CreateAntecedentReportInputPort {

    @Autowired
    private AntecedentReportClient antecedentReportClient;

    @Autowired
    private CreateFormatPdfOutputPort createFormatPdfOutputPort;

    @Value("${token.verifik}")
    private String token;
    @Override
    public String generateAntecedentReport(RequestAntecedentReport requestAntecedentReport) throws DocumentException, IOException, URISyntaxException, JRException, ParseException {

        ApiResponse publicSpendingWatchdogResponse = antecedentReportClient.getInfoPublicSpendingWatchdog(token, requestAntecedentReport.getDocumentType(), requestAntecedentReport.getDocumentNumber());
        ApiResponse attorneyOfficeResponse = antecedentReportClient.getInfoDisciplinaryAntecedents(token, requestAntecedentReport.getDocumentType(), requestAntecedentReport.getDocumentNumber());
        ApiResponse policeAntecedentsResponse = antecedentReportClient.getInfoPoliceAntecedents(token, requestAntecedentReport.getDocumentType(), requestAntecedentReport.getDocumentNumber());
        String base64MainResponse = createFormatPdfOutputPort.createMainReport(requestAntecedentReport.getName(),
                requestAntecedentReport.getEmail());
        String base64AttorneyOfficeResponse = createFormatPdfOutputPort.createAttorneyOfficeReport(attorneyOfficeResponse);
        String base64PoliceAntecedentsResponse = createFormatPdfOutputPort.createPoliceReport(policeAntecedentsResponse);
        String base64PublicSpendingWatchdogResponse = publicSpendingWatchdogResponse.getData().getPdfBase64();
        return "Ok";
    }
}
