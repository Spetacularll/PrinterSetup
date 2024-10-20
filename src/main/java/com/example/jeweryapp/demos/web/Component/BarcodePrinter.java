package com.example.jeweryapp.demos.web.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class BarcodePrinter implements Printable {

    private BufferedImage barcodeImage;
    private String barcodeText;  // 条形码的文本内容

    // 构造函数，生成条形码
    public BarcodePrinter(String text) throws Exception {
        this.barcodeText = text;
        this.barcodeImage = generateBarcodeImage(text);
    }

    // 使用 ZXing 生成条形码图片
    private BufferedImage generateBarcodeImage(String text) throws Exception {
        int width = 113; // 设置条形码宽度
        int height = 50; // 设置条形码高度，留出空间给文字显示
        String imageFormat = "png";

        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.CODE_128, width, height);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, imageFormat, outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        return ImageIO.read(inputStream);
    }

    // 打印机接口实现，用于打印条形码及文本
    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // 绘制条形码图片
        g.drawImage(barcodeImage, 0, 0, null);

        // 设置字体和字体大小
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));

        // 计算文本的绘制位置，确保文字在条形码下方显示
        int textX = 0;  // 文字的X坐标与条形码图片对齐
        int textY = barcodeImage.getHeight() + 20;  // 文字的Y坐标，位于条形码图片下方

        // 绘制条形码对应的文本内容
        g2d.drawString(barcodeText, textX, textY);

        return PAGE_EXISTS;
    }

    // 自定义纸张格式：4.0 cm * 3.0 cm
    public static PageFormat getCustomPageFormat(PrinterJob printerJob) {
        PageFormat pageFormat = printerJob.defaultPage();
        Paper paper = new Paper();

        // 设置纸张大小，单位是点 (1cm = 28.35 points)
        double paperWidth = 113.4; // 4.0 cm in points
        double paperHeight = 85.05; // 3.0 cm in points
        paper.setSize(paperWidth, paperHeight);

        // 设置可打印区域，给出边距 (如无边距设置为 0)
        double margin = 5; // 设置 5 点的边距（可调整）
        paper.setImageableArea(margin, margin, paperWidth - 2 * margin, paperHeight - 2 * margin);

        pageFormat.setPaper(paper);

        return pageFormat;
    }

    // 测试条形码打印功能
    public static void main(String[] args) {
        try {
            BarcodePrinter barcodePrinter = new BarcodePrinter("x./qwe123");

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
