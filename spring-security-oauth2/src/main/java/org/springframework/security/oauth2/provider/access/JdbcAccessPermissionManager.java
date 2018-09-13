package org.springframework.security.oauth2.provider.access;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by wangxiangyun on 2018/9/13.
 */
public class JdbcAccessPermissionManager implements AccessPermissionManager {
    
    private final JdbcTemplate jdbcTemplate;
    
    public JdbcAccessPermissionManager(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource required");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @Override
    public List<Role> getUserRole(User user) {
        String sql = "SELECT * FROM role WHERE id IN (SELECT role_id FROM user_role WHERE user_id =?) ";
        List<Role> roleList = jdbcTemplate.query(sql, new RowMapper<Role>() {
            @Override
            public Role mapRow(ResultSet resultSet, int i) throws SQLException {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                Role role = new Role();
                role.setId(id);
                role.setName(name);
                return role;
            }
        }, user.getUsername());
        return roleList;
    }
    
    @Override
    public List<Permission> getUserAllPermisson(User user) {
        String sql = "SELECT * FROM permission WHERE id IN "
                + "(SELECT id FROM role_permisson WHERE  role_id IN "
                + "( SELECT id FROM role WHERE id IN (SELECT role_id FROM user_role WHERE user_id =?)))";
        List<Permission> permissionList = jdbcTemplate.query(sql, new RowMapper<Permission>() {
            @Override
            public Permission mapRow(ResultSet resultSet, int i) throws SQLException {
                String id = resultSet.getString("id");
                String pid = resultSet.getString("pid");
                String name = resultSet.getString("name");
                String path = resultSet.getString("url");
                String method = resultSet.getString("method");
                Permission permission = new Permission();
                permission.setId(id);
                permission.setPid(pid);
                permission.setName(name);
                permission.setPath(path);
                permission.setMethod(method);
                return permission;
            }
        }, user.getUsername());
        return permissionList;
    }
    
    @Override
    public boolean checkPermission(Authentication httpAuth, Authentication auth2) {
        //检查是不是用户登录 如果是客户端登陆就不行了
        if (auth2 instanceof OAuth2Authentication) {
            OAuth2Authentication auth2Authentication = (OAuth2Authentication) auth2;
            if (auth2Authentication.getUserAuthentication() == null) {
                return false;
            }
        }
        Object object = httpAuth.getCredentials();
        if (object instanceof HttpServletRequest) {
            HttpServletRequest request = (HttpServletRequest) object;
            final String URI = request.getRequestURI();
            final String METHOD = request.getMethod();
            final String ALL_METHOD = "ALL";
            List<Permission> permissionList = this.getUserAllPermisson((User) auth2.getPrincipal());
            
            if (!permissionList.isEmpty()) {
                PathMatcher matcher = new AntPathMatcher();
                for (Permission permission : permissionList) {
                    String patternURL = permission.getPath();
                    String grandMethod = permission.getMethod();
                    boolean matchFlag = matcher.match(patternURL, URI);
                    if (matchFlag == true && grandMethod.equals(ALL_METHOD)) {
                        return true;
                    }
                    if (matchFlag == true && grandMethod.equals(METHOD)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
