package com.example.demo_fcm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class ProfileAct extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private RecyclerView rv_user;
    public static final String NODE_USER="users";
    private List<UserModel> list;
     ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        progressBar=findViewById(R.id.progresbar);
        mAuth=FirebaseAuth.getInstance();
        loadUser();
        getToken();
        FirebaseMessaging.getInstance().subscribeToTopic(NODE_USER);
    }

    private void loadUser() {
        progressBar.setVisibility(View.VISIBLE);
        rv_user=findViewById(R.id.rv_user);
        rv_user.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        DatabaseReference dbUsers= FirebaseDatabase.getInstance().getReference("users");
        dbUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (dataSnapshot.exists()){
                    for (DataSnapshot dsUser: dataSnapshot.getChildren()){
                        UserModel userModel=dsUser.getValue(UserModel.class);
                        list.add(userModel);
                    }
                    UserAdapter userAdapter=new UserAdapter(ProfileAct.this,list);
                    rv_user.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getToken(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()){
                            String token=task.getResult().getToken();
                            saveToken(token);
                        }
                        else {
                            Toast.makeText(ProfileAct.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() == null){
            Intent intent=new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void saveToken(String token) {
        String email=mAuth.getCurrentUser().getEmail();
        UserModel userModel=new UserModel(email,token);
        DatabaseReference dbUsers= FirebaseDatabase.getInstance().getReference(NODE_USER);
        dbUsers.child(mAuth.getCurrentUser().getUid().toString())
                .setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ProfileAct.this,"token saved",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
