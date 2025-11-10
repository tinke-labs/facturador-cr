package com.mycompany.factura.models.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Tax {

    @JsonProperty("codigo")
    private String code;

    @JsonProperty("tarifa")
    private BigDecimal rate;

    @JsonProperty("monto")
    private BigDecimal amount;

    @JsonProperty("exonerado")
    private BigDecimal exoneratedAmount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getExoneratedAmount() {
        return exoneratedAmount;
    }

    public void setExoneratedAmount(BigDecimal exoneratedAmount) {
        this.exoneratedAmount = exoneratedAmount;
    }
}
