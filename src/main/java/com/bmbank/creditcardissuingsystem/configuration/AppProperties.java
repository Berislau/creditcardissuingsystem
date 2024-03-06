package com.bmbank.creditcardissuingsystem.configuration;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Validated
@ConfigurationProperties(prefix = "creditcardissuingsystem")
public class AppProperties {

    @Getter @Setter private String fileStorageLocation = "default/path";
}
