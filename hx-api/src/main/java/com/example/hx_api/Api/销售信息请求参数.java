package com.example.hx_api.Api;

import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("all")
public class 销售信息请求参数 {
   public String type ="RunProc";
    public String dbname ="xerp_qxhexingxc";
    public String ProcName ="P_GetFootmark";
    public String bsid ="255982";
    public String tokenid ="443842AC-E352-4AD0-B454-9671585E16EB";
    public String pushtokenid ="";
    public String device ="1";
    public String version ="3.5.2";
    public  String ProcArray ;
    public void setHyid(String hyid){
        ProcArray="{\n" +
                " \"INFO\":[\n" +
                "  {\n" +
                "   \"FIELD\":\"bsid\",\n" +
                "   \"VALUE\":\"255982\",\n" +
                "   \"TYPE\":1\n" +
                "  },\n" +
                "  {\n" +
                "   \"FIELD\":\"hyid\",\n" +
                "   \"VALUE\":\""+hyid+"\",\n" +
                "   \"TYPE\":1\n" +
                "  },\n" +
                "  {\n" +
                "   \"FIELD\":\"bNum\",\n" +
                "   \"VALUE\":1,\n" +
                "   \"TYPE\":0\n" +
                "  },\n" +
                "  {\n" +
                "   \"FIELD\":\"qNum\",\n" +
                "   \"VALUE\":300,\n" +
                "   \"TYPE\":0\n" +
                "  },\n" +
                "  {\n" +
                "   \"FIELD\":\"filterType\",\n" +
                "   \"VALUE\":0,\n" +
                "   \"TYPE\":0\n" +
                "  }\n" +
                " ]\n" +
                "}";
    }

}
