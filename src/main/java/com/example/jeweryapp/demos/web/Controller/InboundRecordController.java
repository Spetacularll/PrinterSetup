package com.example.jeweryapp.demos.web.Controller;


import com.example.jeweryapp.demos.web.Entity.InboundRecord;
import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/inbound")
public class InboundRecordController {

    @Autowired
    private ProductService productService;

    @PostMapping("/product")
    public String createProductAndInboundRecord(@ModelAttribute Product product,
                                                @RequestParam Long supplierId,
                                                Model model) {
        InboundRecord inboundRecord = productService.addProductAndInboundRecord(product, supplierId);
        model.addAttribute("inboundRecord", inboundRecord);
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
