package com.easyapp.demo;
import android.widget.BaseAdapter;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;
import java.io.File;
import com.easyapp.Render;
import android.widget.Toast;
import android.content.Context;
import android.view.animation.AnimationUtils;

public class ListAdapater extends BaseAdapter{
    
    private final List<HashMap<String, Object>> list = new ArrayList<>();
    private Context context;

    public ListAdapater(Context context){
        this.context = context;
    }
    
    public void update(List<HashMap<String, Object>> list){
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public HashMap<String,Object> getItem(int p1){
        return list.get(p1);
    }

    @Override
    public long getItemId(int p1){
        return 0;
    }

    @Override
    public View getView(int pos, View view, ViewGroup p3){
        try{
        view = LayoutInflater.from(context).inflate(R.layout.raw, null, true);
        final TextView text = view.findViewById(R.id.rawTextView1);
        final ImageView image = view.findViewById(R.id.rawImageView1);
        text.setText(list.get(pos).get("data").toString());
        int width = image.getWidth();
        int heigth = image.getHeight();
        Render.into(image)
        .resize(800, 800)
        .animation(AnimationUtils.loadAnimation(image.getContext(), android.R.anim.slide_in_left))
        .load(new File(getItem(pos).get("data").toString()));
        }catch(Throwable t){
            Toast.makeText(view.getContext(), t.toString(), 0).show();
        }
       
        return view;
    }

    
    
    
}
