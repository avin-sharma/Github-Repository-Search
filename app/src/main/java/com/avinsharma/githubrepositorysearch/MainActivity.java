package com.avinsharma.githubrepositorysearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchQueryEditText;
    private Button mSearchButton;
    private TextView mSearchResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchQueryEditText = findViewById(R.id.et_search_bar);
        mSearchButton = findViewById(R.id.button_search);
        mSearchResultTextView = findViewById(R.id.tv_search_result);
    }
}
