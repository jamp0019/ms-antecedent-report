package com.invexdijin.msantecedentreport.application.core.usecase;

import com.invexdijin.msantecedentreport.adapters.out.client.AntecedentReportClient;
import com.invexdijin.msantecedentreport.adapters.out.client.EmailNotificationClient;
import com.invexdijin.msantecedentreport.adapters.out.client.MapClient;
import com.invexdijin.msantecedentreport.adapters.out.client.SearchPersonClient;
import com.invexdijin.msantecedentreport.application.core.domain.request.RequestSearch;
import com.invexdijin.msantecedentreport.application.core.domain.request.RequestPdfEmail;
import com.invexdijin.msantecedentreport.application.core.domain.response.ConsolidatedResponse;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import com.invexdijin.msantecedentreport.application.core.domain.response.searchperson.MapResponse;
import com.invexdijin.msantecedentreport.application.core.exception.InternalServerError;
import com.invexdijin.msantecedentreport.application.ports.in.CreateReportInputPort;
import com.invexdijin.msantecedentreport.application.ports.out.CreateFormatPdfOutputPort;
import com.invexdijin.msantecedentreport.application.ports.out.CreateUtilOutputPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Base64;

@Service
@Slf4j
public class CreateReportUseCase implements CreateReportInputPort {

    @Autowired
    private AntecedentReportClient antecedentReportClient;

    @Autowired
    private SearchPersonClient searchPersonClient;

    @Autowired
    private EmailNotificationClient emailNotificationClient;

    @Autowired
    private MapClient mapClient;

    @Autowired
    private CreateFormatPdfOutputPort createFormatPdfOutputPort;

    @Autowired
    private CreateUtilOutputPort createUtilOutputPort;

    @Value("${token.verifik}")
    private String verifikToken;

    @Value("${client.google.maps.key}")
    private String mapsKey;
    @Override
    public ConsolidatedResponse generateAntecedentReport(RequestSearch requestSearch) throws Exception {

        ConsolidatedResponse consolidatedResponse;
        try{
            ApiResponse publicSpendingWatchdogResponse = antecedentReportClient.getInfoPublicSpendingWatchdog(verifikToken, requestSearch.getDocumentType(), requestSearch.getDocumentNumber());
            ApiResponse attorneyOfficeResponse = antecedentReportClient.getInfoDisciplinaryAntecedents(verifikToken, requestSearch.getDocumentType(), requestSearch.getDocumentNumber());
            ApiResponse policeAntecedentsResponse = antecedentReportClient.getInfoPoliceAntecedents(verifikToken, requestSearch.getDocumentType(), requestSearch.getDocumentNumber());

            byte[] bytesMainResponse = createFormatPdfOutputPort.createMainReport(requestSearch.getPaymentName(), requestSearch.getPaymentEmail());
            byte[] bytesAttorneyOfficeResponse;
            if(attorneyOfficeResponse.getData().isRequired()){
                bytesAttorneyOfficeResponse = createFormatPdfOutputPort.createAttorneyOfficeReport(attorneyOfficeResponse);
            }else {
                bytesAttorneyOfficeResponse = createFormatPdfOutputPort.createAttorneyOfficeNoReport(attorneyOfficeResponse);
            }
            byte[] bytesPoliceAntecedentsResponse = createFormatPdfOutputPort.createPoliceReport(policeAntecedentsResponse);
            String base64PublicSpendingWatchdogResponse = publicSpendingWatchdogResponse.getData().getPdfBase64().split(",")[1];
            byte[] base64Decoder = Base64.getDecoder().decode(base64PublicSpendingWatchdogResponse);

            String consolidatedBase64Report = createFormatPdfOutputPort.mergePdfAndReturnBase64(
                    bytesMainResponse,
                    bytesAttorneyOfficeResponse,
                    bytesPoliceAntecedentsResponse,
                    base64Decoder);
            RequestPdfEmail requestPdfEmail = createUtilOutputPort.createRequestPdfEmail(requestSearch.getPaymentName(),requestSearch.getPaymentEmail(), consolidatedBase64Report);
            emailNotificationClient.sendPdfByEmail(requestPdfEmail);
            consolidatedResponse = createUtilOutputPort.createConsolidatedResponse(policeAntecedentsResponse, requestSearch);
            consolidatedResponse.setAttorneyOfficeLegend(attorneyOfficeResponse.getData().getLegend());
            consolidatedResponse.setPoliceDetail(policeAntecedentsResponse.getData().getDetails());
            consolidatedResponse.setPublicSpendingWatchdogMessage("La contraloría delegada para responsabilidad fiscal, intervención judicial y cobro coactivo. Ver detalle en pdf adjunto.");
            //log.info("Generated PDF --> "+consolidatedBase64Report);
        } catch (Exception ex){
            log.error("Failed connection person voting service or sent email");
            throw new InternalServerError(ex.getMessage());
        }
        return consolidatedResponse;
    }

    @Override
    public ConsolidatedResponse generateSearchPersonReport(RequestSearch requestSearch) throws Exception {
        ConsolidatedResponse consolidatedResponse;
        try {
            ApiResponse personVotingResponse = searchPersonClient.getInfoPersonVoting(verifikToken, requestSearch.getDocumentType(), requestSearch.getDocumentNumber());
            personVotingResponse.getData().setDocumentType(requestSearch.getDocumentType());
            byte[] bytesMainResponse = createFormatPdfOutputPort.createMainReport(requestSearch.getPaymentName(), requestSearch.getPaymentEmail());
            byte[] bytesPersonVotingResponse = createFormatPdfOutputPort.createSearchPersonReport(personVotingResponse, requestSearch);
            String consolidatedBase64Report = createFormatPdfOutputPort.mergePdfAndReturnBase64(
                    bytesMainResponse,
                    bytesPersonVotingResponse);
            RequestPdfEmail requestPdfEmail = createUtilOutputPort.createRequestPdfEmail(requestSearch.getPaymentName(),requestSearch.getPaymentEmail(), consolidatedBase64Report);
            emailNotificationClient.sendPdfByEmail(requestPdfEmail);
            consolidatedResponse = createUtilOutputPort.createConsolidatedResponse(personVotingResponse, requestSearch);
            MapResponse mapResponse = mapClient.getGeometry(personVotingResponse.getData().getAddress()
                            .concat(",").concat(personVotingResponse.getData().getDepartment())
                            .concat(",").concat(personVotingResponse.getData().getMunicipality()),
                    mapsKey);
            consolidatedResponse.setAddress(personVotingResponse.getData().getAddress());
            consolidatedResponse.setDepartment(personVotingResponse.getData().getDepartment());
            consolidatedResponse.setMunicipality(personVotingResponse.getData().getMunicipality());
            consolidatedResponse.setGeometry(mapResponse.getResults().get(0).getGeometry());
        } catch (Exception ex){
            log.error("Failed connection person voting service or sent email");
            throw new InternalServerError(ex.getMessage());
        }
        return consolidatedResponse;
    }

}
