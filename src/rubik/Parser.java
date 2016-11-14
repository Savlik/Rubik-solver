package rubik;

import java.util.*;

/**
 * Handles converting of input to actual CubeState.
 * Also checks if resulting CubeState is possible to solve.
 */
public class Parser {
    
    static private String replace(String s, char c, int pos){
        char[] chars = s.toCharArray();
        chars[pos] = c;
        return String.valueOf(chars);
    }
    
    /**
     * transfers CubeState to a map of colors on faces. Inverse function of Parser.parse()
     * @param cs CubeState to be tranfered
     * @return map of colors on faces 
     */
    static public Map<Character, String> getFaces(CubeState cs){
        Map<Character, String> faces = new HashMap<>();
        faces.put('U', "    U    ");
        faces.put('L', "    L    ");
        faces.put('F', "    F    ");
        faces.put('R', "    R    ");
        faces.put('D', "    D    ");
        faces.put('B', "    B    ");
        
        //cubies
        for (int i = 0; i < 8; i++) {
            int pos = cs.cp.perm.get(i);
            int orientation = cs.co.groups.get(pos).num;
            for (int j = 0; j < 3; j++) {
                char color = Matching.cubieFace[i][j];
                int num = Matching.cubiePos[pos][(j + orientation)%3];
                faces.put(
                    Matching.cubieFace[pos][(j + orientation)%3], 
                    replace(
                        faces.get(Matching.cubieFace[pos][(j + orientation)%3]),
                        color,
                        num
                    )
                );
            }
        }
        
        //edges
        for (int i = 0; i < 12; i++) {
            int pos = cs.ep.perm.get(i);
            int orientation = cs.eo.groups.get(pos).num;
            for (int j = 0; j < 2; j++) {
                char color = Matching.edgeFace[i][j];
                int num = Matching.edgePos[pos][(j + orientation)%2];
                faces.put(
                    Matching.edgeFace[pos][(j + orientation)%2], 
                    replace(
                        faces.get(Matching.edgeFace[pos][(j + orientation)%2]),
                        color,
                        num
                    )
                );
            }
        }
        return faces;
    }
    
    /**
     * parses input form a file to a CubeState. Checks if input it possible to solve.
     * @param faces map of colors on faces.
     * @return resulting CubeState
     * @throws Exception only if cube is wrongly set or impossible to solve
     */
    static public CubeState parse(Map<Character, String> faces) throws Exception{
        
        CubeState cs = new CubeState();
        
        // --- cubies ---
        Set<Integer> cubiesUsed = new HashSet<>();
        for (int i = 0; i < 8; i++) {
            // take colors
            // and find orientation
            Set<Character> s = new HashSet<>();
            int cubieOrient = -1;
            for (int j = 0; j < 3; j++) {
                char c = faces.get(Matching.cubieFace[i][j]).charAt(Matching.cubiePos[i][j]);
                if(s.contains(c)) throw new Exception("Cubie (" + i + ") has two colors the same.");
                if(s.contains(Matching.op(c))) throw new Exception("Cubie (" + i + ") has two oposite colors.");
                if(c=='U' || c=='D'){
                    cubieOrient = j;
                }
                s.add(c);
            }
            
            //find this piece
            int cubieNum = -1;
            for (int ii = 0; ii < 8; ii++) {
                boolean good = true;
                for (int jj = 0; jj < 3; jj++) {
                    if(!s.contains(Matching.cubieFace[ii][jj])){
                        good = false;
                        break;
                    }
                }
                if(good){
                    cubieNum = ii;
                    break;
                }
            }
            if(cubiesUsed.contains(cubieNum)) throw new Exception("Two same colored cubies.");
            cubiesUsed.add(cubieNum);
            cs.cp.perm.set(cubieNum, i);
            cs.co.groups.get(i).num = cubieOrient;
            
            //check third orientation
            int jj = (cubieOrient+1)%3;
            char ch = faces.get(Matching.cubieFace[i][jj]).charAt(Matching.cubiePos[i][jj]);
            if(ch != Matching.cubieFace[cubieNum][1]) throw new Exception("Cubie colors in wrong order.");
        }
        //check orient parity
        int parity = 0;
        for (int i = 0; i < 8; i++) {
            parity += cs.co.groups.get(i).num;
        }
        if(parity % 3 != 0) throw new Exception("Parity for cubies is wrong.");
        
        // --- edges ---
        Set<Integer> edgesUsed = new HashSet<>();
        for (int i = 0; i < 12; i++) {
            Set<Character> s = new HashSet<>();
            for (int j = 0; j < 2; j++) {
                char c = faces.get(Matching.edgeFace[i][j]).charAt(Matching.edgePos[i][j]);
                if(s.contains(c)) throw new Exception("Edge (" + i + ") has two colors the same.");
                if(s.contains(Matching.op(c))) throw new Exception("Edge (" + i + ") has two oposite colors.");
                s.add(c);
            }
            
            //find this piece
            int edgeNum = -1;
            for (int ii = 0; ii < 12; ii++) {
                boolean good = true;
                for (int jj = 0; jj < 2; jj++) {
                    if(!s.contains(Matching.edgeFace[ii][jj])){
                        good = false;
                        break;
                    }
                }
                if(good){
                    edgeNum = ii;
                    break;
                }
            }
            //orient
            int edgeOrient;
            if(faces.get(Matching.edgeFace[i][0]).charAt(Matching.edgePos[i][0])==Matching.edgeFace[edgeNum][0]){
                edgeOrient = 0;
            }else{
                edgeOrient = 1;
            }
            if(edgesUsed.contains(edgeNum)) throw new Exception("Two same colored cubies.");
            edgesUsed.add(edgeNum);
            cs.ep.perm.set(edgeNum, i);
            cs.eo.groups.get(i).num = edgeOrient;
        }
        //check orient parity
        parity = 0;
        for (int i = 0; i < 12; i++) {
            parity += cs.eo.groups.get(i).num;
        }
        if(parity % 2 != 0) throw new Exception("Parity for edges is wrong.");
        
        // --- centers ---
        for (Map.Entry<Character, String> entry : faces.entrySet())
        {
            if(entry.getKey()!=entry.getValue().charAt(4))
                throw new Exception("Center piece is wrong color.");
        }

        // check cubeie-edge parity
        if(cs.cp.parity()!=cs.ep.parity()) throw new Exception("Parity of edges and cubies do not match.");
        
        return cs;
    }
}
