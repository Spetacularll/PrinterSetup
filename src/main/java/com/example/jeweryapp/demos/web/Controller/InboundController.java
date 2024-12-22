package com.example.jeweryapp.demos.web.Controller;


import com.example.jeweryapp.demos.web.Entity.InboundRecord;
import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("api/inbound")
public class InboundController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    public ResponseEntity<?> createProductAndInboundRecord(@ModelAttribute Product product,
                                                           @RequestParam("imageFile") MultipartFile imageFile,
                                                           @RequestParam Long supplierId) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 处理图片上传
            if (!imageFile.isEmpty()) {
                String fileName = imageFile.getOriginalFilename();
                String uploadDir = "/var/www/uploads/";
                String filePath = uploadDir + fileName;

                // 创建目录并保存文件
                Path path = Paths.get(filePath);
                Files.createDirectories(path.getParent());
                Files.write(path, imageFile.getBytes());

                // 设置图片URL到产品实体中
                product.setImageUrl("http://8.138.89.11/uploads/" + fileName);
            }

            // 调用服务方法创建产品和入库记录
            InboundRecord inboundRecord = productService.addProductAndInboundRecord(product, supplierId);

            // 返回成功响应
            response.put("message", "商品和入库记录创建成功");
            response.put("inboundRecord", inboundRecord);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            // 返回错误响应
            response.put("error", "图片上传失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @GetMapping("/product")
    public String showInboundForm(Model model) {
        model.addAttribute("product", new Product());
        return "inbound";
    }

    @GetMapping("/product/success")
    public String showSuccess() {
        return "success";
    }
}
