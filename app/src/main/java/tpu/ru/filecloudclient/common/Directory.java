package tpu.ru.filecloudclient.common;

import android.util.Log;

import com.owncloud.android.lib.resources.files.ReadRemoteFolderOperation;
import com.owncloud.android.lib.resources.files.RemoteFile;

import tpu.ru.filecloudclient.OwnCloudHolder;
import tpu.ru.filecloudclient.tasks.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Directory extends RemoteFile {
    private ArrayList<RemoteFile> mArray;
    private String mName;

    public Directory() {
    }

    public Directory(String remotePath){
        //TODO Remove static getInstance;
        GetFilesListTask task = new GetFilesListTask(new ReadRemoteFolderOperation(remotePath), OwnCloudHolder.getInstance());
        task.execute();
        try {
            mArray = task.get();
        }catch(ExecutionException ex){
            Log.e(getClass().getSimpleName(), "DIRECTORY CLASS EXCEPTION " + ex.toString());
        }
        catch(InterruptedException ex) {
            Log.e(getClass().getSimpleName(), "DIRECTORY CLASS EXCEPTION " + ex.toString());
        }
    }

    public Directory(ArrayList<RemoteFile> mArray, String mName) {
        this.mArray = mArray;
        this.mName = mName;
    }

    public ArrayList<RemoteFile> getArray() {
        return mArray;
    }

    public void setArray(ArrayList<RemoteFile> mArray) {
        this.mArray = mArray;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

}
