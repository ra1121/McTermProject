
package com.app.guessme;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class MainActivity extends AppCompatActivity {
    public static String TYPE = "type";
    private List<Guess> guessArrayList;
    private ImageView imgGuessChallenge;
    private RelativeLayout layoutHint;
    private TextView txtHint,txtHintButton;
    private int index = 0;
    private int hintIndex = 0;
    private Guess guess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeStatusBarColor();
        findViewById();
        getGuessList();
    }

    private void findViewById() {
        imgGuessChallenge = findViewById(R.id.imgGuessChallenge);
        ImageView imgBack = findViewById(R.id.imgBack);
        txtHint = findViewById(R.id.txtHint);
        txtHintButton = findViewById(R.id.txtHintButton);
        RelativeLayout layoutNext = findViewById(R.id.layoutDone);
        layoutHint = findViewById(R.id.layoutHint);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        imgGuessChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guess != null) {
                    Glide.with(MainActivity.this).load(guess.getLogo())
                            .transition(withCrossFade())
                            .into(imgGuessChallenge);
                }

            }
        });

        layoutHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hintIndex == 0) {
                    txtHint.setText(getString(R.string.lbl_hint_new_line)+guess.getHint1());
                    txtHint.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.colorOranges));
                    hintIndex++;
                } else if (hintIndex == 1) {
                    txtHint.setText(getString(R.string.lbl_hint_new_line)+guess.getHint2());
                    txtHintButton.setText(R.string.lbl_answer);
                    txtHint.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.colorOranges));
                    hintIndex++;
                } else if (hintIndex == 2) {
                    txtHint.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.colorLightGreen));
                    txtHint.setText(getString(R.string.lbl_answer_new_line)+guess.getAnswer());
                }
            }
        });

        layoutNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    index++;
                    int size = guessArrayList.size() - 1;
                    if (size > index) {
                        setUiData();
                    } else {
                        Toast.makeText(MainActivity.this, R.string.msg_no_more_guesses, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("TAG", "onClick: " + e);
                }
            }
        });
    }


    private void getGuessList() {
        try {
            String jsonResponse = loadJSONFromAsset(getIntent().getStringExtra(TYPE));
            Guess[] optionListJsonResponse = parseJSON(jsonResponse);
            guessArrayList = new ArrayList(Arrays.asList(optionListJsonResponse));
            Collections.shuffle(guessArrayList);
            if (guessArrayList == null) {
                guessArrayList = new ArrayList<>();
            }

            setUiData();
        } catch (Exception e) {
            Log.e("TAG", "getQuestionList: " + e);
        }
    }

    private void setUiData() {
        txtHint.setText(R.string.lbl_tap_hint_button);
        txtHintButton.setText(R.string.lbl_hint);
        txtHint.setTextColor(ContextCompat.getColor(MainActivity.this,R.color.colorOranges));
        hintIndex = 0;
        guess = guessArrayList.get(index);
        Log.e("TAG", "setUiData: " + guess.logo);
        Glide.with(this).load(guess.getLogo())
                .transition(withCrossFade())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imgGuessChallenge);
    }

    public Guess[] parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(response, Guess[].class);

    }

    private String loadJSONFromAsset(String catName) {
        String json = null;
        try {
            InputStream is = getAssets().open(catName + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            int read = is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlack));
        }
    }
}
