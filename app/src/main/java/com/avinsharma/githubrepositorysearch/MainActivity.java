package com.avinsharma.githubrepositorysearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avinsharma.githubrepositorysearch.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText mSearchQueryEditText;
    private Button mSearchButton;
    private TextView mSearchResultTextView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchQueryEditText = findViewById(R.id.et_search_bar);
        mSearchButton = findViewById(R.id.button_search);
        mSearchResultTextView = findViewById(R.id.tv_search_result);
        mProgressBar = findViewById(R.id.pb_search);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String githubSearchQuery = mSearchQueryEditText.getText().toString();
                Log.d("SEARCH", githubSearchQuery);
                if(githubSearchQuery.equals("")){
                    Context context = v.getContext();
                    Toast.makeText(context, "Please enter a search term.", Toast.LENGTH_SHORT).show();
                }else{
                    URL url = NetworkUtils.buildUrl(githubSearchQuery);
                    new QueryGithubAPITask().execute(url);
                }
            }
        });
    }

    private class QueryGithubAPITask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Hide Results
            mSearchResultTextView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(URL... urls) {
            try {
                return NetworkUtils.getResponseFromHttpUrl(urls[0]);
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            mSearchResultTextView.setText(s);
            mSearchResultTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
