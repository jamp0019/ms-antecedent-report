package com.invexdijin.msantecedentreport.application.ports.out;

import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import com.itextpdf.text.DocumentException;
import net.sf.jasperreports.engine.JRException;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface CreateFormatPdfOutputPort {
    byte[] createAttorneyOfficeNoReport(ApiResponse disciplinaryAntecedentsResponse) throws IOException, JRException, ParseException;

    byte[] createMainReport(String addresseeName, String addresseeEmail) throws Exception;

    byte[] createAttorneyOfficeReport(ApiResponse publicSpendingWatchdogResponse) throws JRException;

    byte[] createPoliceReport(ApiResponse policeAntecedentsResponse) throws JRException;

    String mergePdfAndReturnBase64(byte[]... pdfBytesArray) throws DocumentException, IOException;
}
