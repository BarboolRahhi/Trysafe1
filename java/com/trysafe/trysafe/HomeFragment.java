package com.trysafe.trysafe;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.trysafe.trysafe.Adapters.HomePageAdapter;
import com.trysafe.trysafe.Api.BloggerApi;
import com.trysafe.trysafe.BloggerModels.Item;
import com.trysafe.trysafe.BloggerModels.PostList;
import com.trysafe.trysafe.Models.HomePageModel;
import com.trysafe.trysafe.Models.GridLayoutModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;

    ProgressBar homeProgressBar;

    List<HomePageModel> homePageModelsList;
    HomePageAdapter homePageAdapter;
    List<Item> javaList = new ArrayList<>();
    List<Item> projectList = new ArrayList<>();
    List<Item> arduinoList = new ArrayList<>();


    private FirebaseFirestore firebaseFirestore;

    List<GridLayoutModel> gridList = new ArrayList<>();

    CircleImageView profileImage;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        profileImage = view.findViewById(R.id.profile_image);
//        if (isConnected()){
//            Toast.makeText(getActivity(), "Device is Connected", Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(getActivity(), "Device is not COnnected with internet", Toast.LENGTH_SHORT).show();
//        }

        homeProgressBar = view.findViewById(R.id.homeProgressBar);

        firebaseFirestore = FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recycler_View);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        homePageModelsList = new ArrayList<>();
        homePageModelsList.add(new HomePageModel(1, "Categories",null, null, gridList));
        homePageModelsList.add(new HomePageModel(0, "Java Programming","Java", javaList, null));
        homePageModelsList.add(new HomePageModel(0, "Projects","project",projectList , null));
//        homePageModelsList.add(new HomePageModel(0, "Arduino", arduinoList, null));




        homePageAdapter = new HomePageAdapter(homePageModelsList);
        recyclerView.setAdapter(homePageAdapter);

        homeProgressBar.setVisibility(View.VISIBLE);

        getCategoryData();
        getProjectData();
        getJavaData();
 //       getArduinoData();


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),AccountActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){

                    String imageUrl = task.getResult().getString("image");

                    Glide.with(getActivity())
                            .load(imageUrl)
                            .placeholder(R.drawable.defaultavater)
                            .into(profileImage);

                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getCategoryData() {
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //  Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.d(TAG, "cate = " + document.get("categoryName").toString() + " , " + document.get("icon").toString());
                                gridList.add(new GridLayoutModel(document.get("categoryName").toString(),document.get("label").toString(), document.get("icon").toString()));
                            }

                            homePageAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    private boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworlInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworlInfo != null && activeNetworlInfo.isConnected();
    }

    public void getJavaData(){
        homeProgressBar.setVisibility(View.VISIBLE);
        Call<PostList> call = BloggerApi.getPostService().getPostList(BloggerApi.URL_LIST_JAVA);
        call.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                if (response.isSuccessful()) {
                    homeProgressBar.setVisibility(View.INVISIBLE);
                    PostList list = response.body();
                    javaList.addAll(list.getItems());
                    homePageAdapter.notifyDataSetChanged();

                }
                homeProgressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                Toast.makeText(getActivity(), "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getProjectData(){

        Call<PostList> call1 = BloggerApi.getPostService().getPostList(BloggerApi.URL_LIST_PROJECT);
        call1.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                if (response.isSuccessful()) {
                    homeProgressBar.setVisibility(View.INVISIBLE);
                    PostList list = response.body();
                    projectList.addAll(list.getItems());
                    homePageAdapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getArduinoData(){

        Call<PostList> call1 = BloggerApi.getPostService().getPostList(BloggerApi.URL_LIST_ARDUINO);
        call1.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {
                if (response.isSuccessful()) {
                    homeProgressBar.setVisibility(View.INVISIBLE);
                    PostList list = response.body();
                    arduinoList.addAll(list.getItems());
                    homePageAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFailure(Call<PostList> call, Throwable t) {
                Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
