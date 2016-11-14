
package rubik;

import java.util.ArrayList;

/**
 * Represents state of edges in phase 1 when solving
 */
public class Phase1Edge {
    /**
     * orientation of all edges
     */
    public EdgeOrientation eo;
    /**
     * position of edges
     * contains 1 if the edge is from U or D face, 0 otherwise
     */
    public ArrayList<Integer> ep;
    
    /**
     * creates new Phase1Edge, sets it to the state when cube is solved
     */
    public Phase1Edge(){
        init();
    }
    
    /**
     * creates new Phase1Edge coresponding to the number
     * @param num number of the Pahse1Edge
     * @see toNum()
     */
    public Phase1Edge(int num){
        init();
        fromNum(num);
    }
    
    private void init(){
        ep = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            ep.add((i<4 || i>=8)?1:0);
        }
        eo = new EdgeOrientation();
    }
    
    private void fromNum(int num){
        for (int i = 0; i < 12; i++) {
            ep.set(i,num&1);
            num>>=1;
        }
        for (int i = 0; i < 12; i++) {
            eo.groups.get(i).num = num&1;
            num>>=1;
        }
    }
    /**
     * converts Phase1Edge to unique number
     * @return number that represents this Phase1Edge
     */
    public int toNum(){
        int num =0;
        for (int i = 11; i >= 0; i--) {
            num<<=1;
            num += eo.groups.get(i).num;
        }
        for (int i = 11; i >= 0; i--) {
            num<<=1;
            num += ep.get(i);
        }
        return num;
    }
    
}
