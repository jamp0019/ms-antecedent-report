package com.invexdijin.msantecedentreport.application.core.domain.response.antecedents;

import lombok.Data;

@Data
public class ApiResponse {

    public Info data;
    public Signature signature;
    public String id;

}
