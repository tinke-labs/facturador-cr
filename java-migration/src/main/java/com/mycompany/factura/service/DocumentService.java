package com.mycompany.factura.service;

import com.mycompany.factura.models.dto.DocumentRequest;
import com.mycompany.factura.models.dto.ReceiverMessageRequest;
import com.mycompany.factura.models.entities.ElectronicDocument;
import com.mycompany.factura.models.entities.ReceiverMessage;

public interface DocumentService {

    ElectronicDocument createDocument(DocumentRequest request);

    String generateXml(ElectronicDocument document);

    String signXml(String xml, ElectronicDocument document);

    void validateAgainstXsd(String xml);

    void sendToHacienda(ElectronicDocument document, String signedXml);

    void queryStatus(String key);

    void registerRetry(String key, String errorMessage);

    ReceiverMessage processReceiverMessage(ReceiverMessageRequest request);
}
