package com.example.administrator.schoolsmartadmin;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ClientInfoStatus;
import java.util.HashMap;
import java.util.Map;

import tools.FileUtils;
import tools.NetWork;
import tools.TypeConverter;
import tools.URLValues;

public class AddGoodActivity extends AppCompatActivity {

    EditText nameEdit,priceEdit,numEdit;
    Spinner spinner;
    TextView add;
    ImageView img;
    String imagePath="";
    final int sendToastWhat=0x101,addWhat=0x102;
    final String sendToastKey="sendToastKey",addKey="addKey";
    ProgressDialog MyDialog;
    int type=1;
    Handler handler=new Handler(){
        public void handleMessage(Message message){
            switch (message.what) {
                case addWhat:
                    MyDialog.dismiss();
                    String addResult=message.getData().getString(addKey);
                    if(!addResult.equals("")) {
                        try {
                            JSONObject jsonObject = new JSONObject(addResult);
                            boolean code=jsonObject.getBoolean("code");
                            if(code){
                                setResult(1);
                                finish();
                                sendToast("上传成功");
                            }else{
                                sendToast("上传失败");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        sendToast("上传失败");
                    }

                    break;
                case sendToastWhat:
                    String toast=message.getData().getString(sendToastKey);
                    Toast.makeText(AddGoodActivity.this,toast,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_good);

        initView();
        MyDialog =new ProgressDialog(AddGoodActivity.this);
        MyDialog.setMessage("正在上传");
        MyDialog.setCancelable(false);

    }
    public void initView(){
        nameEdit=(EditText)findViewById(R.id.name);
        priceEdit=(EditText)findViewById(R.id.price);
        numEdit=(EditText)findViewById(R.id.num);
        spinner=(Spinner)findViewById(R.id.spinner);
        add=(TextView)findViewById(R.id.add);
        img=(ImageView)findViewById(R.id.img);
        img.setOnClickListener(new ClickListener());
        add.setOnClickListener(new ClickListener());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type=position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public class ClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.img:
                    Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
                    intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
                    startActivityForResult(intent, 1);
                    break;
                case R.id.add:
                    String name=nameEdit.getText().toString().trim();
                    String price=priceEdit.getText().toString().trim();
                    String num=numEdit.getText().toString().trim();

                    if(name.equals("")||price.equals("")||num.equals("")){
                        sendToast("请输入完整");
                    }
                    else {
                        if(imagePath.equals("")){
                            sendToast("请选择图片");
                        }
                        else {
                            MyDialog.show();
                            addGood(name, price, num);
                        }
                    }
                    break;
            }
        }
    }

    private void addGood(final String name, final String price, final String num) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetWork netWork=new NetWork();
                Map<String,String> map=new HashMap<String,String>();
                map.put("name",name);
                map.put("price",price);
                map.put("num",num);
                map.put("type",type+"");
                map.put("img", TypeConverter.GetImageStr(imagePath));
                String result=netWork.doPostImg(map, URLValues.addGoodURL);
                if(result==null)
                    result="" ;
                sendMessage(addWhat,addKey,result);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            imagePath = FileUtils.getUriPath(this, uri);
            Bitmap image=BitmapFactory.decodeFile(imagePath);
            img.setImageBitmap(image);

        }
    }
}
