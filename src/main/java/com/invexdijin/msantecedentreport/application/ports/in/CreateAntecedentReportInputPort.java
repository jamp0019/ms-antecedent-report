package com.invexdijin.msantecedentreport.application.ports.in;

import com.invexdijin.msantecedentreport.application.core.domain.request.RequestAntecedentReport;
import net.sf.jasperreports.engine.JRException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface CreateAntecedentReportInputPort {

    String generateAntecedentReport(RequestAntecedentReport requestAntecedentReport) throws Exception;
}
