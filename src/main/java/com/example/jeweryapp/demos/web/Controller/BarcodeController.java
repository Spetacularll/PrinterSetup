package com.example.jeweryapp.demos.web.Controller;

import com.example.jeweryapp.demos.web.Component.BarcodePrinter;
import com.example.jeweryapp.demos.web.Service.ProductService;
import com.example.jeweryapp.demos.web.common.BarcodeRequest;
import com.example.jeweryapp.demos.web.common.response.BarcodeResponse;
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
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.awt.print.*;
import com.google.zxing.*;
import com.google.zxing.client.j2se.*;
import com.google.zxing.common.BitMatrix;
import java.awt.*;


@RestController
@RequestMapping("/api/barcode")
public class BarcodeController {

    @Autowired
    private ProductService productService;

    @PostMapping("/print")
    public ResponseEntity<?> printBarcode(@RequestBody BarcodeRequest request) {
        try {
            // 验证输入
            validateRequest(request);

            // 生成条形码文本
            String barcodeText = generateBarcodeText(request);

            // 创建并打印条形码
            String barcodeid = productService.getFormattedId();

            BarcodePrinter barcodePrinter = new BarcodePrinter(barcodeText,"1");
            PrinterJob job = PrinterJob.getPrinterJob();

            // 设置自定义纸张格式
            PageFormat customPageFormat = BarcodePrinter.getCustomPageFormat(job);
            job.setPrintable(barcodePrinter, customPageFormat);

            // 执行打印
            job.print();

            return ResponseEntity.ok(new BarcodeResponse(true, barcodeText, "打印成功"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new BarcodeResponse(false, null, e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BarcodeResponse(false, null, "打印失败: " + e.getMessage()));
        }
    }

    private void validateRequest(BarcodeRequest request) {
        if (request.getCategory() < 0 || request.getCategory() > 20) {
            throw new IllegalArgumentException("种类必须在0-20之间");
        }
        if (request.getColor() < 0 || request.getColor() > 10) {
            throw new IllegalArgumentException("颜色编码必须在0-10之间");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("名称简写不能为空");
        }
        if (request.getThickness() == null || request.getThickness().trim().isEmpty()) {
            throw new IllegalArgumentException("厚度不能为空");
        }
        if (request.getPrice() == null || request.getPrice().trim().isEmpty()) {
            throw new IllegalArgumentException("价格不能为空");
        }
    }

    private String generateBarcodeText(BarcodeRequest request) {
        return String.format("%d%d%s/%s%s",
                request.getCategory(),
                request.getColor(),
                request.getName(),
                request.getThickness(),
                request.getPrice());
    }
}



