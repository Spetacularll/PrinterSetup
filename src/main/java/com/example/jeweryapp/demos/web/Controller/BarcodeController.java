package com.example.jeweryapp.demos.web.Controller;

import com.example.jeweryapp.demos.web.Component.BarcodePrinter;
import com.example.jeweryapp.demos.web.Component.PriceEncoder;
import com.example.jeweryapp.demos.web.Entity.InboundRecord;
import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.Service.ProductService;
import com.example.jeweryapp.demos.web.common.BarcodeRequest;
import com.example.jeweryapp.demos.web.common.response.ApiResponse;
import com.example.jeweryapp.demos.web.common.response.BarcodeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.print.*;
import com.google.zxing.*;
import com.google.zxing.client.j2se.*;
import com.google.zxing.common.BitMatrix;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

/**
 * 分离版本
 * 整合版本在ProductController中
 * **/
@RestController
@RequestMapping("/api/barcode")
public class BarcodeController {

    @Autowired
    private ProductService productService;




    @PostMapping("/print")
    public ResponseEntity<?> printBarcode(@RequestBody BarcodeRequest inputData) {
        try {
            // 验证输入
            if (inputData.getStyle() == null || inputData.getOwner() == null ||
                    inputData.getTime() == null || inputData.getSerialNumber() == null ||
                    inputData.getCircleSize() == null || inputData.getWidth() == null) {
                return ResponseEntity.badRequest().body("输入不完整");
            }

            // 拼接字符串，注意在编号后添加空格
            String result = String.format(
                    "%s%s%s%s %s%d",
                    inputData.getStyle(),
                    inputData.getOwner(),
                    inputData.getTime(),
                    inputData.getSerialNumber(),
                    inputData.getCircleSize(),
                    inputData.getWidth(),
                    PriceEncoder.encodePrice(inputData.getPrice())
            );
            BarcodePrinter barcodePrinter = new BarcodePrinter(result,"1");
            PrinterJob job = PrinterJob.getPrinterJob();

            // 设置自定义纸张格式
            PageFormat customPageFormat = BarcodePrinter.getCustomPageFormat(job);
            job.setPrintable(barcodePrinter, customPageFormat);

            // 执行打印
            job.print();

            return ResponseEntity.ok(new BarcodeResponse(true, result, "打印成功"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new BarcodeResponse(false, null, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BarcodeResponse(false, null, "打印失败: " + e.getMessage()));
        }
    }

    @PostMapping("/create-product")
    public ApiResponse<?> createProductWithBarcode(
            @RequestParam("dto") String dtoString,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 解析 DTO JSON 数据
            ObjectMapper objectMapper = new ObjectMapper();
            BarcodeRequest barcodeRequest = objectMapper.readValue(dtoString, BarcodeRequest.class);

            // 拼接字符串，注意在编号后添加空格
            String result = String.format(
                    "%s%s%s%s %s%s%s",
                    barcodeRequest.getStyle(),
                    barcodeRequest.getOwner(),
                    barcodeRequest.getTime(),
                    barcodeRequest.getSerialNumber(),
                    barcodeRequest.getCircleSize(),
                    barcodeRequest.getWidth(),
                    PriceEncoder.encodePrice(barcodeRequest.getPrice())
            );
            System.out.println(result);


            // 验证 DTO 数据的完整性
            if (barcodeRequest.getStyle() == null || barcodeRequest.getOwner() == null ||
                    barcodeRequest.getTime() == null || barcodeRequest.getSerialNumber() == null ||
                    barcodeRequest.getCircleSize() == null || barcodeRequest.getWidth() == null ||
                    barcodeRequest.getPrice() == null || barcodeRequest.getSupplier() == null) {
                return ApiResponse.error(400, "请求参数不完整");
            }

            // 创建 Product 实体并设置属性
            Product product = new Product();
            product.setCategory(barcodeRequest.getStyle());
            product.setOwner(barcodeRequest.getOwner());
            product.setPrice(BigDecimal.valueOf(barcodeRequest.getPrice()));
            System.out.println(barcodeRequest.getPrice());
            product.setDescription(barcodeRequest.getCircleSize() + "-" + barcodeRequest.getWidth());

            // 根据 Owner 设置条形码
            String barcodeNumber;
            if ("AB".equals(barcodeRequest.getOwner())) {
                barcodeNumber = "1234" + barcodeRequest.getSerialNumber();
                product.setOwner("spring");
            } else if ("CD".equals(barcodeRequest.getOwner())) {
                barcodeNumber = "3446" + barcodeRequest.getSerialNumber();
                product.setOwner("huang");
            } else {
                barcodeNumber = "7693" + barcodeRequest.getSerialNumber();
                product.setOwner("Ayi");
            }
            product.setBarcode(barcodeNumber);
            BarcodePrinter barcodePrinter = new BarcodePrinter(result,barcodeNumber);
            PrinterJob job = PrinterJob.getPrinterJob();

            // 设置自定义纸张格式
            PageFormat customPageFormat = BarcodePrinter.getCustomPageFormat(job);
            job.setPrintable(barcodePrinter, customPageFormat);
            // 执行打印
            job.print();
            // 处理图片文件
            if (imageFile != null && !imageFile.isEmpty()) {
                String uploadDir = "/var/www/uploads/";
                String fileName = imageFile.getOriginalFilename();
                String filePath = uploadDir + fileName;

                Path path = Paths.get(filePath);
                Files.createDirectories(path.getParent());
                Files.write(path, imageFile.getBytes());

                // 设置图片 URL 到产品实体
                product.setImageUrl("http://8.138.89.11/uploads/" + fileName);
            }
            // 保存 Product 到数据库
            productService.saveProduct(product);

            // 创建并保存 InboundRecord
            InboundRecord inboundRecord = productService.addProductAndInboundRecord(product, Long.valueOf(barcodeRequest.getSupplier()));

            // 返回响应
            response.put("message", "商品和入库记录创建成功");
            response.put("barcode", barcodeNumber);
            response.put("inboundRecord", inboundRecord);
            return ApiResponse.success(response);

        } catch (IOException e) {
            e.printStackTrace();
            return ApiResponse.error("解析请求数据失败: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.error("操作失败: " + e.getMessage());
        }
    }

}



