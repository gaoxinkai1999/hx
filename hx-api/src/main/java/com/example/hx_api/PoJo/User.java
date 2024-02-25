package com.example.hx_api.PoJo;

import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@ToString
public class User {

    public int id;
    public String name;
    public String password;
    public Dept 所属部门;

}
