package com.invexdijin.msantecedentreport.adapters.out.client;

import com.invexdijin.msantecedentreport.application.core.domain.request.RequestPdfEmail;
import com.invexdijin.msantecedentreport.application.core.domain.response.antecedents.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "email",
        url = "http://localhost:8081")
public interface EmailNotificationClient {
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/invexdijin/pdf")
    ApiResponse sendPdfByEmail(@RequestBody RequestPdfEmail requestPdfEmail);
}
