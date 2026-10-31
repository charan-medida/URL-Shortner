
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LambdaController {

    private final LambdaService lambdaService;

    public LambdaController(LambdaService lambdaService) {
        this.lambdaService = lambdaService;
    }

    @GetMapping("/lambda/hello")
    public String callLambda(
            @RequestParam(defaultValue = "Student") String name) {

        return lambdaService.invokeLambda(name);
    }
}
