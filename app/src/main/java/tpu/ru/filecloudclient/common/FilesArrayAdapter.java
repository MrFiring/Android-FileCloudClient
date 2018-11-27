package tpu.ru.filecloudclient.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.owncloud.android.lib.resources.files.RemoteFile;

import java.util.ArrayList;

import tpu.ru.filecloudclient.R;


public class FilesArrayAdapter extends ArrayAdapter<RemoteFile> {
    final int mResource;
    final ArrayList<RemoteFile> mObjects;
    final ArrayList<String> mNamesList;

    public FilesArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<RemoteFile> objects) {
        super(context, resource, objects);
        this.mResource = resource;
        this.mObjects = objects;

        mNamesList = new ArrayList<>();
        String path = "";
        for(RemoteFile file : mObjects){
            /*
            path = file.getRemotePath();
            if(path.endsWith("/")) path =  path.substring(0, path.length()-1);
            path = path.substring(path.lastIndexOf('/'));


            mNamesList.add(path);
        */

            path = file.getRemotePath();
            if(path.length() > 0 && !path.equals("/")) {
                if (path.endsWith("/")) path = path.substring(0, path.length() - 1);
                path = path.substring(path.lastIndexOf('/')+1);
                file.setRemotePath(path);
            }
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater  = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(mResource, parent, false);
        TextView fileNameView = (TextView)view.findViewById(R.id.lv_nameview);
        fileNameView.setText(mObjects.get(position).getRemotePath());

        return view;
    }
}
