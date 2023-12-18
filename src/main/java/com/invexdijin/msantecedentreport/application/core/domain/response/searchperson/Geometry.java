package com.invexdijin.msantecedentreport.application.core.domain.response.searchperson;

import lombok.Data;

@Data
public class Geometry {

    private Viewport viewport;
    private Location location;
    private String locationType;

}
