package com.company.project;

import com.alibaba.fastjson.JSONObject;

public class Constants {
    private static final String tongjiJson = "{\"杨南涛\":{\"sorce\":3,\"month\":{\"0\":1,\"1\":0},\"year\":{\"0\":16,\"1\":32}},\"郑秀娜\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":13,\"1\":33}},\"韩非\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":1},\"year\":{\"0\":35,\"1\":10}},\"程志俊\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":1,\"1\":9}},\"查振毅\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":6,\"1\":24}},\"霍树岭\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":19,\"1\":33}},\"杨瑜民\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":1},\"year\":{\"0\":19,\"1\":36}},\"陈黎明\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":14,\"1\":11}},\"周宁\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":16,\"1\":33}},\"林春兰\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":5,\"1\":4}},\"鲍平\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":31,\"1\":13}},\"徐金城\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":17,\"1\":30}},\"贺东旭\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":19,\"1\":19}},\"豆耀仁\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":1},\"year\":{\"0\":9,\"1\":5}},\"叶正生\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":1},\"year\":{\"0\":6,\"1\":60}},\"陈为付\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":2,\"1\":8}},\"邵冬冬\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":0,\"1\":19}},\"梁铭树\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":4},\"year\":{\"0\":3,\"1\":34}},\"朱文健\":{\"sorce\":2,\"month\":{\"0\":2,\"1\":0},\"year\":{\"0\":33,\"1\":13}},\"干娜\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":1,\"1\":4}},\"胡永航\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":18,\"1\":15}},\"何志阳\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":1,\"1\":1}},\"黄莺\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":23,\"1\":12}},\"卢德志\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":3,\"1\":4}},\"林洁卿\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":20,\"1\":10}},\"吕晗\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":10,\"1\":15}},\"李金铢\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":1,\"1\":12}},\"李光耀\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":8,\"1\":14}},\"沙金宝\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":19,\"1\":8}},\"杨磊\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":2},\"year\":{\"0\":11,\"1\":8}},\"任硕\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":9,\"1\":7}},\"王与同\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":7,\"1\":5}},\"张静\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":13,\"1\":2}},\"刘振中\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":1,\"1\":5}},\"夏冬\":{\"sorce\":2,\"month\":{\"0\":1,\"1\":0},\"year\":{\"0\":7,\"1\":23}},\"陶涛\":{\"sorce\":3,\"month\":{\"0\":1,\"1\":0},\"year\":{\"0\":23,\"1\":25}},\"周春\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":25,\"1\":5}},\"赵志武\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":18,\"1\":4}},\"杨飞\":{\"sorce\":2,\"month\":{\"0\":1,\"1\":0},\"year\":{\"0\":22,\"1\":29}},\"王建兵\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":8,\"1\":15}},\"李宾\":{\"sorce\":3,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":15,\"1\":25}},\"刘奎\":{\"sorce\":2,\"month\":{\"0\":0,\"1\":0},\"year\":{\"0\":9,\"1\":2}}}";
    public static JSONObject baiFangTongji;

    static {
        baiFangTongji = JSONObject.parseObject(tongjiJson);
    }

}