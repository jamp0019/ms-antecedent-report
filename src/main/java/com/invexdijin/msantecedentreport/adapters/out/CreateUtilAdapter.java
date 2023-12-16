package com.invexdijin.msantecedentreport.adapters.out;

import com.invexdijin.msantecedentreport.application.core.domain.request.RequestPdfEmail;
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
    public ConsolidatedResponse createConsolidatedResponse(ApiResponse apiResponse) {
        ConsolidatedResponse consolidatedResponse = new ConsolidatedResponse();
        consolidatedResponse.setFullName(apiResponse.getData().getFullName());
        consolidatedResponse.setFirstName(apiResponse.getData().getFirstName());
        consolidatedResponse.setDocumentType(apiResponse.getData().getDocumentType());
        consolidatedResponse.setDocumentNumber(apiResponse.getData().getDocumentNumber());
        return consolidatedResponse;
    }
}
