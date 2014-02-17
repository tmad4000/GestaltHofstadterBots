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
    
    static String randStr="794833197237160393201957046873261871765839224966989508157349348072106764409534422627271973217151823069258939135322958975941117864554464474659882013509149245363361361261909556418799535880361108255300561467580171804426827916940071996208439111939444522584053500593120014693846277545417656494006285925332864180866505493709041861463104911885089706813603771755321643275424672614578571543656724455862311180497760149029355250683302585691154279964535478189015302178508652968419753432801303755484984352540978370360908391281219115195475209118088526658624015112932859770239372150277843275068997867209466466702103538255134979560705628783261512536279538838381499845503806427046774827544309211600473780091008233884627256550666029152820204008794888450956227981540673700245844429385220085905358366394273259894671459957249919216216948925976514611476661285846496643396328781632669372387319923219710969215682389399765500793190787948331972999182338075896809585350517200424288361863988193745758914983414038689410455655414957544296835827948331972";
    static void genRandStr(){
        for(int i=0;i<1023;i++)
            System.out.print((int)(Math.random()*10));
    }
    
    public IntentionalSnakes() throws InterruptedException {
        super("CrazySnakes");
        setSize(500, 500);
        sP = new SnakePanel();
        add(sP);
//        GestaltPanel=new GestaltPanel(sP)
//        add(gP);
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
                if (isOpenG("hit wall")) {
                    addGestalt(subconsciousLRChoice());
                    filter("hit wall").close();
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
//#debug                System.out.println(randStr.charAt(nd));
//#debug                System.out.println(nd);
//#debug                System.out.println(((int)randStr.charAt(nd%randStr.length())-(int)'0') );
                if (((int)randStr.charAt(nd%randStr.length())-(int)'0') % 2 == 0) {
                    
//                if (((int) Math.PI * (nd / 50)) % 2 == 0) {
                    o = 1;
                }
                nd++;
                //System.out.println(o);
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
        
        int time=0;

        UniverseObject[] uOs = new UniverseObject[w.length + 1];

        public Universe() {
            //w = new HWall(100);

            s = new WholeSnake(250, 60, 2); //snake goes down at first
            uOs[0] = s;
            
            for (int i = 1; i < uOs.length; i++) {
                uOs[i] = w[i-1];
            }
            
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
            
            time++;

            if(time>50)
            {
                s.x=250;
                s.y=60;

                s.dir=2;
                time=0;
                System.out.println("Restart");
                repaint();
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
