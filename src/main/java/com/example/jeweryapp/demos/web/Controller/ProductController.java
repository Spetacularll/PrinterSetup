package com.example.jeweryapp.demos.web.Controller;

import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.Service.ProductService;
import com.example.jeweryapp.demos.web.common.response.ApiResponse;
import com.example.jeweryapp.demos.web.common.response.ResultCode;
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

@RestController // 使用 @RestController 而不是 @Controller
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    /**
     *  To get all the products in stock.
     *  GET API
     * **/
    @GetMapping("/inbound")
    public ApiResponse<List<Product>> getAllProducts() {
        List<Product> products = productService.getProductsInStock();
        return ApiResponse.success(products);
    }
    /**
     * 
     * **/
    // 根据条码获取产品
    @GetMapping("/barcode")
    public ApiResponse<Product> getProductByBarcode(@RequestParam(value = "barcode", required = false) String barcode) {
        if (barcode != null) {
            try {
                Product product = productService.getProductByBarcode(barcode);
                return ApiResponse.success(product);
            } catch (IllegalArgumentException e) {
                return ApiResponse.error(ResultCode.NOT_FOUND.getCode(), e.getMessage());
            }
        } else {
            return ApiResponse.error(ResultCode.PARAM_ERROR.getCode(), "条码不能为空");
        }
    }

    @PostMapping("/delete")
    public ApiResponse<Void> deleteProductByBarcode(@RequestParam String barcode) {
        try {
            productService.deleteProductByBarcode(barcode);
            return ApiResponse.success(null, "Product marked as deleted successfully"); // 传入 null 作为数据部分
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(ResultCode.NOT_FOUND.getCode(), e.getMessage());
        }
    }


    // 根据条码更新产品
    @PostMapping("/barcode")
    public ApiResponse<Product> updateProductByBarcode(@RequestParam String barcode,
                                                       @ModelAttribute Product updatedProduct,
                                                       @RequestParam("imageFile") MultipartFile imageFile) {
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

            // 返回成功响应
            return ApiResponse.success(product, "Product updated successfully");

        } catch (IOException e) {
            return ApiResponse.error(ResultCode.DATA_ERROR.getCode(), "Image upload failed: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            return ApiResponse.error(ResultCode.NOT_FOUND.getCode(), e.getMessage());
        }
    }
}
