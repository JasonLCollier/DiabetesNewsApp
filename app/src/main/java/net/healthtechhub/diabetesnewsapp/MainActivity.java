package net.healthtechhub.diabetesnewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Post>> {

    private static final String LOG_TAG = MainActivity.class.getName();

    /** URL for post data from the dataset */
    private static final String USGS_REQUEST_URL =
            "https://content.guardianapis.com/search?from-date=2015-01-01&q=diabetes&api-key=fc0bf16e-3603-4cc6-a119-30b7b9635d72";

    /**
     * Constant value for the post loader ID.
     */
    private static final int POST_LOADER_ID = 1;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /** RecyclerView that displays posts */
    private RecyclerView mPostRecyclerView;

    /** Adapter for the list of posts */
    private PostAdapter mPostAdapter;

    /** ArrayList of posts */
    private List<Post> mPosts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize references to views
        mPostRecyclerView = this.findViewById(R.id.post_recycler_view);
        mEmptyStateTextView = findViewById(R.id.empty_view);

        // Initialise the post adapter
        mPostAdapter = new PostAdapter(mPosts);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mPostRecyclerView.setLayoutManager(mLayoutManager);
        mPostRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // set the adapter
        mPostRecyclerView.setAdapter(mPostAdapter);

        // row click listener
        mPostRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mPostRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Post post = mPosts.get(position);
                Intent postViewIntent = new Intent(MainActivity.this, WebViewActivity.class);
                postViewIntent.putExtra("postUrl", post.getUrl());
                startActivity(postViewIntent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));

        /// Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(POST_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public Loader<List<Post>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new PostLoader(this, USGS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Post>> loader, List<Post> posts) {
        mPosts = posts;

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No posts found."
        mEmptyStateTextView.setText(R.string.no_posts);

        // If there is a valid list of {@link Post}s, then add them to the adapter's
        // data set. This will trigger the RecyclerView to update.
        if (mPosts != null && !mPosts.isEmpty()) {
            mPostRecyclerView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setVisibility(View.GONE);

            //mPostAdapter.notifyDataSetChanged();
            mPostAdapter = new PostAdapter(mPosts);
            mPostRecyclerView.setAdapter(mPostAdapter);
        }
        else {
            mPostRecyclerView.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Post>> loader) {
        // Loader reset, so we can clear out our existing data.
        mPosts.clear();
        mPostAdapter.notifyDataSetChanged();
    }

}
