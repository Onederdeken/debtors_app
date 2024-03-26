package com.example.debtors2;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.debtors2.Model.Model;
import com.example.debtors2.adapters.AdapterListItems;
import com.example.debtors2.json.Json;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activity_list extends AppCompatActivity {
    private ListView list;
    private TextView NameDebtor, cost;
    private Bundle arguments;
    private int position, total_cost = 0;
    private String name;
    private List<Model> model = new ArrayList<Model>();
    private EditText nametext,costtext;
    private AdapterListItems adapter;


    Map<String,Integer> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        NameDebtor = findViewById(R.id.name);
        list = findViewById(R.id.list);
        cost = findViewById(R.id.textcost);
        nametext = findViewById(R.id.setName);
        costtext = findViewById(R.id.SetCost);
        costtext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return submit(v,keyCode,event);
            }
        });
        nametext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return  submit(v,keyCode,event);
            }
        });
        Get_data();
        Set_data();

    }


    private void Get_data(){
        arguments = getIntent().getExtras();
        position = (int) arguments.get("position");
        name = (String) arguments.get("name");

    }
    private int sum(List<Model> model){
        int sum = 0;
        for (int i = 0; i < model.size(); i++) {
            sum += model.get(i).cost;
        }
        return sum;
    }

    private void Set_data() {

        NameDebtor.setText(name);

        try {
            Json json = new Json(this);
            model = json.importFromJSON(position);
            adapter = new AdapterListItems(this, R.layout.product_item, model,cost);
            list.setAdapter(adapter);


        } catch (Exception e) {
            Toast.makeText(this, "поломка", Toast.LENGTH_SHORT).show();
        }
        total_cost = sum(model);
        cost.setText(Integer.toString(total_cost));


    }

    private boolean submit(View v, int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_ENTER && !(nametext.getText().toString().isEmpty()) && !(costtext.getText()).toString().isEmpty()){
            Model model  = new Model(position,Integer.parseInt(costtext.getText().toString()),nametext.getText().toString());
            this.model.add(model);
            adapter.notifyDataSetChanged();
            try {
                Json json = new Json(this,model);
                json.exportToJSON();
            }catch (Exception e){
                Toast.makeText(this, "пизда", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            nametext.setText("");
            costtext.setText("");
            return true;
        }
        return false;
    }
}