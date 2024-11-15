package com.example.jeweryapp.demos.web.Controller;

import com.example.jeweryapp.demos.web.Entity.InboundRecord;
import com.example.jeweryapp.demos.web.Entity.OutboundRecord;
import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.Service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/api")
public class RecordController {
/**
 *
 * To Search a single product with the barcode.
 * corresponding to the query action.
 * To get all the Record to that product
 * **/

    @Autowired
    private RecordService recordService;

    @GetMapping("/records")
    public String getRecordsByBarcode(@RequestParam("barcode") String barcode, Model model) {
        Product product = recordService.getProductByBarcode(barcode);
        List<InboundRecord> inboundRecords = recordService.getInboundRecordsByProduct(product);
        List<OutboundRecord> outboundRecords = recordService.getOutboundRecordsByProduct(product);

        model.addAttribute("product", product);
        model.addAttribute("inboundRecords", inboundRecords);
        model.addAttribute("outboundRecords", outboundRecords);
        return "records";
    }
}
