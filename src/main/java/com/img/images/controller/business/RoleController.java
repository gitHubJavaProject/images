package com.img.images.controller.business;

import com.img.images.model.Role;
import com.img.images.service.RoleService;
import com.img.images.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("business/p/roles")
public class RoleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RoleService roleService;

    @RequestMapping("list")
    public ModelAndView list(ModelAndView mv,
                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                             @RequestParam(value = "name", required = false) String name) {
        mv.setViewName("business/protect/roles");
        List<Role> roles = roleService.search(page, size, name);
        mv.addObject("name", null == name ? "" : name);
        mv.addObject("page", page);
        mv.addObject("size", size);
        mv.addObject("roles", roles);
        return mv;
    }

    @RequestMapping("_")
    public ModelAndView getRole(ModelAndView mv) {
        mv.setViewName("business/protect/role");
        return mv;
    }

    @RequestMapping("list/{id}")
    public ModelAndView getRoleById(ModelAndView mv, @PathVariable("id") Long id) {
        mv.setViewName("business/protect/role");
        mv.addObject("role", roleService.get(id));
        return mv;
    }

    @RequestMapping("{id}/rolePrivileges")
    public ModelAndView rolePrivileges(ModelAndView mv, @PathVariable("id") Long id) {
        mv.setViewName("business/protect/role_privileges");
        List<Map<String, Object>> privileges = roleService.getPrivileges();
        mv.addObject("privileges", privileges);
        mv.addObject("rPrivileges", roleService.getRolePrivileges(id));
        mv.addObject("roleId", id);
        return mv;
    }

    @RequestMapping("{id}/updatePrivileges")
    public R updatePrivileges(@PathVariable("id") Long id, Long[] rPrivileges) {
        try {
            return roleService.updatePrivileges(id, rPrivileges);
        } catch (Exception e) {
            LOGGER.error("系统内部错误！", e);
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }

    @PostMapping("add")
    public R addRole(Role role) {
        roleService.save(role);
        return R.ok(201, "新增成功！").put("role", role).put("icon", "success");
    }

    @PutMapping("update/{id}")
    public R updateRole(@PathVariable("id") Long id, Role role) {
        try {
            Role oldRole = roleService.get(id);
            if (null == oldRole) {
                return R.error(404, "数据不存在！").put("icon", "warning");
            }
            oldRole.setName(role.getName());
            oldRole.setDescription(role.getDescription());
            roleService.update(oldRole);
            return R.ok(204, "成功！").put("role", role).put("icon", "warning");
        } catch (Exception e) {
            LOGGER.error("系统内部错误！", e);
            return R.error(500, "系统内部错误！").put("icon", "error");
        }
    }
}
