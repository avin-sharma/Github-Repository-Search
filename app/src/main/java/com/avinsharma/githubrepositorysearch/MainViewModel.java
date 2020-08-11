package com.avinsharma.githubrepositorysearch;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.avinsharma.githubrepositorysearch.model.GithubRepository;
import com.avinsharma.githubrepositorysearch.utilities.JSONUtils;
import com.avinsharma.githubrepositorysearch.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainViewModel extends AndroidViewModel {

    private Repository repository = new Repository();
    private LiveData<GithubRepository[]> mRepos = repository.getRepos();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<GithubRepository[]> getRepos() {
        return mRepos;
    }

    public void searchGithub(URL url) {
        repository.searchGithub(url);
    }
}
