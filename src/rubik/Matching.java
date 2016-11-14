package rubik;

/**
 * Static Class that handles mathing of cubies and edges to actual color of the cube
 */
public class Matching {
    /**
     * faces of cubies in order of cubies numbering
     */
    public static char[][] cubieFace = {
        //upper layer
        {'U','F','L'},
        {'U','R','F'},
        {'U','B','R'},
        {'U','L','B'},
        //bottom layer
        {'D','L','F'},
        {'D','F','R'},
        {'D','R','B'},
        {'D','B','L'}
    };
    
    /**
     * positions of cubies on the face in order of cubies numbering
     */
    public static int[][] cubiePos = {
        //upper layer
        {6,0,2},
        {8,0,2},
        {2,8,2},
        {0,0,6},
        //bottom layer
        {0,8,6},
        {2,8,6},
        {8,8,2},
        {6,0,6}
    };
    /**
     * faces of edges in order of edges numbering
     */
    public static char[][] edgeFace = {
        //upper layer
        {'U','F'},
        {'U','R'},
        {'U','B'},
        {'U','L'},
        //middle layer
        {'F','L'},
        {'F','R'},
        {'B','R'},
        {'B','L'},
        //bottom layer
        {'D','F'},
        {'D','R'},
        {'D','B'},
        {'D','L'}
    };
    
    /**
     * positions of edges on the face in order of edges numbering
     */
    public static int[][] edgePos = {
        //upper layer
        {7,1},
        {5,1},
        {1,7},
        {3,1},
        //middle layer
        {3,5},
        {5,3},
        {5,5},
        {3,3},
        //bottom layer
        {1,7},
        {5,7},
        {7,1},
        {3,7}
    };
    
    /**
     * returns opposite face
     * @param c char as a face
     * @return opposite face of c
     */
    public static char op(char c){
        switch(c){
            case 'U':
                return 'D';
            case 'D':
                return 'U';
            case 'R':
                return 'L';
            case 'L':
                return 'R';
            case 'F':
                return 'B';
            case 'B':
                return 'F';
            default:
                return c;
        }
    }
}
