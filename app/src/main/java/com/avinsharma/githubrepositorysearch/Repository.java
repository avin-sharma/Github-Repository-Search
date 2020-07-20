package com.avinsharma.githubrepositorysearch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.avinsharma.githubrepositorysearch.model.GithubRepository;
import com.avinsharma.githubrepositorysearch.utilities.JSONUtils;
import com.avinsharma.githubrepositorysearch.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class Repository {
    private MutableLiveData<GithubRepository[]> mRepos = new MutableLiveData<>();
    private Thread mThread;

    public LiveData<GithubRepository[]> getRepos(){
        return mRepos;
    }
    public void searchGithub(final URL url){
        Runnable fetchJsonRunnable = new Runnable() {
            @Override
            public void run() {
                queryGithubSearchApi(url);
            }
        };

        // Stop the thread if its initialized
        // If the thread is not working interrupt will do nothing
        // If its working it stops the previous work and starts the
        // new runnable
        if (mThread != null){
            mThread.interrupt();
        }
        mThread = new Thread(fetchJsonRunnable);
        mThread.start();
    }

    private void queryGithubSearchApi(URL url) {
        try {
            String response = NetworkUtils.getResponseFromHttpUrl(url);
            JSONObject json = new JSONObject(response);
            JSONArray repos = json.getJSONArray("items");
            GithubRepository[] githubRepositories = new GithubRepository[repos.length()];
            for(int i = 0; i < repos.length(); i++){
                JSONObject repo = repos.getJSONObject(i);
                githubRepositories[i] = JSONUtils.parseRepositoryJSON(repo);
            }

            // Set value of out LiveData(Mutable)
            mRepos.setValue(githubRepositories);
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }
}
