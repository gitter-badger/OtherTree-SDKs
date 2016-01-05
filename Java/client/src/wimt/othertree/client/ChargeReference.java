package wimt.othertree.client;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by Nick Cuthbert on 05/01/2016.
 */
public class ChargeReference
{

    public boolean equals(ChargeReference other)        {
        if (null==other) return false;

        if (this==other) return true;

        return id.equals(other.id) && charge.equals(other.charge);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (null==obj) return false;

        if (this==obj) return true;

        if (!(obj instanceof ChargeReference)) return false;
        return equals((ChargeReference)obj);
    }

    @Override
    public int hashCode() {
        return (id.hashCode() * 397) ^charge.hashCode();
    }


    @SerializedName("Charge")
    public String charge;



    @SerializedName("Id")
    public String id;


    public ChargeReference(String charge, String id)
    {
        this.charge = charge;
        this.id= id;
    }

    public ChargeReference()
    {
        this("","");
    }


}
