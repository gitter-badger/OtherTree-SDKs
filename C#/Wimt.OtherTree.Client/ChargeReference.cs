using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace Wimt.OtherTree.References
{
    [Serializable]
    public class ChargeReference : IEquatable<ChargeReference>
    {
        public bool Equals(ChargeReference other)
        {
            if (ReferenceEquals(null, other)) return false;
            if (ReferenceEquals(this, other)) return true;
            return Id.Equals(other.Id) && Charge.Equals(other.Charge);
        }

        public override bool Equals(object obj)
        {
            if (ReferenceEquals(null, obj)) return false;
            if (ReferenceEquals(this, obj)) return true;
            if (obj.GetType() != this.GetType()) return false;
            return Equals((ChargeReference)obj);
        }

        public override int GetHashCode()
        {
            unchecked
            {
                return (Id.GetHashCode() * 397) ^ Charge.GetHashCode();
            }
        }



        public Guid Charge
        {
            get { return _charge; }
            set { _charge = value; }
        }

        [DataMember]
        public Guid _charge = Guid.Empty;
        [DataMember]
        public Guid _id = Guid.Empty;

        public Guid Id
        {
            get { return _id; }
            set { _id = value; }
        }

        public ChargeReference(Guid charge)
        {
            Charge = charge;
            Id = Guid.NewGuid();
        }

        public ChargeReference(Guid charge, Guid id)
        {
            Charge = charge;
            Id = id;
        }

        public ChargeReference()
        {

            Charge = Guid.Empty;
            Id = Guid.Empty;
        }


    }
}