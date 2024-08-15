package com.ivoronline.multitenant.tenant.repository;

import com.ivoronline.multitenant.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Integer> { }
