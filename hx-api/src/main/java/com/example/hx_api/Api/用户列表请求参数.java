package com.example.hx_api.Api;

import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("all")
public class 用户列表请求参数 {
    public String type="RunProc";
    public String dbname="xerp_qxhexingxc";
    public String ProcName="p_klqe_QueryVipLimit_new";
    public String ProcArray;

    public void setProcArray(int pageid) {
        ProcArray ="{\n" +
                " \"INFO\":[\n" +
                "  {\n" +
                "   \"FIELD\":\"queryType\",\n" +
                "   \"VALUE\":\"all\",\n" +
                "   \"TYPE\":1\n" +
                "  },\n" +
                "  {\n" +
                "   \"FIELD\":\"orderType\",\n" +
                "   \"VALUE\":\"totalmoney\",\n" +
                "   \"TYPE\":1\n" +
                "  },\n" +
                "  {\n" +
                "   \"FIELD\":\"pageid\",\n" +
                "   \"VALUE\":"+pageid+",\n" +
                "   \"TYPE\":0\n" +
                "  },\n" +
                "  {\n" +
                "   \"FIELD\":\"bsid\",\n" +
                "   \"VALUE\":255982,\n" +
                "   \"TYPE\":0\n" +
                "  },\n" +
                "  {\n" +
                "   \"FIELD\":\"filterJson\",\n" +
                "   \"VALUE\":\"{\n" +
                "    \\\"opCardBeDate\\\":\\\"\\\",\n" +
                "    \\\"opCardEndDate\\\":\\\"\\\",\n" +
                "    \\\"ageBe\\\":\\\"\\\",\n" +
                "    \\\"ageEnd\\\":\\\"\\\",\n" +
                "    \\\"area\\\":\\\"\\\",\n" +
                "    \\\"birthBeDate\\\":\\\"\\\",\n" +
                "    \\\"birthEndDate\\\":\\\"\\\",\n" +
                "    \\\"bottomsSize\\\":\\\"\\\",\n" +
                "    \\\"cardRestBe\\\":\\\"\\\",\n" +
                "    \\\"cardResEnd\\\":\\\"\\\",\n" +
                "    \\\"color\\\":\\\"\\\",\n" +
                "    \\\"consumeBeDate\\\":\\\"\\\",\n" +
                "    \\\"consumeEndDate\\\":\\\"\\\",\n" +
                "    \\\"saleBe\\\":\\\"\\\",\n" +
                "    \\\"saleEnd\\\":\\\"\\\",\n" +
                "    \\\"consumeTimesBe\\\":\\\"5\\\",\n" +
                "    \\\"consumeTimesEnd\\\":\\\"\\\",\n" +
                "    \\\"curIntegralBe\\\":\\\"\\\",\n" +
                "    \\\"curIntegralEnd\\\":\\\"\\\",\n" +
                "    \\\"customerTag\\\":\\\"\\\",\n" +
                "    \\\"dvalue\\\":\\\"\\\",\n" +
                "    \\\"fvalue\\\":\\\"\\\",\n" +
                "    \\\"guideName\\\":\\\"\\\",\n" +
                "    \\\"haswechat\\\":\\\"\\\",\n" +
                "    \\\"isBindOnline\\\":\\\"\\\",\n" +
                "    \\\"isWxFollow\\\":\\\"\\\",\n" +
                "    \\\"ivalue\\\":\\\"\\\",\n" +
                "    \\\"job\\\":\\\"\\\",\n" +
                "    \\\"keyword\\\":\\\"\\\",\n" +
                "    \\\"memoryBeDate\\\":\\\"\\\",\n" +
                "    \\\"memoryEndDate\\\":\\\"\\\",\n" +
                "    \\\"mvalue\\\":\\\"\\\",\n" +
                "    \\\"noConsumeDaysBe\\\":\\\"0\\\",\n" +
                "    \\\"noConsumeDaysEnd\\\":\\\"180\\\",\n" +
                "    \\\"opCardGuide\\\":\\\"\\\",\n" +
                "    \\\"referHyid\\\":\\\"\\\",\n" +
                "    \\\"revisitBeDate\\\":\\\"\\\",\n" +
                "    \\\"revisitEndDate\\\":\\\"\\\",\n" +
                "    \\\"rvalue\\\":\\\"\\\",\n" +
                "    \\\"shoesSize\\\":\\\"\\\",\n" +
                "    \\\"shopid\\\":\\\"\\\",\n" +
                "    \\\"size\\\":\\\"\\\",\n" +
                "    \\\"topsSize\\\":\\\"\\\",\n" +
                "    \\\"allIntegralBe\\\":\\\"1000\\\",\n" +
                "    \\\"allIntegralEnd\\\":\\\"\\\",\n" +
                "    \\\"cardType\\\":\\\"\\\"\n" +
                "   }\",\n" +
                "   \"TYPE\":1\n" +
                "  }\n" +
                " ]\n" +
                "}";
    }
}
