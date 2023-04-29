package com.example.hx_api.PoJo;

import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@ToString
public class User {

    public int id;
    public String username;
    public String password;
    public String 职位;
    public String 所属部门;

}
