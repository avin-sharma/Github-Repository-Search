package com.avinsharma.githubrepositorysearch.utilities;

import com.avinsharma.githubrepositorysearch.model.Repository;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {
    public static Repository parseRepositoryJSON(JSONObject repo){
        Repository newRepository = null;

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

            newRepository = new Repository(name, description, stargazersCount, language, updatedAt, license);

        }catch (JSONException e){
            e.printStackTrace();
        }

        return newRepository;
    }
}
