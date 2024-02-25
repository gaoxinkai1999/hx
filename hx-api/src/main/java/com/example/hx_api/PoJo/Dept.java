package com.example.hx_api.PoJo;

import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@ToString
public class Dept {
    public int id;
    public String name;
    public String type;

    public ArrayList<User> users;
}
