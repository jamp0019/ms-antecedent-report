package com.invexdijin.msantecedentreport.adapters.out;

import com.google.gson.Gson;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import com.invexdijin.msantecedentreport.application.ports.out.CreateFormatPdfOutputPort;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import java.io.*;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class CreateFormatPdfAdapter implements CreateFormatPdfOutputPort {

    @Override
    public byte[] createMainReport(String addresseeName, String addresseeEmail) throws Exception {

        JasperReport mainJasperReport;
        try {

            mainJasperReport = (JasperReport) JRLoader.loadObject(ResourceUtils.getFile("MainReport.jasper"));
        } catch (FileNotFoundException | JRException e) {
            try {
                File file = ResourceUtils.getFile("classpath:MainReport.jrxml");
                mainJasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(mainJasperReport, "MainReport.jasper");
            } catch (FileNotFoundException | JRException ex) {
                throw new RuntimeException(e);
            }
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name_addressee", addresseeName);
        parameters.put("email", addresseeEmail);

        JasperPrint mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, parameters, new JREmptyDataSource());

        return JasperExportManager.exportReportToPdf(mainJasperPrint);
    }

    @Override
    public byte[] createSearchPersonReport(ApiResponse searchPersonResponse) throws JRException {

        JasperReport searchPersonJasperReport;
        try {

            searchPersonJasperReport = (JasperReport) JRLoader.loadObject(ResourceUtils.getFile("SearchReport.jasper"));
        } catch (FileNotFoundException | JRException e) {
            try {
                File file = ResourceUtils.getFile("classpath:SearchReport.jrxml");
                searchPersonJasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(searchPersonJasperReport, "SearchReport.jasper");
            } catch (FileNotFoundException | JRException ex) {
                throw new RuntimeException(e);
            }
        }
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("documentType", searchPersonResponse.getData().getDocumentType());
        parameters.put("documentNumber", searchPersonResponse.getData().getDocumentNumber());
        parameters.put("fullname", searchPersonResponse.getData().getFullName());
        parameters.put("state", "Vígente");
        parameters.put("address", searchPersonResponse.getData().getAddress());
        parameters.put("department", searchPersonResponse.getData().getDepartment());
        parameters.put("municipality", searchPersonResponse.getData().getMunicipality());

        JasperPrint searchPersonJasperPrint = JasperFillManager.fillReport(searchPersonJasperReport, parameters, new JREmptyDataSource());

        return JasperExportManager.exportReportToPdf(searchPersonJasperPrint);

    }

    @Override
    public byte[] createAttorneyOfficeReport(ApiResponse publicSpendingWatchdogResponse) throws JRException {
        JasperReport attorneyOfficeReport;
        try {

            attorneyOfficeReport = (JasperReport) JRLoader.loadObject(ResourceUtils.getFile("ProcuraduriaReport.jasper"));
        } catch (FileNotFoundException | JRException e) {
            try {
                File file = ResourceUtils.getFile("classpath:ProcuraduriaReport.jrxml");
                attorneyOfficeReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(attorneyOfficeReport, "ProcuraduriaReport.jasper");
            } catch (FileNotFoundException | JRException ex) {
                throw new RuntimeException(e);
            }
        }
        Gson gson = new Gson();
        String jsonArray = gson.toJson(publicSpendingWatchdogResponse.getData().getAntecedentes());

        InputStream inputStream = new ByteArrayInputStream(jsonArray.getBytes());
        JsonDataSource ds = new JsonDataSource(inputStream);

        Map<String, Object> params = new HashMap<>();
        JasperPrint jsonPrintDetail = JasperFillManager.fillReport(attorneyOfficeReport, params, ds);

        return JasperExportManager.exportReportToPdf(jsonPrintDetail);
    }

    @Override
    public byte[] createPoliceReport(ApiResponse policeAntecedentsResponse) throws JRException {

        JasperReport policeReport;
        try {

            policeReport = (JasperReport) JRLoader.loadObject(ResourceUtils.getFile("PoliciaReport.jasper"));
        } catch (FileNotFoundException | JRException e) {
            try {
                File file = ResourceUtils.getFile("classpath:PoliciaReport.jrxml");
                policeReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(policeReport, "PoliciaReport.jasper");
            } catch (FileNotFoundException | JRException ex) {
                throw new RuntimeException(e);
            }
        }

        String[] details = policeAntecedentsResponse.getData().getDetails().split("\n");

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        String strDate = simpleDateFormat.format(date);
        String[] fullDate = strDate.split(" ");

        Map<String, Object> parameters = new HashMap<>();
        if(details.length==1){
            parameters.put("details_1", details[0]);
            parameters.put("details_2", "");
        }else{
            parameters.put("details_1", details[0]);
            parameters.put("details_2", details[1]+"\n\n"+details[3]);
        }
        parameters.put("hour", fullDate[1]+" "+fullDate[2]);
        parameters.put("date", fullDate[0]);
        parameters.put("identification", policeAntecedentsResponse.getData().getDocumentNumber());
        parameters.put("full_name", policeAntecedentsResponse.getData().getFullName());
        parameters.put("details_3",policeAntecedentsResponse.getData().getLegend());
        parameters.put("details_4","Si tiene alguna duda con el resultado, consulte las preguntas frecuentes o acérquese a las\n" +
                "instalaciones de la Policía Nacional más cercanas.");
        JasperPrint jsonPrintDetail = JasperFillManager.fillReport(policeReport, parameters, new JREmptyDataSource());

        return JasperExportManager.exportReportToPdf(jsonPrintDetail);
    }

    @Override
    public byte[] createAttorneyOfficeNoReport(ApiResponse disciplinaryAntecedentsResponse) throws IOException, JRException, ParseException {

        JasperReport attorneyOfficeNoReport;
        try {

            attorneyOfficeNoReport = (JasperReport) JRLoader.loadObject(ResourceUtils.getFile("ProcuraduriaNoReport.jasper"));
        } catch (FileNotFoundException | JRException e) {
            try {
                File file = ResourceUtils.getFile("classpath:ProcuraduriaNoReport.jrxml");
                attorneyOfficeNoReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(attorneyOfficeNoReport, "ProcuraduriaNoReport.jasper");
            } catch (FileNotFoundException | JRException ex) {
                throw new RuntimeException(e);
            }
        }

        DateFormatSymbols sym = DateFormatSymbols.getInstance(new Locale("es","ar"));
        sym.setMonths(new String[]{"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre" });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd' de 'MMMM' del 'yyyy", sym);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        String strDate = simpleDateFormat.format(new Date());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("date", strDate);
        parameters.put("details", "Consulta en línea de Antecedentes Disciplinarios, La Procuraduria General de la Nacion certifica Que siendo las 16 horas del 02/10/2023 el Señor(a) JOHN ALEXANDER MARTINEZ PINTO identificado(a) con Cédula de ciudadanía Número 1024530679 El ciudadano no presenta antecedentes.");
        JasperPrint jsonPrintDetail = JasperFillManager.fillReport(attorneyOfficeNoReport, parameters, new JREmptyDataSource());

        return JasperExportManager.exportReportToPdf(jsonPrintDetail);
    }

    @Override
    public String mergePdfAndReturnBase64(byte[]... pdfBytesArray) throws DocumentException, IOException {

        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfCopy copy = new PdfCopy(document, outputStream);
        document.open();

        for (byte[] pdfBytes : pdfBytesArray) {
            PdfReader reader = new PdfReader(pdfBytes);
            copy.addDocument(reader);
        }

        document.close();
        outputStream.close();

        byte[] mergedPdfBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(mergedPdfBytes);
    }

}
