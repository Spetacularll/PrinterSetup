package com.example.jeweryapp.demos.web.Controller;

import com.example.jeweryapp.demos.web.Component.BarcodePrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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


@Controller
public class BarcodeController {


    // 显示输入表单页面
    @GetMapping("/barcode-form")
    public String showBarcodeForm() {
        return "barcode-form";
    }
    @PostMapping("/print-barcodes")
    public ResponseEntity<String> printBarcodes(@RequestBody Map<String, List<String>> request) {
        List<String> base64Images = request.get("images");

        if (base64Images != null && !base64Images.isEmpty()) {
            try {
                for (String base64Image : base64Images) {
                    byte[] imageBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);

                    // 将解码后的图片写入文件或流
                    try (InputStream in = new ByteArrayInputStream(imageBytes)) {
                        BufferedImage image = ImageIO.read(in);

                        // 调用打印机API进行打印
                        printImage(image);
                    }
                }

                return ResponseEntity.ok("打印成功");
            } catch (IOException | PrinterException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("打印失败");
            }
        }
        return ResponseEntity.badRequest().body("无效的图片数据");
    }
    public void printImage(BufferedImage image) throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();

        // 创建自定义的纸张和页面格式
        Paper paper = new Paper();
        double width = 72 * 4; // 4 英寸宽 (1 英寸 = 72 点)
        double height = 72 * 6; // 6 英寸高
        paper.setSize(width, height);
        paper.setImageableArea(0, 0, width, height); // 设置整个区域为可打印区域

        PageFormat pageFormat = new PageFormat();
        pageFormat.setPaper(paper);

        // 设置打印方向（如果需要）
        pageFormat.setOrientation(PageFormat.PORTRAIT);

        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex != 0) {
                    return NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

                // 计算缩放比例，以适应纸张大小
                double scaleX = pageFormat.getImageableWidth() / image.getWidth();
                double scaleY = pageFormat.getImageableHeight() / image.getHeight();
                double scale = Math.min(scaleX, scaleY);

                int scaledWidth = (int) (image.getWidth() * scale);
                int scaledHeight = (int) (image.getHeight() * scale);

                // 居中绘制图像
                int x = (int) ((pageFormat.getImageableWidth() - scaledWidth) / 2);
                int y = (int) ((pageFormat.getImageableHeight() - scaledHeight) / 2);

                g2d.drawImage(image, x, y, scaledWidth, scaledHeight, null);

                return PAGE_EXISTS;
            }
        }, pageFormat);

        try {
            job.print();  // 直接执行打印，不弹出对话框
        } catch (PrinterException e) {
            throw new PrinterException("打印失败: " + e.getMessage());
        }
    }

    @PostMapping("/generate-and-print-barcode")
    public ResponseEntity<String> generateAndPrintBarcode(@RequestBody Map<String, String> request) {
        String barcodeText = request.get("text");

        if (barcodeText != null && !barcodeText.isEmpty()) {
            try {
                // 使用 ZXing 生成条形码并打印
                BarcodePrinter barcodePrinter = new BarcodePrinter(barcodeText);

                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(barcodePrinter);

                // 自定义纸张格式
                PageFormat customPageFormat = BarcodePrinter.getCustomPageFormat(job);
                job.setPrintable(barcodePrinter, customPageFormat);

                // 自动打印，不显示对话框
                job.print();

                return ResponseEntity.ok("条形码已成功生成并打印");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("打印失败");
            }
        }
        return ResponseEntity.badRequest().body("无效的条形码文本");
    }

}
