package Gella.Tailor_assistant.View;

import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;

public class MainWindow2 {

	private JFrame frame;
	private JPanel buttons ;
	private JPanel fields;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	public MainWindow2() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(20,20,screenSize.width-20, screenSize.height-20);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Fon fon = new Fon();
		frame.setContentPane(fon);
		fon.setLayout(null);
		
		buttons = new JPanel();
		buttons.setBounds(662, 5, 10, 10);
		frame.getContentPane().add(buttons);
		
		fields = new JPanel();
		fields.setBounds(677, 5, 10, 10);
		frame.getContentPane().add(fields);
	}

}
