package com.invexdijin.msantecedentreport.adapters.out.client;

import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import com.invexdijin.msantecedentreport.application.core.domain.response.searchperson.MapResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "map",
        url = "https://maps.googleapis.com")
public interface MapClient {
    @RequestMapping(method = RequestMethod.GET, value = "/maps/api/geocode/json")
    MapResponse getGeometry(@RequestParam("address") String address,
                                    @RequestParam("key") String key);

}
