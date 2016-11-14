package rubik;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * Main class of project. Serves as form interface with the program.
 */
public class Rubik {
    private static CubeState original;
    private static Solver solver;
    private static JFrame main;
    private static Drawer drawer;
    private static ArrayList<Integer> solution;
    private static JButton animate_button, solve_button;
    private static JLabel label;
    private static javax.swing.Timer timer;
    
    private static CubeState cs;
    private static int frame;
    
    private static final ActionListener taskPerformer = (ActionEvent evt) -> {
        showFrame();
    };
    
    /**
     * Initializes the control form.
     */
    public static void init(){
        main = new JFrame("Rubik");
        main.setVisible(true);
        main.setSize(500,500);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container pane = main.getContentPane();
        
        drawer = new Drawer();
        pane.add(drawer, BorderLayout.CENTER);
        
        label = new JLabel();
        pane.add(label, BorderLayout.SOUTH);
        
        JPanel inner = new JPanel();
        inner.setLayout(new GridLayout(10, 1, 10, 0));

        JButton randomize_button = new JButton("randomize");
        randomize_button.addActionListener((ActionEvent e) -> {
            randomize();
            timer.stop();
        }); 
        inner.add(randomize_button);
        
        JButton file_button = new JButton("open from file");
        file_button.addActionListener((ActionEvent e) -> {
            timer.stop();
            JFileChooser openFile = new JFileChooser();
            int result = openFile.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                Map<Character, String> faces = new HashMap<>();
                try{
                    File selectedFile = openFile.getSelectedFile();
                    InputStream inputStream = new FileInputStream(selectedFile);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    faces.put('U', reader.readLine());
                    faces.put('L', reader.readLine());
                    faces.put('F', reader.readLine());
                    faces.put('R', reader.readLine());
                    faces.put('D', reader.readLine());
                    faces.put('B', reader.readLine());
                    
                    System.out.println(faces.toString());
                }catch(Exception exc){
                    System.out.println(exc.getStackTrace());
                }
                try{
                    original = Parser.parse(faces);
                    drawer.setCubeState(original);
                    drawer.repaint();
                }catch(Exception exc){
                    label.setText("Erorr: "+exc.getMessage());
                }
            }
        });
   
        inner.add(file_button);
        
        solve_button = new JButton("Solve");
        solve_button.addActionListener((ActionEvent e) -> {
            solve();
            timer.stop();
        }); 
        solve_button.setEnabled(false);
        inner.add(solve_button);
        
        animate_button = new JButton("Animate");
        animate_button.addActionListener((ActionEvent e) -> {
            cs = original;
            frame = 0;
            timer.start();
        }); 
        animate_button.setEnabled(false);
        
        inner.add(animate_button);
        pane.add(inner, BorderLayout.LINE_END);
        inner.repaint();
        pane.repaint();
        main.repaint();
        
        timer = new javax.swing.Timer(1000 ,taskPerformer);
    }
    
    /**
     * Main function of the program.
     * @param args none
     */
    public static void main(String[] args){
        init();
        main.repaint();
        
        original  = new CubeState();
        solver = new Solver();
        solution = null;
        
        solve_button.setEnabled(true);
    }
    
    /**
     * Tries to solve current CubeState.
     */
    private static void solve(){
        label.setText("");
        long startTime = System.currentTimeMillis();
        solution = solver.solve(original);
        long stopTime = System.currentTimeMillis();
        
        if(solution==null){
            animate_button.setEnabled(false);
            label.setText("Couldn't solve this cube. (Timeout "+(stopTime - startTime)+"ms)");
            //System.out.println("timeout ("+(stopTime - startTime)+"ms)");
        }else{
            animate_button.setEnabled(true);
            String s = "";
            s += "Solution length "+solution.size()+" found in "+(stopTime - startTime)+"ms: ";
            for (int i = 0; i < solution.size(); i++) {
                s += Solver.generators_name.get(solution.get(i))+" ";
            }
            System.out.println(s);
            label.setText(s);
        }
    }
    
    /**
     * Randomizes the CubeState.
     */
    private static void randomize(){
        label.setText("");
        original = CubeState.getRandom();
        drawer.setCubeState(original);
        drawer.repaint();
    }
    
    /**
     * Shows next frame of last solution.
     */
    private static void showFrame(){
        if(frame>=solution.size()){
            timer.stop();
            return;
        }
        cs = cs.times(Solver.generators.get(solution.get(frame)));
        drawer.setCubeState(cs);
        drawer.repaint();
        main.repaint();
        frame++;
    }
    
}
