package tpu.ru.filecloudclient;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import tpu.ru.filecloudclient.common.SimpleRecyclerAdapter;

public class PageFragment extends Fragment {
    private OnListItemClickListener mItemClickListener;


    public static PageFragment newInstance(){
        PageFragment frg = new PageFragment();

        return frg;

    }

    public PageFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.main_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        List<String> list = new ArrayList<>();
        for(int i =0; i < 5;i++)
            list.add("Item " + i);

        SimpleRecyclerAdapter adapter = new SimpleRecyclerAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(mItemClickListener);


        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mItemClickListener = (OnListItemClickListener)context;
        }catch (ClassCastException ex){
            throw new ClassCastException(context.toString() + " must implement OnListItemClickListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mItemClickListener = null;
    }


    public interface OnListItemClickListener{
        void onListItemClick(String title);
    }
}
