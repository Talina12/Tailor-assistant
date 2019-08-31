package Gella.Tailor_assistant.View;

import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;

public class MainWindow2 {

	private JFrame frame;
	private Box buttons ;
	private JPanel fields;
	private JPanel info;
	private JButton newOrderButton;
	private JButton updateOrderButton;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	Border border;
	Fon fon;
	
	public MainWindow2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(20,10,screenSize.width-40, screenSize.height-60);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fon = new Fon();
		frame.setContentPane(fon);
		
		border = BorderFactory.createEtchedBorder();
		
		buttons = new Box(BoxLayout.X_AXIS);
		buttons.setMinimumSize(new Dimension((int) (screenSize.width/1.5),screenSize.height/10 ));
		buttons.setMaximumSize(new Dimension((int) (screenSize.width/1.5),screenSize.height/10 ));
		//buttons.setBorder(border);
		//buttons.setBackground(new Color(0,0,0,0));
		newOrderButton = new JButton("New order");
		updateOrderButton = new JButton("Update order");
		setButton(newOrderButton,buttons);
		setButton(updateOrderButton, buttons);
		frame.getContentPane().add(buttons);
		fields = new JPanel();
		fields.setPreferredSize(new Dimension(500,30 ));
		fields.setMaximumSize(new Dimension(500,70 ));
	//	frame.getContentPane().add(fields);
		
		info = new JPanel();
		info.setPreferredSize(new Dimension(500,30 ));
		info.setMaximumSize(new Dimension(500,70 ));
	//	frame.getContentPane().add(info);
	}

	public JFrame getFrame() {
		return frame;
	}

	private void setButton(JButton b,Box panel) {
		b.setFont(new Font("Tahoma", Font.PLAIN, 14));
		//b.setPreferredSize(new Dimension(screenSize.width/10,screenSize.height/9 ));
	    panel.add(Box.createHorizontalGlue());
		b.setMinimumSize(new Dimension(screenSize.width/9,screenSize.height/9 ));
		b.setMaximumSize(new Dimension(screenSize.width/9,screenSize.height/9 ));
		b.setPreferredSize(new Dimension(screenSize.width/9,screenSize.height/9 ));
		panel.add(b);
		panel.add(Box.createHorizontalGlue());
		
	}
}
