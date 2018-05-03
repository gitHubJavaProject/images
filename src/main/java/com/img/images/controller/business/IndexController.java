package com.img.images.controller.business;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("business/p")
public class IndexController {
    @RequestMapping("index")
    public ModelAndView uploadPage(ModelAndView mv) {
        mv.setViewName("business/protect/index");
        return mv;
    }
}
