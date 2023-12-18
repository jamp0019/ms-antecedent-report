package com.invexdijin.msantecedentreport.application.core.domain.response.searchperson;

import lombok.Data;
import java.util.List;
@Data
public class MapResponse {

    private List<ResultsItem> results;
    private String status;

}
