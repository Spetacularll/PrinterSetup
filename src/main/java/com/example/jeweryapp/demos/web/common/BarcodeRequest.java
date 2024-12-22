package com.example.jeweryapp.demos.web.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BarcodeRequest {
    private int category;     // 种类 (0-20)
    private int color;        // 颜色编码 (0-10)
    private String name;      // 名称简写
    private String thickness; // 厚度
    private String price;     // 价格
}