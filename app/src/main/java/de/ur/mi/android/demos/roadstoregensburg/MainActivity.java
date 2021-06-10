package de.ur.mi.android.demos.roadstoregensburg;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final double[] UNI_REGENSBURG_KUGEL_COORDINATES = {48.9982709d, 12.0946318d};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
    }


    private void initUI() {
        setContentView(R.layout.activity_main);
    }
}