package com.matrixdeveloper.aivita.Discover;



import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.annotations.NotNull;
import com.matrixdeveloper.aivita.Main_Menu.MainMenuFragment;
import com.matrixdeveloper.aivita.Main_Menu.RelateToFragment_OnBack.OnBackPressListener;
import com.matrixdeveloper.aivita.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.matrixdeveloper.aivita.Profile.ProfileFragment;
import com.matrixdeveloper.aivita.R;
import com.matrixdeveloper.aivita.SimpleClasses.ApiRequest;
import com.matrixdeveloper.aivita.SimpleClasses.Fragment_Callback;
import com.matrixdeveloper.aivita.SimpleClasses.Variables;
import com.matrixdeveloper.aivita.SoundLists.Sounds_GetSet;
import com.matrixdeveloper.aivita.Videos.Followings;
import com.matrixdeveloper.aivita.Videos.Popular_Get_Set;
import com.matrixdeveloper.aivita.model.general.SearchData;
import com.matrixdeveloper.aivita.model.request.SearchRequest;
import com.matrixdeveloper.aivita.model.response.SearchResponse;
import com.matrixdeveloper.aivita.network.NetworkUtils;
import com.matrixdeveloper.aivita.Discover.adapters.listAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.matrixdeveloper.aivita.SimpleClasses.Variables.sharedPreferences;


public class SearchFragment extends RootFragment {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private TabLayout tabLayout;
    private String  type="@",TAG="SearchFragment";
    private ProgressBar progressBar;
    private TextView warning;
    private ArrayList<SearchData> searchData;
    private listAdapter adapter;
    private boolean isLoading = true;

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.search_view);
        tabLayout = view.findViewById(R.id.search_tab_layout);
        recyclerView = view.findViewById(R.id.search_recycler);
        progressBar = view.findViewById(R.id.progressBar);
        warning = view.findViewById(R.id.text_warning);

        initScrollListener();

        searchData = new ArrayList<>();
        init();
        progressBar.setVisibility(GONE);
        type ="@";
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch (tab.getPosition()) {
                    case 0:
                        type ="@";
                        break;
                    case 1:
                        type = "#";
                        break;
                }
                searchData.clear();
                if (!searchView.getQuery().toString().isEmpty())
                    search(searchView.getQuery().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                init();
                if (!searchView.getQuery().toString().isEmpty())
                    search(searchView.getQuery().toString());
                else {
                    SearchView.SearchAutoComplete edittext =
                            searchView.findViewById(androidx.appcompat.R.id.search_src_text);
                    //edittext.setError("Can't be empty");
                }
                return true;
            }
        });

        return view;
    }

    private void search(String query) {
        isLoading = true;
        Log.e(TAG,"Query "+ query);
        SearchRequest req = new SearchRequest( query, "0", "15", type);
        Log.e(TAG,"Request "+ req.toString());
        Call<SearchResponse> call = NetworkUtils.getClient().getSearch(req);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("success"))
                        setRecycler(response.body());
                    else {
                        showWarning();
                        warning.setText(response.body().getMsg());
                    }
                } else {
                    Log.e(TAG,"Response Body"+ "null");
                    showWarning();
                    warning.setText("Not Found");
                }
                Log.e(TAG,"Response "+ response.toString());
                isLoading = false;
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
              //  Toast.makeText(getContext(), "16 "+ t.getMessage(), Toast.LENGTH_SHORT).show();
                if (t.getLocalizedMessage() != null)
                    Log.e(TAG,"Search_Error"+ t.getLocalizedMessage());
                isLoading = false;
            }
        });
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == searchData.size() - 1) {
                        //bottom of list!
                        update(searchView.getQuery().toString());
                        isLoading = true;
                    }
                }
            }
        });
    }


    private void update(String query) {

        int start = searchData.size();
      //  Toast.makeText(getContext(), String.valueOf(searchData.size()+" + "+query), Toast.LENGTH_SHORT).show();

        SearchRequest req = new SearchRequest( query, String.valueOf(start), String.valueOf(start + 15), type);
        Call<SearchResponse> call = NetworkUtils.getClient().getSearch(req);
        call.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(@NotNull Call<SearchResponse> call, @NotNull Response<SearchResponse> response) {
                if (response.body() != null) {
                    if (response.body().getStatus().equals("success")) {
                        searchData.addAll(searchData.size() - 1, response.body().getData());
                        if (adapter != null)
                            adapter.notifyDataSetChanged();
                        else Log.e(TAG,"onUpdating "+ "adapter null");
                    } else {
                        showWarning();
                        warning.setText(response.body().getMsg());
                    }
                } else {
                    Log.e(TAG,"Response Body "+ "null");
                    showWarning();
                    warning.setText("Not Found");
                }
                Log.e(TAG,"Response "+ response.toString());
                isLoading = false;
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Toast.makeText(getContext(), "13 "+t.getMessage(), Toast.LENGTH_SHORT).show();
                if (t.getLocalizedMessage() != null)
                    Log.e(TAG,"Search_Error"+ t.getLocalizedMessage());
                isLoading = false;
            }

        });
    }

    private void setRecycler(SearchResponse response) {
        searchData = response.getData();
        showRecycler();
        adapter = new listAdapter(getContext(), searchData, new listAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion, SearchData item) {
                if (type.equals("@") && !item.getFb_id().equals("")){
                    OpenProfile(item.getFb_id());
                }
                else {
                  //  Toast.makeText(getContext(), item.getTag_name(), Toast.LENGTH_SHORT).show();
                    String tag =item.getTag_name();
                    tag = tag.substring(0, 0) + tag.substring(1);
                    getTaggedVideo(tag);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    private void getTaggedVideo(String tag_name) {

            DiscoverVideo_F following_f = new DiscoverVideo_F();

            FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
            Bundle args = new Bundle();
            args.putString("tag_name", tag_name);
            following_f.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.MainMenuFragment, following_f);
            transaction.commit();


    }

    private void OpenProfile(String id) {
        if(Variables.sharedPreferences.getString(Variables.u_id,"0").equals(id)){
            TabLayout.Tab profile= MainMenuFragment.tabLayout.getTabAt(4);
            assert profile != null;
            profile.select();

        }else {
            ProfileFragment profile_f = new ProfileFragment();
            FragmentTransaction transaction = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left, R.anim.in_from_left, R.anim.out_to_right);

            Bundle args = new Bundle();
            args.putString("user_id", id);
            args.putString("user_name","");
            args.putString("user_pic","");
            profile_f.setArguments(args);
            transaction.addToBackStack(null);
            transaction.replace(R.id.MainMenuFragment, profile_f).commit();
        }

    }

//    public boolean onBackPressed() {
//        // currently visible tab Fragment
//        final SearchFragment fragment = (SearchFragment) getFragmentManager().findFragmentByTag("tag");
//
//       // OnBackPressListener currentFragment = (OnBackPressListener) adapter.getRegisteredFragment(pager.getCurrentItem());
//
//        if (fragment != null) {
//            // lets see if the currentFragment or any of its childFragment can handle onBackPressed
//            return fragment.onBackPressed();
//        }
//
//        // this Fragment couldn't handle the onBackPressed call
//        return false;
//    }

    private void init() {
        warning.setVisibility(GONE);
        progressBar.setVisibility(VISIBLE);
        recyclerView.setVisibility(GONE);
    }

    private void showWarning() {
        warning.setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        recyclerView.setVisibility(GONE);
    }

    private void showRecycler() {
        warning.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        recyclerView.setVisibility(VISIBLE);
    }
}
