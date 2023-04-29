package com.example.hx_api;

import com.example.hx_api.Api.*;
import com.example.hx_api.Dao.UserDao;
import com.example.hx_api.PoJo.User;
import com.example.hx_api.PoJo.Vip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
class ShoubaApplicationTests {
    @Autowired
    Api api;
    @Autowired
    用户列表请求参数 a;
    @Autowired
    销售信息请求参数 b;

@Autowired
    UserDao userDao;

    @Test
    void contextLoads() throws IOException, ParseException {


        ArrayList<User> users = userDao.getUsers();
        for (User user : users) {
            System.out.println(user);
        }
        ArrayList<Vip> vipByUserID = userDao.getVipByUserID(1);
        for (Vip vip : vipByUserID) {
            System.out.println(vip);
        }


//        for (int i = 1; i < 179; i++) {
//            a.setProcArray(i);
//            响应 result = api.用户列表(a);
//            JSONArray message = result.MESSAGE;
//            List<用户> s = message.toJavaList(用户.class);
//            for (用户 user : s) {
//                System.out.println(user);
//                b.setHyid(user.getHYID());
//
//                响应 销售信息 = api.销售信息(b);
//                List<订单> orders = 销售信息.MESSAGE.toJavaList(订单.class);
//                for (订单 order : orders) {
//                    //先判断日期
//                    if (判断时间(order.D_DATE)) {
//                        if (order.C_TYPE.equals("销售")) {
//                            user.set总购买数(user.总购买数 + 1);
//                            if (order.C_ZHUTI.equals("兽霸店")) {
//                                user.set兽霸购买数(user.兽霸购买数 + 1);
//                            }
//                        }
//                    } else {
//                        break;
//                    }
//                }
//                if ((user.兽霸购买数 * 1.0) / user.总购买数 > 0.3 && user.总购买数 >= 5) {
//                    //修改日期格式
//                    String sb = user.d_lastBuy;
//                    String s1 = sb.substring(0, 4);
//                    String s2 = sb.substring(4, 6);
//                    String s3 =sb.substring(6);
//                    user.d_lastBuy= s1+"-"+s2+"-"+s3;
//
//                    userDao.add(user);
//                }
//
//            }
//
//
//        }
    }


    //
//        int num=1;
//        Workbook book = new XSSFWorkbook("C:\\Users\\g\\Desktop\\客户维护.xlsx");
//        Sheet sheetAt = book.getSheetAt(0);
//        while (num < 753) {
//            Row row = sheetAt.getRow(num);
//            Cell cell = row.getCell(1);
//            String stringCellValue = cell.getStringCellValue();
//            userDao.add_old(stringCellValue);
//            num += 1;
//        }
    public boolean 判断时间(String time) {
        java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String s = time;
        String start = "2022-01-01";
        Date date = null;
        try {
            date = formatter.parse(s);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date start_date = null;
        try {
            start_date = formatter.parse(start);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date.after(start_date);
    }
    public void demo(){
        a.setProcArray(1);
        响应 result = api.用户列表(a);
        System.out.println(result);
    }


}
