package com.project.logicapp.ui;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.project.logicapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void handleButtonClick(View view){
        Intent intent = new Intent(MainActivity.this, GameBoardActivity.class);
        startActivity(intent);
    }


}