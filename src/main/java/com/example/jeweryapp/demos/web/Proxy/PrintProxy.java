package com.example.jeweryapp.demos.web.Proxy;

import com.example.jeweryapp.demos.web.Component.BarcodePrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.print.*;
import java.io.*;
import java.net.*;

public class PrintProxy {
    public static void main(String[] args) throws IOException {
        int port = 8081; // 本地打印代理监听的端口
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("本地打印代理启动，监听端口：" + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleClient(clientSocket)).start();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (InputStream input = clientSocket.getInputStream();
             OutputStream output = clientSocket.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();

            // 解析客户端发送的 JSON 数据
            PrintTask task = objectMapper.readValue(input, PrintTask.class);
            System.out.println("收到打印任务：" + task);

            // 执行打印任务
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(new BarcodePrinter(task.getContent(), task.getBarcode()));
            if (job.printDialog()) {
                job.print();
            }

            // 返回响应
            output.write("打印任务完成\n".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 打印任务对象
    public static class PrintTask {
        private String content;
        private String barcode;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }
    }
}
