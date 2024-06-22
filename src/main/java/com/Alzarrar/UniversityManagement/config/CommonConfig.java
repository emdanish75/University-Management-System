package com.Alzarrar.UniversityManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ResourceBundle;

@Configuration
public class CommonConfig {

  @Bean
  public ResourceBundle resourceBundle() {
    return ResourceBundle.getBundle("Bundle");
  }

}
