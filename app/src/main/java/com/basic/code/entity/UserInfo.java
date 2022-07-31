package com.basic.code.entity;

public class UserInfo {

  private String loginName;

  private String password;

  public String getLoginName() {
    return loginName;
  }

  public UserInfo setLoginName(String loginName) {
    this.loginName = loginName;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public UserInfo setPassword(String password) {
    this.password = password;
    return this;
  }
}
