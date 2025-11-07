package com.facturador.invoice.web;

import com.facturador.invoice.InvoiceService;
import com.facturador.invoice.dto.CreateInvoiceRequest;
import com.facturador.invoice.dto.InvoiceResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> create(@Valid @RequestBody CreateInvoiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.createInvoice(request));
    }

    @GetMapping("/{clave}")
    public ResponseEntity<InvoiceResponse> find(@PathVariable String clave) {
        return ResponseEntity.ok(invoiceService.getInvoice(clave));
    }

    @PostMapping("/{clave}/retry")
    public ResponseEntity<InvoiceResponse> retry(@PathVariable String clave) {
        return ResponseEntity.ok(invoiceService.retryInvoice(clave));
    }
}
