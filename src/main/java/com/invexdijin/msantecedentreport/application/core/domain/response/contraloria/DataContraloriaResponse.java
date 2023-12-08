package com.invexdijin.msantecedentreport.application.core.domain.response.contraloria;

import java.util.Date;

@lombok.Data
public class DataContraloriaResponse {

    public String documentType;
    public String documentNumber;
    public Date searchDate;
    public String pdfBase64;
}
