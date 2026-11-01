
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StepFunctionsController {

    private final StepFunctionsService stepFunctionsService;

    public StepFunctionsController(
            StepFunctionsService stepFunctionsService) {
        this.stepFunctionsService = stepFunctionsService;
    }

    @GetMapping("/workflow/hello")
    public String startWorkflow(
            @RequestParam(defaultValue = "Student") String name) {

        return stepFunctionsService.startWorkflow(name);
    }
}
