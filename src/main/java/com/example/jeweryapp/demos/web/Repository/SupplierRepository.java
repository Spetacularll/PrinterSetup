package com.jewery.demo1.demos.web.Repository;

import com.jewery.demo1.demos.web.Entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier,Long> {
}
