package org.springframework.security.oauth2.provider.access;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by wangxiangyun on 2018/9/14.
 */
public class UserCore extends User {
    private static final long serialVersionUID  = -738377427181202286L;
    private String userId;
    private String age   ;
    private String phone ;
    private String idCard ;
    private String userType ;
    private String storeId ;
    private String companyId ;
    private String tenantId ;
    private boolean resetFlag ;
    public UserCore(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
    
    public UserCore(String username, String password, boolean enabled, boolean accountNonExpired,
                    boolean credentialsNonExpired, boolean accountNonLocked,
                    Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public boolean isResetFlag() {
        return resetFlag;
    }

    public void setResetFlag(boolean resetFlag) {
        this.resetFlag = resetFlag;
    }
}
