package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import backend.IRemoteGame;

import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;

public class Login2 extends JFrame {

	private JPanel contentPane;
	private JTextField userNameArea;
	private JPasswordField passwordArea;
	

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public Login2(IRemoteGame remoteGame) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JLabel Header = new JLabel("SCRABBLE ONLINE");
		sl_contentPane.putConstraint(SpringLayout.NORTH, Header, 53, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, Header, -37, SpringLayout.EAST, contentPane);
		Header.setForeground(Color.ORANGE);
	    Header.setBounds(0, 42, 350, 40);
		Header.setFont(new Font("Arial", Font.BOLD, 70));
		contentPane.setBackground(new Color(51, 102, 153));
		contentPane.add(Header);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setForeground(Color.WHITE);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblUsername, -506, SpringLayout.EAST, contentPane);
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 40));
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setForeground(Color.WHITE);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblPassword, 332, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblUsername, -31, SpringLayout.NORTH, lblPassword);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblPassword, 0, SpringLayout.WEST, lblUsername);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 40));
		contentPane.add(lblPassword);
		
		userNameArea = new JTextField();
		userNameArea.setFont(new Font("Tahoma", Font.PLAIN, 40));
		sl_contentPane.putConstraint(SpringLayout.NORTH, userNameArea, 7, SpringLayout.NORTH, lblUsername);
		sl_contentPane.putConstraint(SpringLayout.WEST, userNameArea, 55, SpringLayout.EAST, lblUsername);
		sl_contentPane.putConstraint(SpringLayout.EAST, userNameArea, -85, SpringLayout.EAST, contentPane);
		contentPane.add(userNameArea);
		userNameArea.setColumns(10);
		
		passwordArea = new JPasswordField();
		passwordArea.setFont(new Font("Tahoma", Font.PLAIN, 40));
		sl_contentPane.putConstraint(SpringLayout.NORTH, passwordArea, 7, SpringLayout.NORTH, lblPassword);
		sl_contentPane.putConstraint(SpringLayout.WEST, passwordArea, 0, SpringLayout.WEST, userNameArea);
		sl_contentPane.putConstraint(SpringLayout.EAST, passwordArea, -85, SpringLayout.EAST, contentPane);
		contentPane.add(passwordArea);
		passwordArea.setColumns(10);
		
		JButton loginBtn = new JButton("Login");
		loginBtn.addMouseListener(new MouseAdapter() {
		      @Override
		      public void mouseClicked(MouseEvent e) {
		        // when button login is pressed
		        try {
		          // when button login is pressed, check the username and password
		          // client can join the remoteGame if the validation is okay.
		          if (remoteGame.isLoginValid(userNameArea.getText())) {
		            Client client = new Client(userNameArea.getText());
		            client.joinClientList(remoteGame);
		            // dispose login frame
		            dispose();

		            Lobby lobby = new Lobby(client, remoteGame);
		            lobby.setVisible(true);
		          } else {
		            // TODO add error windows
		            JOptionPane.showMessageDialog(null, "Username not valid. Please retype again", "Error",
		                JOptionPane.PLAIN_MESSAGE);
		          }

		        } catch (RemoteException e1) {
		          e1.printStackTrace();
		        }
		      }
		    });
		sl_contentPane.putConstraint(SpringLayout.SOUTH, loginBtn, -186, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, loginBtn, -445, SpringLayout.EAST, contentPane);
		loginBtn.setFont(new Font("Tahoma", Font.PLAIN, 40));
		contentPane.add(loginBtn);
		
		JLabel register = new JLabel("Click here to create new account");
		register.addMouseListener(new MouseAdapter() {
		      @Override
		      public void mouseClicked(MouseEvent e) {
		        Register regis = new Register(remoteGame);
		        regis.setVisible(true);
		        regis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		      }
		    });
		register.setForeground(Color.WHITE);
		sl_contentPane.putConstraint(SpringLayout.NORTH, register, 47, SpringLayout.SOUTH, loginBtn);
		sl_contentPane.putConstraint(SpringLayout.WEST, register, 92, SpringLayout.WEST, contentPane);
		register.setFont(new Font("Tahoma", Font.PLAIN, 40));
		contentPane.add(register);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addMouseListener(new MouseAdapter() {
		      @Override
		      public void mouseClicked(MouseEvent e) {
		        System.exit(0);
		      }
		    });
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnCancel, 0, SpringLayout.NORTH, loginBtn);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnCancel, 99, SpringLayout.EAST, loginBtn);
		btnCancel.setFont(new Font("Arial", Font.PLAIN, 40));
		contentPane.add(btnCancel);
	}
}
