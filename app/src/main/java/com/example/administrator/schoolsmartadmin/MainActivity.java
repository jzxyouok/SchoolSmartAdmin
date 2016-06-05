package com.example.administrator.schoolsmartadmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.GoodsAdapter;
import bean.Good;
import tools.NetWork;
import tools.URLValues;

public class MainActivity extends AppCompatActivity {
    ImageView add;
    ListView list;
    final int getGoodsWhat = 0x101, sendToastWhat = 0x102,deleteGoodWhat=0x103;
    final String getGoodsKey = "getShopResult", sendToastText = "sendToast",deleteGoodKey="deleteGoodKey";
    List<Good> goods;
    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {

                case sendToastWhat:
                    String toast=message.getData().getString(sendToastText);
                    Toast.makeText(MainActivity.this, toast, Toast.LENGTH_SHORT).show();
                    break;
                case getGoodsWhat:
                    String homeData=message.getData().getString(getGoodsKey);
                    try {
                        goods=new ArrayList<Good>();
                        JSONObject jsonObject=new JSONObject(homeData);
                        JSONArray jsonArray=jsonObject.getJSONArray("goods");
                        for(int i=0;i<jsonArray.length();i++) {
                            Good good=new Good();
                            JSONObject goodObject=jsonArray.getJSONObject(i);
                            good.setId(goodObject.getInt("id"));
                            good.setName(goodObject.getString("name"));
                            good.setNum(goodObject.getInt("num"));
                            good.setPrice(goodObject.getDouble("price"));
                            good.setType(goodObject.getInt("type"));
                            good.setImg(goodObject.getString("img"));
                            goods.add(good);
                        }
                        GoodsAdapter goodsAdapter=new GoodsAdapter(MainActivity.this,goods);
                        list.setAdapter(goodsAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case deleteGoodWhat:
                    String deleteValue=message.getData().getString(deleteGoodKey);
                    try {
                        JSONObject jsonObject=new JSONObject(deleteValue);
                        boolean code=jsonObject.getBoolean("code");
                        if(code){
                            sendToast("删除成功");
                            initData();
                        }
                        else{
                            sendToast("删除失败");
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
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }
    public void initView(){
        list=(ListView)findViewById(R.id.goods_list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, GoodActivity.class);
                intent.putExtra("id", goods.get(position).getId());
                startActivity(intent);
            }
        });
       list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
               new AlertDialog.Builder(MainActivity.this)
                       .setTitle("确认")
                       .setMessage("确定吗？")
                       .setCancelable(false)
                       .setPositiveButton("是", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               deleteGood(goods.get(position).getId());
                           }
                       })
                       .setNegativeButton("否", null)
                       .show();

               return true;
           }
       });
        add=(ImageView)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddGoodActivity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==1){
            initData();
        }
    }

    private void deleteGood(final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetWork netWork=new NetWork();
                Map<String,String> map=new HashMap<String,String>();
                map.put("goodId",id+"");
                String result=netWork.doPost(map,URLValues.deleteGood);
                if(result!=null){
                    sendMessage(deleteGoodWhat,deleteGoodKey,result);
                }
            }
        }).start();

    }

    public void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetWork netWork=new NetWork();
                Map<String,String> map=new HashMap<String,String>();
                map.put("type",5+"");
                String getGoodsResult=netWork.doGet(map, URLValues.getGoodsURL);
                if(getGoodsResult==null){
                    sendToast("连接失败");
                }
                else{
                    sendMessage(getGoodsWhat,getGoodsKey,getGoodsResult);
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

    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
