package tpu.ru.filecloudclient.common;

import java.util.regex.Pattern;

public class PathController {
    private String mPath;

    public PathController(){this.mPath = "";}

    public PathController(String Path) {
        this.mPath = Path;
    }

    public String getPath() {
        return mPath;
    }

    public String getCurrentDirName(){
        if(mPath.length() > 1) {
            String[] dirs = mPath.split(Pattern.quote("/"));


            return dirs[dirs.length - 1];
        }
        return null;
    }

    public void setPath(String mPath) {
        this.mPath = mPath;
    }

    public String Backward(){
        if(mPath.length() > 1)
        mPath = mPath.substring(0, mPath.lastIndexOf('/'));
        else {
            mPath = "";
            return null;
        }

        return mPath;
    }

    public String Forward(String nextDir){
        mPath += "/" + nextDir;

        return mPath;
    }



}
