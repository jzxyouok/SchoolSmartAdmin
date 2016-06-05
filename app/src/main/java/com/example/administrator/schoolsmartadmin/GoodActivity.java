package com.example.administrator.schoolsmartadmin;

import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.CommentAdapter;
import bean.Comment;
import tools.NetWork;
import tools.URLValues;

public class GoodActivity extends AppCompatActivity {
    /**
     * 商品详情
     */
    ListView commentList;
    final int addCartWhat = 0x101, sendToastWhat = 0x102,getGoodWhat=0x103,collectWhat=0x104;
    final String addCartKey = "getShopResult", sendToastText = "sendToast",getGoodKey="getGoodKey",collectKey="collectKey";
    int id=0;
    TextView nameText,priceText,numText,commentNumText;
    boolean ifCollect=false;
    TextView goodCommentNumText;
    ImageView goodImg;
    Handler handler=new Handler(){
        public void handleMessage(Message message){
            switch (message.what){
                case addCartWhat:
                    String result=message.getData().getString(addCartKey);
                    try {
                        JSONObject jsonObject=new JSONObject(result);
                        String code=jsonObject.getString("code");
                        if(code.equals("success")){
                            sendToast("加入购物车成功");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case sendToastWhat:
                    String toast=message.getData().getString(sendToastText);
                    Toast.makeText(GoodActivity.this, toast, Toast.LENGTH_SHORT).show();
                    break;
                case getGoodWhat:
                    String getGoodResult=message.getData().getString(getGoodKey);
                    try {
                        JSONObject jsonObject=new JSONObject(getGoodResult);
                        JSONArray jsonArray=jsonObject.getJSONArray("comments");
                        List<Comment> comments=new ArrayList<Comment>();
                        for(int i=0;i<jsonArray.length();i++){
                            Comment comment=new Comment();
                            JSONObject commentObject=jsonArray.getJSONObject(i);

                            comment.setType(commentObject.getInt("type"));
                            comment.setContent(commentObject.getString("content"));
                            comment.setTime(commentObject.getString("time"));
                            comment.setName(commentObject.getString("userName"));
                            comments.add(comment);
                        }
                        CommentAdapter adapter=new CommentAdapter(GoodActivity.this,comments);
                        commentList.setAdapter(adapter);
                        MainActivity.setListViewHeightBasedOnChildren(commentList);
                        String name=jsonObject.getString("name");
                        Double price=jsonObject.getDouble("price");
                        int num=jsonObject.getInt("num");
                        int goodComment=jsonObject.getInt("good_comment");
                        int badComment = jsonObject.getInt("bad_comment");
                        String img=jsonObject.getString("img");
                        String time=jsonObject.getString("time");
                        int type = jsonObject.getInt("type");
                        ifCollect=jsonObject.getBoolean("collect");
                        nameText.setText(name);
                        priceText.setText(price+" ¥");
                        numText.setText("剩余"+num+" 份");
                        commentNumText.setText(goodComment+badComment+"人评价");
                        try {
                            Glide.with(GoodActivity.this).load(URLValues.imgPath + img).into(goodImg);
                        }catch (Exception e){

                        }
                        if(goodComment+badComment>0){
                            goodCommentNumText.setText(goodComment*100/(goodComment+badComment)+"%");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case collectWhat:
                    String collectValue=message.getData().getString(collectKey);
                    try {
                        JSONObject jsonObject=new JSONObject(collectValue);
                        boolean collectResult=jsonObject.getBoolean("code");
                        if(collectResult){
                            initData();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        good=(Good)getIntent().getSerializableExtra("good");
        setContentView(R.layout.activity_good);

        id=getIntent().getIntExtra("id",-1);
        initView();
        initData();
    }
    public void initView(){
        goodCommentNumText=(TextView)findViewById(R.id.good_comment_num);
        ImageView back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new BTClickListener());
        goodImg=(ImageView)findViewById(R.id.good_img);
        commentList=(ListView)findViewById(R.id.envaluate);

        nameText=(TextView)findViewById(R.id.name);
        priceText=(TextView)findViewById(R.id.price);
        numText=(TextView)findViewById(R.id.num);
        commentNumText=(TextView)findViewById(R.id.comment_num);
    }



    public class BTClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;

            }
        }
    }
    public void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetWork netWork=new NetWork();
                Map<String,String> map=new HashMap<String, String>();
                map.put("id",id+"");
                map.put("userId","admin");
                String result= netWork.doGet(map,URLValues.getGoodURL);
                if(result!=null){
                    sendMessage(getGoodWhat,getGoodKey,result);
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
        sendMessage(sendToastWhat, sendToastText, toast);
    }


}
