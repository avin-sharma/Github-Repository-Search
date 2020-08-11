package com.avinsharma.githubrepositorysearch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avinsharma.githubrepositorysearch.model.GithubRepository;

public class GithubRepositoryAdapter extends RecyclerView.Adapter<GithubRepositoryAdapter.RepositoryAdapterViewHolder> {

    private final String TAG = getClass().getSimpleName();

    private GithubRepository[] mAllRepositories;
    final private ListItemClickListener mOnClickListener;

    interface ListItemClickListener{
        void onListItemClick(int position);
    }

    public GithubRepositoryAdapter(ListItemClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }

    public class RepositoryAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView mRepositoryName;
        public final TextView mRepositoryDescription;
        public final TextView mRepositoryStarCount;
        public final TextView mRepositoryLanguage;
        public final TextView mRepositoryUpdatedAt;
        public final TextView mRepositoryLicense;

        public RepositoryAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.mRepositoryName = itemView.findViewById(R.id.tv_repo_name);
            this.mRepositoryDescription = itemView.findViewById(R.id.tv_repo_description);
            this.mRepositoryStarCount = itemView.findViewById(R.id.tv_star_count);
            this.mRepositoryLanguage = itemView.findViewById(R.id.tv_language);
            this.mRepositoryUpdatedAt = itemView.findViewById(R.id.tv_updated_at);
            this.mRepositoryLicense = itemView.findViewById(R.id.tv_license);

            itemView.setOnClickListener(this);
        }

        private void bind(GithubRepository repo){
            String name = repo.getName();
            String description = repo.getDescription();
            Integer stargazersCount = repo.getStargazersCount();
            String language = repo.getLanguage();
            String updatedAt = repo.getUpdatedAt();
            String license = repo.getLicense();

            mRepositoryName.setText(name);
            mRepositoryStarCount.setText(String.valueOf(stargazersCount));
            mRepositoryUpdatedAt.setText(updatedAt);

            // Since the data in these can be null we check and bind data
            // or remove the view otherwise
            bindOrHideTextView(mRepositoryDescription, description);
            bindOrHideTextView(mRepositoryLanguage, language);
            bindOrHideTextView(mRepositoryLicense, license);

        }

        private void bindOrHideTextView(TextView textView, String data){
            if (data == null){
                textView.setVisibility(View.GONE);
            }
            else{
                textView.setText(data);
                textView.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
            Log.d(TAG, "Clicked!");
        }
    }

    @NonNull
    @Override
    public RepositoryAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.repository_list_item, parent, false);

        return new RepositoryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryAdapterViewHolder holder, int position) {
        holder.bind(mAllRepositories[position]);
    }

    @Override
    public int getItemCount() {
        if (mAllRepositories == null) return 0;
        return mAllRepositories.length;
    }

    public void setmAllRepositories(GithubRepository[] repositories){
        mAllRepositories = repositories;
        notifyDataSetChanged();
    }
}
