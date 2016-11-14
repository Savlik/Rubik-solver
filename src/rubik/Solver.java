package rubik;

import java.io.*;
import java.util.*;

/**
 * Handles solving of the cube.
 */
public class Solver {
    private CubeState original;
    
    /**
     * List of all generators of the cube.
     */
    public static ArrayList<CubeState> generators;

    /**
     * List of generators names in order the appear in Solver.generators
     */
    public static ArrayList<String> generators_name;
    
    private static Map<Integer, Integer> phase1edge_dist;
    private static Map<Integer, Integer> phase1cubie_dist;
    private static Map<Long, Integer> phase2edge_dist;
    private static Map<Long, Integer> phase2cubie_dist;
    
    private long startTime;
    private final int timeout = 10000; //in ms
    private final int maxDepth = 21;
    
    /**
     * Creates new Solver. Creates generators of the cube a precomputes heuristics.
     */
    public Solver(){
       generators = new ArrayList<>();
       generators_name = new ArrayList<>();
       CubeState cs;

       //U
       cs = new CubeState();
       cs.cp.perm.set(0, 3);
       cs.cp.perm.set(1, 0);
       cs.cp.perm.set(2, 1);
       cs.cp.perm.set(3, 2);
       cs.ep.perm.set(0, 3);
       cs.ep.perm.set(1, 0);
       cs.ep.perm.set(2, 1);
       cs.ep.perm.set(3, 2);
       generators.add(cs);
       generators.add(cs.times(cs));
       generators.add(cs.times(cs).times(cs));
       generators_name.add("U");
       generators_name.add("U2");
       generators_name.add("U'");
       
       //D
       cs = new CubeState();
       cs.cp.perm.set(4, 5);
       cs.cp.perm.set(5, 6);
       cs.cp.perm.set(6, 7);
       cs.cp.perm.set(7, 4);
       cs.ep.perm.set(8, 9);
       cs.ep.perm.set(9, 10);
       cs.ep.perm.set(10, 11);
       cs.ep.perm.set(11, 8);
       generators.add(cs);
       generators.add(cs.times(cs));
       generators.add(cs.times(cs).times(cs));
       generators_name.add("D");
       generators_name.add("D2");
       generators_name.add("D'");
       
        //R
       cs = new CubeState();
       cs.cp.perm.set(1, 2);
       cs.cp.perm.set(2, 6);
       cs.cp.perm.set(6, 5);
       cs.cp.perm.set(5, 1);
       cs.ep.perm.set(1, 6);
       cs.ep.perm.set(6, 9);
       cs.ep.perm.set(9, 5);
       cs.ep.perm.set(5, 1);
       cs.co.groups.get(1).num = 2;
       cs.co.groups.get(2).num = 1;
       cs.co.groups.get(6).num = 2;
       cs.co.groups.get(5).num = 1;
       generators.add(cs);
       generators.add(cs.times(cs));
       generators.add(cs.times(cs).times(cs));
       generators_name.add("R");
       generators_name.add("R2");
       generators_name.add("R'");
       
       //L
       cs = new CubeState();
       cs.cp.perm.set(0, 4);
       cs.cp.perm.set(4, 7);
       cs.cp.perm.set(7, 3);
       cs.cp.perm.set(3, 0);
       cs.ep.perm.set(3, 4);
       cs.ep.perm.set(4, 11);
       cs.ep.perm.set(11, 7);
       cs.ep.perm.set(7, 3);
       cs.co.groups.get(0).num = 1;
       cs.co.groups.get(4).num = 2;
       cs.co.groups.get(7).num = 1;
       cs.co.groups.get(3).num = 2;
       generators.add(cs);
       generators.add(cs.times(cs));
       generators.add(cs.times(cs).times(cs));
       generators_name.add("L");
       generators_name.add("L2");
       generators_name.add("L'");
       
       //F
       cs = new CubeState();
       cs.cp.perm.set(0, 1);
       cs.cp.perm.set(1, 5);
       cs.cp.perm.set(5, 4);
       cs.cp.perm.set(4, 0);
       cs.ep.perm.set(0, 5);
       cs.ep.perm.set(5, 8);
       cs.ep.perm.set(8, 4);
       cs.ep.perm.set(4, 0);
       cs.co.groups.get(0).num = 2;
       cs.co.groups.get(1).num = 1;
       cs.co.groups.get(5).num = 2;
       cs.co.groups.get(4).num = 1;
       cs.eo.groups.get(0).num = 1;
       cs.eo.groups.get(5).num = 1;
       cs.eo.groups.get(8).num = 1;
       cs.eo.groups.get(4).num = 1;
       generators.add(cs);
       generators.add(cs.times(cs));
       generators.add(cs.times(cs).times(cs));
       generators_name.add("F");
       generators_name.add("F2");
       generators_name.add("F'");
       
       //B
       cs = new CubeState();
       cs.cp.perm.set(3, 7);
       cs.cp.perm.set(7, 6);
       cs.cp.perm.set(6, 2);
       cs.cp.perm.set(2, 3);
       cs.ep.perm.set(2, 7);
       cs.ep.perm.set(7, 10);
       cs.ep.perm.set(10, 6);
       cs.ep.perm.set(6, 2);
       cs.co.groups.get(3).num = 1;
       cs.co.groups.get(7).num = 2;
       cs.co.groups.get(6).num = 1;
       cs.co.groups.get(2).num = 2;
       cs.eo.groups.get(2).num = 1;
       cs.eo.groups.get(7).num = 1;
       cs.eo.groups.get(10).num = 1;
       cs.eo.groups.get(6).num = 1;
       generators.add(cs);
       generators.add(cs.times(cs));
       generators.add(cs.times(cs).times(cs));
       generators_name.add("B");
       generators_name.add("B2");
       generators_name.add("B'");

       System.out.println("precomputing");
       precompute_phase1edge();
       precompute_phase1cubie();
       precompute_phase2edge();
       precompute_phase2cubie();
       System.out.println("finished");
    }
    
