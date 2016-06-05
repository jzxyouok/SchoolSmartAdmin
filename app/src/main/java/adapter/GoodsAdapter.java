package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.schoolsmartadmin.GoodActivity;
import com.example.administrator.schoolsmartadmin.R;

import java.util.List;

import bean.Good;
import tools.URLValues;


/**
 * Created by Administrator on 2016/2/18.
 */
public class GoodsAdapter extends BaseAdapter {
    Context context;
    List<Good> goodList;
    LayoutInflater inflater;


    public GoodsAdapter(Context context, List<Good> goodList) {
        this.context = context;
        this.goodList = goodList;
        this.inflater= LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return goodList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView=inflater.inflate(R.layout.goods_grid_item,null);
        TextView name=(TextView) convertView.findViewById(R.id.name);
        TextView num=(TextView)convertView.findViewById(R.id.num);
        TextView price=(TextView)convertView.findViewById(R.id.price);
        TextView type=(TextView)convertView.findViewById(R.id.type);
        ImageView goodImg=(ImageView)convertView.findViewById(R.id.img);

        Good good=goodList.get(position);
        try {
            Glide.with(context).load(URLValues.imgPath + good.getImg()).into(goodImg);
        }catch (Exception e){

        }
        name.setText(good.getName());
        num.setText("剩余 "+good.getNum()+"份");
        price.setText(good.getPrice()+" ¥");
        String typeName="";
        switch (good.getType()){
            case 1:
                typeName="超市";
                break;
            case 2:
                typeName="外卖";
                break;
            case 3:
                typeName="早餐";
                break;
            case 4:
                typeName="团购";
                break;
        }
        type.setText(typeName);
        return convertView;
    }
}
