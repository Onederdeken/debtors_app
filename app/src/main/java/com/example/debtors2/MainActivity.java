package com.example.debtors2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.debtors2.adapters.Adapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button button_add;
    private ImageButton button_delete_buck;
    private List<String> names = new ArrayList<String>();
    private Adapter<String> adapter;
    private FragmentTransaction ft;
    private ListView list_items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        button_add = findViewById(R.id.button_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Add_Activity.class);
                startActivity(intent);
            }
        });
        list_items = findViewById(R.id.test);
        download();
    }

    public void download(){
        try {
            FileInputStream file = openFileInput("names_debtors.txt");
            InputStreamReader reader = new InputStreamReader(file);
            BufferedReader bR = new BufferedReader(reader);
            String lines = "";
            while((lines= bR.readLine())!=null){
                names.add(lines);
            }

            ListView list = findViewById(R.id.test);
            ft = getSupportFragmentManager().beginTransaction();
            adapter = new Adapter<String>(this,  R.layout.model_for_list_main, R.id.text_for_list, names,ft);
            list.setAdapter(adapter);
            file.close();
        }catch (IOException e){
            fragment_not_debtors ndb = new fragment_not_debtors();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_main,ndb);
            ft.addToBackStack(null);
            ft.commit();
        }

    }
}