    /**
     * Precomputes heuristics distance for phase 1 of the edges.
     * Tries to read the data from a file.
     * Writes it to the file after computing.
     */
    private void precompute_phase1edge(){
        phase1edge_dist = new HashMap<>();

        File f = new File("phase1edge.obj");
        if(f.exists() && !f.isDirectory()) { 
            try{
                FileInputStream fis = new FileInputStream(f);  
                ObjectInputStream s = new ObjectInputStream(fis);  
                phase1edge_dist = (HashMap<Integer,Integer>)s.readObject();         
                s.close();
                return;
            }catch(Exception e){
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
        
        Queue q = new LinkedList();
        phase1edge_dist.put(new Phase1Edge().toNum(), 0);
        q.add(new Phase1Edge().toNum());
        while(!q.isEmpty()){
            int num = (Integer)q.poll();
            Phase1Edge p1e = new Phase1Edge(num);
            int dist = phase1edge_dist.get(num);
            for (int i = 0; i < generators.size(); i++) {
                int num2 = generators.get(i).action(p1e).toNum();

                if(!phase1edge_dist.containsKey(num2)){
                    phase1edge_dist.put(num2, dist+1);
                    q.add(num2);
                    if(phase1edge_dist.size()%200000==0){
                        System.out.println(phase1edge_dist.size()/20000+"%");
                    }
                }
            }
        }

        try{
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream s = new ObjectOutputStream(fos);          
            s.writeObject(phase1edge_dist);
            s.flush();
        }catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
    
    /**
     * Precomputes heuristics distance for phase 1 of the cubies.
     * Tries to read the data from a file.
     * Writes it to the file after computing.
     */
    private void precompute_phase1cubie(){
        phase1cubie_dist = new HashMap<>();
        
        File f = new File("phase1cubie.obj");
        if(f.exists() && !f.isDirectory()) { 
            try{
                FileInputStream fis = new FileInputStream(f);  
                ObjectInputStream s = new ObjectInputStream(fis);  
                phase1cubie_dist = (HashMap<Integer,Integer>)s.readObject();         
                s.close();
                return;
            }catch(Exception e){
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }

        Queue q = new LinkedList();
        phase1cubie_dist.put(new CubieOrientation().toNum(), 0);
        q.add(new CubieOrientation().toNum());
        while(!q.isEmpty()){
            int num = (Integer)q.poll();
            CubieOrientation co = new CubieOrientation(num);
            int dist = phase1cubie_dist.get(num);
            for (int i = 0; i < generators.size(); i++) {
                int num2 = generators.get(i).action(co).toNum();
                
                if(!phase1cubie_dist.containsKey(num2)){
                    phase1cubie_dist.put(num2, dist+1);
                    q.add(num2);
                }
            }
        }
        
        try{
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream s = new ObjectOutputStream(fos);          
            s.writeObject(phase1cubie_dist);
            s.flush();
        }catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
    
    /**
     * Precomputes heuristics distance for phase 2 of the edges.
     * Tries to read the data from a file.
     * Writes it to the file after computing.
     */
    private void precompute_phase2edge(){
        phase2edge_dist = new HashMap<>();
                
        File f = new File("phase2edge.obj");
        if(f.exists() && !f.isDirectory()) { 
            try{
                FileInputStream fis = new FileInputStream(f);  
                ObjectInputStream s = new ObjectInputStream(fis);  
                phase2edge_dist = (HashMap<Long,Integer>)s.readObject();         
                s.close();
                return;
            }catch(Exception e){
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
        
        Queue q = new LinkedList();
        phase2edge_dist.put(new PermutationGroup(12).toNum(), 0);
        q.add(new PermutationGroup(12).toNum());
        while(!q.isEmpty()){
            long num = (Long)q.poll();
            PermutationGroup pg = new PermutationGroup(12,num);
            int dist = phase2edge_dist.get(num);
            for (int i = 0; i < generators.size(); i++) {
                if(i==6 || i==8 || i==9 || i==11 || i==12 || i==14 || i==15 || i==17) continue;
                long num2 = pg.times(generators.get(i).ep).toNum();
                
                if(!phase2edge_dist.containsKey(num2)){
                    phase2edge_dist.put(num2, dist+1);
                    q.add(num2);
                    if(phase2edge_dist.size()%200000==0){
                        System.out.println((phase2edge_dist.size()/20000 + 50) +"%");
                    }
                }
            }
        }
        
        try{
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream s = new ObjectOutputStream(fos);          
            s.writeObject(phase2edge_dist);
            s.flush();
        }catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
    
    /**
     * Precomputes heuristics distance for phase 2 of the cubies.
     * Tries to read the data from a file.
     * Writes it to the file after computing.
     */    
    private void precompute_phase2cubie(){
        phase2cubie_dist = new HashMap<>();
        
        File f = new File("phase2cubie.obj");
        if(f.exists() && !f.isDirectory()) { 
            try{
                FileInputStream fis = new FileInputStream(f);  
                ObjectInputStream s = new ObjectInputStream(fis);  
                phase2cubie_dist = (HashMap<Long,Integer>)s.readObject();         
                s.close();
                return;
            }catch(Exception e){
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
        
        Queue q = new LinkedList();
        phase2cubie_dist.put(new PermutationGroup(8).toNum(), 0);
        q.add(new PermutationGroup(8).toNum());
        while(!q.isEmpty()){
            long num = (Long)q.poll();
            PermutationGroup pg = new PermutationGroup(8,num);
            int dist = phase2cubie_dist.get(num);
            for (int i = 0; i < generators.size(); i++) {
                if(i==6 || i==8 || i==9 || i==11 || i==12 || i==14 || i==15 || i==17) continue;
                long num2 = pg.times(generators.get(i).cp).toNum();
                
                if(!phase2cubie_dist.containsKey(num2)){
                    phase2cubie_dist.put(num2, dist+1);
                    q.add(num2);
                }
            }
        }
        
        try{
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream s = new ObjectOutputStream(fos);          
            s.writeObject(phase2cubie_dist);
            s.flush();
        }catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }
    }
    
    /**
     * Returns heuristics of CubeState for pahse 1
     * @param cs CubeState 
     * @return minimal moves necessary to get to phase 2
     */
    private int heuristics1(CubeState cs){
        return Math.max(
            phase1edge_dist.get(cs.toPhase1Edge().toNum()),
            phase1cubie_dist.get(cs.co.toNum())
        );
    }
    
    /**
     * Returns heuristics of CubeState for pahse 2
     * @param cs CubeState 
     * @return minimal moves necessary to solve CubeState
     */
    private int heuristics2(CubeState cs){
        return Math.max(
            phase2edge_dist.get(cs.ep.toNum()),
            phase2cubie_dist.get(cs.cp.toNum())
        );
    }
    
    /**
     * Tries to solve CubeState. Maximal time is 10s, after that is timeouts.
     * @param cs CubeState to solve
     * @return list of generators ids if solution found, null if it timeouts
     */
    public ArrayList<Integer> solve(CubeState cs){
        original = cs;
        startTime = System.currentTimeMillis();
        
        Set<CubeState> closed = new HashSet<>();
        PriorityQueue<Solution> q = new PriorityQueue<>();
        Solution so = new Solution(cs);
        so.compute_h1();
        q.add(so);
        closed.add(cs);
        while(!q.isEmpty()){
            if(System.currentTimeMillis()-startTime > timeout){
                return null;
            }
            Solution sol = q.poll();
            if(sol.est == 0){
                Solution sol3 = solve2(sol);
                if(sol3!=null){
                    ArrayList<Integer> res = sol.moves;
                    res.addAll(sol3.moves);
                    return res;
                }
            }
            
            for (int i = 0; i < generators.size(); i++) {
                CubeState cs2 = sol.cs.times(generators.get(i));
                if(!closed.contains(cs2)){
                    closed.add(cs2);
                    Solution sol2 = new Solution(cs2);
                    sol2.dist = sol.dist + 1;
                    sol2.moves = (ArrayList<Integer>)sol.moves.clone();
                    sol2.moves.add(i);
                    sol2.compute_h1();
                    
                    if(sol2.est + sol2.dist <= maxDepth){
                        q.add(sol2);
                    }
                }
            }
        }
        return null;
    }
    
    /**
    * Tries to solve CubeState. Uses only generators of phase2. 
     * @param sol1 Solution of phase 1
     * @return Solution of pahse 2, null if not found or timeouted.
     */
    private Solution solve2(Solution sol1){
        Set<CubeState> closed2 = new HashSet<>();
        CubeState cs = sol1.cs;
        if(sol1.moves.size()>0){
            CubeState last = cs.times(generators.get(sol1.moves.get(sol1.moves.size()-1)).inv());
            if(heuristics1(last)==0) return null;
        }
        int phase1dist = sol1.dist;
        //System.out.println("phase 2: " + phase1dist);
        PriorityQueue<Solution> q = new PriorityQueue<>();
        Solution so = new Solution(cs);
        so.compute_h2();
        q.add(so);
        closed2.add(cs);
        while(!q.isEmpty()){
            if(System.currentTimeMillis()-startTime > timeout){
                return null;
            }
            Solution sol = q.poll();
            
            if(sol.est == 0){
                return sol;
            }
            
            for (int i = 0; i < generators.size(); i++) {
                if(i==6 || i==8 || i==9 || i==11 || i==12 || i==14 || i==15 || i==17) continue;
                CubeState cs2 = sol.cs.times(generators.get(i));
                if(!closed2.contains(cs2)){
                    closed2.add(cs2);
                    Solution sol2 = new Solution(cs2);
                    sol2.dist = sol.dist + 1;
                    sol2.moves = (ArrayList<Integer>)sol.moves.clone();
                    sol2.moves.add(i);
                    sol2.compute_h2();
                    if(sol2.est + sol2.dist + phase1dist <= maxDepth){
                        q.add(sol2);
                    }
                }
            }
        }
        return null;
    }
    
    /**
     * Struct used in A* search.
     */
    private class Solution implements Comparable{
        public CubeState cs;
        public ArrayList<Integer> moves;
        public int dist;
        public int est;
        
        /**
         * new Solution.
         * @param cs CubeSate at the end of moves
         */
        public Solution(CubeState cs){
            this.cs = cs;
            moves = new ArrayList<>();
            dist = 0;
            est = 0;
        };
        
        /**
         * Computes heuristics for phase 1 and stores it.
         */
        public void compute_h1(){
            est = heuristics1(cs);
        }
        
        /**
         * Computes heuristics for phase 2 and stores it.
         */
        public void compute_h2(){
            est = heuristics2(cs);
        }

        /**
         * comparation method for A* algoritm.
         * @param o obejct of rhs Solution.
         * @return comparation of the two Solution which should be expanded first in A*
         */
        @Override public int compareTo(Object o) {
            Solution s = (Solution)o;
            int f1 = dist + est;
            int f2 = s.dist + s.est;
            if(f1!=f2) return Integer.compare(f1,f2);
            return Integer.compare(est,s.est);
        }
        
        
    }
}
