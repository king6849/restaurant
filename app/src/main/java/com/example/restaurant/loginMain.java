package com.example.restaurant;

import Service.SendRequest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.fastjson.JSONObject;
import until.DataConvert;

import java.util.HashMap;

public class loginMain extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
//    public static final String HostAddrs = "http://192.168.43.167:8081";
    public static final String HostAddrs = "http://4waeq4.natappfree.cc";
    private SendRequest sendRequest;
    private DataConvert dataConvert;

    private Button loginBtn, forgetBtn, registeredBtn;
    private TextView phone, password;
    private RadioGroup radioGroup;
    private static String role = "user";

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {

            case R.id.user:
                role = "user";
                break;

            case R.id.restaurant:
                role = "restaurant";
                break;
        }
    }

    private class loginHandler extends Handler {
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
                String code = jsonObject.get("code").toString();
                String rmsg = jsonObject.get("msg").toString();
                System.out.println(loginMain.role);
                if (code.equals("200")) {
                    Intent intent;
                    if (loginMain.role.equals("user")) {
                        intent = new Intent(context, customer.customerMain.class);
                    } else {
                        intent = new Intent(context, restaurant.restaurantMain.class);
                    }
                    startActivity(intent);
                } else {
                    AlertDialog alertDialog1 = new AlertDialog.Builder(context)
                            .setTitle("这是标题")//标题
                            .setMessage(rmsg)//内容
                            .setIcon(R.mipmap.ic_launcher)//图标
                            .create();
                    alertDialog1.show();
                }
            }
        }
    }

    private loginHandler loginHandler = new loginHandler();

    //初始化组件
    public void initComponents() {
        sendRequest = new SendRequest();
        dataConvert = new DataConvert();
        loginBtn = findViewById(R.id.loginBtn);
        forgetBtn = findViewById(R.id.wangJiMiMaButton);
        registeredBtn = findViewById(R.id.zhuCeButton);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        radioGroup = findViewById(R.id.role);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initComponents();

    }


    //登录入口
    public void login(View view) {
        loginHandler.setContext(loginMain.this);

        sendRequest.setHandler(loginHandler);
        //地址
        sendRequest.setUrl(HostAddrs + "/" + this.role + "/login");
        //安卓handle接收消息标识，可以使任意的整数
        sendRequest.setWhat(0);
        //get还是post请求
        sendRequest.setMethod("post");
        //这个无论是get，还是post，参数可有可无
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone.getText().toString());
        params.put("password", password.getText().toString());
        //请求参数
        sendRequest.setHashMapParam(params);
        Thread thread = new Thread(sendRequest);
        thread.start();
    }

    //注册
    public void zhuce(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(loginMain.this);
        //builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("选择身份");
        //    指定下拉列表的显示数据
        final String[] person = {"普通用户", "店家"};
        //    设置一个下拉的列表选择项
        builder.setItems(person, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent intent = new Intent(loginMain.this, customer.customerRegisted.class);
                    intent.putExtra("role", person[which]);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(loginMain.this, restaurant.restaurantRegisted.class);
                    intent.putExtra("role", person[which]);
                    startActivity(intent);
                }
            }
        });
        builder.show();
    }


}
