package com.invexdijin.msantecedentreport.application.ports.out;

import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import net.sf.jasperreports.engine.JRException;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface CreateFormatPdfOutputPort {
    String createAttorneyOfficeNoReport(ApiResponse disciplinaryAntecedentsResponse) throws IOException, JRException, ParseException;

    String createMainReport(String addresseeName, String addresseeEmail) throws FileNotFoundException, JRException;

    String createAttorneyOfficeReport(ApiResponse publicSpendingWatchdogResponse) throws JRException;

    String createPoliceReport(ApiResponse policeAntecedentsResponse) throws JRException;
}
