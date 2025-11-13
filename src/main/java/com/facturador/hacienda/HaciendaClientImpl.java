package com.facturador.hacienda;

import com.facturador.hacienda.dto.HaciendaStatusResponse;
import com.facturador.hacienda.dto.HaciendaSubmissionResponse;
import com.facturador.models.tenant.Tenant;
import com.facturador.storage.EncryptionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HaciendaClientImpl implements HaciendaClient {

    private final WebClient haciendaClient;
    private final WebClient tokenClient;
    private final HaciendaProperties properties;
    private final EncryptionService encryptionService;

    public HaciendaClientImpl(WebClient.Builder builder, HaciendaProperties properties, EncryptionService encryptionService) {
        this.properties = properties;
        this.encryptionService = encryptionService;
        this.haciendaClient = builder.baseUrl(properties.getBaseUrl()).build();
        this.tokenClient = builder.baseUrl(properties.getTokenUrl()).build();
    }

    @Override
    public String obtainToken(Tenant tenant) {
        String credentials = tenant.getCertificatePinEncrypted() != null
            ? encryptionService.decrypt(tenant.getCertificatePinEncrypted())
            : "";
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", properties.getClientId());
        form.add("client_secret", properties.getClientSecret());
        form.add("grant_type", "password");
        form.add("username", tenant.getName());
        form.add("password", credentials);
        return tokenClient.post()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(form))
            .retrieve()
            .bodyToMono(TokenResponse.class)
            .map(TokenResponse::access_token)
            .blockOptional()
            .orElseThrow(() -> new IllegalStateException("Unable to obtain token"));
    }

    @Override
    public HaciendaSubmissionResponse sendInvoice(Tenant tenant, String signedXml, String token) {
        return haciendaClient.post()
            .uri("/recepcion/v1/recepcion")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_XML)
            .bodyValue(signedXml)
            .retrieve()
            .toEntity(String.class)
            .map(entity -> new HaciendaSubmissionResponse(entity.getStatusCode().name(), entity.getHeaders().getFirst(HttpHeaders.LOCATION)))
            .block();
    }

    @Override
    public HaciendaStatusResponse checkStatus(Tenant tenant, String clave, String token) {
        return haciendaClient.get()
            .uri("/recepcion/v1/recepcion/{clave}", clave)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(String.class)
            .map(body -> new HaciendaStatusResponse("UNKNOWN", body))
            .block();
    }

    private record TokenResponse(String access_token, String token_type, long expires_in) {
    }
}
