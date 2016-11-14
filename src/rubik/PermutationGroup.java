package rubik;

import java.util.*;

/**
 * General permutation group
 */
public class PermutationGroup implements Group{
    /**
     * list that represents permutation
     */
    public ArrayList<Integer> perm;
    /**
     * number of elements in this permutation
     */
    public int size;
    
    /**
     * creates new permutation group of size n, and sets it to indentity
     * @param n size of permutation
     */
    public PermutationGroup(int n){
        size = n;
        perm = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            perm.add(i);
        }
    }
    
    /**
     * creates new PermutationGroup from its number
     * @param n size of permutation
     * @param num number of permutation
     * @see toNum()
     */
    public PermutationGroup(int n, long num){
        size = n;
        perm = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            perm.add(i);
        }
        fromNum(num);
    }
    
    @Override public PermutationGroup inv(){
        PermutationGroup p = new PermutationGroup(size);
        for (int i = 0; i < size; i++) {
            p.perm.set(perm.get(i), i);
        }
        return p;
    }
    
    @Override public PermutationGroup times(Group g){
        PermutationGroup p = (PermutationGroup)g;
        PermutationGroup r = new PermutationGroup(size);
        for (int i = 0; i < size; i++) {
            r.perm.set(i, p.perm.get(perm.get(i)));
        }
        return r;
    }
    
    /**
     * action of this permutation on CubieOrientation as a set
     * permutation should be size 8
     * @param co CubieOrientation as a set
     * @return new CubieOrientation after action
     */
    public CubieOrientation action(CubieOrientation co){
        CubieOrientation r = new CubieOrientation();
        for (int i = 0; i < size; i++) {
            r.groups.set(perm.get(i), co.groups.get(i));
        }
        return r;
    }
    
    /**
     * action of this permutation on EdgeOrientation as a set
     * permutation should be size 12
     * @param eo EdgeOrientation as a set
     * @return new EdgeOrientation after action
     */
    public EdgeOrientation action(EdgeOrientation eo){
        EdgeOrientation r = new EdgeOrientation();
        for (int i = 0; i < size; i++) {
            r.groups.set(perm.get(i), eo.groups.get(i));
        }
        return r;
    }
     
    /**
     * action of this permutation on list of integers
     * permutation and list should be same size
     * @param al ArrayList of integer
     * @return new ArrayList after action
     */   
    public ArrayList<Integer> action(ArrayList<Integer> al){
        ArrayList<Integer> r = new ArrayList<>();
        for (int i = 0; i < al.size(); i++) {
            r.add(0);
        }
        for (int i = 0; i < size; i++) {
            r.set(perm.get(i), al.get(i));
        }
        return r;
    }
    
    /**
     * computes and returns parity (sign) of permutation 
     * @return 0 if permutation is even, 1 if permutation is odd
     */
    public int parity(){
        int p = 0;
        Set<Integer> used = new HashSet<>();
        for (int i = 0; i < size; i++) {
            if(used.contains(i)) continue;
            int n = 0;
            int k = i;
            while(!used.contains(k)){
                used.add(k);
                n++;
            }
            if(n%2==0) p++;
        }
        return p%2;
    }
    /**
     * converts permutation to unique number
     * @return number that represents this permutation
     */
    public long toNum(){
        long r = 0;
        for (int i = 0; i < size; i++) {
            r *= size;
            r += perm.get(i);
        }
        return r;
    }
    
    private void fromNum(long num){
        for (int i = size-1; i >= 0; i--) {
            perm.set(i, (int)(num%size));
            num /= size; 
        }
    }
    
    @Override public int hashCode(){
        int r = 0;
        for (int i = 0; i < size; i++) {
            r *= size;
            r += perm.get(i);
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
        final PermutationGroup other = (PermutationGroup) obj;
        if (this.size != other.size) {
            return false;
        }
        for (int i = 0; i < this.size; i++) {
            if(!this.perm.get(i).equals(other.perm.get(i)))
                return false;
        }
        return true;
    }
}
