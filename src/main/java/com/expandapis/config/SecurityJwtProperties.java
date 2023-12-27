package com.expandapis.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * {@link SecurityJwtProperties}
 *
 * @author Dmytro Trotsenko on 12/24/23
 */

@Component
@ConfigurationProperties(prefix = "security.jwt")
@Getter
@Setter
public class SecurityJwtProperties {

  private String tokenType;
  private String secretKey;
  private long expiration;

}
