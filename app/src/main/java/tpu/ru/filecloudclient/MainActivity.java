package tpu.ru.filecloudclient;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.owncloud.android.lib.common.OwnCloudClient;
import com.owncloud.android.lib.common.accounts.AccountUtils;
import com.owncloud.android.lib.common.operations.RemoteOperationResult;
import com.owncloud.android.lib.resources.files.ReadRemoteFolderOperation;
import com.owncloud.android.lib.resources.files.RemoteFile;
import com.owncloud.android.lib.resources.shares.GetRemoteShareOperation;
import com.owncloud.android.lib.resources.users.GetRemoteUserInfoOperation;
import com.owncloud.android.lib.resources.users.GetRemoteUserQuotaOperation;

import java.lang.reflect.Array;
import java.util.ArrayList;

import tpu.ru.filecloudclient.common.Directory;
import tpu.ru.filecloudclient.common.FilesArrayAdapter;
import tpu.ru.filecloudclient.common.PathController;


public class MainActivity extends AppCompatActivity {

    NavigationView navView = null;
    DrawerLayout mDrawer = null;
    ListView mFileListView = null;
    OwnCloudClient mClient;
    final PathController pathController = new PathController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        navView = (NavigationView)findViewById(R.id.nav_view);
        mFileListView = (ListView)findViewById(R.id.listFileView);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);

                mDrawer.closeDrawers();
                menuItem.setChecked(false);

                return true;
            }
        });

        mClient = OwnCloudHolder.getInstance();


        final GetRemoteUserInfoOperation userOperation = new GetRemoteUserInfoOperation();

        GetInfoTask task = new GetInfoTask(userOperation);
        task.execute(mClient);


        //TODO Move it to another thread;
        final Directory genDir = new Directory("/");
        FilesArrayAdapter adpt = new FilesArrayAdapter(this, R.layout.file_with_image_list, genDir.getArray());

        mFileListView.setAdapter(adpt);


        mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RemoteFile file = genDir.getArray().get(position);
                if(file.getMimeType().equals("DIR")){
                        String curPath = pathController.Forward(file.getRemotePath());

                        Directory dir = new Directory(curPath);
                        FilesArrayAdapter adapter = new FilesArrayAdapter(view.getContext(), R.layout.file_with_image_list, dir.getArray());
                        mFileListView.setAdapter(adapter);
                        genDir.setArray(dir.getArray());
                        genDir.setName(dir.getName());


                }
            }
        });

    }

    @Override
    public void onBackPressed(){
        updateListView(pathController.Backward(), this);

    }

    private void updateListView(String curPath, Context context){
        Directory dir = new Directory(curPath);
        FilesArrayAdapter adapter = new FilesArrayAdapter(context, R.layout.file_with_image_list, dir.getArray());
        mFileListView.setAdapter(adapter);
    }

    ///Gets user information from the server and add its to the textViews
    class GetInfoTask  extends AsyncTask<OwnCloudClient, Void, GetRemoteUserInfoOperation.UserInfo>{
        private GetRemoteUserInfoOperation oper = null;

        public GetInfoTask(GetRemoteUserInfoOperation oper){
            this.oper = oper;

        }

        @Override
        protected GetRemoteUserInfoOperation.UserInfo doInBackground(OwnCloudClient... client) {
            RemoteOperationResult res = oper.execute(client[0]);

           GetRemoteUserInfoOperation.UserInfo info;
            ArrayList<GetRemoteUserInfoOperation.UserInfo> infos = (ArrayList)res.getData();

            GetRemoteUserInfoOperation.UserInfo inf = infos.get(0);
           /*
            if( infos.size() > 0)
            info = (GetRemoteUserInfoOperation.UserInfo) infos.get(0);
           else
               info = null;
           */
           return inf;

        }

        @Override
        protected void onPostExecute(GetRemoteUserInfoOperation.UserInfo s) {

            super.onPostExecute(s);

            if(s == null){
                Toast.makeText(getApplicationContext(), "An error occured with UserInfo", Toast.LENGTH_SHORT).show();
            }

             TextView user =  (TextView)mDrawer.findViewById(R.id.nh_username);
            user.setText(s.mDisplayName);
            TextView email = (TextView)mDrawer.findViewById(R.id.nh_email);
            email.setText(s.mEmail);
        }
    }


}
