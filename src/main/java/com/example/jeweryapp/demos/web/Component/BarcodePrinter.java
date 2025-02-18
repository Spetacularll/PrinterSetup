package com.example.jeweryapp.demos.web.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BarcodePrinter implements Printable {

    private static final AtomicInteger COUNTER = new AtomicInteger(1); // 条形码计数器
    private BufferedImage barcodeImage;
    private String displayText;

    public BarcodePrinter(String displayText, String barcodeNumber) throws Exception {
        this.displayText = displayText;
        this.barcodeImage = generateBarcodeImage(barcodeNumber);
    }

    private BufferedImage generateBarcodeImage(String text) throws Exception {
        int width = 120;  // 调整条形码宽度至纸张可打印范围内
        int height = 40;  // 高度适中以适配条形码清晰度
        String imageFormat = "png";

        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 0); // 移除条形码边距

        BitMatrix bitMatrix = new MultiFormatWriter().encode(
                text,
                BarcodeFormat.CODE_128,
                width,
                height,
                hints
        );

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return ImageIO.read(inputStream);
    }

    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;

        // 可打印区域的起点
        double imageableX = pageFormat.getImageableX();
        double imageableY = pageFormat.getImageableY();

        // 可打印区域的宽度和高度
        double imageableWidth = pageFormat.getImageableWidth();
        double imageableHeight = pageFormat.getImageableHeight();

        // 条形码和文字的总高度
        int totalHeight = barcodeImage.getHeight() + 10; // 条形码高度 + 文字间距

        // 居中计算
        double x = imageableX + (imageableWidth - barcodeImage.getWidth()) / 2;
        double y = imageableY + (imageableHeight - totalHeight) / 2;

        // 下移20单位
        y += 20;

        g2d.translate(x, y);

        // 分割显示文本
        String[] lines = displayText.split(" ", 2);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));

        // 调整文本打印位置上移5单位
        g2d.drawString(lines[0], 20, -20); // 第一行文字距离条形码上方10点
        if (lines.length > 1) {
            g2d.drawString(lines[1], 10, -10); // 第二行文字紧贴条形码上方
        }

        // 绘制条形码
        g.drawImage(barcodeImage, 10, -5, null); // 下移20单位

        return PAGE_EXISTS;
    }


    public static PageFormat getCustomPageFormat(PrinterJob printerJob) {
        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = new Paper();

        double paperWidth = 40 * 2.835;  // ≈ 113.4 points
        double paperHeight = 30 * 2.835; // ≈ 85.05 points
        paper.setSize(paperWidth, paperHeight);

        double margin = 5; // 边距设置为5
        paper.setImageableArea(
                margin,
                margin,
                paperWidth - 2 * margin,
                paperHeight - 2 * margin
        );

        pageFormat.setPaper(paper);
        return pageFormat;
    }

    public static void main(String[] args) {
        while (true) {
            try {
                // 获取当前条形码编号，格式化为8位
                String barcodeNumber = String.format("%08d", COUNTER.getAndIncrement());

                // 输入显示文本
                System.out.println("请输入显示文本:");
                java.util.Scanner scanner = new java.util.Scanner(System.in);
                String displayText = scanner.nextLine();

                BarcodePrinter barcodePrinter = new BarcodePrinter(displayText, barcodeNumber);

                PrinterJob job = PrinterJob.getPrinterJob();
                job.setPrintable(barcodePrinter);

                PageFormat customPageFormat = getCustomPageFormat(job);
                job.setPrintable(barcodePrinter, customPageFormat);

                boolean doPrint = job.printDialog();
                if (doPrint) {
                    job.print();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
