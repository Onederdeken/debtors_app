package com.example.debtors2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.debtors2.Model.Model;
import com.example.debtors2.R;
import com.example.debtors2.json.Json;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterListItems extends ArrayAdapter<Model> {
    private final LayoutInflater inflater;
    private final int layout;
    private final List<Model> productList;
    private String[] keys;
    private  TextView Total_cost;

    public AdapterListItems(Context context, int resource, List<Model> products, TextView Total_cost) {
        super(context,resource,products);
        this.productList = products;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.keys = keys;
        this.Total_cost = Total_cost;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = inflater.inflate(this.layout,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Model map = productList.get(position);
        viewHolder.name.setText(map.name);;
        viewHolder.Cost.setText(Integer.toString(map.cost));

        int a = sum(productList);
        Total_cost.setText(Integer.toString(a));
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int coste = 0;
                try {
                    Json json = new Json(getContext());
                    List<Model> model = json.read_json();
                    JSONObject object = new JSONObject();
                    File file = new File("/data/data/com.example.debtors2/files/test.json");
                    FileOutputStream f = new FileOutputStream(file, false);

                    for (int i = 0; i < model.size(); i++) {
                        if(model.get(i).id != productList.get(position).id || (model.get(i).name != productList.get(position).name && model.get(i).cost != productList.get(position).cost)){
                            try {
                                JSONObject mod = new JSONObject("{id:" + Integer.toString(model.get(i).id) + ",name:" + model.get(i).name + ",cost:" + Integer.toString(model.get(i).cost) + "}\n");
                                object.put("product", mod);
                                f.write((object + "\n").getBytes());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        else{
                            coste = model.get(i).cost;
                        }
                    }

                    productList.remove(position);

                    f.close();
                    notifyDataSetChanged();
                    int a = sum(productList);
                    Total_cost.setText(Integer.toString(a));

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        });

        return convertView;
    }
    private int sum(List<Model> model){

        int sum = 0;
        for (int i = 0; i < model.size(); i++) {
            sum += model.get(i).cost;
        }
        return sum;
    }
    private static class ViewHolder{
        final ImageButton removeButton;
        final TextView name, Cost;

        ViewHolder(View View) {
            removeButton = View.findViewById(R.id.buckBtn2);
            name = View.findViewById(R.id.text_name_product);
            Cost = View.findViewById(R.id.text_cost_product);

        }
    }
}
