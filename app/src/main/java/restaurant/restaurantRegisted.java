package restaurant;

import Service.SendRequest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSONObject;
import com.example.restaurant.R;
import com.example.restaurant.loginMain;
import until.DataConvert;

import java.util.HashMap;

public class restaurantRegisted extends AppCompatActivity {
    private TextView name, phone, password, storeName, address, introduction;

    private SendRequest sendRequest;
    private DataConvert dataConvert;
    private String role;

    private class restaurantRegisteredHandler extends Handler {
        //上下文
        private Context context;


        public void setContext(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Bundle bundle = msg.getData();
                JSONObject jsonObject = dataConvert.convertToJson(bundle.get("data").toString());
                String rmsg = jsonObject.get("msg").toString();
                Toast.makeText(restaurantRegisted.this, rmsg, Toast.LENGTH_LONG).show();

            }
        }
    }

    private restaurantRegisteredHandler restaurantRegisteredHandler = new restaurantRegisteredHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_activity_zhuce);
        this.role = getIntent().getStringExtra("role");
        if(role.equals("普通用户")){
            this.role="user";
        }else {
            this.role="restaurant";
        }
        init();
    }

    public void init() {
        sendRequest = new SendRequest();
        dataConvert = new DataConvert();
        name = findViewById(R.id.restaurant_name);
        password = findViewById(R.id.restaurant_password);
        phone = findViewById(R.id.restaurant_phone);
        address = findViewById(R.id.restaurant_address);
        storeName = findViewById(R.id.restaurant_store_name);
        introduction = findViewById(R.id.restaurant_introduction);
    }


    public void restaurantRegistered(View view) {
        restaurantRegisteredHandler.setContext(restaurantRegisted.this);
        sendRequest.setHandler(restaurantRegisteredHandler);
        //地址
        sendRequest.setUrl(loginMain.HostAddrs + "/" + role + "/registered");
        //安卓handle接收消息标识，可以使任意的整数
        sendRequest.setWhat(0);
        //get还是post请求
        sendRequest.setMethod("post");
        //这个无论是get，还是post，参数可有可无
        HashMap<String, String> params = new HashMap<>();
        params.put("name", name.getText().toString());
        params.put("password", password.getText().toString());
        params.put("phone", phone.getText().toString());
        params.put("address", address.getText().toString());
        params.put("storeName", storeName.getText().toString());
        params.put("introduction", introduction.getText().toString());
        //请求参数
        sendRequest.setHashMapParam(params);
        Thread thread = new Thread(sendRequest);
        thread.start();
    }

}
