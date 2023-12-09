package com.invexdijin.msantecedentreport.application.core.domain.response.antecedents;

import lombok.Data;

import java.util.List;

@Data
public class Antecedente {
    private String name;
    private List<Sancion> sanciones;
    private List<Delito> delitos;
    private List<Instancia> instancias;
}
