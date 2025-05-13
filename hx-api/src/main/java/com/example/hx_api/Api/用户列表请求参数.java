package com.example.hx_api.Api;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("all")
@Data
public class 用户列表请求参数 implements 请求参数{
    public String type="RunProc";
    public String dbname="xerp_qxhexingxc";
    public String ProcName="p_klqe_QueryVipLimit_new";
    public String ProcArray;

    public void all(int pageid) {
        ProcArray = "{" +
                "\"INFO\": [" +
                "{\"FIELD\":\"queryType\",\"VALUE\":\"all\",\"TYPE\":1}," +
                "{\"FIELD\":\"orderType\",\"VALUE\":\"totalmoney\",\"TYPE\":1}," +
                "{\"FIELD\":\"pageid\",\"VALUE\":"+pageid+",\"TYPE\":0}," +
                "{\"FIELD\":\"bsid\",\"VALUE\":255982,\"TYPE\":0}," +
                "{\"FIELD\":\"filterJson\",\"VALUE\":\"" +
                "{\\\"opCardBeDate\\\":\\\"\\\"," + // 开卡开始日期
                "\\\"opCardEndDate\\\":\\\"\\\"," + // 开卡结束日期
                "\\\"ageBe\\\":\\\"\\\"," + // 年龄开始
                "\\\"ageEnd\\\":\\\"\\\"," + // 年龄结束
                "\\\"area\\\":\\\"\\\"," + // 地区
                "\\\"birthBeDate\\\":\\\"\\\"," + // 生日开始日期
                "\\\"birthEndDate\\\":\\\"\\\"," + // 生日结束日期
                "\\\"bottomsSize\\\":\\\"\\\"," + // 下装尺码
                "\\\"cardRestBe\\\":\\\"\\\"," + // 卡余额下限
                "\\\"cardResEnd\\\":\\\"\\\"," + // 卡余额上限
                "\\\"color\\\":\\\"\\\"," + // 喜好颜色
                "\\\"consumeBeDate\\\":\\\"\\\"," + // 消费开始日期
                "\\\"consumeEndDate\\\":\\\"\\\"," + // 消费结束日期
                "\\\"saleBe\\\":\\\"\\\"," + // 销售额下限
                "\\\"saleEnd\\\":\\\"\\\"," + // 销售额上限
                "\\\"consumeTimesBe\\\":\\\"\\\"," + // 消费次数下限
                "\\\"consumeTimesEnd\\\":\\\"\\\"," + // 消费次数上限
                "\\\"curIntegralBe\\\":\\\"\\\"," + // 当前积分下限
                "\\\"curIntegralEnd\\\":\\\"\\\"," + // 当前积分上限
                "\\\"customerTag\\\":\\\"\\\"," + // 客户标签
                "\\\"dvalue\\\":\\\"\\\"," + // D值
                "\\\"fvalue\\\":\\\"2,3,4,5,6\\\"," + // F值
                "\\\"guideName\\\":\\\"\\\"," + // 导购姓名
                "\\\"haswechat\\\":\\\"\\\"," + // 是否有微信
                "\\\"height\\\":\\\"\\\"," + // 身高
                "\\\"isBindOnline\\\":\\\"\\\"," + // 是否绑定在线
                "\\\"isWxFollow\\\":\\\"\\\"," + // 是否微信关注
                "\\\"ivalue\\\":\\\"\\\"," + // I值
                "\\\"job\\\":\\\"\\\"," + // 职业
                "\\\"keyword\\\":\\\"\\\"," + // 关键字
                "\\\"memoryBeDate\\\":\\\"\\\"," + // 纪念日开始日期
                "\\\"memoryEndDate\\\":\\\"\\\"," + // 纪念日结束日期
                "\\\"mvalue\\\":\\\"2,3,4,5,6\\\"," + // M值
                "\\\"noConsumeDaysBe\\\":\\\"\\\"," + // 未消费天数下限
                "\\\"noConsumeDaysEnd\\\":\\\"\\\"," + // 未消费天数上限
                "\\\"opCardGuide\\\":\\\"\\\"," + // 开卡导购
                "\\\"referHyid\\\":\\\"\\\"," + // 推荐会员ID
                "\\\"revisitBeDate\\\":\\\"\\\"," + // 复访开始日期
                "\\\"revisitEndDate\\\":\\\"\\\"," + // 复访结束日期
                "\\\"rvalue\\\":\\\"1,2,3\\\"," + // R值
                "\\\"shoesSize\\\":\\\"\\\"," + // 鞋码
                "\\\"shopid\\\":\\\"\\\"," + // 店铺ID
                "\\\"size\\\":\\\"\\\"," + // 尺码
                "\\\"skin\\\":\\\"\\\"," + // 皮肤类型
                "\\\"tix\\\":\\\"\\\"," + // 特殊需求
                "\\\"topsSize\\\":\\\"\\\"," + // 上装尺码
                "\\\"allIntegralBe\\\":\\\"\\\"," + // 总积分下限
                "\\\"allIntegralEnd\\\":\\\"\\\"," + // 总积分上限
                "\\\"trousersLength\\\":\\\"\\\"," + // 裤长
                "\\\"cardType\\\":\\\"\\\"}\"" + // 卡类型
                ",\"TYPE\":1}" +
                "]" +
                "}";


    }


