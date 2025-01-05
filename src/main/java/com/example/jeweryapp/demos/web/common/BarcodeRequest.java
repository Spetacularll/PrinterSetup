package com.example.jeweryapp.demos.web.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BarcodeRequest {
    private String style;         // 样式
    private String owner;         // 所有人
    private String time;          // 时间
    private String serialNumber;  // 编号
    private Double circleSize;    // 圈口
    private Integer width;        // 宽度
    private Integer price;
    private Integer supplier;
}