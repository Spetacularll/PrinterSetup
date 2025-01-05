package com.example.jeweryapp.demos.web.Entity;

import javax.persistence.*;

import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Products")
@NoArgsConstructor
@AllArgsConstructor
@Data
//@EntityListeners({AuditingEntityListener.class, AuditListener.class})
public class Product {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Setter
    @Getter
    @Column(nullable = false, length = 100)
    private String owner;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @Setter
    @Getter
    @Column(nullable = false, unique = true, length = 50)
    private String barcode;


    /**
     * 种类
     * **/
    @Setter
    @Getter
    @Column(length = 50)
    private String category;

    @Setter
    @Getter
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
/**
 * 圈口信息+厚度
 * **/
    @Setter
    @Getter
    @Column(columnDefinition = "TEXT")
    private String description;

    @Setter
    @Getter
    @Column(nullable = false)
    private int stock;
    @Column(nullable = false)
    private boolean isDeleted=false;

    @Column(length = 255)
    private String imageUrl; // 新增字段用于存储图片路径或URL


    public Long getId() {
        return productId;
    }

    // Getters and setters


}
