package tpu.ru.filecloudclient.common;

public class PathController {
    private String mPath;

    public PathController(){this.mPath = "";}

    public PathController(String Path) {
        this.mPath = Path;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String mPath) {
        this.mPath = mPath;
    }

    public String Backward(){
        mPath = mPath.substring(0, mPath.lastIndexOf('/'));
        return mPath;
    }

    public String Forward(String nextDir){
        mPath += "/" + nextDir;

        return mPath;
    }



}
