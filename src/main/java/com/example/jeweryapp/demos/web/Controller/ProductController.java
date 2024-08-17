package com.example.jeweryapp.demos.web.Controller;

import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/barcode")
    public String getProductByBarcode(@RequestParam(value = "barcode", required = false) String barcode, Model model) {
        if (barcode != null) {
            try {
                Product product = productService.getProductByBarcode(barcode);
                model.addAttribute("product", product);
            } catch (IllegalArgumentException e) {
                model.addAttribute("error", e.getMessage());
            }
        }
        return "product-edit";
    }
    @PostMapping("/delete")
    public String deleteProductByBarcode(@RequestParam String barcode, Model model) {
        try {
            productService.deleteProductByBarcode(barcode);
            model.addAttribute("success", "Product marked as deleted successfully");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/api/products/inbound"; // 重定向到 products-in-stock 页面
    }

    @GetMapping("/inbound")
    public String viewProductsInStock(Model model) {
        List<Product> productsInStock = productService.getProductsInStock();
        model.addAttribute("products", productsInStock);
        return "products-in-stock";
    }



    @PostMapping("/barcode")
    public String updateProductByBarcode(@RequestParam String barcode,
                                         @ModelAttribute Product updatedProduct,
                                         @RequestParam("imageFile") MultipartFile imageFile, // 新增的图片文件
                                         Model model) {
        try {
            // 根据条形码找到现有产品
            Product existingProduct = productService.findProductByBarcode(barcode);

            // 更新产品的非图片属性
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setDescription(updatedProduct.getDescription());

            // 如果上传了新图片，处理图片文件
            if (!imageFile.isEmpty()) {
                // 生成文件名和保存路径
                String fileName = imageFile.getOriginalFilename();
                String uploadDir = "product-images/"; // 定义保存路径
                String filePath = uploadDir + fileName;

                // 创建目录并保存文件
                Path path = Paths.get(filePath);
                Files.createDirectories(path.getParent());
                Files.write(path, imageFile.getBytes());

                // 更新产品的图片URL
                existingProduct.setImageUrl(filePath);
            }

            // 更新产品信息
            Product product = productService.updateProductByBarcode(barcode, existingProduct);

            // 将更新后的产品信息添加到模型中
            model.addAttribute("product", product);
            model.addAttribute("success", "Product updated successfully");

        } catch (IOException e) {
            model.addAttribute("error", "Image upload failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }

        // 返回到编辑页面
        return "product-edit";
    }

}
