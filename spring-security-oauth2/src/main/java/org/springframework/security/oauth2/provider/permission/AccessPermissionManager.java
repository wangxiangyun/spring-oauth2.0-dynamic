package org.springframework.security.oauth2.provider.permission;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * 用户管理用户的权限的超类
 * Created by wangxiangyun on 2018/9/13.
 */
public interface AccessPermissionManager {
    /**
     * 获取当前登录用户的角色列表
     * @param user
     * @return
     */
  List<Role> getUserRole(User user);
    
    /**
     * 获取当前用户的所有角色的权限集合
     * @param user
     * @return
     */
  List<Permission> getUserAllPermisson(User user);
    
    /**
     * 判断是否有权限
     * @param httpAuth
     * @param auth2
     * @return
     */
  boolean  checkPermission(Authentication httpAuth, Authentication auth2);
  
}
