package com.avinsharma.githubrepositorysearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avinsharma.githubrepositorysearch.model.GithubRepository;
import com.avinsharma.githubrepositorysearch.utilities.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements GithubRepositoryAdapter.ListItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private EditText mSearchQueryEditText;
    private Button mSearchButton;
    private ProgressBar mProgressBar;
    private GithubRepository[] mRepos;
    private RecyclerView mRepositoryRecyclerView;
    private GithubRepositoryAdapter mGithubRepositoryAdapter;
    private MainViewModel viewModel;

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
                searchGithub();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mGithubRepositoryAdapter = new GithubRepositoryAdapter(this);
        mRepositoryRecyclerView.setLayoutManager(layoutManager);
        mRepositoryRecyclerView.setAdapter(mGithubRepositoryAdapter);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        viewModel.getRepos().observe(this, new Observer<GithubRepository[]>() {
            @Override
            public void onChanged(GithubRepository[] githubRepositories) {
                mRepos = githubRepositories;
                mGithubRepositoryAdapter.setmAllRepositories(githubRepositories);
                mRepositoryRecyclerView.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void searchGithub() {

        String githubSearchQuery = mSearchQueryEditText.getText().toString();
        Context context = MainActivity.this;

        if(githubSearchQuery.equals("")){
            Toast.makeText(context, "Please enter a search term.", Toast.LENGTH_SHORT).show();
        }else{
            URL url = NetworkUtils.buildUrl(githubSearchQuery, PreferenceManager.getDefaultSharedPreferences(context).getString(getString(R.string.pref_sort_by_key), null));
//            new QueryGithubAPITask().execute(url);
            viewModel.searchGithub(url);
            // Hide Results
            mRepositoryRecyclerView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onListItemClick(int position) {
        Toast.makeText(this, mRepos[position].getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d("Key", key);
        if (key.equals(getString(R.string.pref_sort_by_key))){
            searchGithub();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
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
