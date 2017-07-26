package com.project.nat.advice_nation.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.nat.advice_nation.Adapter.NkoinsSubcatAdapter;
import com.project.nat.advice_nation.Ankoins.Activity_AnkoinsTranjection;
import com.project.nat.advice_nation.Ankoins.Activity_TransferAnkoins;
import com.project.nat.advice_nation.Base.SubcategoryActivity;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.RecylerViewClick.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Arrays;


public class AnKoins extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private Context context;
    private RecyclerView recyclerView;

    public AnKoins() {

    }
    public static AnKoins newInstance(String param1, String param2) {
        AnKoins fragment = new AnKoins();
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
        View view =inflater.inflate(R.layout.fragment_ankoins, container, false);
        Initi(view);
        return view;
    }



    private static String[] data = new String[]
            {
                    "Buy Ankoins", "Current Ankoins", "Transfer Ankoins", "Transaction History"
            };



    private void Initi(View view)
    {

        recyclerView = (RecyclerView)view.findViewById(R.id.subrecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        NkoinsSubcatAdapter adapter = new NkoinsSubcatAdapter(context, new ArrayList<>(Arrays.asList(data)));
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position)
                    {
                        switch (position)
                        {
                            case 0:
                                 return;
                            case 1:
                                return;
                            case 2:
                                Activity_TransferAnkoins.startScreen(context);
                                return;
                            case 3:
                                Activity_AnkoinsTranjection.startScreen(context);
                                return;

                        }
                        // do whatever
                    }

                    @Override public void onLongItemClick(View view, int position)
                    {
                        // do whatever
                    }
                })
        );

    }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, SubcategoryActivity.class));
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
