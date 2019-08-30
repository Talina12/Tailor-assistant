package Gella.Tailor_assistant.View;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Fon extends JPanel {
    Image min;
    
    public Fon(){
       super(null);
    	this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS) );//new BoxLayout(this,BoxLayout.Y_AXIS);
    	min = new ImageIcon("zigzag2.jpg").getImage();
        //setLayout(new BorderLayout());
    }
    
    @Override
    public void paintComponent(Graphics g){
         Graphics2D g2d=(Graphics2D)g;
         g2d.drawImage(min,0,0,this);
         }
    
        }