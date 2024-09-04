package com.example.jeweryapp.demos.web.Controller;


import com.example.jeweryapp.demos.web.Entity.InboundRecord;
import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/api/inbound")
public class InboundController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    public String createProductAndInboundRecord(@ModelAttribute Product product,
                                                @RequestParam("imageFile") MultipartFile imageFile, // 接收上传的图片
                                                @RequestParam Long supplierId,
                                                Model model,
                                                RedirectAttributes redirectAttributes) {
        try {
            // 处理图片上传
            if (!imageFile.isEmpty()) {
                // 生成文件名和保存路径
                String fileName = imageFile.getOriginalFilename();
                String uploadDir = "/var/www/uploads/"; // 定义保存路径
                String filePath = uploadDir + fileName;

                // 创建目录并保存文件
                Path path = Paths.get(filePath);
                Files.createDirectories(path.getParent());
                Files.write(path, imageFile.getBytes());
                System.out.println("图片上传成功: " + filePath);
                // 设置图片URL到产品实体中
                product.setImageUrl("http://8.134.109.68:8080/" + fileName);
            }

            // 调用服务方法创建产品和入库记录
            InboundRecord inboundRecord = productService.addProductAndInboundRecord(product, supplierId);

            // 添加入库记录到模型
            model.addAttribute("inboundRecord", inboundRecord);

            // 添加成功消息
            redirectAttributes.addFlashAttribute("message", "商品和入库记录创建成功");
        } catch (IOException e) {
            // 添加错误消息
            redirectAttributes.addFlashAttribute("error", "图片上传失败: " + e.getMessage());
        }

        return "redirect:/api/inbound/product/success";
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
