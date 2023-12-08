package com.invexdijin.msantecedentreport.application.ports.in;

import com.invexdijin.msantecedentreport.application.core.domain.request.RequestAntecedentReport;
import com.itextpdf.text.DocumentException;
import net.sf.jasperreports.engine.JRException;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URISyntaxException;

public interface CreateAntecedentReportInputPort {

    String generateAntecedentReport(RequestAntecedentReport requestAntecedentReport) throws DocumentException, IOException, URISyntaxException, JRException, ParseException;
}
