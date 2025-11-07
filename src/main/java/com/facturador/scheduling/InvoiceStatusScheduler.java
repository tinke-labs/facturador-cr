package com.facturador.scheduling;

import com.facturador.invoice.Invoice;
import com.facturador.invoice.InvoiceRepository;
import com.facturador.invoice.InvoiceService;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InvoiceStatusScheduler {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;

    public InvoiceStatusScheduler(InvoiceRepository invoiceRepository, InvoiceService invoiceService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceService = invoiceService;
    }

    @Scheduled(fixedDelayString = "${app.scheduler.invoice-status-delay:300000}")
    public void checkStatuses() {
        List<Invoice> pending = invoiceRepository.findByStatus(Invoice.Status.SENT.name());
        pending.forEach(invoiceService::refreshStatus);
    }
}
