package com.img.images.service;

import com.img.images.mapper.*;
import com.img.images.model.Privilege;
import com.img.images.model.Role;
import com.img.images.model.User;
import com.img.images.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PrivilegeMapper privilegeMapper;

    @Autowired
    private RolePrivilegeMapper rolePrivilegeMapper;

    public List<UserRole> getUserRoleByUserId(Long userId) {
        return userMapper.getUserRoleByUserId(userId);
    }

    public void update(User user) {
        userMapper.update(user);
    }

    public List<User> search(Integer page, Integer size, String name){
        if (page < 1) {
            page = 1;
        }
        return userMapper.search((page - 1) * size, size, name);
    }

    public User getByUserName(String userName) {
        return userMapper.getByUserName(userName);
    }

    public void create(User user){
        userMapper.create(user);
    }

    public User getById(Long id) {
        return userMapper.getById(id);
    }

    public void startOrStopUser(User user) {
        userMapper.startOrStopUser(user);
    }

    public List<Role> findRolesByUserId(Long userId) {
        return this.roleMapper.findRolesByUserId(userId);
    }

    public List<UserRole> findRoleByUserId(Long id) {
        return this.userRoleMapper.findByUserId(id);
    }

    public void updatePwdAndPhone(User user){
        userMapper.updatePwdAndPhone(user);
    }

    public List<User> getByOrgId(Integer orgId){
        return userMapper.getByOrgId(orgId);
    }

    public void saveChange(Long[] roles, Long id) {
        List<UserRole> userRoles = userRoleMapper.findByUserId(id);
        for (UserRole ur : userRoles) {
            userRoleMapper.delete(ur.getId());
        }
        for (Long r : roles) {
            UserRole userRole = new UserRole();
            userRole.setRoleId(r);
            userRole.setUserId(id);
            userRoleMapper.create(userRole);
        }
    }

    public List<Privilege> getPrivilegesByUser(Long id) {
        return userMapper.getPrivilegesByUser(id);
    }
}
