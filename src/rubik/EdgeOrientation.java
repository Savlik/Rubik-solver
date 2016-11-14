package rubik;

import java.util.ArrayList;

/**
 * Group that represents orientation of edges as direct product of
 * 12 cyclic groups 
 * @author Savlik
 */
public class EdgeOrientation implements Group{
    /**
     * list of 12 cyclic groups (order 2)
     */
    public ArrayList<CyclicGroup> groups;
    
    /**
     * default constructor, creates unit element
     */
    public EdgeOrientation(){
        groups = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            groups.add(new CyclicGroup(2));
        }
    }

    @Override public EdgeOrientation inv(){
        EdgeOrientation eo = new EdgeOrientation();
        for (int i = 0; i < 12; i++) {
            eo.groups.set(i, groups.get(i).inv());
        }
        return eo;
    }
    
    @Override public EdgeOrientation times(Group g){
        EdgeOrientation eo  = (EdgeOrientation)g;
        EdgeOrientation r = new EdgeOrientation();
        for (int i = 0; i < 12; i++) {
            r.groups.set(i, groups.get(i).times(eo.groups.get(i)));
        }
        return r;
    }
    
    @Override public int hashCode(){
        int r = 0;
        for (int i = 0; i < 12; i++) {
            r *= 2;
            r += groups.get(i).num;
        }
        return r;
    }

    @Override public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EdgeOrientation other = (EdgeOrientation) obj;
        for (int i = 0; i < this.groups.size(); i++) {
            if(!this.groups.get(i).equals(other.groups.get(i)))
                return false;
        }
        return true;
    }
}
