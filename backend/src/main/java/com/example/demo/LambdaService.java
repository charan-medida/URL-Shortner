
package com.example.demo;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class LambdaService {

    private final RestClient restClient = RestClient.create();

    public String invokeLambda(String name) {

        String encodedName = URLEncoder.encode(
                name, StandardCharsets.UTF_8);

        String url = "https://2bien6laeh.execute-api.ap-south-1.amazonaws.com/hello?name="
                + encodedName;

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }
}
