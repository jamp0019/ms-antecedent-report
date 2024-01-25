package com.invexdijin.msantecedentreport.adapters.out;

import com.invexdijin.msantecedentreport.application.core.domain.request.RequestPdfEmail;
import com.invexdijin.msantecedentreport.application.core.domain.request.RequestSearch;
import com.invexdijin.msantecedentreport.application.core.domain.response.ConsolidatedResponse;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import com.invexdijin.msantecedentreport.application.ports.out.CreateUtilOutputPort;
import org.springframework.stereotype.Component;

@Component
public class CreateUtilAdapter implements CreateUtilOutputPort {
    @Override
    public RequestPdfEmail createRequestPdfEmail(String name, String email, String base64pdf) {
        RequestPdfEmail requestPdfEmail = new RequestPdfEmail();
        requestPdfEmail.setName(name);
        requestPdfEmail.setEmail(email);
        requestPdfEmail.setPdfBase64(base64pdf);
        return requestPdfEmail;
    }

    @Override
    public ConsolidatedResponse createConsolidatedResponse(ApiResponse apiResponse, RequestSearch requestSearch) {
        ConsolidatedResponse consolidatedResponse = new ConsolidatedResponse();
        consolidatedResponse.setFullName(requestSearch.getSearchFullName());
        consolidatedResponse.setFirstName(requestSearch.getSearchName());
        consolidatedResponse.setLastName(requestSearch.getSearchLastName());
        if(!(apiResponse == null)){
            consolidatedResponse.setDocumentType(apiResponse.getData().getDocumentType());
            consolidatedResponse.setDocumentNumber(apiResponse.getData().getDocumentNumber());
        }else{
            consolidatedResponse.setDocumentType("No hay información");
            consolidatedResponse.setDocumentNumber("No hay información");
        }
        return consolidatedResponse;
    }
}
