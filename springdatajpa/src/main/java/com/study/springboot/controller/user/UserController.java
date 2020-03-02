package com.study.springboot.controller.user;

import com.study.springboot.controller.BaseController;
import com.study.springboot.entity.User;
import com.study.springboot.entity.form.UserForm;
import com.study.springboot.service.user.UserService;
import com.study.springboot.util.result.AjaxResult;
import com.study.springboot.util.result.ResultMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user/")
@Api(value = "用户相关接口", tags = "用户相关接口")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取所有用户列表", notes = "获取所有用户列表", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "getUserList", method = RequestMethod.GET)
    public ResultMsg getUserList() {
        return this.returnSuccess(userService.findAll());
    }

    @ApiOperation(value = "分页获取用户列表", notes = "分页获取用户列表", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "getUserListByPage", method = RequestMethod.GET)
    public ResultMsg getUserListByPage(@RequestParam(required = false) String name, @RequestParam int pageNumber,
                                       @RequestParam int pageSize) {
        UserForm userForm = new UserForm();
        userForm.setSearch(name);
        Page<User> userList = userService.getUserByPage(userForm, pageNumber, pageSize);
        if (!userList.isEmpty()) {
            return this.returnSuccess(userService.getUserByPage(userForm, pageNumber, pageSize));
        } else {
            return this.returnError("列表查询失败");
        }
    }


}
