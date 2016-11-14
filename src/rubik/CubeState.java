package rubik;

import java.util.*;

/**
 * Represents the state of the cube as a semidirect product of 
 * permutation groups and orientation groups.
 */
public class CubeState implements Group{

    /**
     * PermutationGroup of edges
     */
    public PermutationGroup ep;
    /**
     * PermutationGroup of cubies
     */
    public PermutationGroup cp;
    /**
     * Orintation group of the edges 
     */
    public EdgeOrientation eo;
    /**
     * Orintation group of the cubies
     */
    public CubieOrientation co;
    
    /**
     * Creates new CubeState. Sets it to solved cube.
     */
    public CubeState(){
        ep = new PermutationGroup(12);
        cp = new PermutationGroup(8);
        eo = new EdgeOrientation();
        co = new CubieOrientation();
    }
    
    /**
     * creates and returns new CubeState after random shuffle
     * @return CubeState of random shuffled cube
     */
    public static CubeState getRandom(){
        CubeState cs = new CubeState();
        Random rand = new Random();
        for (int i = 0; i < 20; i++) {
            cs = cs.times(Solver.generators.get(rand.nextInt(18)));
        }
        return cs;
    }
    
    /**
     * inversion of CubeState
     * @return CubeState that is inverse to this one
     */
    @Override public CubeState inv(){
        CubeState r = new CubeState();
        r.cp = cp.inv();
        r.co = cp.inv().action(co.inv());
        r.ep = ep.inv();
        r.eo = ep.inv().action(eo.inv());
        return r;
    }
    
    /**
     * multiplay and returns product of two CubeStates
     * @param g second CubeState
     * @return new CubesState as a product of this one and g
     */
    @Override public CubeState times(Group g){
        CubeState cs = (CubeState)g;
        CubeState r = new CubeState();
        r.co = cs.co.times(cs.cp.action(co));
        r.cp = cp.times(cs.cp);
        r.eo = cs.eo.times(cs.ep.action(eo));
        r.ep = ep.times(cs.ep);
        return r;
    }
    
    /**
     * action of this CubeState as group on Phase1Edge as a set
     * @param p1e Phase1Edge as a set in action
     * @return result of the action
     */
    public Phase1Edge action(Phase1Edge p1e){
        Phase1Edge r = new Phase1Edge();
        r.ep = ep.action(p1e.ep);
        r.eo = eo.times(ep.action(p1e.eo));
        return r;
    }
    
    /**
     * action of this CubeState as group on CubieOrientation as a set
     * @param co CubieOrientation as a set in action
     * @return result of the action
     */
    public CubieOrientation action(CubieOrientation co){
        return this.co.times(cp.action(co));
    }
    
    /**
     * convers to Phase1Edge and returns it
     * @return Phase1Edge of this CubeState
     */
    public Phase1Edge toPhase1Edge(){
        Phase1Edge r = new Phase1Edge();
        r.eo = eo;
        r.ep = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            r.ep.add(0);
        }
        for (int i = 0; i < 12; i++) {
            r.ep.set(ep.perm.get(i), (i<4 || i>=8)?1:0);
        }
        return r;
    }
    
    @Override public int hashCode(){
        return (ep.hashCode()<<3) ^ (cp.hashCode()<<2) ^ 
                (eo.hashCode()<<1) ^ (co.hashCode());
    }

    @Override public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CubeState other = (CubeState) obj;
        if (!this.ep.equals(other.ep)) {
            return false;
        }
        if (!this.eo.equals(other.eo)) {
            return false;
        }
        if (!this.cp.equals(other.cp)) {
            return false;
        }
        if (!this.co.equals(other.co)) {
            return false;
        }
        return true;
    }
}
