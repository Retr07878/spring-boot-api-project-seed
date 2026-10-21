package com.retr0.apispringboot.health;

import com.retr0.apispringboot.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
    public ApiResponse<HealthResponse> health(){
        return ApiResponse.success(new HealthResponse("UP"));
    }
    public record HealthResponse(String status){}
}
