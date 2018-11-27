package tpu.ru.filecloudclient;

import com.owncloud.android.lib.common.OwnCloudClient;

public class OwnCloudHolder {
    private static OwnCloudClient instance;
    private static boolean isSetted  = false;

    public synchronized  static void setInstance(OwnCloudClient instance){ if(!isSetted)OwnCloudHolder.instance = instance; }

    public synchronized static OwnCloudClient getInstance(){
        return instance;
    }

}
