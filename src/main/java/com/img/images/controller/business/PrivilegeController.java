package com.img.images.controller.business;

import com.img.images.model.Privilege;
import com.img.images.service.PrivilegeService;
import com.img.images.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("business/p/privileges")
public class PrivilegeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private PrivilegeService privilegeService;

    @RequestMapping("list")
    public ModelAndView list(ModelAndView mv,
                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "1") Integer size,
                             @RequestParam(value = "name", required = false) String name) {
        mv.setViewName("business/protect/privileges");
        List<Privilege> privileges = privilegeService.search(page, size, name);
        mv.addObject("name", null == name ? "" : name);
        mv.addObject("page", page);
        mv.addObject("size", size);
        mv.addObject("privileges", privileges);
        return mv;
    }

    @RequestMapping("list/{id}")
    public R getPrivilege(@PathVariable("id") Long id) {
        Privilege privilege = privilegeService.getById(id);
        return R.ok(200, "成功！").put("privilege", privilege);
    }

    @DeleteMapping("delete/{id}")
    public R delete(@PathVariable("id") Long id) {
        List<Privilege> subPrivileges = privilegeService.getByParent(id);
        if(null != subPrivileges && subPrivileges.size() != 0) {
            return R.error(403,"请先删除子列表！");
        }
        privilegeService.delete(id);
        return R.ok(204,"成功！");
    }

    @PostMapping("add")
    public R create(Privilege privilege) {
        privilegeService.create(privilege);
        return R.ok(201,"新建成功！");
    }

    @PutMapping("update/{id}")
    public R update(Privilege privilege, @PathVariable("id") Long id) {
        Privilege privilege1 = privilegeService.getById(id);
        if (null == privilege1) {
            return R.error(404,"未找到此条数据！");
        }
        privilege.setId(id);
        privilegeService.update(privilege);
        return R.ok(204,"修改成功！");
    }
}
