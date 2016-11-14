package rubik;

/**
 * General cyclic Ggoup
 */
public class CyclicGroup implements Group{
    /**
     * value of the element
     */
    public int num;
    /**
     * order of this cyclic group (number of elements)
     */
    public int size;
    
    /**
     * new element of CyclicGroup of specified order. Sets it to unit element.
     * @param n order of CyclicGroup
     */
    public CyclicGroup(int n){
        num = 0;
        size = n;
    }
    
    @Override public CyclicGroup inv(){
        CyclicGroup p = new CyclicGroup(size);
        p.num = (size - num)%size;
        return p;
    }
    
    @Override public CyclicGroup times(Group g){
        CyclicGroup c = (CyclicGroup)g;
        CyclicGroup r = new CyclicGroup(size);
        r.num = (num + c.num) % size;
        return r;
    }
    
    @Override public int hashCode(){
        return this.num;
    }

    @Override public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CyclicGroup other = (CyclicGroup) obj;
        if (this.num != other.num) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }
        return true;
    }
}
