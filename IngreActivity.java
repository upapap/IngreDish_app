package com.example.ingredish_app;


import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class IngreActivity extends AppCompatActivity {
;

    int[] buttonIds = { R.id.button4, R.id.button5, R.id.button6,
            R.id.button7, R.id.button8, R.id.button9,
            R.id.btn_vg1, R.id.btn_vg2, R.id.btn_vg3,
            R.id.btn_vg4, R.id.btn_vg5, R.id.btn_vg6,
            R.id.btn_vg7, R.id.btn_vg8, R.id.btn_vg9,
            R.id.btn_fi1, R.id.btn_fi2, R.id.btn_fi3,
            R.id.btn_fi4, R.id.btn_fi5, R.id.btn_fi6,
            R.id.btn_1, R.id.btn_2, R.id.btn_3,
            R.id.btn_4, R.id.btn_5, R.id.btn_6 };
    boolean[] isSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingre);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.ingre), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        isSelected = new boolean[buttonIds.length];

        for (int i = 0; i < buttonIds.length; i++) {
            final int index = i;
            Button button = (Button) findViewById(buttonIds[i]);

            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!isSelected[index]) {

                        v.setBackgroundColor(Color.parseColor("#2196F3"));
                        isSelected[index] = true;
                    } else {

                        v.setBackgroundColor(Color.BLUE);
                        isSelected[index] = false;
                    }
                }
            });

            button.setBackgroundColor(Color.BLUE);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }
}
