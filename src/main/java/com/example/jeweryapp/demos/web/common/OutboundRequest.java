package com.example.jeweryapp.demos.web.common;

public class OutboundRequest {
    private String productbarcode;
    private String destination;
    public OutboundRequest(String productbarcode, String destination) {}
    public String getProductbarcode() {return productbarcode;}
    public String getDestination() {return destination;}
    public OutboundRequest(){
    }
}
