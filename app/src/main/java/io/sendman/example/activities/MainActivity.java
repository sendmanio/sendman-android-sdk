package io.sendman.example.activities;

import androidx.appcompat.app.AppCompatActivity;
import io.sendman.example.R;
import io.sendman.sendman.SendMan;
import io.sendman.sendman.SendManConfig;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SendManConfig config = new SendManConfig("81b2b9a9213a2cd0d664ad6673d84b5156121166", "2728a0301adda50dce376e6eedc8de5c3a8af82a");
        config.setServerUrl("http://192.168.1.100:4200");
        SendMan.setAppConfig(config);
        SendMan.setUserId("1234");

        HashMap<String, String> userProperties = new HashMap<>();
        userProperties.put("email", "email@email.com");
        userProperties.put("Native App", "YES");
        SendMan.setUserProperties(userProperties);
        setContentView(R.layout.activity_main);

        findViewById(R.id.openNotificationsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.customProperty1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMan.setUserProperties(Collections.singletonMap("Custom Property", "Value 1"));
            }
        });

        findViewById(R.id.customProperty2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMan.setUserProperties(Collections.singletonMap("Custom Property", "Value 2"));
            }
        });


    }
}
