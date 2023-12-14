package com.invexdijin.msantecedentreport.application.core.domain.request;

import lombok.Data;

@Data
public class RequestPdfEmail {

    private String name;
    private String email;
    private String message;
    private String pdfBase64;

    public RequestPdfEmail(){
        this.message="Sending the next information";
    }

}
