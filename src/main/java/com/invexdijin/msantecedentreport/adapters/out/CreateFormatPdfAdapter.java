package com.invexdijin.msantecedentreport.adapters.out;

import com.google.gson.Gson;
import com.invexdijin.msantecedentreport.application.core.domain.request.RequestSearch;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import com.invexdijin.msantecedentreport.application.ports.out.CreateFormatPdfOutputPort;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class CreateFormatPdfAdapter implements CreateFormatPdfOutputPort {

    @Override
    public byte[] createMainReport(String addresseeName, String addresseeEmail) throws Exception {

        /*try {
            log.info("Creating PoliciaReport.jasper");
            File file = ResourceUtils.getFile("classpath:PoliciaReport.jrxml");
            mainJasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRSaver.saveObject(mainJasperReport, "PoliciaReport.jasper");
            log.info("Creation success!!!");

            log.info("Creating ProcuraduriaNoReport.jrxml");
            File file1 = ResourceUtils.getFile("classpath:ProcuraduriaNoReport.jrxml");
            mainJasperReport = JasperCompileManager.compileReport(file1.getAbsolutePath());
            JRSaver.saveObject(mainJasperReport, "ProcuraduriaNoReport.jasper");
            log.info("Creation success!!!");

            log.info("Creating ProcuraduriaReport.jrxml");
            File file2 = ResourceUtils.getFile("classpath:ProcuraduriaReport.jrxml");
            mainJasperReport = JasperCompileManager.compileReport(file2.getAbsolutePath());
            JRSaver.saveObject(mainJasperReport, "ProcuraduriaReport.jasper");
            log.info("Creation success!!!");

            log.info("Creating SearchReport.jrxml");
            File file3 = ResourceUtils.getFile("classpath:SearchReport.jrxml");
            mainJasperReport = JasperCompileManager.compileReport(file3.getAbsolutePath());
            JRSaver.saveObject(mainJasperReport, "SearchReport.jasper");
            log.info("Creation success!!!");

            mainJasperReport = (JasperReport) JRLoader.loadObject(Objects.requireNonNull(getClass().getResource("/jasper/MainReport.jasper")));
        } catch (JRException e) {
            log.error("MainReport.jasper no exist!!!");
            try {
                log.info("Creating MainReport.jasper");
                File file = ResourceUtils.getFile("classpath:MainReport.jrxml");
                mainJasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(mainJasperReport, "jasper/MainReport.jasper");
                log.info("Creation success!!!");
            } catch (FileNotFoundException | JRException ex) {
                log.error("No can't open or create MainReport.jasper");
                throw new RuntimeException(e);
            }
        }*/
        JasperReport mainJasperReport;
        mainJasperReport = (JasperReport) JRLoader.loadObject(Objects.requireNonNull(getClass().getResource("/jasper/MainReport.jasper")));
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name_addressee", addresseeName);
        parameters.put("email", addresseeEmail);

        JasperPrint mainJasperPrint = JasperFillManager.fillReport(mainJasperReport, parameters, new JREmptyDataSource());
        log.info("Jasper report main has been created successful");
        return JasperExportManager.exportReportToPdf(mainJasperPrint);
    }

    @Override
    public byte[] createSearchPersonReport(ApiResponse searchPersonResponse, RequestSearch requestSearch) throws JRException {

        JasperReport searchPersonJasperReport;
        searchPersonJasperReport = (JasperReport) JRLoader.loadObject(Objects.requireNonNull(getClass().getResource("/jasper/SearchReport.jasper")));
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("documentType", searchPersonResponse.getData().getDocumentType());
        parameters.put("documentNumber", searchPersonResponse.getData().getDocumentNumber());
        parameters.put("fullname", requestSearch.getSearchFullName());
        parameters.put("state", "Vígente");
        parameters.put("address", searchPersonResponse.getData().getAddress());
        parameters.put("department", searchPersonResponse.getData().getDepartment());
        parameters.put("municipality", searchPersonResponse.getData().getMunicipality());

        JasperPrint searchPersonJasperPrint = JasperFillManager.fillReport(searchPersonJasperReport, parameters, new JREmptyDataSource());
        log.info("Search person jasper report has been created successful");
        return JasperExportManager.exportReportToPdf(searchPersonJasperPrint);

    }

    @Override
    public byte[] createAttorneyOfficeReport(ApiResponse publicSpendingWatchdogResponse) throws JRException {

        JasperReport attorneyOfficeReport;
        attorneyOfficeReport = (JasperReport) JRLoader.loadObject(Objects.requireNonNull(getClass().getResource("/jasper/ProcuraduriaReport.jasper")));
        Gson gson = new Gson();
        String jsonArray = gson.toJson(publicSpendingWatchdogResponse.getData().getAntecedentes());

        InputStream inputStream = new ByteArrayInputStream(jsonArray.getBytes());
        JsonDataSource ds = new JsonDataSource(inputStream);

        Map<String, Object> params = new HashMap<>();
        JasperPrint jsonPrintDetail = JasperFillManager.fillReport(attorneyOfficeReport, params, ds);
        log.info("Procuraduria jasper report has been created successful");
        return JasperExportManager.exportReportToPdf(jsonPrintDetail);
    }

    @Override
    public byte[] createPoliceReport(ApiResponse policeAntecedentsResponse) throws JRException {

        JasperReport policeReport;
        policeReport = (JasperReport) JRLoader.loadObject(Objects.requireNonNull(getClass().getResource("/jasper/PoliciaReport.jasper")));
        String[] details = policeAntecedentsResponse.getData().getDetails().split("\n");

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        String strDate = simpleDateFormat.format(date);
        String[] fullDate = strDate.split(" ");

        Map<String, Object> parameters = new HashMap<>();
        if(details.length==1){
            parameters.put("details_1", details[0]);
            parameters.put("details_2", " ");
        }else{
            parameters.put("details_1", details[0]);
            parameters.put("details_2", details[1]+"\n\n"+details[3]);
        }
        parameters.put("hour", fullDate[1]+" "+fullDate[2]);
        parameters.put("date", fullDate[0]);
        parameters.put("identification", policeAntecedentsResponse.getData().getDocumentNumber());
        parameters.put("full_name", policeAntecedentsResponse.getData().getFullName());
        if(policeAntecedentsResponse.getData().getLegend()==null){
            parameters.put("details_3","Esta consulta es válida siempre y cuando el número de identificación y nombres, correspondan con el documento de identidad registrado y solo aplica para el territorio colombiano de acuerdo a lo establecido en el ordenamiento constitucional.");
        }else{
            parameters.put("details_3",policeAntecedentsResponse.getData().getLegend());
        }
        parameters.put("details_4","Si tiene alguna duda con el resultado, consulte las preguntas frecuentes o acérquese a las\n" +
                "instalaciones de la Policía Nacional más cercanas.");
        JasperPrint jsonPrintDetail = JasperFillManager.fillReport(policeReport, parameters, new JREmptyDataSource());
        log.info("Police jasper report has been created successful");
        return JasperExportManager.exportReportToPdf(jsonPrintDetail);
    }

    @Override
    public byte[] createAttorneyOfficeNoReport(ApiResponse disciplinaryAntecedentsResponse) throws JRException {

        JasperReport attorneyOfficeNoReport;
        attorneyOfficeNoReport = (JasperReport) JRLoader.loadObject(Objects.requireNonNull(getClass().getResource("/jasper/ProcuraduriaNoReport.jasper")));

        DateFormatSymbols sym = DateFormatSymbols.getInstance(new Locale("es","ar"));
        sym.setMonths(new String[]{"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre" });
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd' de 'MMMM' del 'yyyy", sym);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        String strDate = simpleDateFormat.format(new Date());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("date", strDate);
        parameters.put("details", disciplinaryAntecedentsResponse.getData().getLegend());
        JasperPrint jsonPrintDetail = JasperFillManager.fillReport(attorneyOfficeNoReport, parameters, new JREmptyDataSource());
        log.info("Procuraduria no jasper report has been created successful");
        return JasperExportManager.exportReportToPdf(jsonPrintDetail);
    }

    @Override
    public String mergePdfAndReturnBase64(byte[]... pdfBytesArray) throws DocumentException, IOException {

        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfCopy copy = new PdfCopy(document, outputStream);
        document.open();

        for (byte[] pdfBytes : pdfBytesArray) {
            if(!(pdfBytes.length ==0)){
                PdfReader reader = new PdfReader(pdfBytes);
                copy.addDocument(reader);
            }
        }

        document.close();
        outputStream.close();
        byte[] mergedPdfBytes = outputStream.toByteArray();
        log.info("Consolited PDFs has been created.");
        return Base64.getEncoder().encodeToString(mergedPdfBytes);
    }

}
