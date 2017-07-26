package com.project.nat.advice_nation.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.nat.advice_nation.Adapter.DashboardAdapter;
import com.project.nat.advice_nation.Adapter.NkoinsSubcatAdapter;
import com.project.nat.advice_nation.Ankoins.Activity_AnkoinsTranjection;
import com.project.nat.advice_nation.Ankoins.Activity_TransferAnkoins;
import com.project.nat.advice_nation.Base.SubcategoryActivity;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.RecylerViewClick.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;

import static android.R.attr.data;


public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private Context context;

    public HomeFragment() {

    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dashboard, container, false);
        Initi(view);
        return view;
    }


    private void Initi(View view) {

        String[]data=getResources().getStringArray(R.array.dashboard_title);
        int[]color=getResources().getIntArray(R.array.dashbord_icon_color);
        recyclerView = (RecyclerView)view.findViewById(R.id.subrecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DashboardAdapter adapter = new DashboardAdapter(context, new ArrayList<>(Arrays.asList(data)),color);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position)
                    {
                        SubcategoryActivity.startScreen(context);

                        // do whatever
                    }

                    @Override public void onLongItemClick(View view, int position)
                    {
                        // do whatever
                    }
                })
        );

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
