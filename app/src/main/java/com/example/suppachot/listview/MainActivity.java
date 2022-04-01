package com.example.suppachot.listview;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private static final String TAG = "my_app";
    public static final String REQUEST_TAG = "myrequest";
    private RequestQueue mQueue;
    ProgressDialog pDialog;
    private List<DataModel> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        datas.add(new DataModel(1, "แผงวงจรควบคุมรีโมท", "แผงวงจรแบบไร้สายระยะไกล", "pic6.jpg"));
//        datas.add(new DataModel(2, "เครื่องกรองน้ำดื่ม", "เครื่องกรองน้ำดื่ม 50 L/hr", "pic6.jpg"));
//        datas.add(new DataModel(3, "เครื่องกรองน้ำ RO", "เครื่องกรองน้ำดื่มแบบ RO 3.5 L/hr", "pic6.jpg"));
//        datas.add(new DataModel(4, "เครื่องสำรองไฟ 500W", "อุปกรณ์สำรองไฟขนาด 500 W", "pic6.jpg"));

//        String url = "http://web.com/android/json/browse.php";
        String url = "http://itpart.com/android/json/test5records.php";

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading..");
        pDialog.show();

        JsonArrayRequest jsRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Gson gson = new Gson();

                        JSONObject jsObj;   // = null;
                        for (int i=0; i < response.length(); i++ ) {
                            try {
                                jsObj = response.getJSONObject(i);
                                String title = jsObj.getString("title");
                                Log.d(TAG, title);

                                DataModel dataitem = gson.fromJson(String.valueOf(jsObj), DataModel.class);
                                datas.add(dataitem);
                                Log.d(TAG,"gson "+ dataitem.getTitle());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (datas.size() > 0){
                            displayListview();
                        }

                        pDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,error.toString());
                        Toast.makeText(getBaseContext(),error.toString(),Toast.LENGTH_SHORT).show();
                        pDialog.hide();
                    }
                });  // Request

        mQueue = Volley.newRequestQueue(this);
        jsRequest.setTag(REQUEST_TAG);
        mQueue.add(jsRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null){
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    public void displayListview(){
        MyAdapter adapter = new MyAdapter(this,datas);
        ListView lv = findViewById(R.id.listView);
        lv.setOnItemClickListener(this);
        lv.setAdapter(adapter);
    }

    // for json listview
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, String.valueOf(i));
        Intent itn = new Intent(this, DetailpageActivity.class);
        itn.putExtra("recID", i);
        startActivity(itn);
    }

}
