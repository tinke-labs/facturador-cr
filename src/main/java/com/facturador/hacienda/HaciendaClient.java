package com.facturador.hacienda;

import com.facturador.hacienda.dto.HaciendaStatusResponse;
import com.facturador.hacienda.dto.HaciendaSubmissionResponse;
import com.facturador.tenant.Tenant;

public interface HaciendaClient {

    String obtainToken(Tenant tenant);

    HaciendaSubmissionResponse sendInvoice(Tenant tenant, String signedXml, String token);

    HaciendaStatusResponse checkStatus(Tenant tenant, String clave, String token);
}
