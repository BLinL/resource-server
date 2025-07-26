package org.kk.resource_server.domain.vm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JWTToken {

    private String idToken;
    private String refreshToken;
    public JWTToken() {}
    public JWTToken(String idToken, String refreshToken) {
        this.idToken = idToken;
        this.refreshToken = refreshToken;
    }

    @JsonProperty("id_token")
    public String getIdToken() {
        return idToken;
    }


    @JsonProperty("refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}