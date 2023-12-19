package com.invexdijin.msantecedentreport.application.ports.in;

import com.invexdijin.msantecedentreport.application.core.domain.request.RequestSearch;
import com.invexdijin.msantecedentreport.application.core.domain.response.ConsolidatedResponse;

public interface CreateReportInputPort {

    ConsolidatedResponse generateAntecedentReport(RequestSearch requestSearch) throws Exception;
    ConsolidatedResponse generateSearchPersonReport(RequestSearch requestSearch) throws Exception;

}
