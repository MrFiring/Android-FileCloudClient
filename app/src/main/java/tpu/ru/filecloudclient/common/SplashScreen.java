package tpu.ru.filecloudclient.common;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.owncloud.android.lib.resources.files.ReadRemoteFolderOperation;
import com.owncloud.android.lib.resources.files.RemoteFile;
import com.owncloud.android.lib.resources.users.GetRemoteUserInfoOperation;

import java.util.ArrayList;

import tpu.ru.filecloudclient.LoginActivity;
import tpu.ru.filecloudclient.MainActivity;
import tpu.ru.filecloudclient.OwnCloudHolder;
import tpu.ru.filecloudclient.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
    }
}
