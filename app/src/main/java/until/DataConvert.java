package until;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

//数据转换工具
public class DataConvert {

    //String to Json
    public JSONObject convertToJson(String data) {
        return JSONObject.parseObject(data);
    }

}
