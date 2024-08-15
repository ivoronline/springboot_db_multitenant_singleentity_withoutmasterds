package com.ivoronline.multitenant.tenant.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenantDataSource extends AbstractRoutingDataSource {

  //=========================================================================================================
  // DETERMINE CURRENT LOOKUP KEY
  //=========================================================================================================
  @Override
  protected Object determineCurrentLookupKey() {
    return TenantContext.getTenant();
  }
    
}
