package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.schoolsmartadmin.R;

import java.util.List;

import bean.Comment;


/**
 * Created by Administrator on 2016/5/11.
 */
public class CommentAdapter extends BaseAdapter {

    Context context;
    List<Comment> comments;
    LayoutInflater inflater;
    public CommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
        this.inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return comments.size();
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
    public View getView(int position, View view, ViewGroup parent) {
        view=inflater.inflate(R.layout.comment_item,null);
        TextView name=(TextView)view.findViewById(R.id.name);
        TextView time=(TextView)view.findViewById(R.id.time);
        TextView content=(TextView)view.findViewById(R.id.content);
        TextView type=(TextView)view.findViewById(R.id.type);
        Comment comment=comments.get(position);
        name.setText(comment.getName());
        if(comment.getType()==1){
            type.setText("好评");
        }
        else{
            type.setText("差评");
        }
        time.setText(comment.getTime());
        content.setText(comment.getContent());
        return view;
    }
}
