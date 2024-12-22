package com.example.jeweryapp.demos.web.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BarcodeResponse {
    private boolean success;
    private String barcodeText;
    private String message;
}