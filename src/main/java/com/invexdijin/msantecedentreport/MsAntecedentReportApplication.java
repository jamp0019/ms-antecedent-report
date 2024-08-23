package com.invexdijin.msantecedentreport;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRSaver;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.util.ResourceUtils;

import java.io.File;

@SpringBootApplication
@EnableFeignClients
@Slf4j
public class MsAntecedentReportApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(MsAntecedentReportApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*JasperReport policeJasperReport;
		log.info("Creating PoliciaReport.jasper");
		File file = ResourceUtils.getFile("classpath:SearchReport.jrxml");
		policeJasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
		JRSaver.saveObject(policeJasperReport, "SearchReport.jasper");
		log.info("Creation success!!!");*/
	}
}
