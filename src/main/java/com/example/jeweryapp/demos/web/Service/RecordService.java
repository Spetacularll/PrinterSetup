package com.example.jeweryapp.demos.web.Service;

import com.example.jeweryapp.demos.web.Entity.InboundRecord;
import com.example.jeweryapp.demos.web.Entity.OutboundRecord;
import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.Repository.InboundRecordRepository;
import com.example.jeweryapp.demos.web.Repository.OutboundRecordRepository;
import com.example.jeweryapp.demos.web.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {

    @Autowired
    private InboundRecordRepository inboundRecordRepository;

    @Autowired
    private OutboundRecordRepository outboundRecordRepository;

    @Autowired
    private ProductRepository productRepository;

    public Product getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<InboundRecord> getInboundRecordsByProduct(Product product) {
        return inboundRecordRepository.findByProduct(product);
    }

    public List<OutboundRecord> getOutboundRecordsByProduct(Product product) {
        return outboundRecordRepository.findByProduct(product);
    }
}