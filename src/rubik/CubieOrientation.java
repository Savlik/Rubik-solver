package rubik;

import java.util.*;
/**
 * Group that represents orientation of cubies as direct product of
 * 8 cyclic groups
 * @author Savlik
 */
public class CubieOrientation implements Group{
    
    /**
     * list of 8 cyclic groups (order 3)
     */
    public ArrayList<CyclicGroup> groups;
    
    /**
     * default constructor, creates unit element
     */
    public CubieOrientation(){
        groups = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            groups.add(new CyclicGroup(3));
        }
    }
    
    /**
     * creates CubieOrientation form its number
     * @param num number of CubieOrientation
     * @see toNum()
     */
    public CubieOrientation(int num){
        groups = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            groups.add(new CyclicGroup(3));
        }
        for (int i = 7; i >= 0; i--) {
            groups.get(i).num = num%3;
            num /= 3;
        }
    }
    
    @Override public CubieOrientation inv(){
        CubieOrientation co = new CubieOrientation();
        for (int i = 0; i < 8; i++) {
            co.groups.set(i, groups.get(i).inv());
        }
        return co;
    }
    
    @Override public CubieOrientation times(Group g){
        CubieOrientation co  = (CubieOrientation)g;
        CubieOrientation r = new CubieOrientation();
        for (int i = 0; i < 8; i++) {
            r.groups.set(i, groups.get(i).times(co.groups.get(i)));
        }
        return r;
    } 
    
    /**
     * converts CubieOrientation to unique integer
     * @return number represention this CubieOrientation
     */
    public int toNum(){
        int num = 0;
        for (int i = 0; i < 8; i++) {
            num *= 3;
            num += groups.get(i).num;
        }
        return num;
    }
    
    @Override public int hashCode(){
        int r = 0;
        for (int i = 0; i < 8; i++) {
            r *= 3;
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
        final CubieOrientation other = (CubieOrientation) obj;
        for (int i = 0; i < this.groups.size(); i++) {
            if(!this.groups.get(i).equals(other.groups.get(i)))
                return false;
        }
        return true;
    }
}
