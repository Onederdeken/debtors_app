package com.example.debtors2.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.example.debtors2.Model.Model;
import com.example.debtors2.R;
import com.example.debtors2.activity_list;
import com.example.debtors2.fragment_not_debtors;
import com.example.debtors2.json.Json;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;

public class Adapter<String> extends ArrayAdapter<String> {
    private final LayoutInflater inflater;
    private final int layout;
    FragmentTransaction ft;
    private final List<String> productList;
    public Adapter(Context context, int resource, int resource2, List<String> products, FragmentTransaction ft) {
        super(context, resource,resource2, products);
        this.productList = products;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.ft =ft;
        replace();

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if(convertView==null){
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final String product = productList.get(position);

        viewHolder.nameView.setText(product.toString());
        viewHolder.nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), activity_list.class);
                intent.putExtra("position",position);
                intent.putExtra("name",productList.get(position).toString());
                getContext().startActivity(intent);


            }
        });

        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Json json = new Json(getContext());
                List<Model> model;
                try {
                     model = json.read_JSON_position(position);

                }
                catch (Exception e){
                    model = new ArrayList<>();
                    e.printStackTrace();
                }
                JSONObject object = new JSONObject();

                for(int i = 0; i<model.size(); i++){
                    Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject mod = new JSONObject("{id:"+Integer.toString(model.get(i).id)+",name:"+model.get(i).name+",cost:"+Integer.toString(model.get(i).cost)+"}\n");
                        object.put("product",mod);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    if(model.size()>0){
                        File files = new File("/data/data/com.example.debtors2/files/test.json");
                        FileOutputStream f1 = new FileOutputStream(files,false);
                        f1.write( (object.toString()+"\n").getBytes());
                        f1.close();
                    }
                    else{
                        File files = new File("/data/data/com.example.debtors2/files/test.json");
                        FileOutputStream f1 = new FileOutputStream(files,false);
                        f1.write( ("").getBytes());
                        f1.close();
                    }
                    File file = new File("/data/data/com.example.debtors2/files/names_debtors.txt");
                    FileOutputStream f = new FileOutputStream(file,false);
                    productList.remove(position);
                    for(int i = 0; i<productList.size(); i++) {
                        f.write((productList.get(i) + "\n").getBytes());
                    }
                        f.close();
                        notifyDataSetChanged();
                }catch (IOException e){
                    e.printStackTrace();
                }
                replace();
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        final ImageButton removeButton;
        final TextView nameView;
        ViewHolder(View view){

            removeButton = view.findViewById(R.id.buckBtn);
            nameView = view.findViewById(R.id.text_for_list);

        }
    }

    public void replace(){
        if(productList.isEmpty()){
            fragment_not_debtors ndb = new fragment_not_debtors();
            ft.replace(R.id.frame_main,ndb);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}


