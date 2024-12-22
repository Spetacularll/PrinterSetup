package com.example.jeweryapp.demos.web.common;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OutRecord {
    private String destination;
    private String productName;
    private LocalDateTime outboundDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price; // 新增字段
    private String barcode;
    public OutRecord(String destination, String productName, LocalDateTime outboundDate, BigDecimal price, String barcode) {
        this.destination = destination;
        this.productName = productName;
        this.outboundDate = outboundDate;
        this.price = price;
        this.barcode = barcode;
    }
    public OutRecord(){}

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDateTime getOutboundDate() {
        return outboundDate;
    }

    public void setOutboundDate(LocalDateTime outboundDate) {
        this.outboundDate = outboundDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
