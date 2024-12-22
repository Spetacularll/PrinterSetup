package com.example.jeweryapp.demos.web.Controller;


import com.example.jeweryapp.demos.web.Entity.OutboundRecord;
import com.example.jeweryapp.demos.web.Entity.Product;
import com.example.jeweryapp.demos.web.Service.OutboundRecordService;
import com.example.jeweryapp.demos.web.Service.ProductService;
import com.example.jeweryapp.demos.web.common.OutRecord;
import com.example.jeweryapp.demos.web.common.OutboundRequest;
import com.example.jeweryapp.demos.web.common.response.ApiResponse;
import com.example.jeweryapp.demos.web.common.response.ResultCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/outbound")
public class OutboundRecordController {

    @Autowired
    private ProductService productService;
    @Autowired
    private OutboundRecordService outboundRecordService;

    @PostMapping
    public ApiResponse<Void> createOutboundRecord(@RequestBody OutboundRequest request) {
        try {
            productService.addOutboundRecord(request.getProductbarcode(), request.getDestination());
            return ApiResponse.success();
        } catch (Exception e) {
            return ApiResponse.error(ResultCode.BUSINESS_ERROR.getCode(), "创建出库记录失败");
        }
    }


    @GetMapping("/outbound-records")
    public ApiResponse<List<OutRecord>> getOutboundRecords() {
//        List<OutboundRecord> outboundRecords = outboundRecordService.getAllOutboundRecords();
//        return ApiResponse.success(outboundRecords);
    List<OutRecord> OutboundRecords = outboundRecordService.getAllOutboundRecords();
    for (OutRecord outRecord : OutboundRecords) {

        System.out.println(outRecord+"the size of which is :" + OutboundRecords.size());
    }
    return ApiResponse.success(OutboundRecords);
    }

    @GetMapping("/success")
    public ApiResponse<Void> showSuccess() {
        return ApiResponse.success();
    }

}
