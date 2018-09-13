package org.springframework.security.oauth2.provider.permission;

/**
 * Created by wangxiangyun on 2018/9/13.
 */
public class Role {
    private String id;
    private String name;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
