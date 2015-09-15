package com.devonulrich.genesisclient.login;

public class LoginInfo {

    public String email;
    public String pass;
    public String id;

    public boolean autoLogin;

    public LoginInfo(String email, String pass, String id, boolean autoLogin) {
        this.email = email;
        this.pass = pass;
        this.id = id;

        this.autoLogin = autoLogin;
    }

    public LoginInfo() {
        email = "";
        pass = "";
        id = "";
    }
}
