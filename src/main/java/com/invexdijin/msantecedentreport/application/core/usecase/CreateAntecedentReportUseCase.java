package com.invexdijin.msantecedentreport.application.core.usecase;

import com.invexdijin.msantecedentreport.adapters.out.client.AntecedentReportClient;
import com.invexdijin.msantecedentreport.adapters.out.client.EmailNotificationClient;
import com.invexdijin.msantecedentreport.application.core.domain.request.RequestAntecedentReport;
import com.invexdijin.msantecedentreport.application.core.domain.request.RequestPdfEmail;
import com.invexdijin.msantecedentreport.application.core.domain.response.ConsolidatedResponse;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import com.invexdijin.msantecedentreport.application.ports.in.CreateAntecedentReportInputPort;
import com.invexdijin.msantecedentreport.application.ports.out.CreateFormatPdfOutputPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
public class CreateAntecedentReportUseCase implements CreateAntecedentReportInputPort {

    @Autowired
    private AntecedentReportClient antecedentReportClient;

    @Autowired
    private EmailNotificationClient emailNotificationClient;

    @Autowired
    private CreateFormatPdfOutputPort createFormatPdfOutputPort;

    @Value("${token.verifik}")
    private String token;
    @Override
    public ConsolidatedResponse generateAntecedentReport(RequestAntecedentReport requestAntecedentReport) throws Exception {

        ApiResponse publicSpendingWatchdogResponse = antecedentReportClient.getInfoPublicSpendingWatchdog(token, requestAntecedentReport.getDocumentType(), requestAntecedentReport.getDocumentNumber());
        ApiResponse attorneyOfficeResponse = antecedentReportClient.getInfoDisciplinaryAntecedents(token, requestAntecedentReport.getDocumentType(), requestAntecedentReport.getDocumentNumber());
        ApiResponse policeAntecedentsResponse = antecedentReportClient.getInfoPoliceAntecedents(token, requestAntecedentReport.getDocumentType(), requestAntecedentReport.getDocumentNumber());
        byte[] bytesMainResponse = createFormatPdfOutputPort.createMainReport(requestAntecedentReport.getName(),requestAntecedentReport.getEmail());
        byte[] bytesAttorneyOfficeResponse = createFormatPdfOutputPort.createAttorneyOfficeReport(attorneyOfficeResponse);
        byte[] bytesPoliceAntecedentsResponse = createFormatPdfOutputPort.createPoliceReport(policeAntecedentsResponse);
        String base64PublicSpendingWatchdogResponse = publicSpendingWatchdogResponse.getData().getPdfBase64().split(",")[1];
        byte[] base64Decoder = Base64.getDecoder().decode(base64PublicSpendingWatchdogResponse);
        String consolidatedBase64Report = createFormatPdfOutputPort.mergePdfAndReturnBase64(
                bytesMainResponse,
                bytesAttorneyOfficeResponse,
                bytesPoliceAntecedentsResponse,
                base64Decoder);
        RequestPdfEmail requestPdfEmail = new RequestPdfEmail();
        requestPdfEmail.setName(requestAntecedentReport.getName());
        requestPdfEmail.setEmail(requestAntecedentReport.getEmail());
        requestPdfEmail.setPdfBase64(consolidatedBase64Report);

        emailNotificationClient.sendPdfByEmail(requestPdfEmail);
        ConsolidatedResponse consolidatedResponse = new ConsolidatedResponse();
        consolidatedResponse.setFullName(policeAntecedentsResponse.getData().getFullName());
        consolidatedResponse.setFirstName(policeAntecedentsResponse.getData().getFirstName());
        consolidatedResponse.setDocumentType(policeAntecedentsResponse.getData().getDocumentType());
        consolidatedResponse.setDocumentNumber(policeAntecedentsResponse.getData().getDocumentNumber());
        consolidatedResponse.setAttorneyOfficeLegend(attorneyOfficeResponse.getData().getLegend());
        consolidatedResponse.setPoliceDetail(policeAntecedentsResponse.getData().getDetails());
        consolidatedResponse.setPublicSpendingWatchdogMessage("La contraloría delegada para responsabilidad fiscal, intervención judicial y cobro coactivo. Ver detalle en pdf adjunto.");
        return consolidatedResponse;
    }

}
