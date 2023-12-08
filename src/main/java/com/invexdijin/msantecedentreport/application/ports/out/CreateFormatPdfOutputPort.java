package com.invexdijin.msantecedentreport.application.ports.out;

import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentesdisciplinarios.ResponseAntecedentesDisciplinarios;
import com.itextpdf.text.DocumentException;
import net.sf.jasperreports.engine.JRException;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public interface CreateFormatPdfOutputPort {
    String generatePdfWithJasperReport(ResponseAntecedentesDisciplinarios responseAntecedentesDisciplinarios) throws IOException, JRException, ParseException;
}
