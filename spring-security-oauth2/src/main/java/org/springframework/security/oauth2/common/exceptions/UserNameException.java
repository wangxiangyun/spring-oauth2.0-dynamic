package org.springframework.security.oauth2.common.exceptions;

/**
 * Created by wangxiangyun on 2018/9/17.
 */
public class UserNameException  extends OAuth2Exception{
    public UserNameException(String msg, Throwable t) {
        super(msg, t);
    }
    
    public UserNameException(String msg) {
        super(msg);
    }
    
    @Override
    public int getHttpErrorCode() {
        // The spec says this can be unauthorized
        return 401;
    }
    
    @Override
    public String getOAuth2ErrorCode() {
        // Not in the spec
        return "username_not_found";
    }
}
