package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Lobby extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Lobby frame = new Lobby();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Lobby() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(new Color(51, 102, 153));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JButton btnCreateNewGame = new JButton("Create New Game");
		sl_contentPane.putConstraint(SpringLayout.WEST, btnCreateNewGame, 255, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnCreateNewGame, -466, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnCreateNewGame, -244, SpringLayout.EAST, contentPane);
		btnCreateNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO check if game exists
			}
		});
		contentPane.add(btnCreateNewGame);
		
		JButton btnPlayerList = new JButton("Player List");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnPlayerList, 76, SpringLayout.SOUTH, btnCreateNewGame);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnPlayerList, 275, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnPlayerList, -330, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnPlayerList, -254, SpringLayout.EAST, contentPane);
		btnPlayerList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO show current player in lobby
			}
		});
		contentPane.add(btnPlayerList);
		
		JLabel lblUsername = new JLabel("SCRABBLE ONLINE");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnCreateNewGame, 108, SpringLayout.SOUTH, lblUsername);
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblUsername, 49, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblUsername, 219, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblUsername, -213, SpringLayout.EAST, contentPane);
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
	    lblUsername.setFont(new Font("Verdana", Font.PLAIN, 26));
	    lblUsername.setForeground(Color.ORANGE);
		contentPane.add(lblUsername);
	}
}
