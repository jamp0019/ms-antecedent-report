package com.invexdijin.msantecedentreport.application.core.domain.response.contraloria;

import lombok.Data;

@Data
public class ResponseContraloria {
    private DataContraloriaResponse data;
    private SignatureContraloriaResponse signature;
    private String id;
}