    public void setProcArray(int pageid){
        ProcArray="{\n" +
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
                "   \"VALUE\":\"\",\n" +
                "   \"TYPE\":1\n" +
                "  }\n" +
                " ]\n" +
                "}";
    }

    public void demo(int pageid){
        ProcArray="{\n" +
                "\t\"INFO\":[\n" +
                "\t\t{\n" +
                "\t\t\t\"FIELD\":\"queryType\",\n" +
                "\t\t\t\"VALUE\":\"all\",\n" +
                "\t\t\t\"TYPE\":1\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"FIELD\":\"orderType\",\n" +
                "\t\t\t\"VALUE\":\"totalmoney\",\n" +
                "\t\t\t\"TYPE\":1\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"FIELD\":\"pageid\",\n" +
                "\t\t\t\"VALUE\":3,\n" +
                "\t\t\t\"TYPE\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"FIELD\":\"bsid\",\n" +
                "\t\t\t\"VALUE\":255982,\n" +
                "\t\t\t\"TYPE\":0\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"FIELD\":\"filterJson\",\n" +
                "\t\t\t\"VALUE\":\"{\n" +
                "\t\t\t\t\\\"accumulateBe\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"accumulateEnd\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"opCardBeDate\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"opCardEndDate\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"ageBe\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"ageEnd\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"area\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"birthBeDate\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"birthEndDate\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"bottomsSize\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"cardRestBe\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"cardResEnd\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"color\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"consumeBeDate\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"consumeEndDate\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"saleBe\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"saleEnd\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"consumeTimesBe\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"consumeTimesEnd\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"curIntegralBe\\\":\\\"5000\\\",\n" +
                "\t\t\t\t\\\"curIntegralEnd\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"customerTag\\\":\\\"郜小敏\\/殷艳霞\\/王青花\\/何丽美\\/史燕花\\\",\n" +
                "\t\t\t\t\\\"dvalue\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"fvalue\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"guideName\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"haswechat\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"height\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"isAddCorpwx\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"isBindOnline\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"isWxFollow\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"ivalue\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"job\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"keyword\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"memoryBeDate\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"memoryEndDate\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"mvalue\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"noConsumeDaysBe\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"noConsumeDaysEnd\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"norevisitBeDate\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"norevisitEndDate\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"opCardGuide\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"referHyid\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"revisitBeDate\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"revisitEndDate\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"revisitTheme\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"revisitType\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"rvalue\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"shoesSize\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"shopid\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"size\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"skin\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"tix\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"topsSize\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"allIntegralBe\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"allIntegralEnd\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"trousersLength\\\":\\\"\\\",\n" +
                "\t\t\t\t\\\"cardType\\\":\\\"\\\"\n" +
                "\t\t\t}\",\n" +
                "\t\t\t\"TYPE\":1\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}";
    }




}
