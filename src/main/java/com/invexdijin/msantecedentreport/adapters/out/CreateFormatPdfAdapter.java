package com.invexdijin.msantecedentreport.adapters.out;

import com.google.gson.Gson;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import com.invexdijin.msantecedentreport.application.ports.out.CreateFormatPdfOutputPort;

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
    public String createMainReport(String addresseeName, String addresseeEmail) throws FileNotFoundException, JRException {

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

        JasperPrint MainJasperPrint = JasperFillManager.fillReport(mainJasperReport, parameters, new JREmptyDataSource());
        byte[] reportContent = JasperExportManager.exportReportToPdf(MainJasperPrint);
        return Base64.getEncoder().encodeToString(reportContent);
    }

    @Override
    public String createAttorneyOfficeReport(ApiResponse publicSpendingWatchdogResponse) throws JRException {
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

        byte[] reportContent = JasperExportManager.exportReportToPdf(jsonPrintDetail);
        return Base64.getEncoder().encodeToString(reportContent);
    }

    @Override
    public String createPoliceReport(ApiResponse policeAntecedentsResponse) throws JRException {

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

        //Set report data
        String[] details = policeAntecedentsResponse.getData().getDetails().split("\n");

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        String strDate = simpleDateFormat.format(date);
        String[] fullDate = strDate.split(" ");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("details_1", details[0]);
        parameters.put("details_2", details[1]+"\n\n"+details[3]);
        parameters.put("hour", fullDate[1]+" "+fullDate[2]);
        parameters.put("date", fullDate[0]);
        parameters.put("identification", policeAntecedentsResponse.getData().getDocumentNumber());
        parameters.put("full_name", policeAntecedentsResponse.getData().getFullName());
        parameters.put("details_3",policeAntecedentsResponse.getData().getLegend());
        parameters.put("details_4","Si tiene alguna duda con el resultado, consulte las preguntas frecuentes o acérquese a las\n" +
                "instalaciones de la Policía Nacional más cercanas.");
        JasperPrint jsonPrintDetail = JasperFillManager.fillReport(policeReport, parameters, new JREmptyDataSource());

        byte[] reportContent = JasperExportManager.exportReportToPdf(jsonPrintDetail);
        return Base64.getEncoder().encodeToString(reportContent);
    }

    @Override
    public String createAttorneyOfficeNoReport(ApiResponse disciplinaryAntecedentsResponse) throws IOException, JRException, ParseException {

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

        byte[] reportContent = JasperExportManager.exportReportToPdf(jsonPrintDetail);
        return Base64.getEncoder().encodeToString(reportContent);
    }

}
