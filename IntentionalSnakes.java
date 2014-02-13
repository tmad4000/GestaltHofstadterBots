/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GestaltHofstadterBots;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
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

        public WholeSnake(int x, int y, int dir) {
            //snake body stuff
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.v=3;
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
            System.out.println(dir);
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(Color.YELLOW);
            g.fillOval(x, y, 10, 10);
        }

        class SnakeMind extends GestaltBox {

            int nd = 0;

            public SnakeMind() {
                addGestalt(new Gestalt(0));
            }

            void processGestalts() {
                if (isOpenG("crossed wall bottom")) {
                    addGestalt(subconsciousLRChoice());
                    filter("crossed wall bottom").close();
                }

                //surveyGestalts
                int decision = 0;
                ArrayList<Gestalt> dirGs = openGs().filter("#dir").gestalts;
                for (Gestalt dirG : dirGs) {
                    decision += dirG.dir; //#hack like a man to double business bound I stand in pause and both neglect! #todo integrate Justify, dialectics
                    dirG.close();
                }

                dir = (dir+decision)%4;

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

    class HWall extends UniverseObject {

        public HWall(int y) {
            this.y = y;
        }

        //no internal updates to make for a wall!
        @Override
        public void next() {

        }

        @Override
        public void paint(Graphics g) {
            
            g.setColor(Color.BLACK);
            g.drawRect(0, y, 500, 14);
        }
    }

    abstract class UniverseObject extends GestaltBox {

        int x, y, dir, v; //dir is 0 1 2 3 clockwise from top

        public abstract void next();

        public abstract void paint(Graphics g);
    }

    class Universe {

        HWall w;
        WholeSnake s;

        UniverseObject[] uOs = new UniverseObject[2];

        public Universe() {
            w = new HWall(100);
            s = new WholeSnake(250, 60, 2); //snake goes down at first
            uOs[0] = w;
            uOs[1] = s;
        }

        void next() {
            
            for (UniverseObject o : uOs) {

                o.next();

                int dx = 0, dy = 0;
                switch (o.dir) {
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
                dx *= o.v;
                dy *= o.v;
                
                
                
                int nx = o.x + dx, ny = o.y + dy;
                
                //#hack (ish)
                if (o.y < w.y && ny > w.y) {
                    s.addGestalt(new Gestalt("crossed wall bottom"));
                    System.out.println("crossed wall bottom");
                }

                o.x = nx;
                o.y = ny;
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
