package com.invexdijin.msantecedentreport.application.ports.out;

import com.invexdijin.msantecedentreport.application.core.domain.request.RequestPdfEmail;
import com.invexdijin.msantecedentreport.application.core.domain.request.RequestSearch;
import com.invexdijin.msantecedentreport.application.core.domain.response.ConsolidatedResponse;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;

public interface CreateUtilOutputPort {

    RequestPdfEmail createRequestPdfEmail(String name, String email, String base64pdf);
    ConsolidatedResponse createConsolidatedResponse(ApiResponse apiResponse, RequestSearch requestSearch);

}
