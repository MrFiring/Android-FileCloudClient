package tpu.ru.filecloudclient;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.owncloud.android.lib.resources.files.RemoteFile;

import java.util.regex.Pattern;

import tpu.ru.filecloudclient.common.Directory;
import tpu.ru.filecloudclient.common.FilesArrayAdapter;
import tpu.ru.filecloudclient.common.PathController;

public class FilesFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private TextView textCurrentDir;

    private PathController pathController = new PathController();
    private Directory genDir = new Directory("/");


    public FilesFragment() {


    }


    public static FilesFragment newInstance() {
        FilesFragment fragment = new FilesFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_files, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textCurrentDir = (TextView)view.findViewById(R.id.files_fragment_tv_curdir);


        listView = (ListView)view.findViewById(R.id.fragment_files_lv);

        FilesArrayAdapter adpt = new FilesArrayAdapter(this.getContext(), R.layout.file_with_image_list,
                genDir.getArray());

        listView.setAdapter(adpt);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RemoteFile file = genDir.getArray().get(position);
                if(file.getMimeType().equals("DIR")){
                    String curPath = pathController.Forward(file.getRemotePath());

                    if(curPath.length() > 2 && textCurrentDir.getVisibility() == View.GONE){
                        textCurrentDir.setVisibility(View.VISIBLE);
                    }

                    if(textCurrentDir.getVisibility() == View.VISIBLE)
                        textCurrentDir.setText(file.getRemotePath());

                    Directory dir = new Directory(curPath);
                    FilesArrayAdapter adapter = new FilesArrayAdapter(view.getContext(), R.layout.file_with_image_list, dir.getArray());
                    listView.setAdapter(adapter);
                    genDir.setArray(dir.getArray());
                    genDir.setName(dir.getName());




                }
            }
        });



    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void turnBack(){
        String newPath = null;
        if((newPath = pathController.Backward()) != null){
            updateListView(newPath, this.getContext());
            if(textCurrentDir.getVisibility() == View.VISIBLE) {
                String dir = pathController.getCurrentDirName();
                if(dir != null)
                    textCurrentDir.setText(dir);
                else
                    textCurrentDir.setVisibility(View.GONE);
            }

        }



    }

    private void updateListView(String curPath, Context context){
                if(curPath == null) return;

                Directory dir = new Directory(curPath);
                FilesArrayAdapter adapter = new FilesArrayAdapter(context, R.layout.file_with_image_list, dir.getArray());
                listView.setAdapter(adapter);
                genDir.setArray(dir.getArray());
                genDir.setName(dir.getName());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
