package tpu.ru.filecloudclient.common;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

        mObjects.remove(0);

        mNamesList = new ArrayList<>();
        String path = "";
        for(RemoteFile file : mObjects){


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

        RemoteFile obj = mObjects.get(position);



        View view = inflater.inflate(mResource, parent, false);
        TextView fileNameView = (TextView)view.findViewById(R.id.lv_nameview);
        Spanned span = Html.fromHtml(obj.getRemotePath() + "<br>Modify: <i>" + DateUtils.formatElapsedTime(obj.getModifiedTimestamp())  +"</i><br>Size: " +
                obj.getSize()/1024 + "bytes",Html.FROM_HTML_MODE_COMPACT);
        fileNameView.setText(span);

        ImageView img = (ImageView)view.findViewById(R.id.lv_imageview);

        //TODO Replace deprecated method to not deprecated method)
        if(obj.getMimeType().equals("DIR"))
        img.setImageDrawable((view.getResources().getDrawable(android.R.drawable.ic_input_add)));


        return view;
    }
}
