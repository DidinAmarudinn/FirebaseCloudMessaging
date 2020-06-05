package com.example.demo_fcm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SendNotification extends AppCompatActivity {
    private TextView tv_email_name;
    private EditText et_title,et_body;
    private Button btn_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        UserModel userModel= (UserModel) getIntent().getSerializableExtra("user");
        btn_send=findViewById(R.id.btn_send);
        et_body=findViewById(R.id.et_body);
        tv_email_name=findViewById(R.id.tv_nama_email);
        et_title=findViewById(R.id.et_title);
        tv_email_name.setText("sending to user" +userModel.email);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(userModel);
            }
        });

    }

    private void sendNotification(UserModel userModel) {
        String title=et_title.getText().toString();
        String body=et_body.getText().toString();
        if (title.isEmpty() || body.isEmpty()){
            et_title.setError("Title Requierd");
            et_body.setError("Title Requierd");
            et_title.requestFocus();
            et_body.requestFocus();
            return;
        }

        Retrofit retrofit=ApiService.getRetrofitServices();
        Endpoint endpoint=retrofit.create(Endpoint.class);
        Call<ResponseBody> call=endpoint.sendNotification(userModel.token,title,body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(SendNotification.this,response.body().string(),Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
