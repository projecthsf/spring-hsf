package io.github.projecthsf.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.github.projecthsf.annotation.HsfConsumer;
import io.github.projecthsf.binding.HsfRequestBody;
import io.github.projecthsf.property.HsfConsumerProperty;
import io.github.projecthsf.util.HsfJsonUtil;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class HsfConsumerService {
    private final HsfConsumerProperty property;

    public Object invokeProvider(Object proxy, Method method, Object[] args, HsfConsumer annotation) throws Exception {
        String providerUrl = getProviderUrl(method.getDeclaringClass());
        URL url = new URL(providerUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST"); // PUT is another valid option
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        HsfRequestBody body = new HsfRequestBody();
        body.setClassInterface(method.getDeclaringClass().getName());
        body.setMethod(method.getName());
        body.setVersion(annotation.version());
        body.setParams(args);
        body.setParamTypes(method.getParameterTypes());
        String requestBody = HsfJsonUtil.serialize(body);
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
            log.info("Invoke provider failed: {}\nRequest: {}\nResponse Code: {}", providerUrl, requestBody, con.getResponseCode());
            throw new IOException("Provider response code: " + con.getResponseCode());
        }

        StringBuilder response = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

        log.info("Invoke provider success: {}\nRequest: {}\nResponse: {}", providerUrl, requestBody, response);
        return HsfJsonUtil.deserialize(response.toString(), method.getGenericReturnType());
    }

    private String getProviderUrl(Class<?> clazz) throws IllegalStateException {
        if (property.getCustomer() == null) {
            throw new IllegalStateException("Property spring.hsf.consumer.host is required but could not found");
        }

        String rootPath = property.getCustomer().getRootPath() == null ?  "/hsf" : property.getCustomer().getRootPath();

        if (property.getCustomer().getHosts() == null) {
            return property.getCustomer().getHost() + rootPath;
        }

        return property.getCustomer().getHosts().getOrDefault(clazz.getName(), property.getCustomer().getHost()) + rootPath;
    }
}
