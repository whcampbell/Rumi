package com.example.rumi;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class dashboard extends Fragment {
    private ArrayList<DashConvo> convoList;
    private RecyclerView reView;
    private RecyclerAdapter.RecyclerClickListenter listener;


    public dashboard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        TextView houseTextView = view.findViewById(R.id.houseNumber);



        houseTextView.setText(MainActivity.getHouseName());

        reView = view.findViewById(R.id.Recycler);



        convoList = new ArrayList<>();
        convoList.add(new DashConvo("once upon a midnight dreary while I pondered weak and weary\n" +
                "over many a quaint and curious volume of forgotten lore\n" +
                "while I nodded nearly napping suddenly there came a tapping", "this is a comment"));
        convoList.add(new DashConvo("head2", "this is another"));
        convoList.add(new DashConvo("head3", "and this is a third"));
        convoList.add(new DashConvo("how bout we add another head that's on the longer end of things", "and then the comment won't be too long"));
        convoList.add(new DashConvo("head5", "commememembklt"));
        convoList.add(new DashConvo("heeed6", "the fitnessgram pacer test is a multistage aerobic fitness examination"));

        setAdapter();

        return view;
    }

    private void setAdapter() {
        setOnClickListener();
        RecyclerAdapter adapter = new RecyclerAdapter(convoList, listener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        reView.setLayoutManager(layoutManager);
        reView.setItemAnimator(new DefaultItemAnimator());
        reView.setAdapter(adapter);
    }

    private void setOnClickListener() {
        listener = new RecyclerAdapter.RecyclerClickListenter() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ConvoActivity.class);
                intent.putExtra("head", convoList.get(position).getConvoHead());
                startActivity(intent);
            }
        };
    }

    public void onClickPost(View view) {
        // TODO add new convo to database and top of recycler view
    }

}