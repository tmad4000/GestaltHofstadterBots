/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package GestaltHofstadterBots;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.JPanel;

/**
 *
 * @author Jacob-MTech
 */
public class IntentionalSnakes_b2 extends JFrame {
    SnakePanel sP;
    public IntentionalSnakes_b2() throws InterruptedException {
        super("CrazySnakes");
        setSize(500, 500);
        sP=new SnakePanel();
        add(sP);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);

        while(true){
            Thread.sleep(30);
            sP.next();
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        IntentionalSnakes_b2 s = new IntentionalSnakes_b2();
        
        // TODO code application logic here
    }
    
    class Gestalt {
        int sts=0; //4=closed
        int dir=0;
        public Gestalt(int dir) {
            this.dir=dir;
        }
    }
     
    class GestaltBox {
        ArrayList<Gestalt> gestalts=new ArrayList<Gestalt>();
        
        public GestaltBox() {
            
        }
        
        public void add(Gestalt g) {
            gestalts.add(g);
        }
        
        public ArrayList<Gestalt> openGs() {
             ArrayList<Gestalt> o=new ArrayList<Gestalt>();
             
            for(int i=0;i<gestalts.size();i++) {
                Gestalt g = gestalts.get(i);
                if(g.sts<4)
                    o.add(g);
            }
            return o;
        }
    }
    
    class WholeSnake extends UniverseObject {
        SnakeBody sb=new SnakeBody();
        SnakeMind sm=new SnakeMind();
        
        public WholeSnake(int x,int y,int dir) {
            sb.x=x;
            sb.y=y;
            sb.dir=dir;
        }
        
        //dir is 0 1 2 3 clockwise from top
        class SnakeBody {
             int x,y,dir;
         }

         class SnakeMind {
             int x=10,y=10;
             //int[] r={250,250,0,0}; //x,y,dx,dy,r,g,b,dr,dg,db
             int[] dr={2,0,0,0,0}; //dx,dy,dr,dg,db
             //int[] drp={0,0,0,0,0}; //dx,dy,dr,dg,db
             int[] r={250,250,128,128,128}; //x,y,r,g,b

             GestaltBox gb=new GestaltBox();
             int nd=0;

             public Snake() {
                 gestalts.add(new Gestalt(0));
             }

             void processGestalts() {
                 //surveyGestalts
                 int decision=0;
                 for(int i=0;i<gestalts.size();i++) {
                     Gestalt g = gestalts.get(i);
                     if()

                     decision+=gestalts.get(i.dir);
                 }
             }

             void next() {

                 addGestalts();
                 processGestalts();

                 if(((int)Math.PI*(nd/50))%2==0) {
                     gestalts.add(new Gestalt(1));
                 }
                 else {
                     gestalts.add(new Gestalt(1));
                 }
                 nd++;
             }
         }
         
     }

    class HWall extends UniverseObject {
        int yCoord;
        public HWall(int yCoord) {
            this.yCoord=yCoord;
        }
    }
    
    abstract class UniverseObject {
        int x, y, dir,v;
//public void animateNext();
    }
    class Universe {
        
        HWall w;
        WholeSnake s;
        
        UniverseObject[] u=new UniverseObject[2];
        
        public Universe() {
            w=new HWall(600);
            s=new WholeSnake(500,300,2);
            u[0]=w;
            u[1]=s;
        }
        void next() {
            for(UniverseObject o:u) {
                
                int dx=0,dy=0;
                switch(o.dir) {
                    case 0:
                        dx=0;
                        dy=-1;
                        break;
                    case 1:
                        dx=1;
                        dy=0;
                        break;
                    case 2:
                        dx=0;
                        dy=1;
                        break;
                    case 3:
                        dx=-1;
                        dy=0;
                        break;
                }
                dx*=o.v;
                dy*=o.v;
                
                
                int nx=o.x+dx,ny=o.y+dy;
                for(UniverseObject o2:u) {
                    if()
                }
                
                o.x=nx;
                o.y+=ny;
                
            }
        }
        
        
    }
    
    class SnakePanel extends JPanel {
        int x=10,y=10;
        //int[] r={250,250,0,0}; //x,y,dx,dy,r,g,b,dr,dg,db
        int[] dr={0,0,0,0,0}; //dx,dy,dr,dg,db
        //int[] drp={0,0,0,0,0}; //dx,dy,dr,dg,db
        int[] r={250,250,128,128,128}; //x,y,r,g,b
        int[] b={this.getWidth(),this.getHeight(),255,255,255}; //x,y,r,g,b
        
        public void paint(Graphics g) {
            g.setColor(new Color(r[2],r[3],r[4]));
            //g.setColor(Color.yellow);
            g.fillOval(r[0],r[1],10, 10);
        }
        
        
        void nV() {
            
            for(int i=0;i<dr.length;i++) {
                
                
//                r[i]+=dr[i];
                r[0]+=1.5*Math.sin(dr[0]);
                r[1]+=1.5*Math.cos(dr[0]);
        
                if(r[i]<0||r[i]>b[i]) {
                    dr[i]*=-1;
                    
                    i--;
                }
                else {
                    dr[i]+=Math.round(3*(Math.random()-.5));
                    //dr[i]+=(Math.round(3*Math.sin(Math.random()-.5)));
//                    if(i%2==0)
//                        dr[i]+=(Math.round(3*Math.sin(2*Math.PI*Math.random()-.5)));
//                    else
//                        dr[i]+=(Math.round(3*Math.cos(2*Math.PI*Math.random()-.5)));
                    
                }
                
            }

        }
        
        public void next() {
            b[0]=this.getWidth();
            b[1]=this.getHeight();
            nV();
//            
//            x+=2+3*(Math.random()-.5);
//            y+=3*(Math.random()-.5);
//            if(x<0)
//                x=0;
//            if(y<0)
//                y=0;
            
            //(int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256)
            /*
            r[2]+=Math.round(3*(Math.random()-.5));
            r[3]+=Math.round(3*(Math.random()-.5));
            //System.out.println(r[2]);
            r[0]+=r[2];
            r[1]+=r[3];
            
            if(r[0]<0||r[0]>getWidth())
                r[2]*=-1;
            if(r[1]<0||r[1]>getWidth())
                r[3]*=-1;
            */
            repaint();
            
            //repaint();
        }
        
    }   
    
}
