package com.example.jeweryapp.demos.web.Controller;

import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/inbound")
    public String viewProductsInStock(Model model) {
        List<Product> productsInStock = productService.getProductsInStock();
        model.addAttribute("products", productsInStock);
        return "products-in-stock";
    }

    @PostMapping("/barcode")
    public String updateProductByBarcode(@RequestParam String barcode,
                                         @ModelAttribute Product updatedProduct,
                                         Model model) {
        try {
            Product product = productService.updateProductByBarcode(barcode, updatedProduct);
            model.addAttribute("product", product);
            model.addAttribute("success", "Product updated successfully");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "product-edit";
    }
}
