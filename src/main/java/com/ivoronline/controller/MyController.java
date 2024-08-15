package com.ivoronline.controller;

import com.ivoronline.multitenant.tenant.config.TenantConfig;
import com.ivoronline.multitenant.tenant.entity.Person;
import com.ivoronline.multitenant.tenant.repository.PersonRepository;
import com.ivoronline.multitenant.tenant.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.sql.DataSource;

@RestController
public class MyController {

  //PROPERTIES
  @Autowired PersonRepository personRepository;
  
  //=========================================================================================================
  // SAVE PERSON
  //=========================================================================================================
  // http://localhost:8080/SavePerson?tenant=1&name=John&age=50
  @ResponseBody
  @GetMapping("/SavePerson")
  public Person savePerson(@RequestParam String name, @RequestParam Integer age) {
    Person person = personRepository.save(new Person(0, name, age));
    return person;
  }
  
  //=========================================================================================================
  // ADD TENANT
  //=========================================================================================================
  @ResponseBody
  @GetMapping("/AddTenant")
  public String addTenant() {
    DataSource dataSource = createDataSource();
    TenantConfig.targetDataSources.put("SCHEMA3", dataSource);
    TenantConfig.tenantDataSource.afterPropertiesSet();
    return "OK";
  }

  //=========================================================================================================
  // CREATE DATA SOURCE
  //=========================================================================================================
  public DataSource createDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
                            dataSource.setUrl     ("jdbc:oracle:thin:@localhost:1522/orcl");
                            dataSource.setUsername("SCHEMA3");
                            dataSource.setPassword("LETMEIN");
    return dataSource;
  }
  
}
