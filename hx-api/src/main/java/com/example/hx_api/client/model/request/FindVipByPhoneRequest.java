package com.example.hx_api.client.model.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FindVipByPhoneRequest {
    private String type = "RunProc";
    private String dbname = "xerp_qxhexingxc"; // 与旧版一致
    private String ProcName = "p_MB_ios_queryVipParam"; // 与旧版一致
    private String ProcArray;

    // 根据旧版逻辑，bsid, tokenid 等不是顶级字段，而是 ProcArray 的一部分
    // 我们这里保持与旧版请求参数类的结构一致，方便理解和迁移
    // 如果外部API的实际调用方式有变，需要调整此类或 HxApiClient 的调用方式

    public FindVipByPhoneRequest(String phone) {
        Map<String, Object> fieldBsid = new HashMap<>();
        fieldBsid.put("FIELD", "bsid");
        fieldBsid.put("VALUE", "2"); // 旧版固定为 "2"
        fieldBsid.put("TYPE", 1);

        Map<String, Object> fieldParam = new HashMap<>();
        fieldParam.put("FIELD", "param");
        fieldParam.put("VALUE", phone);
        fieldParam.put("TYPE", 1);

        Map<String, Object> fieldPageId = new HashMap<>();
        fieldPageId.put("FIELD", "pageid:"); // 注意旧版有个冒号
        fieldPageId.put("VALUE", 1);
        fieldPageId.put("TYPE", 0);

        Map<String, Object> fieldIsExact = new HashMap<>();
        fieldIsExact.put("FIELD", "isExact");
        fieldIsExact.put("VALUE", 1);
        fieldIsExact.put("TYPE", 0);

        List<Map<String, Object>> infoList = new ArrayList<>();
        infoList.add(fieldBsid);
        infoList.add(fieldParam);
        infoList.add(fieldPageId);
        infoList.add(fieldIsExact);

        Map<String, List<Map<String, Object>>> procArrayMap = new HashMap<>();
        procArrayMap.put("INFO", infoList);

        // 将Map转换为JSON字符串
        // 注意：这里简单地用了toString()，实际可能需要一个JSON库如FastJSON或Jackson来序列化
        // 但由于旧版是直接拼接字符串，这里也尽量模拟。
        // 更健壮的方式是让HxApiClient的sendRequest方法接受Map，并在内部用JSON库序列化。
        // 或者，如果外部API能接受更结构化的JSON，则直接传递Map。
        // 为保持与旧版ProcArray字符串格式一致，这里手动构造。
        StringBuilder sb = new StringBuilder("{\n");
        sb.append("        \"INFO\":[\n");
        sb.append("        {\n");
        sb.append("        \"FIELD\":\"bsid\",\n");
        sb.append("        \"VALUE\":\"2\",\n"); // 旧版固定为 "2"
        sb.append("        \"TYPE\":1\n");
        sb.append("        },\n");
        sb.append("        {\n");
        sb.append("        \"FIELD\":\"param\",\n");
        sb.append("        \"VALUE\":\"").append(phone).append("\",\n"); // 确保phone作为字符串
        sb.append("        \"TYPE\":1\n");
        sb.append("        },\n");
        sb.append("        {\n");
        sb.append("        \"FIELD\":\"pageid:\",\n");
        sb.append("        \"VALUE\":1,\n");
        sb.append("        \"TYPE\":0\n");
        sb.append("        },\n");
        sb.append("        {\n");
        sb.append("        \"FIELD\":\"isExact\",\n");
        sb.append("        \"VALUE\":1,\n");
        sb.append("        \"TYPE\":0\n");
        sb.append("        }\n");
        sb.append("        ]\n");
        sb.append("        }");
        this.ProcArray = sb.toString();
    }
}
