package com.avinsharma.githubrepositorysearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avinsharma.githubrepositorysearch.model.Repository;
import com.avinsharma.githubrepositorysearch.utilities.JSONUtils;
import com.avinsharma.githubrepositorysearch.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements RepositoryAdapter.ListItemClickListener{

    private EditText mSearchQueryEditText;
    private Button mSearchButton;
    private ProgressBar mProgressBar;
    private Repository[] mRepos;
    private RecyclerView mRepositoryRecyclerView;
    private RepositoryAdapter mRepositoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchQueryEditText = findViewById(R.id.et_search_bar);
        mSearchButton = findViewById(R.id.button_search);
        mProgressBar = findViewById(R.id.pb_search);
        mRepositoryRecyclerView = findViewById(R.id.rv_repo_list);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String githubSearchQuery = mSearchQueryEditText.getText().toString();
                if(githubSearchQuery.equals("")){
                    Context context = v.getContext();
                    Toast.makeText(context, "Please enter a search term.", Toast.LENGTH_SHORT).show();
                }else{
                    URL url = NetworkUtils.buildUrl(githubSearchQuery);
                    new QueryGithubAPITask().execute(url);
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mRepositoryAdapter = new RepositoryAdapter(this);
        mRepositoryRecyclerView.setLayoutManager(layoutManager);
        mRepositoryRecyclerView.setAdapter(mRepositoryAdapter);
    }

    @Override
    public void onListItemClick(int position) {
        Toast.makeText(this, mRepos[position].getName(), Toast.LENGTH_SHORT).show();
    }

    private class QueryGithubAPITask extends AsyncTask<URL, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Hide Results
            mRepositoryRecyclerView.setVisibility(View.INVISIBLE);
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
            try {
                // Get the main JSON object from the String
                JSONObject json = new JSONObject(s);

                // Get all the repos
                JSONArray repos = json.getJSONArray("items");

                mRepos = new Repository[repos.length()];

                // loop every repo and add the name to the TextView
                for(int i = 0; i < repos.length(); i++){
                    JSONObject repo = repos.getJSONObject(i);
                    mRepos[i] = JSONUtils.parseRepositoryJSON(repo);
                }
                mRepositoryAdapter.setmAllRepositories(mRepos);
            }catch (JSONException e){
                e.printStackTrace();
            }

            mRepositoryRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu_option:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
