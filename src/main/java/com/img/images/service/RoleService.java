package com.img.images.service;

import com.img.images.mapper.PrivilegeMapper;
import com.img.images.mapper.RoleMapper;
import com.img.images.mapper.RolePrivilegeMapper;
import com.img.images.model.Privilege;
import com.img.images.model.Role;
import com.img.images.model.RolePrivilege;
import com.img.images.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PrivilegeMapper privilegeMapper;

    @Autowired
    private RolePrivilegeMapper rolePrivilegeMapper;

    public Role get(Long id) {
        return roleMapper.get(id);
    }

    public void save(Role role) {
        roleMapper.save(role);
    }

    public void update(Role role) {
        roleMapper.update(role);
    }

    public void delete(Long id) {
        roleMapper.delete(id);
    }

    public List<Role> search(Integer page, Integer size, String name) {
        if (page < 1) {
            page = 1;
        }
        return roleMapper.search((page - 1) * size, size, name);
    }

    public List<Privilege> getPrivilegesByRole(Long id) {
        return roleMapper.getPrivilegesByRole(id);
    }

    public List<Map<String, Object>> getPrivileges() {
        List<Map<String, Object>> privileges = new ArrayList<>();
        List<Privilege> allPrivileges = privilegeMapper.getAllPrivileges();
        getRolePrivileges(privileges, allPrivileges, 0L, 1);
        return privileges;
    }

    public List<Long> getRolePrivileges(Long id) {
        List<Long> rPrivileges = new ArrayList<>();
        List<Privilege> rolePrivileges = roleMapper.getPrivilegesByRole(id);
        for (Privilege p : rolePrivileges) {
            if (null != p) {
                rPrivileges.add(p.getId());
            }
        }
        return rPrivileges;
    }

    public List<Map<String, Object>> getRolePrivileges(List<Map<String, Object>> privileges, List<Privilege> allPrivileges, Long id, int level) {
        for (Privilege p : allPrivileges) {
            if (p.getParentId() == id.longValue()) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", p.getId());
                map.put("name", p.getName());
                map.put("level", level);
                map.put("parent",p.getParentId());
                privileges.add(map);
                getRolePrivileges(privileges, allPrivileges, p.getId(), level + 1);
            }
        }
        return privileges;
    }

    public R updatePrivileges(Long id, Long[] rPrivileges) throws Exception {
        Role role = roleMapper.get(id);
        if (null == role) {
            return R.error(404, "角色不存在！").put("icon", "warning");
        }
        List<Privilege> oldPrivileges = roleMapper.getPrivilegesByRole(id);
        String rPStr = "#";
        for (Long pId : rPrivileges) {
            rPStr += pId + "#";
        }
        for (Privilege p : oldPrivileges) {
            if (null != p) {
                if (rPStr.contains("#" + p.getId() + "#")) {
                    rPStr = rPStr.replaceAll("#" + p.getId() + "#", "#");
                } else {
                    if (p.getId() != 1L) {
                        rolePrivilegeMapper.deleteByPrivilege(id, p.getId());
                    }
                }
            }
        }
        String[] rPArray = rPStr.split("#");
        for (String rp : rPArray) {
            if (!"".equals(rp) && !"0".equals(rp)) {
                List<RolePrivilege> oldRolePrivilege = rolePrivilegeMapper.getByPrivilege(id, Long.parseLong(rp));
                if (null == oldRolePrivilege || oldRolePrivilege.size() == 0) {
                    RolePrivilege rolePrivilege = new RolePrivilege();
                    rolePrivilege.setRoleId(id);
                    rolePrivilege.setPrivilegeId(Long.parseLong(rp));
                    rolePrivilegeMapper.save(rolePrivilege);
                }
            }
        }
        List<RolePrivilege> oldRolePrivilege = rolePrivilegeMapper.getByPrivilege(id, 1L);
        if (null == oldRolePrivilege || oldRolePrivilege.size() == 0) {
            RolePrivilege rolePrivilege = new RolePrivilege();
            rolePrivilege.setRoleId(id);
            rolePrivilege.setPrivilegeId(1L);
            rolePrivilegeMapper.save(rolePrivilege);
        }
        return R.ok(204, "成功！").put("icon", "success");
    }

    public List<Role> findAll() {
        return roleMapper.findAll();
    }
}
