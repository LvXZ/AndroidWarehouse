package com.lvxz.app.warehouse;

import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.gson.Gson;
import com.lvxz.app.warehouse.dto.ResponseDTO;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";



    private EditText accountText;
    private EditText passwordText;
    private EditText inputText;
    private EditText outputText;
    private EditText transferText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountText = findViewById(R.id.text_account);
        passwordText = findViewById(R.id.text_password);
        inputText = findViewById(R.id.text_input);
        outputText = findViewById(R.id.text_output);
        transferText = findViewById(R.id.text_transfer);

    }

    //入库
    public void inputFromID(View view){

        String account = accountText.getText().toString();
        String password = passwordText.getText().toString();
        String inputOrder = inputText.getText().toString();


        String URLstring = "input";
        String jsonStr = "{\"employeeId\":\""+ account +"\",\"password\":\""+ password +"\",\"formID\":\""+ inputOrder +"\"}";//json数据
        postDataAsync(URLstring, jsonStr);
    }


    //出库
    public void outputFromID(View view){

        String account = accountText.getText().toString();
        String password = passwordText.getText().toString();
        String outputOrder = outputText.getText().toString();

        String URLstring = "output";
        String jsonStr = "{\"employeeId\":\""+ account +"\",\"password\":\""+ password +"\",\"formID\":\""+ outputOrder +"\"}";//json数据
        postDataAsync(URLstring, jsonStr);
    }

    //转移
    public void transferFromID(View view){

        String account = accountText.getText().toString();
        String password = passwordText.getText().toString();
        String transferOrder = transferText.getText().toString();

        String URLstring = "transfer";
        String jsonStr = "{\"employeeId\":\""+ account +"\",\"password\":\""+ password +"\",\"formID\":\""+ transferOrder +"\"}";//json数据
        postDataAsync(URLstring, jsonStr);
    }



    //post协议
    private void postDataAsync(String URLstring, String jsonStr) {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。

        System.out.println("----------------------");
        MediaType mediaTypeJSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
        //String jsonStr = "{\"employeeId\":\"150804213\",\"password\":\"1234\",\"formID\":\"in1234\"}";//json数据.
        RequestBody body = RequestBody.create(mediaTypeJSON, jsonStr);

        Request request = new Request.Builder()//创建Request 对象。
                .url("http://192.168.2.108:9000/dispatch/" + URLstring)
                .post(body)//传递请求体
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("--------------失败了-----");


            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("--------------00000000--------");
                if(response.isSuccessful()){//回调的方法执行在子线程。
                    //Toast.makeText(MainActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG,"获取数据成功了");
                    Log.d(TAG,"response.code()=="+response.code());

                    String responseData = response.body().string();
                    Log.d(TAG,"response.body().string()=="+responseData);

                    Gson gson = new Gson();
                    ResponseDTO responseDTO = gson.fromJson(responseData,ResponseDTO.class);

                    Looper.prepare();
                    Toast.makeText(MainActivity.this, responseDTO.getMsg(), Toast.LENGTH_SHORT).show();
                    Looper.loop();

                }
            }
        });
    }



}
