package com.ivoronline.multitenant.tenant.config;

public class TenantContext {

  private static final ThreadLocal<String> tenant = new ThreadLocal<>();
  
  public static void setTenant(String newTenant) {
    tenant.set(newTenant);
  }
  
  public static String getTenant() {
    return tenant.get();
  }
  
  public static void clear() {
    tenant.remove();
  }
    
}