package com.example.jeweryapp.demos.web.Service;


import com.example.jeweryapp.demos.web.Entity.InboundRecord;
import com.example.jeweryapp.demos.web.Entity.OutboundRecord;
import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.Entity.Supplier;
import com.example.jeweryapp.demos.web.Repository.InboundRecordRepository;
import com.example.jeweryapp.demos.web.Repository.OutboundRecordRepository;
import com.example.jeweryapp.demos.web.Repository.ProductRepository;
import com.example.jeweryapp.demos.web.Repository.SupplierRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InboundRecordRepository inboundRecordRepository;

    @Autowired
    private OutboundRecordRepository outboundRecordRepository;

    @Autowired
    private SupplierRepository supplierRepository;
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public InboundRecord addInboundRecord(Long productId, Long supplierId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productId));
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid supplier ID: " + supplierId));

        if (product.getStock() > 0) {
            throw new IllegalStateException("Product is already in stock");
        }

        InboundRecord inboundRecord = new InboundRecord();
        inboundRecord.setProduct(product);
        inboundRecord.setSupplier(supplier);
        inboundRecord.setQuantity(1);
        inboundRecord.setInboundDate(LocalDateTime.now());

        product.setStock(1);
        productRepository.save(product);

        return inboundRecordRepository.save(inboundRecord);
    }

    @Transactional
    public InboundRecord addProductAndInboundRecord(Product product, Long supplierId) {
        if (product.getStock() > 0) {
            throw new IllegalStateException("Product is already in stock");
        }

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid supplier ID: " + supplierId));

        product.setStock(1);
        product = productRepository.save(product);

        InboundRecord inboundRecord = new InboundRecord();
        inboundRecord.setProduct(product);
        inboundRecord.setSupplier(supplier);
        inboundRecord.setQuantity(1);
        inboundRecord.setInboundDate(LocalDateTime.now());

        return inboundRecordRepository.save(inboundRecord);
    }

    @Transactional
    public OutboundRecord addOutboundRecord(Long productId, String destination) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productId));

        if (product.getStock() == 0) {
            throw new IllegalStateException("Product is out of stock");
        }

        OutboundRecord outboundRecord = new OutboundRecord();
        outboundRecord.setProduct(product);
        outboundRecord.setQuantity(1);
        outboundRecord.setOutboundDate(LocalDateTime.now());
        outboundRecord.setDestination(destination);

        product.setStock(0);
        productRepository.save(product);

        return outboundRecordRepository.save(outboundRecord);
    }

    public int getProductStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + productId));
        return product.getStock();
    }

    public Product getProductByBarcode(String barcode) {
        return productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid barcode: " + barcode));
    }

    @Transactional
    public Product updateProductByBarcode(String barcode, Product updatedProduct) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid barcode: " + barcode));

        product.setProductName(updatedProduct.getProductName());
        product.setCategory(updatedProduct.getCategory());
        product.setPrice(updatedProduct.getPrice());
        product.setDescription(updatedProduct.getDescription());

        return productRepository.save(product);
    }

    public List<Product> getProductsInStock() {

        List<Product> products = productRepository.findByStock(1);
    return products;
    }
}
