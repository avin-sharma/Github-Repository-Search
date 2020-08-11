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

    /**
     * @return mRepo    A MutableLiveData object casted as
     *                  a LiveData object.
     */
    public LiveData<GithubRepository[]> getRepos(){
        return mRepos;
    }

    /**
     * Creates a new background thread and queries the Github API.
     * @param url
     */
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

    /**
     * A method which queries the Github API to fetch the results
     * for the term we searched. Does not run asynchronously and
     * needs to be called in a thread off the main UI thread.
     * Updates mRepo.
     *
     * @param url
     */
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
            mRepos.postValue(githubRepositories);
        }catch (IOException | JSONException e){
            e.printStackTrace();
        }
    }
}
