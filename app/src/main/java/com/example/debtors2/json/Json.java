package com.example.debtors2.json;
import android.content.Context;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.debtors2.Model.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Json {
    private  final String FILE_NAME = "test.json";
    private Context context;
    private Model dataList;
    private JSONObject object;

    public Json(Context context, Model dataList){
        this.context = context;
        this.dataList = dataList;
    }
    public Json(Context context){
        this.context = context;
    }
    public void exportToJSON(){
        try {
            JSONObject model = new JSONObject("{id:"+Integer.toString(dataList.id)+",name:"+dataList.name+",cost:"+Integer.toString(dataList.cost)+"}\n");
            object = new JSONObject();
            object.put("product",model);

        } catch (JSONException e) {
            Toast.makeText(context, "жопа", Toast.LENGTH_SHORT).show();
            throw new RuntimeException(e);
        }
        try{
            FileOutputStream file = context.openFileOutput(FILE_NAME,Context.MODE_APPEND);
            file.write((object.toString()+"\n").getBytes());

        } catch (Exception e) {
            Toast.makeText(context, "хуй", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public List<Model> importFromJSON(int position) {
        List<Model> model = new ArrayList<Model>();
        try{

            FileInputStream file= context.openFileInput(FILE_NAME);
            InputStreamReader reader = new InputStreamReader(file);
            BufferedReader bR = new BufferedReader(reader);

            String lines = "", result="";

            while((lines= bR.readLine())!=null){
               JSONObject object1 = new JSONObject(lines);
               object1 = object1.getJSONObject("product");
               if(object1.getInt("id") == position){
                   model.add(new Model(position,object1.getInt("cost"),object1.getString("name")));
               }
            }


        }
        catch (IOException ex){

        } catch (JSONException e) {

        }
        return model;

    }
    public List<Model> read_JSON_position(int position){
        List<Model> model = new ArrayList<Model>();
        try{

            FileInputStream file= context.openFileInput(FILE_NAME);
            InputStreamReader reader = new InputStreamReader(file);
            BufferedReader bR = new BufferedReader(reader);

            String lines = "", result="";

            while((lines= bR.readLine())!=null){
                JSONObject object1 = new JSONObject(lines);
                object1 = object1.getJSONObject("product");
                if(object1.getInt("id") != position){
                    model.add(new Model(object1.getInt("id") - 1,object1.getInt("cost"),object1.getString("name")));
                }
            }
            file.close();
        }
        catch (IOException ex){
            ex.printStackTrace();

        } catch (JSONException e) {
        }
        return model;
    }
    public List<Model> read_json(){
        List<Model> model = new ArrayList<Model>();
        try{

            FileInputStream file= context.openFileInput(FILE_NAME);
            InputStreamReader reader = new InputStreamReader(file);
            BufferedReader bR = new BufferedReader(reader);

            String lines = "", result="";

            while((lines= bR.readLine())!=null){
                JSONObject object1 = new JSONObject(lines);
                object1 = object1.getJSONObject("product");
                model.add(new Model(object1.getInt("id"),object1.getInt("cost"),object1.getString("name")));

            }
            return model;
        }
        catch (IOException ex){
            ex.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static class DataItems {
        private List<Model> users;

        List<Model> getUsers() {
            return users;
        }
        void setUsers(List<Model> users) {
            this.users = users;
        }
    }

}
