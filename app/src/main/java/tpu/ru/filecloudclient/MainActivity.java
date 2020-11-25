package tpu.ru.filecloudclient;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import java.util.List;

import tpu.ru.filecloudclient.common.Directory;
import tpu.ru.filecloudclient.common.FilesArrayAdapter;
import tpu.ru.filecloudclient.common.PathController;
import tpu.ru.filecloudclient.tasks.StudyFragment;


public class MainActivity extends AppCompatActivity implements FilesFragment.OnFragmentInteractionListener, PageFragment.OnListItemClickListener, StudyFragment.TabLayoutSetupCallback {

    NavigationView navView = null;
    DrawerLayout mDrawer = null;
    private Toolbar mToolbar;
    private TabLayout tabLayout;


    private ActionBarDrawerToggle mDrawerToggle;

    OwnCloudClient mClient;
    final PathController pathController = new PathController();
    final Directory genDir = new Directory("/");

    private FragmentManager fragmentMgr = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tabLayout = (TabLayout)findViewById(R.id.tab_layout);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawer.addDrawerListener(mDrawerToggle);

        navView = (NavigationView)findViewById(R.id.nav_view);
        //mFileListView = (ListView)findViewById(R.id.listFileView);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                Fragment fragment = null;
                Class fragmentClass = null;
                switch(menuItem.getItemId()){
                    case R.id.m_logout:
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                        
                    case R.id.m_study:
                        fragmentClass = StudyFragment.class;
                        tabLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.m_files:
                        tabLayout.setVisibility(View.GONE);
                        fragmentClass = FilesFragment.class;
                        break;
                    default:
                        tabLayout.setVisibility(View.GONE);
                        fragmentClass = FilesFragment.class;
                        break;

                }

                try{
                    fragment = (Fragment)fragmentClass.newInstance();
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                if(fragmentMgr == null)
                fragmentMgr = getSupportFragmentManager();
                fragmentMgr.beginTransaction().replace(R.id.content_frame, fragment).commit();



                menuItem.setChecked(true);
                setTitle(menuItem.getTitle());


                mDrawer.closeDrawers();


                return true;
            }
        });


        mClient = OwnCloudHolder.getInstance();


        final GetRemoteUserInfoOperation userOperation = new GetRemoteUserInfoOperation();

        GetInfoTask task = new GetInfoTask(userOperation);
        task.execute(mClient);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListItemClick(String title) {

    }


    @Override
    public void setupTabLayout(ViewPager viewPager) {

        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(fragmentMgr != null){
            List<Fragment> fragments = fragmentMgr.getFragments();
            for(Fragment i : fragments){
                if(i instanceof FilesFragment && i.isVisible()){
                    ((FilesFragment)i).turnBack();
                }
            }


        }
        else
            super.onBackPressed();
  //      updateListView(pathController.Backward(), this);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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
