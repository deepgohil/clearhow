package com.example.quietly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

////////////////////codeingerpath
//        Notification-fcm/app/src/main/java/com/codeinger/notification_fcm/ui/activity
public class MainActivity extends AppCompatActivity {
   BottomNavigationView navigationView;
   FrameLayout frame;
//   LinearLayout topBarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Intent intent=new Intent(MainActivity.this,account.class);
            startActivity(intent);
        }

        frame=findViewById(R.id.frame);
//        topBarLayout=findViewById(R.id.topBarLayout);
        navigationView = findViewById(R.id.navigation);

        navigationView.setOnNavigationItemSelectedListener(navListner);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new shopfregment()).commit();



            }
            private BottomNavigationView.OnNavigationItemSelectedListener navListner=new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedfregment=null;
                    switch (item.getItemId()) {
                        case R.id.shop:
//                            topBarLayout.setVisibility(View.VISIBLE);
                            selectedfregment=new shopfregment();
                            break;
                        case R.id.notes:
                            selectedfregment=new notesfregment();
//                            topBarLayout.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.notification:
                            selectedfregment=new notificationFragment();
//                            topBarLayout.setVisibility(View.INVISIBLE);
                            break;
                        case R.id.you:
                            selectedfregment=new userFragment();
//                            topBarLayout.setVisibility(View.INVISIBLE);
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,selectedfregment).commit();
                    return true;
                }
            };

}