package org.springframework.security.oauth2.provider.authentication;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by wxy on 17/2/15.
 */
public class PermissionGrantedAuthority implements GrantedAuthority {

    private String url;
    private String method;

    public String getPermissionUrl() {
        return url;
    }

    public void setPermissionUrl(String permissionUrl) {
        this.url = permissionUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public PermissionGrantedAuthority(String url, String method) {
        this.url = url;
        this.method = method;
    }

    @Override
    public String getAuthority() {
        return this.url + ";" + this.method;
    }
}