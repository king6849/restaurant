package Service;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.alibaba.fastjson.JSONObject;
import com.example.restaurant.MainActivity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendRequest implements Runnable {
    private String url;
    //消息传递标识
    private int what;
    //参数列表
    private HashMap<String, String> hashMapParam;
    private String method;
    private Handler handler;

    public void setUrl(String url)  {
        this.url = url;

    }

    public void setWhat(int what) {
        this.what = what;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setHashMapParam(HashMap<String, String> hashMapParam) {
        this.hashMapParam = hashMapParam;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public SendRequest() {
    }


    //post请求
    public void sendPostRequest() {
        try {
            HttpClient client = new DefaultHttpClient();
            System.out.println("url=" + url);
            HttpPost request = new HttpPost(new URI(url));
            //是否带参数
            if (hashMapParam != null) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                for (String key : this.hashMapParam.keySet()) {
                    params.add(new BasicNameValuePair(key, this.hashMapParam.get(key)));
                }
                request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            }
            HttpResponse response = client.execute(request);
            //判断请求是否成功
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String dataString = EntityUtils.toString(entity);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("data", dataString);
                message.setData(bundle);
                message.what = this.what;
                handler.sendMessage(message);
                this.hashMapParam = null;
                System.out.println("第一手数据" + dataString);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //get请求
    public void sendGetRequest() {
        try {
            HttpClient client = new DefaultHttpClient();
            if (hashMapParam != null) {
                url += "?";
                int len = this.hashMapParam.size();
                int i = 0;
                for (String key : hashMapParam.keySet()) {
                    url += key + "=" + this.hashMapParam.get(key);
                    if (len - 1 != i) {
                        url += "&";
                    }
                    i++;
                }
            }
            System.out.println("url=" + url);
            HttpGet request = new HttpGet(new URI(url));
            this.hashMapParam = null;
            //是否带参数
            HttpResponse response = client.execute(request);
            //判断请求是否成功
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                String dataString = EntityUtils.toString(entity);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("data", dataString);
                message.setData(bundle);

                message.what = this.what;
                handler.sendMessage(message);

                System.out.println("第一手数据" + dataString);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startRequest() {
        System.out.println("method is " + method);
        if (method.equals("post")) {
            this.sendPostRequest();
        } else {
            this.sendGetRequest();
        }
    }

    @Override
    public void run() {
        this.startRequest();
    }
}

