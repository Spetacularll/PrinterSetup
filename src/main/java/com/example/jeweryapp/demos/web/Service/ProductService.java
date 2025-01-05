package com.example.jeweryapp.demos.web.Service;


import com.example.jeweryapp.demos.web.Entity.InboundRecord;
import com.example.jeweryapp.demos.web.Entity.OutboundRecord;
import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.Entity.Supplier;
import com.example.jeweryapp.demos.web.Repository.InboundRecordRepository;
import com.example.jeweryapp.demos.web.Repository.OutboundRecordRepository;
import com.example.jeweryapp.demos.web.Repository.ProductRepository;
import com.example.jeweryapp.demos.web.Repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
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
    public void deleteProductByBarcode(String barcode) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid barcode: " + barcode));
        if(product!=null){
            product.setDeleted(true);
            productRepository.save(product);
        }

    }

    public String getFormattedId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // 获取主键值
        Long id = product.getId();

        // 转换为左侧填充的8位字符串
        return String.format("%08d", id);
    }
    public List<Product> searchProducts(String owner, String category, Double priceMin, Double priceMax) {
        // 如果所有参数均为 null，则直接返回所有产品
        if (owner == null && category == null && priceMin == null && priceMax == null) {
            return productRepository.findAll((root, query, builder) -> {
                // 强制添加 isDeleted 条件
                return builder.isFalse(root.get("isDeleted"));
            });
        }

        return productRepository.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(builder.isFalse(root.get("isDeleted")));
            if (owner != null) {
                predicates.add(builder.equal(root.get("owner"), owner));
            }
            if (category != null) {
                predicates.add(builder.equal(root.get("category"), category));
            }
            if (priceMin != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("price"), priceMin));
            }
            if (priceMax != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("price"), priceMax));
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        });
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

    public Product findProductByBarcode(String barcode){
        return productRepository.findProductByBarcode(barcode);
    }

    @Transactional
    public InboundRecord addProductAndInboundRecord(Product product, Long supplierId) {
        if (product.getStock() > 0) {
            throw new IllegalStateException("Product is already in stock");
        }
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid supplier ID: " + supplierId));
        product.setStock(1);
 //       product = productRepository.save(product);
        InboundRecord inboundRecord = new InboundRecord();
        inboundRecord.setProduct(product);
        inboundRecord.setSupplier(supplier);
        inboundRecord.setQuantity(1);
        inboundRecord.setInboundDate(LocalDateTime.now());
        return inboundRecordRepository.save(inboundRecord);
    }


    @Transactional
    public OutboundRecord addOutboundRecord(String barcode, String destination) {
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product ID: " + barcode));
//        Product product = productRepository.findProductByBarcode(barcode)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid product barcode: "+ barcode ));
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

        product.setOwner(updatedProduct.getOwner());
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
