package App;

import java.awt.EventQueue;
import java.text.*;
import javax.swing.*;
import javax.swing.text.*;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;

public class testwindow {

	private JFrame frame;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testwindow window = new testwindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public testwindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
		textField.setBounds(106, 79, 128, 33);
		((AbstractDocument) (textField.getDocument())).setDocumentFilter(new DocumentFilter() {
			@Override
			public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)throws BadLocationException
			{
			  if (Character.isLetter(string.charAt(0)))
					Toolkit.getDefaultToolkit().beep();
			  else
			     if (offset==3||offset==7||offset==10)
				    fb.insertString( offset, '-'+string, attr );
				 else 
					 if (offset<=12)
					  fb.insertString( offset, string, attr );
			}
			
			@Override
			public void replace(FilterBypass fb, int offset, int length, String string,AttributeSet attr) throws BadLocationException
			{
			  if (Character.isLetter(string.charAt(0)))
					Toolkit.getDefaultToolkit().beep();
			  else
				if (offset==3||offset==7||offset==10)
				    fb.insertString( offset, '-'+string, attr );
				else 
					if (offset<=12)
					fb.insertString( offset, string, attr );
					}
					} );
		frame.getContentPane().add(textField);
		textField.setColumns(10);
	}
}
