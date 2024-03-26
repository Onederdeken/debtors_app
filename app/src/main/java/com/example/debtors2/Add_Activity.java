package com.example.debtors2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.io.IOException;

public class Add_Activity extends AppCompatActivity {
    private EditText debtor_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.add_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        debtor_name = findViewById(R.id.debtor_name);
    }
    public void saved(View view){
        try{
            FileOutputStream fileout = openFileOutput("names_debtors.txt",MODE_APPEND);
            fileout.write((debtor_name.getText().toString()+"\n").getBytes());
            fileout.close();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

        }catch (IOException e){


        }
    }

}