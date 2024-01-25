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

        byte[] base64Decoder = new byte[0];
        ApiResponse publicSpendingWatchdogResponse;
        try{
            publicSpendingWatchdogResponse = antecedentReportClient.getInfoPublicSpendingWatchdog(verifikToken, requestSearch.getDocumentType(), requestSearch.getDocumentNumber());
            if(!(publicSpendingWatchdogResponse.getData()==null)){
                String base64PublicSpendingWatchdogResponse = publicSpendingWatchdogResponse.getData().getPdfBase64().split(",")[1];
                base64Decoder = Base64.getDecoder().decode(base64PublicSpendingWatchdogResponse);
            }
        }catch(Exception ex){
            log.error(" Can´t create report Contraloria: {}",ex.getMessage());
        }


        byte[] bytesAttorneyOfficeResponse = new byte[0];
        ApiResponse attorneyOfficeResponse = null;
        try{
            attorneyOfficeResponse = antecedentReportClient.getInfoDisciplinaryAntecedents(verifikToken, requestSearch.getDocumentType(), requestSearch.getDocumentNumber());
            if(!(attorneyOfficeResponse.getData()==null)){
                if(attorneyOfficeResponse.getData().isRequired()){
                    bytesAttorneyOfficeResponse = createFormatPdfOutputPort.createAttorneyOfficeReport(attorneyOfficeResponse);
                }else {
                    bytesAttorneyOfficeResponse = createFormatPdfOutputPort.createAttorneyOfficeNoReport(attorneyOfficeResponse);
                }
            }else{
                attorneyOfficeResponse = null;
            }
        }catch (Exception ex){
            log.error(" Can´t create report Procuraduria: {}",ex.getMessage());
        }

        byte[] bytesPoliceAntecedentsResponse = new byte[0];
        ApiResponse policeAntecedentsResponse = null;
        try{
            policeAntecedentsResponse = antecedentReportClient.getInfoPoliceAntecedents(verifikToken, requestSearch.getDocumentType(), requestSearch.getDocumentNumber());
            if(!(policeAntecedentsResponse.getData()==null))
                bytesPoliceAntecedentsResponse = createFormatPdfOutputPort.createPoliceReport(policeAntecedentsResponse);
            else
                policeAntecedentsResponse = null;
        }catch (Exception ex){
            log.error(" Can´t create report Policia: {}", ex.getMessage());
        }

        byte[] bytesMainResponse = createFormatPdfOutputPort.createMainReport(requestSearch.getPaymentName(), requestSearch.getPaymentEmail());

        String consolidatedBase64Report = createFormatPdfOutputPort.mergePdfAndReturnBase64(
                bytesMainResponse,
                bytesAttorneyOfficeResponse,
                bytesPoliceAntecedentsResponse,
                base64Decoder);

        RequestPdfEmail requestPdfEmail = createUtilOutputPort.createRequestPdfEmail(requestSearch.getPaymentName(),requestSearch.getPaymentEmail(), consolidatedBase64Report);
        emailNotificationClient.sendPdfByEmail(requestPdfEmail);

        ConsolidatedResponse consolidatedResponse;
        consolidatedResponse = createUtilOutputPort.createConsolidatedResponse(policeAntecedentsResponse, requestSearch);
        if(!(attorneyOfficeResponse == null)){
            consolidatedResponse.setAttorneyOfficeLegend(attorneyOfficeResponse.getData().getLegend());
        }else{
            consolidatedResponse.setAttorneyOfficeLegend("No hay información");
        }
        if(!(policeAntecedentsResponse == null)){
            consolidatedResponse.setPoliceDetail(policeAntecedentsResponse.getData().getDetails());
        }else{
            consolidatedResponse.setPoliceDetail("No hay información");
        }
        consolidatedResponse.setPublicSpendingWatchdogMessage("La contraloría delegada para responsabilidad fiscal, intervención judicial y cobro coactivo. Ver detalle en pdf adjunto.");
        //log.info("Generated PDF --> "+consolidatedBase64Report);
        return consolidatedResponse;
    }

    @Override
    public ConsolidatedResponse generateSearchPersonReport(RequestSearch requestSearch) throws Exception {

        byte[] bytesPersonVotingResponse = new byte[0];
        ApiResponse personVotingResponse = null;
        try{
            personVotingResponse = searchPersonClient.getInfoPersonVoting(verifikToken, requestSearch.getDocumentType(), requestSearch.getDocumentNumber());

            if(!(personVotingResponse.getData()==null)){
                personVotingResponse.getData().setDocumentType(requestSearch.getDocumentType());
                bytesPersonVotingResponse = createFormatPdfOutputPort.createSearchPersonReport(personVotingResponse, requestSearch);
            }else{
                personVotingResponse = null;
            }
        }catch(Exception ex){
            log.error(" Can´t create report Busqueda por persona: {}",ex.getMessage());
        }

        byte[] bytesMainResponse = createFormatPdfOutputPort.createMainReport(requestSearch.getPaymentName(), requestSearch.getPaymentEmail());

        String consolidatedBase64Report = createFormatPdfOutputPort.mergePdfAndReturnBase64(
                bytesMainResponse,
                bytesPersonVotingResponse);
        RequestPdfEmail requestPdfEmail = createUtilOutputPort.createRequestPdfEmail(requestSearch.getPaymentName(),requestSearch.getPaymentEmail(), consolidatedBase64Report);
        emailNotificationClient.sendPdfByEmail(requestPdfEmail);
        ConsolidatedResponse consolidatedResponse;
        consolidatedResponse = createUtilOutputPort.createConsolidatedResponse(personVotingResponse, requestSearch);
        if(!(personVotingResponse==null)){
            MapResponse mapResponse = mapClient.getGeometry(personVotingResponse.getData().getAddress()
                            .concat(",").concat(personVotingResponse.getData().getDepartment())
                            .concat(",").concat(personVotingResponse.getData().getMunicipality()),
                    mapsKey);
            consolidatedResponse.setAddress(personVotingResponse.getData().getAddress());
            consolidatedResponse.setDepartment(personVotingResponse.getData().getDepartment());
            consolidatedResponse.setMunicipality(personVotingResponse.getData().getMunicipality());
            consolidatedResponse.setGeometry(mapResponse.getResults().get(0).getGeometry());
        }else{
            consolidatedResponse.setAddress("No hay información");
            consolidatedResponse.setDepartment("No hay información");
            consolidatedResponse.setMunicipality("No hay información");
            consolidatedResponse.setGeometry(null);
        }
        return consolidatedResponse;
    }

}
