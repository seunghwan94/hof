package com.lshwan.hof.domain.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthRequestDto {
  private String username;
  private String password;
}
