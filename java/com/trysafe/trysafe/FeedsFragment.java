package com.trysafe.trysafe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.trysafe.trysafe.Adapters.PostAdapter;
import com.trysafe.trysafe.Api.BloggerApi;
import com.trysafe.trysafe.BloggerModels.Item;
import com.trysafe.trysafe.BloggerModels.PostList;
import com.trysafe.trysafe.Utils.EqualSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedsFragment extends Fragment {


    public FeedsFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    PostAdapter adapter;
    List<Item> items = new ArrayList<>();
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    String token = "";

    ProgressBar loadMoreProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_feeds, container, false);

        loadMoreProgressBar = view.findViewById(R.id.loadProgressBar);

        recyclerView = (RecyclerView) view.findViewById(R.id.postList);
        manager = new LinearLayoutManager(getContext());
        adapter = new PostAdapter(getContext(),items);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new EqualSpacingItemDecoration(48));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems))
                {
                    isScrolling = false;
                    getData();

                }
            }
        });


        getData();

        return view;
    }

    private void getData()
    {



        String url = BloggerApi.url + "?key=" + BloggerApi.KEY;
        if(token != ""){
            url = url+ "&pageToken="+ token;
        }
        if(token == null){
            return;
        }

        loadMoreProgressBar.setVisibility(View.VISIBLE);
        Call<PostList> postList = BloggerApi.getPostService().getPostList(url);

        postList.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                if (response.isSuccessful()) {
                    PostList list = response.body();
                    token = list.getNextPageToken();
                    items.addAll(list.getItems());
                    adapter.notifyDataSetChanged();

                }
                loadMoreProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                Toast.makeText(getActivity(), "Error Occured", Toast.LENGTH_SHORT).show();
                loadMoreProgressBar.setVisibility(View.GONE);

            }
        });


    }

}
