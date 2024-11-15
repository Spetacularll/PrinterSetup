package com.example.jeweryapp.demos.web.Controller;

import com.example.jeweryapp.demos.web.Entity.Supplier;
import com.example.jeweryapp.demos.web.Repository.SupplierRepository;
import com.example.jeweryapp.demos.web.Service.SupplierService;
import com.example.jeweryapp.demos.web.common.exception.BusinessException;
import com.example.jeweryapp.demos.web.common.response.ApiResponse;
import com.example.jeweryapp.demos.web.common.response.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    @Autowired
    private final SupplierService supplierService;
    @Autowired
    private SupplierRepository supplierRepository;

    @GetMapping
    public ApiResponse<List<Supplier>> getAllSuppliers() {
        try {
            List<Supplier> suppliers = supplierService.findAllSuppliers();
            return ApiResponse.success(suppliers);
        } catch (Exception e) {
            log.error("获取供应商列表失败", e);
            throw new BusinessException("获取供应商列表失败");
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<Supplier> getSupplierById(@PathVariable Long id) {
        try {
            Supplier supplier = supplierService.findSupplierById(id);
            if (supplier == null) {
                throw new BusinessException(ResultCode.NOT_FOUND);
            }
            return ApiResponse.success(supplier);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("获取供应商详情失败, id: {}", id, e);
            throw new BusinessException("获取供应商详情失败");
        }
    }

    @PostMapping
    public ApiResponse<Supplier> createSupplier(@RequestBody Supplier supplier) {
        try {
            Supplier createdSupplier = supplierService.saveSupplier(supplier);
            return ApiResponse.success(createdSupplier, "供应商创建成功");
        } catch (Exception e) {
            log.error("创建供应商失败", e);
            throw new BusinessException("创建供应商失败");
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        try {
            Supplier updatedSupplier = supplierService.updateSupplier(id, supplier);
            if (updatedSupplier == null) {
                throw new BusinessException(ResultCode.NOT_FOUND);
            }
            return ApiResponse.success(updatedSupplier, "供应商更新成功");
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("更新供应商失败, id: {}", id, e);
            throw new BusinessException("更新供应商失败");
        }
    }

    @PutMapping("/{id}/soft-delete")
    public ApiResponse<Void> deleteSupplier(@PathVariable Long id) {
        try {
            supplierService.deleteSupplier(id);
            return ApiResponse.success(null, "供应商删除成功");
        } catch (Exception e) {
            log.error("删除供应商失败, id: {}", id, e);
            throw new BusinessException("删除供应商失败");
        }
    }
    /**
     * 根据 supplierId 查找供应商并将 isDeleted 设置为 false
     * @param supplierId 供应商ID
     * @return 返回更新后的结果
     */
    @PutMapping("/{supplierId}/restore")
    public ResponseEntity<ApiResponse<Void>> restoreSupplier(@PathVariable Long supplierId) {
        // 查找供应商
        Supplier supplier = supplierService.findSupplierById(supplierId);
        // 设置 isDeleted 为 false
        supplier.setDeleted(false);
        // 保存更新后的供应商
        supplierRepository.save(supplier);
        // 返回成功的 ApiResponse
        return ResponseEntity.ok(ApiResponse.success());
    }
}