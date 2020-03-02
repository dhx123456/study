package com.study.springboot.entity.form;


import com.study.springboot.util.BaseForm;

import javax.persistence.Column;

public class UserForm extends BaseForm<Integer> {
    private String name;    //姓名
    private Integer age;    //年龄
    private Integer sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
