package com.example.restaurant;

import Service.SendRequest;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    //这个我的服务器地址，我这边每一次的启动，地址都会改变，所以以变量形式使用
    public static String HostAddrs = "http://fp4s9q.natappfree.cc";
    public SendRequest getDataFromServe;

    private Button btn1;

    //这个是主线程通信对象，想在任何地方给主线程发送消息得靠mainHandler
    //主线程接收数据，安卓内部通信
    //为避免内存泄漏，正确写法应该是写个内部静态类+弱引用
    private static class MainHandler extends Handler {
        public void MainActivity() {
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                System.out.println("=========");
                System.out.println(msg.getData().toString());
            }
        }
    }

    //这个很重要
    public static final MainHandler mainHandler = new MainHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //一下是栗子
        btn1 = findViewById(R.id.btn1);
        getDataFromServe = new SendRequest();
        //这是向服务器请求数据的例子，看懂了你们就删了他
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromServe.setHandler(mainHandler);
                //地址
                getDataFromServe.setUrl(HostAddrs + "/user/test4");
                //安卓handle接收消息标识，可以使任意的整数
                getDataFromServe.setWhat(0);
                //get还是post请求
                getDataFromServe.setMethod("post");
                //这个无论是get，还是post，参数可有可无
                HashMap<String, String> params = new HashMap<>();
                params.put("king", "king");
                params.put("king2", "king2");
                //请求参数
                getDataFromServe.setHashMapParam(params);
                Thread thread = new Thread(getDataFromServe);
                thread.start();
            }
        });
    }

}
