package customer;

import Service.SendRequest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSONObject;
import com.example.restaurant.R;
import com.example.restaurant.loginMain;
import until.DataConvert;

import java.util.HashMap;

public class customerRegisted extends AppCompatActivity {
    private TextView name, phone, password;

    private SendRequest sendRequest;
    private DataConvert dataConvert;
    private String role;

    private class customerRegisteredHandler extends Handler {
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
                Toast.makeText(customerRegisted.this, rmsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private customerRegisteredHandler registeredHandler = new customerRegisteredHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.costumer_activity_zhuce);
        Intent intent = getIntent();
        this.role = intent.getStringExtra("role");
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
        name = findViewById(R.id.customer_name);
        password = findViewById(R.id.customer_password);
        phone = findViewById(R.id.customer_phone);

    }


    public void customerRegistered(View view) {
        registeredHandler.setContext(customerRegisted.this);
        sendRequest.setHandler(registeredHandler);
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
        //请求参数
        sendRequest.setHashMapParam(params);
        Thread thread = new Thread(sendRequest);
        thread.start();
    }

}
