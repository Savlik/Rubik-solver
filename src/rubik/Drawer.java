package rubik;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Represents JPanel where the cube is drawn.
 */
public class Drawer extends JPanel {
    private CubeState cs;
    private final int size = 35;
    private final double angle = Math.PI/5;
    private final double mult = 0.75;
    private final int pad = 20;
    private final Map<Character, Color> colors;
    private final int dxx, dxy, dyx, dyy, dzx, dzy;
    
    /**
     * Creates new Drawer, sets colors and dimensions of the cube.
     */
    public Drawer(){
        cs = new CubeState();
        colors = new HashMap<>();
        colors.put('U', Color.yellow);
        colors.put('L', Color.pink);
        colors.put('F', Color.green);
        colors.put('R', Color.red);
        colors.put('D', Color.white);
        colors.put('B', Color.blue);
        
        dxx = size;
        dxy = 0;
        dyx = 0;
        dyy = size;
        dzx = (int)(Math.sin(angle)*size*mult);
        dzy = -(int)(Math.cos(angle)*size*mult);
    }
    
    /**
     * Sets internal CubeState that should be drawn.
     * @param cs CubeState to be drawn
     */
    public void setCubeState(CubeState cs){
        this.cs = cs;
    }
    
    /**
     * Draws paralelogram
     * @param g Graphics where should be drawn
     * @param x x coord of left corner
     * @param y y coord of bottom corner
     * @param color character that represents color of paralelogram
     * @param face face where this paralelogram is. Determines skew.
     */
    private void paintPara(Graphics g, int x, int y, char color, char face){
        int[] xPoints = new int[4], yPoints = new int[4];
        xPoints[0] = x;
        yPoints[0] = y;
        if(face=='U' || face=='D'){
            xPoints[1] = x + dxx;
            yPoints[1] = y + dxy;
            xPoints[2] = xPoints[1] - dzx;
            yPoints[2] = yPoints[1] - dzy;
            xPoints[3] = xPoints[2] - dxx;
            yPoints[3] = yPoints[2] - dxy;
        }else if(face=='L' || face=='R'){
            xPoints[1] = x + dzx;
            yPoints[1] = y + dzy;
            xPoints[2] = xPoints[1] + dyx;
            yPoints[2] = yPoints[1] + dyy;
            xPoints[3] = xPoints[2] - dzx;
            yPoints[3] = yPoints[2] - dzy;
        }else{
            xPoints[1] = x + dxx;
            yPoints[1] = y + dxy;
            xPoints[2] = xPoints[1] + dyx;
            yPoints[2] = yPoints[1] + dyy;
            xPoints[3] = xPoints[2] - dxx;
            yPoints[3] = yPoints[2] - dxy;
        }
        g.setColor(colors.get(color));
        g.fillPolygon(xPoints, yPoints, 4);
        g.setColor(Color.black);
        g.drawPolygon(xPoints, yPoints, 4);
    }
    
    /**
     * Draws whole face of the cube.
     * @param g Graphics where should be drawn
     * @param x x coord the center of cube
     * @param y y coord the center of cube
     * @param faces represents colors of faces
     * @param face char representing which face to draw
     */
    private void paintFace(Graphics g,int x, int y, Map<Character, String> faces, char face){
        String s = faces.get(face);
        int dax=0, day=0, dbx=0, dby=0;
        if(face=='U'){
            x+=dzx*3;
            y+=dzy*3;
            dax = dxx;
            day = dxy;
            dbx = -dzx;
            dby = -dzy;
        }else if(face=='L'){
            x -= pad + dzx*3;
            x += dzx*2;
            y += dzy*2;
            dax = -dzx;
            day = -dzy;
            dbx = dyx;
            dby = dyy;
        }else if(face=='F'){
            dax = dxx;
            day = dxy;
            dbx = dyx;
            dby = dyy;
        }else if(face=='R'){
            x += dxx*3;
            dax = dzx;
            day = dzy;
            dbx = dyx;
            dby = dyy;
        }else if(face=='D'){
            x += dzx;
            y += dyy*3 + pad - dzy*2;
            dax = dxx;
            day = dxy;
            dbx = dzx;
            dby = dzy;
        }else if(face=='B'){
            y += dzy*3 - pad - dyy;
            x += dxx*3 + dzx*3 + pad;
            dax = dxx;
            day = dxy;
            dbx = -dyx;
            dby = -dyy;
        }
        for (int i = 0; i < 9; i++) {
            paintPara(g,x + i%3 * dax + i/3 * dbx,y + i%3 * day + i/3 * dby, s.charAt(i), face);
        }
    }
    
    
    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Map<Character, String> faces = Parser.getFaces(cs);
        for (Map.Entry<Character, String> entry : faces.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            paintFace(g,100,200,faces,key);   
        }
        
    }
}