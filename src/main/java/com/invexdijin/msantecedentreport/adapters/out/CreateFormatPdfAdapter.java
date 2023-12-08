package com.invexdijin.msantecedentreport.adapters.out;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.invexdijin.msantecedentreport.application.core.domain.Raw;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentesdisciplinarios.Antecedente;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentesdisciplinarios.Delito;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentesdisciplinarios.ResponseAntecedentesDisciplinarios;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentesdisciplinarios.Sancion;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentespoliciales.DataAntecedentesPolicialesResponse;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentespoliciales.ResponseAntecedentesPoliciales;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentespoliciales.SignatureAntecedentesPolicialesResponse;
import com.invexdijin.msantecedentreport.application.ports.out.CreateFormatPdfOutputPort;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import net.sf.jasperreports.types.date.DateRangeQueryClauseExtensions;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import org.json.simple.JSONArray;

@Component
public class CreateFormatPdfAdapter implements CreateFormatPdfOutputPort {

    @Override
    public String generatePdfWithJasperReport(ResponseAntecedentesDisciplinarios responseAntecedentesDisciplinarios) throws IOException, JRException, ParseException {


        /*try {

            jasperReportMain = (JasperReport) JRLoader.loadObject(ResourceUtils.getFile("item-report.jasper"));
        } catch (FileNotFoundException | JRException e) {
            try {

                File file = ResourceUtils.getFile("classpath:MainReport.jrxml");
                jasperReportMain = JasperCompileManager.compileReport(file.getAbsolutePath());
                JRSaver.saveObject(jasperReportMain, "item-report.jasper");
            } catch (FileNotFoundException | JRException ex) {
                throw new RuntimeException(e);
            }
        }*/
        /*
        JasperReport mainJasperReport;
        File file = ResourceUtils.getFile("classpath:MainReport.jrxml");
        mainJasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name_addressee","John Alexander Martinez Pinto");
        parameters.put("email", "john1992alex@gmail.com");

        JasperPrint MainJasperPrint = JasperFillManager.fillReport(mainJasperReport, parameters, new JREmptyDataSource());
        byte[] reportContent = JasperExportManager.exportReportToPdf(MainJasperPrint);
        String encodedImage = Base64.getEncoder().encodeToString(reportContent);
        return "Ok";*/

        /*
        JasperReport contraloriaJasperReport;
        File fileProcuraduriaReport = ResourceUtils.getFile("classpath:ProcuraduriaReport.jrxml");
        contraloriaJasperReport = JasperCompileManager.compileReport(fileProcuraduriaReport.getAbsolutePath());

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("C:/Proyecto/backend-services/ms-antecedent-report/src/main/resources/ProcuraduriaReport.json"));
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray antecedentJsonArray = (JSONArray) jsonObject.get("antecedentes");
        String stringAntecedentJsonArray = antecedentJsonArray.toJSONString();
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Antecedente>>() {}.getType();

        List<Antecedente> gsonList = gson.fromJson(stringAntecedentJsonArray, listType);
        String jsonArray = gson.toJson(gsonList);


        InputStream inputStream = new ByteArrayInputStream(jsonArray.getBytes());
        JsonDataSource ds = new JsonDataSource(inputStream);

        Map<String, Object> params = new HashMap<>();
        JasperPrint jsonPrintDetail = JasperFillManager.fillReport(contraloriaJasperReport, params, ds);

        byte[] reportContent = JasperExportManager.exportReportToPdf(jsonPrintDetail);
        String encodedImage = Base64.getEncoder().encodeToString(reportContent);
        return "Ok";*/

        JasperReport jsonReportDetail;
        File jsonFileDetail = ResourceUtils.getFile("classpath:PoliciaReport.jrxml");
        jsonReportDetail = JasperCompileManager.compileReport(jsonFileDetail.getAbsolutePath());

        ResponseAntecedentesPoliciales rp = new ResponseAntecedentesPoliciales();
        DataAntecedentesPolicialesResponse data = new DataAntecedentesPolicialesResponse();
        data.setDetails("NO TIENE ASUNTOS PENDIENTES CON LAS AUTORIDADES JUDICIALES\nde conformidad con lo establecido en el artículo 248 de la Constitución Política de Colombia. \n\nEn cumplimiento de la Sentencia SU-458 del 21 de junio de 2012, proferida por la Honorable Corte Constitucional, la leyenda “NO TIENE ASUNTOS PENDIENTES CON LAS AUTORIDADES JUDICIALES” aplica para todas aquellas personas que no registran antecedentes y para quienes la autoridad judicial competente haya decretado la extinción de la condena o la prescripción de la pena.");
        data.setLegend("Esta consulta es válida siempre y cuando el número de identificación y nombres, correspondan con el documento de identidad registrado y solo aplica para el territorio colombiano de acuerdo a lo establecido en el ordenamiento constitucional.");
        SignatureAntecedentesPolicialesResponse signature = new SignatureAntecedentesPolicialesResponse();
        rp.setId("1234");
        rp.setData(data);
        rp.setSignature(signature);
        //Set report data
        String[] details = rp.getData().getDetails().split("\n");

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
        parameters.put("identification", "1024530679");
        parameters.put("full_name", "JOHN ALEXANDER MARTINEZ PINTO");
        parameters.put("details_3",rp.getData().getLegend());
        parameters.put("details_4","Si tiene alguna duda con el resultado, consulte las preguntas frecuentes o acérquese a las\n" +
                "instalaciones de la Policía Nacional más cercanas.");
        //Fill report
        JasperPrint jsonPrintDetail = JasperFillManager.fillReport(jsonReportDetail, parameters, new JREmptyDataSource());

        byte[] reportContent = JasperExportManager.exportReportToPdf(jsonPrintDetail);
        String encodedImage = Base64.getEncoder().encodeToString(reportContent);
        return "Ok";
    }


}
