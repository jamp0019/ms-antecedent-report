package com.invexdijin.msantecedentreport.application.core.domain.response.antecedentespoliciales;

import lombok.Data;

@Data
public class ResponseAntecedentesPoliciales {
    public DataAntecedentesPolicialesResponse data;
    public SignatureAntecedentesPolicialesResponse signature;
    public String id;
}
