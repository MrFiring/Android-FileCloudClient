package tpu.ru.filecloudclient.tasks;

import android.os.AsyncTask;

import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.resources.files.ReadRemoteFolderOperation;
import com.owncloud.android.lib.resources.files.RemoteFile;

import java.util.ArrayList;

public class GetFilesListTask extends AsyncTask<Void, Void, ArrayList<RemoteFile>> {
    private ReadRemoteFolderOperation mOper = null;
    private OwnCloudClient mClient = null;
    private ArrayList<RemoteFile> mData = null;

    public GetFilesListTask(ReadRemoteFolderOperation oper, OwnCloudClient client){
        this.mOper = oper;
        this.mClient = client;
    }
    @Override
    protected ArrayList<RemoteFile> doInBackground(Void... voids) {
        RemoteOperationResult result = mOper.execute(mClient);
        ArrayList<RemoteFile> files = new ArrayList<>();
        for(Object obj : result.getData()){
            files.add((RemoteFile)obj);
        }


        return files;

    }

    @Override
    protected void onPostExecute(ArrayList<RemoteFile> result) {
        super.onPostExecute(result);

        mData = result;
    }
}
