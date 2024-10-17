package com.example.jeweryapp.demos.web.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class BarcodeService {

    public void generateBarcodes(List<String> barcodeDataList) throws Exception {
        for (String data : barcodeDataList) {
            generateBarcodeImage(data, 300, 100, "src/main/resources/static/barcodes/" + data + ".png");
        }
    }

    private void generateBarcodeImage(String data, int width, int height, String filePath) throws Exception {
        BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.CODE_128, width, height);
        Path path = new File(filePath).toPath();
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
