package com.invexdijin.msantecedentreport.application.ports.in;

import com.invexdijin.msantecedentreport.application.core.domain.request.RequestAntecedentReport;
import com.invexdijin.msantecedentreport.application.core.domain.response.ConsolidatedResponse;

public interface CreateAntecedentReportInputPort {

    ConsolidatedResponse generateAntecedentReport(RequestAntecedentReport requestAntecedentReport) throws Exception;
}
