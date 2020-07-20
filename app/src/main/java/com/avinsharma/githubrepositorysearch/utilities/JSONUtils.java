package com.avinsharma.githubrepositorysearch.utilities;

import com.avinsharma.githubrepositorysearch.model.GithubRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
    public static GithubRepository parseRepositoryJSON(JSONObject repo){
        GithubRepository newGithubRepository = null;

        try {
            String name = repo.getString("name");
            String updatedAt = repo.getString("updated_at");
            Integer stargazersCount = repo.getInt("stargazers_count");

            // These could be null
            String description = null;
            if (!repo.isNull("description")){
                description = repo.getString("description");
            }

            String language = null;
            if (!repo.isNull("language")){
                language = repo.getString("language");
            }

            String license = null;
            if (!repo.isNull("license")){
                license = repo.getJSONObject("license").getString("spdx_id");
            }

            newGithubRepository = new GithubRepository(name, description, stargazersCount, language, updatedAt, license);

        }catch (JSONException e){
            e.printStackTrace();
        }

        return newGithubRepository;
    }

    public static GithubRepository[] convertJsonStringToGithubRepos(String jsonString){
        try {
            // Get the main JSON object from the String
            JSONObject json = new JSONObject(jsonString);

            // Get all the repos
            JSONArray repos = json.getJSONArray("items");

            GithubRepository[] githubRepositories = new GithubRepository[repos.length()];

            // loop every repo and add the name to the TextView
            for(int i = 0; i < repos.length(); i++){
                JSONObject repo = repos.getJSONObject(i);
                githubRepositories[i] = JSONUtils.parseRepositoryJSON(repo);
            }
            return githubRepositories;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }
}
