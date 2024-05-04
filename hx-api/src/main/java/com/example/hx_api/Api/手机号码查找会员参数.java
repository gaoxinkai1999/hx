package com.example.hx_api.Api;

import org.springframework.stereotype.Component;


public class 手机号码查找会员参数 implements 请求参数{

    public String type="RunProc";
    public String dbname="xerp_qxhexingxc";
    public String ProcName="p_MB_ios_queryVipParam";
//    public int bsid=255982;
//    public String tokenid="443842AC-E352-4AD0-B454-9671585E16EB";
    public String ProcArray;
    public 手机号码查找会员参数(String phone){

        ProcArray="{\n" +
                "        \"INFO\":[\n" +
                "        {\n" +
                "        \"FIELD\":\"bsid\",\n" +
                "        \"VALUE\":\"2\",\n" +
                "        \"TYPE\":1\n" +
                "        },\n" +
                "        {\n" +
                "        \"FIELD\":\"param\",\n" +
                "        \"VALUE\":"+phone+",\n" +
                "        \"TYPE\":1\n" +
                "        },\n" +
                "        {\n" +
                "        \"FIELD\":\"pageid:\",\n" +
                "        \"VALUE\":1,\n" +
                "        \"TYPE\":0\n" +
                "        },\n" +
                "        {\n" +
                "        \"FIELD\":\"isExact\",\n" +
                "        \"VALUE\":1,\n" +
                "        \"TYPE\":0\n" +
                "        }\n" +
                "        ]\n" +
                "        }";
    }
}

