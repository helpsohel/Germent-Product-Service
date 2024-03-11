package com.example.germentproductservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONObject;

public class Profile extends Fragment {

    String viewProfile = MainActivity.serverURL+"/profile";
    private Handler handler = new Handler();
    private Runnable runnable;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile, container, false);


        TextView f_name,l_name,email, address;
        f_name = view.findViewById(R.id.first_name);
        l_name = view.findViewById(R.id.last_name);
        email = view.findViewById(R.id.email_id);
        address = view.findViewById(R.id.address);

        SharedPreferences preferences = MySharedPreferences.getInstance(container.getContext());

                SendRequest task = new SendRequest(viewProfile,container.getContext());
               JSONObject profileJSON = null;
                try {
                   profileJSON = new JSONObject();
                   profileJSON.put("customer_id",preferences.getInt("customer_id",0));
               }catch (Exception e){}

                task.execute(profileJSON.toString());
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (task.result !=""){

                            try {
                                JSONObject mr = new JSONObject(task.result);
                                if (mr.getBoolean("isOK")) {
                                    JSONObject resJSON = new JSONObject(String.valueOf(mr.get("result")));
                                    f_name.setText(resJSON.getString("first_name"));
                                    l_name.setText(resJSON.getString("last_name"));
                                    email.setText(resJSON.getString("email"));
                                    address.setText(resJSON.getString("address"));
                                }else {
                                    Toast.makeText(container.getContext(), "An Error !. Please log in again", Toast.LENGTH_SHORT).show();
                                    removeAccount(container.getContext());
                                }

                            }catch (Exception e){}

                            handler.removeCallbacks(this);
                        }else {
                            handler.postDelayed(this, 1000);
                        }
                    }
                };
                handler.postDelayed(runnable,1000);


        Button logOut = view.findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = MySharedPreferences.getInstance(container.getContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(container.getContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        return view;
    }
    private void removeAccount(Context container){
        SharedPreferences preferences = MySharedPreferences.getInstance(container);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Intent intent = new Intent(container,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
