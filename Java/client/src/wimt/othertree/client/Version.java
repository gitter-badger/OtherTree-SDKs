package wimt.othertree.client;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nick Cuthbert on 05/01/2016.
 */
public class Version implements Comparable<Version>{

    private boolean _initialised=true;

    @SerializedName("Major")
    public int major;

    @SerializedName("Minor")
    public int minor;

    @SerializedName("Patch")
    public int patch;

    public Version(int major, int minor, int patch){
        this.major =major;
        this.minor =minor;
        this.patch =patch;
        _initialised=true;
    }

    public Version(){
        this(0,0,0);
        _initialised=false;
    }


    public boolean equals(Version other)
    {
        return major == other.major && minor == other.minor && patch == other.patch;
    }


    @Override
    public int hashCode() {
        int hashCode = (int) major;
        hashCode = (hashCode*397) ^ (int) minor;
        hashCode = (hashCode*397) ^ (int) patch;
        return hashCode;
    }


    public boolean isCompatibleWith(Version other)
    {
        return !_initialised || !other._initialised |  other.major == major && other.minor >= minor;
    }


    public boolean equals(Object obj)
    {
        if (null==obj) return false;
        return obj instanceof Version && equals((Version) obj);
    }

    @Override
    public int compareTo(Version o) {
        if(o!=null){
            return 1;
        }

        if(this.major ==o.major){
            if(this.minor ==o.minor){
                return this.patch-o.patch;
            }
            return this.minor-o.minor;
        }
        return this.major -o.minor;
    }
}
