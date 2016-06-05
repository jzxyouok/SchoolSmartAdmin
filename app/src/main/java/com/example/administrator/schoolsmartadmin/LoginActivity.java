package com.example.administrator.schoolsmartadmin;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tools.NetWork;
import tools.URLValues;

public class LoginActivity extends AppCompatActivity {
    final int loginWhat=0x101,sendToastWhat=0x102;
    final String loginKey="loginKey",sendToastKey="sendToastKey";
    EditText userNameEdit,passwordEdit;
    Handler handler=new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case loginWhat:
                    String result=message.getData().getString(loginKey);
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        String code=jsonObject.getString("code");
                        if(code.equals("success")){
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            if(code.equals("no_user")){
                                sendToast("没有该账号");
                            }else{
                                if(code.equals("error")){
                                    sendToast("密码错误");
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case sendToastWhat:
                    String toast=message.getData().getString(sendToastKey);
                    Toast.makeText(LoginActivity.this,toast,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }
    public void initView(){
        userNameEdit=(EditText)findViewById(R.id.user_name);
        passwordEdit=(EditText)findViewById(R.id.password);
        Button loginBT=(Button)findViewById(R.id.login_bt);
        loginBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameEdit.getText().toString().trim();
                String password = passwordEdit.getText().toString().trim();
                login(userName, password);
            }
        });
    }

    private void login(final String userName, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetWork netWork=new NetWork();
                Map<String,String> map=new HashMap<String,String>();
                map.put("userName",userName);
                map.put("password",password);
                String result=netWork.doGet(map, URLValues.loginURL);
                if(result!=null){
                    sendMessage(loginWhat,loginKey,result);
                }
            }
        }).start();
    }

    public void sendMessage(int what, String key, String value) {
        Message message = new Message();
        message.what = what;
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        message.setData(bundle);
        handler.sendMessage(message);
    }
    public void sendToast(String toast) {
        sendMessage(sendToastWhat, sendToastKey, toast);
    }
}
