package com.study.springboot.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;    //姓名
    @Column(name = "age")
    private Integer age;    //年龄
    @Column(name = "sex")
    private Integer sex;   //性别


}
