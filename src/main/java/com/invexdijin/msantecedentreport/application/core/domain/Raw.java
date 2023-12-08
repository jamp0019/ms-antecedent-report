package com.invexdijin.msantecedentreport.application.core.domain;

import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentesdisciplinarios.Delito;
import lombok.Data;

import java.util.List;

@Data
public class Raw {
    private String id;
    private String name;
    private List<Delito> delitos;
}
