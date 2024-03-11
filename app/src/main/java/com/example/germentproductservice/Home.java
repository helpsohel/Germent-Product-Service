package com.example.germentproductservice;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Home extends AppCompatActivity {
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        ImageView product_service_b = findViewById(R.id.product_service_page);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new MyFragment());
        fragmentTransaction.commit();

        product_service_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_service_b.animate()
                        .scaleX(1.3f)
                        .scaleY(1.3f)
                        .setDuration(500)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(@NonNull Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(@NonNull Animator animation) {
                                product_service_b.animate()
                                        .scaleY(1f)
                                        .scaleX(1f)
                                        .start();
                            }

                            @Override
                            public void onAnimationCancel(@NonNull Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(@NonNull Animator animation) {

                            }
                        })
                        .start();
                LinearLayout fc = findViewById(R.id.fragment_container);
                fc.removeAllViews();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new MyFragment());
                fragmentTransaction.commit();
            }
        });

        ImageView profile = findViewById(R.id.profile);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile.animate()
                        .scaleX(1.3f)
                        .scaleY(1.3f)
                        .setDuration(500)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(@NonNull Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(@NonNull Animator animation) {
                                profile.animate()
                                        .scaleY(1f)
                                        .scaleX(1f)
                                        .start();
                            }

                            @Override
                            public void onAnimationCancel(@NonNull Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(@NonNull Animator animation) {

                            }
                        })
                        .start();
                LinearLayout fc = findViewById(R.id.fragment_container);
                fc.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Profile()).commit();
            }
        });
    }

}
