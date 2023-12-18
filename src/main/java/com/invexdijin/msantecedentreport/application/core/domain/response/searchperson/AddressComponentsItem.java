package com.invexdijin.msantecedentreport.application.core.domain.response.searchperson;

import lombok.Data;

import java.util.List;

@Data
public class AddressComponentsItem {

    private List<String> types;
    private String shortName;
    private String longName;

}
