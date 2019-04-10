package com.cm.deity.addressbook.ctl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenming
 * @version 1.0
 * @Description 描述
 * @date 2019/4/10
 */
@RestController
@RequestMapping("/address/user")
public class UserCtrl {
    @RequestMapping("/getUser.do")
    public void getUser(){
        System.out.println("123");
    }
}
