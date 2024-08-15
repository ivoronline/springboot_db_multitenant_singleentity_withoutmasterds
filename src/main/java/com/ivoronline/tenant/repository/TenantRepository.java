package com.ivoronline.tenant.repository;

import com.ivoronline.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Integer> { }
