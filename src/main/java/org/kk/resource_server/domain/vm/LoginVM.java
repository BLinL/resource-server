package org.kk.resource_server.domain.vm;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LoginVM {
    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    @Size(min = 1, max = 100)
    private String password;

    private boolean rememberMe;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoginVM{" +
                "username='" + username + '\'' +
                ", rememberMe=" + rememberMe +
                '}';
    }
}
