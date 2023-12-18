package com.invexdijin.msantecedentreport.application.core.domain.response.searchperson;

import lombok.Data;

import java.util.List;

@Data
public class ResultsItem {

    private String formattedAddress;
    private List<String> types;
    private Geometry geometry;
    private List<AddressComponentsItem> addressComponents;
    private PlusCode plusCode;
    private String placeId;

}
