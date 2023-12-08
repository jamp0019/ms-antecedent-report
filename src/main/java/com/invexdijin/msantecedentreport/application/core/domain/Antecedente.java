package com.invexdijin.msantecedentreport.application.core.domain;

import com.invexdijin.msantecedentreport.application.core.domain.response.antecedentesdisciplinarios.Delito;
import lombok.Data;

import java.util.List;

@Data
public class Antecedente {
    public int id;
    public String name;
    public String username;
    public String email;
    public Address address;
    public List<Delito> delitos;
}
