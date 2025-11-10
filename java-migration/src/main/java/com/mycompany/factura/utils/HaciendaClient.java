package com.mycompany.factura.utils;

import com.mycompany.factura.config.properties.HaciendaProperties;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class HaciendaClient {

    private final HaciendaProperties properties;

    public HaciendaClient(HaciendaProperties properties) {
        this.properties = properties;
    }

    public String obtenerToken() {
        HttpPost post = new HttpPost(properties.getTokenUrl());
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("client_id", properties.getClientId()));
        params.add(new BasicNameValuePair("client_secret", properties.getClientSecret()));
        params.add(new BasicNameValuePair("grant_type", "client_credentials"));
        post.setEntity(new UrlEncodedFormEntity(params));
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        return executeRequest(post);
    }

    public String enviarComprobante(String token, String payload) {
        HttpPost post = new HttpPost(properties.getRecepcionUrl());
        post.setHeader("Authorization", "Bearer " + token);
        post.setHeader("Content-Type", "application/json");
        post.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));
        return executeRequest(post);
    }

    public String consultarEstado(String token, String clave) {
        HttpGet get = new HttpGet(properties.getConsultaUrl() + clave);
        get.setHeader("Authorization", "Bearer " + token);
        return executeRequest(get);
    }

    private String executeRequest(HttpUriRequestBase request) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            ClassicHttpResponse response = (ClassicHttpResponse) client.execute(request);
            int status = response.getCode();
            String body = response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : "";
            if (status >= 200 && status < 300) {
                return body;
            }
            throw new IllegalStateException("Error en Hacienda: " + status + " - " + body);
        } catch (IOException e) {
            throw new IllegalStateException("Error comunicándose con Hacienda", e);
        }
    }

    public static String construirPayloadEnvio(Map<String, Object> datos) {
        return new com.fasterxml.jackson.databind.ObjectMapper().valueToTree(datos).toString();
    }
}
