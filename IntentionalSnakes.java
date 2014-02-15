/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestaltHofstadterBots;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.JPanel;

/**
 *
 * @author Jacob-MTech
 */
public class IntentionalSnakes extends JFrame {

    SnakePanel sP;

    public IntentionalSnakes() throws InterruptedException {
        super("CrazySnakes");
        setSize(500, 500);
        sP = new SnakePanel();
        add(sP);

        //new JButton("restart")
        //add()
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        while (true) {
            Thread.sleep(30);
            sP.next();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        IntentionalSnakes s = new IntentionalSnakes();

        // TODO code application logic here
    }

    class Gestalt {

        int sts = 0; //4=closed
        int dir = 0;
        String msg;
        String type; //observation, desire

        public Gestalt(int dir) {
            this.dir = dir;
            this.msg = "#dir";
            this.type = "desire";
        }

        public Gestalt(String msg) {
            this.msg = msg;
            this.type = "observation";
        }

        boolean contains(String q) {
            return msg.indexOf(q) >= 0;
        }

        void close() {
            this.sts = 4;
        }
    }

    class GestaltBox {

        ArrayList<Gestalt> gestalts = new ArrayList<Gestalt>();

        public GestaltBox() {

        }

        public GestaltBox(ArrayList<Gestalt> gestalts) {
            this.gestalts = gestalts;
        }

        public void addGestalt(Gestalt g) {
            gestalts.add(g);
        }

        public GestaltBox openGs() {
            ArrayList<Gestalt> o = new ArrayList<Gestalt>();

            for (int i = 0; i < gestalts.size(); i++) {
                Gestalt g = gestalts.get(i);
                if (g.sts < 4) {
                    o.add(g);
                }
            }
            return new GestaltBox(o);
        }

        public GestaltBox filter(String q) {

            ArrayList<Gestalt> o = new ArrayList<Gestalt>();

            for (int i = 0; i < gestalts.size(); i++) {
                Gestalt g = gestalts.get(i);
                if (g.contains(q)) {
                    o.add(g);
                }
            }
            return new GestaltBox(o);
        }

        public boolean isOpenG(String s) {
            for (Gestalt g : openGs().gestalts) {
                if (s.equals(g.msg)) {
                    return true;
                }
            }
            return false;
        }

        //close all gestalts in this box. Useful in JQuery-like method chaining
        void close() {
            for (Gestalt g : openGs().gestalts) {
                g.close();
            }
        }
    }

    class WholeSnake extends UniverseObject {

        SnakeMind sm = new SnakeMind();
        
        int dispWidth=6;

        public WholeSnake(int x, int y, int dir) {
            //snake body stuff
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.v = 3;
            
            this.color=Color.YELLOW;
        }

        @Override
        public void addGestalt(Gestalt g) {
            sm.addGestalt(g);
        }

        @Override
        public GestaltBox openGs() {
            return sm.openGs();
        }

        @Override
        public void next() {
            sm.next();
//#debug            System.out.println(dir); 
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(this.color);
            
            g.fillOval(x-dispWidth/2, y-dispWidth/2, dispWidth, dispWidth);
        }

        class SnakeMind extends GestaltBox {

            int nd = 0;

            public SnakeMind() {
                addGestalt(new Gestalt(0));
            }

            void processGestalts() {
                if (isOpenG("hit wall bottom")) {
                    addGestalt(subconsciousLRChoice());
                    filter("hit wall bottom").close();
                }

                //surveyGestalts
                int decision = 0;
                ArrayList<Gestalt> dirGs = openGs().filter("#dir").gestalts;
                for (Gestalt dirG : dirGs) {
                    decision += dirG.dir; //#hack like a man to double business bound I stand in pause and both neglect! #todo integrate Justify, dialectics
                    dirG.close();
                }

                dir = (dir + decision) % 4;

            }

            void next() {

                //addGestalts();
                processGestalts();

            }

            private Gestalt subconsciousLRChoice() {
                int o = -1;
                if (((int) Math.PI * (nd / 50)) % 2 == 0) {
                    o = 1;
                }
                nd++;
                return new Gestalt(o);
            }
        }

    }

    class Wall extends Path {

        int len;

        public Wall(int x, int y, int dir, int len) {
            super(x, y, 0, 0, dir); //#hack
            this.len = len;
            this.x2 = x+dirXY()[0] * len;
            this.y2 = y+dirXY()[1] * len;
           
            this.color=Color.BLACK;
        }

    }

    class Path extends UniverseObject { //not really "UniverseObject" just want the methods

        int x2, y2;

        public Path(int x1, int y1, int x2, int y2, int dir) {
            this.x = x1;
            this.y = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.dir = dir;
            
            this.color=Color.GREEN;
        }

        //no internal updates to make for a path!
        @Override
        public void next() {
        }

        public boolean crosses(Path w) { //assuming rectilinear now
            //System.out.println("dir" + Arrays.toString(dirXY())+Arrays.toString(w.dirXY()));
           if (dirXY()[0] != 0) { //this is horiz path
                if (w.dirXY()[1] != 0) //vert wall
                {
                    if (w.x >= Math.min(this.x, this.x2) && w.x <= Math.max(this.x, this.x2)) {
                        if (this.y >= Math.min(w.y, w.y2) && this.y <= Math.max(w.y, w.y2)) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            else if (dirXY()[1] != 0) { //this is vert path
                if (w.dirXY()[0] != 0) //horiz wall
                {
                    if (w.y >= Math.min(this.y, this.y2) && w.y <= Math.max(this.y, this.y2)) {
                        if (this.x >= Math.min(w.x, w.x2) && this.x <= Math.max(w.x, w.x2)) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } 
           return true;
//           else {
//                throw new RuntimeException("Wall not rectilinear");
//            }
            
            
            // old: 
            //y=m1 x + b1
            // y=m2 x + b2
            // -(b1-b2)/(m1-m2) = x
        }

        public void paint(Graphics g) {
            g.setColor(this.color);
            g.drawLine(x, y, x2, y2);
        }
        
        public String toString() {
            return "(("+x+","+y+"),("+x2+","+y2+"))";
        }
    }

    abstract class UniverseObject extends GestaltBox {

        int x, y, dir, v; //dir is 0 1 2 3 clockwise from top
        Color color;

        public abstract void next();

        public abstract void paint(Graphics g);

        public String dirName() {
            switch (dir) {
                case 0:
                    return "UP";
                case 1:
                    return "RIGHT";
                case 2:
                    return "DOWN";
                case 3:
                    return "LEFT";
                default:
                    throw new RuntimeException("EXCEPTION: dir is not 0 to 3");
            }
        }

        public int[] dirXY() {
            int dx = 0, dy = 0;
            switch (dir) {
                case 0:
                    dx = 0;
                    dy = -1;
                    break;
                case 1:
                    dx = 1;
                    dy = 0;
                    break;
                case 2:
                    dx = 0;
                    dy = 1;
                    break;
                case 3:
                    dx = -1;
                    dy = 0;
                    break;
            }
            int[] o = {dx, dy};
            return o;
        }
    }

    class Universe {

        //HWall w;
        Wall[] w = {new Wall(200, 100, 1, 200), new Wall(280, 0, 2, 170), new Wall(320, 0, 2, 70)};
        WholeSnake s;

        UniverseObject[] uOs = new UniverseObject[w.length + 1];

        public Universe() {
            //w = new HWall(100);

            s = new WholeSnake(250, 60, 2); //snake goes down at first
            for (int i = 0; i < w.length; i++) {
                uOs[i] = w[i];
            }
            uOs[w.length] = s;
            
           // if(o instanceof Path)    
            Path np=new Path(200, 0, 200, 300, 2);
            System.out.print(w[0]+" "+np+ " ");
                        System.out.println(w[0].crosses(new Path(200, 0, 200, 300, 2)));
                        //System.out.println(w[0].crosses(w[1]));
        }

        void next() {

            for (UniverseObject o : uOs) {

                o.next();

                int dx = o.dirXY()[0], dy = o.dirXY()[1];

                dx *= o.v;
                dy *= o.v;

                int nx = o.x + dx, ny = o.y + dy;

                boolean hitWall = false;
                Path p = new Path(o.x, o.y, nx, ny, o.dir);
                
                //System.out.println(new Path(200, 0, 200, 400, 2).crosses(new Path(100, 100, 300, 100, 1)));
                for (Wall currW : w) {
                    
                    
//                     System.out.println(new Path(200, 0, 200, 400, 2).crosses(currW));
                    if (p.crosses(currW)) {
                       
                        hitWall = true;
                    }
                }

                if (hitWall) {
                    Gestalt g = new Gestalt("hit wall");
                    s.addGestalt(g);
                    System.out.println(g.msg);
                } else {
                    o.y = ny;
                    o.x = nx;
                }
            }
        }

        public void paint(Graphics g) {
            for (UniverseObject uO : uOs) {
                uO.paint(g);
            }
        }

    }

    class SnakePanel extends JPanel {

        Universe u = new Universe();

        public void paint(Graphics g) {
            u.paint(g);
        }

        public void next() {
            u.next();
            repaint();
        }
    }

}
