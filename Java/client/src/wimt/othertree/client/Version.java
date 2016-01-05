package wimt.othertree.client;

import java.io.Serializable;

/**
 * Created by Nick Cuthbert on 05/01/2016.
 */
public class Version{

    public int Major=0;
    public int Minor=0;
    public int Patch=0;

    public Version(int major, int minor, int patch){
        Major=major;
        Minor=minor;
        Patch=patch;
    }
}
