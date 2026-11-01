
package com.example.demo;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sfn.SfnClient;
import software.amazon.awssdk.services.sfn.model.StartExecutionRequest;
import software.amazon.awssdk.services.sfn.model.StartExecutionResponse;

@Service
public class StepFunctionsService {

    private final SfnClient sfnClient = SfnClient.builder()
            .region(Region.AP_SOUTH_1)
            .build();

    private static final String STATE_MACHINE_ARN =
            "arn:aws:states:ap-south-1:661491975101:stateMachine:PrepStateMeachine";

    public String startWorkflow(String name) {

        String input = String.format(
                "{\"name\":\"%s\",\"message\":\"Started from Spring Boot\"}",
                name.replace("\\", "\\\\").replace("\"", "\\\"")
        );

        StartExecutionRequest request = StartExecutionRequest.builder()
                .stateMachineArn(STATE_MACHINE_ARN)
                .input(input)
                .build();

        StartExecutionResponse response =
                sfnClient.startExecution(request);

        return response.executionArn();
    }
}
