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
        
        final Object object = getItem(pos).get("key");
        
        if(object instanceof File){
            
            image.post(new Runnable(){

                    @Override
                    public void run(){
                        File file = (File)object;
                        text.setText(file.getName());
                        Render.into(image)
                            .loading(R.drawable.ic_launcher)
                            .resize(image.getWidth(), image.getHeight())
                            .animation(AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left))
                            .load(file);
                    }

                
            });
            
            
        }else{
            
            
        }
        }catch(Throwable t){
            Toast.makeText(view.getContext(), t.toString(), 0).show();
        }
        
        
        return view;
    }

    
    
    
}
