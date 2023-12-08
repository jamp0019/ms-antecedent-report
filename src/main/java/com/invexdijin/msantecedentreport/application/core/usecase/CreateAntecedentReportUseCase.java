package com.invexdijin.msantecedentreport.application.core.usecase;

import com.invexdijin.msantecedentreport.adapters.out.client.AntecedentReportClient;
import com.invexdijin.msantecedentreport.application.core.domain.request.RequestAntecedentReport;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentesdisciplinarios.ResponseAntecedentesDisciplinarios;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentespoliciales.ResponseAntecedentesPoliciales;
import com.invexdijin.msantecedentreport.application.core.domain.response.contraloria.ResponseContraloria;
import com.invexdijin.msantecedentreport.application.ports.in.CreateAntecedentReportInputPort;
import com.invexdijin.msantecedentreport.application.ports.out.CreateFormatPdfOutputPort;
import com.itextpdf.text.DocumentException;
import net.sf.jasperreports.engine.JRException;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class CreateAntecedentReportUseCase implements CreateAntecedentReportInputPort {

    @Autowired
    private AntecedentReportClient antecedentReportClient;

    @Autowired
    private CreateFormatPdfOutputPort createFormatPdfOutputPort;

    @Value("${token.verifik}")
    private String token;
    @Override
    public String generateAntecedentReport(RequestAntecedentReport requestAntecedentReport) throws DocumentException, IOException, URISyntaxException, JRException, ParseException {

        //ResponseContraloria responseContraloria = antecedentReportClient.getContraloria(token, requestAntecedentReport.getDocumentType(), requestAntecedentReport.getDocumentNumber());
        //ResponseAntecedentesPoliciales responseAntecedentesPoliciales = antecedentReportClient.getAntececentesPoliciales(token, requestAntecedentReport.getDocumentType(), requestAntecedentReport.getDocumentNumber());
        //ResponseAntecedentesDisciplinarios responseAntecedentesDisciplinarios = antecedentReportClient.getAntececentesDisciplinarios(token, requestAntecedentReport.getDocumentType(), requestAntecedentReport.getDocumentNumber());
        //createFormatPdfOutputPort.generatePdfBase64(null);
        createFormatPdfOutputPort.generatePdfWithJasperReport(null);
        return "Ok";
    }
}
