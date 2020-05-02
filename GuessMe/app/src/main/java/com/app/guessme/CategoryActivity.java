package com.app.guessme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        changeStatusBarColor();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bntGuessLogo:
                Intent i = new Intent(this, MainActivity.class);
                i.putExtra(MainActivity.TYPE, "cat1");
                startActivity(i);

                break;
            case R.id.bntGuessPlace:
                Intent j = new Intent(this, MainActivity.class);
                j.putExtra(MainActivity.TYPE, "cat2");
                startActivity(j);
                break;
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        }
    }
}
