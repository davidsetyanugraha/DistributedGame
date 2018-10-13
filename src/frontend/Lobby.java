package frontend;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import backend.IRemoteGame;

public class Lobby extends JFrame {

  private JPanel contentPane;
  private Client client;
  
  public Lobby(Client client, IRemoteGame remoteGame) throws RemoteException {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 800, 800);
    this.setTitle("Welcome back " + client.getUniqueName());
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setBackground(new Color(51, 102, 153));
    setContentPane(contentPane);
    SpringLayout sl_contentPane = new SpringLayout();
    contentPane.setLayout(sl_contentPane);

    JButton btnCreateNewGame = new JButton("Create New Game");
    sl_contentPane.putConstraint(SpringLayout.WEST, btnCreateNewGame, 255, SpringLayout.WEST,
        contentPane);
    sl_contentPane.putConstraint(SpringLayout.SOUTH, btnCreateNewGame, -466, SpringLayout.SOUTH,
        contentPane);
    sl_contentPane.putConstraint(SpringLayout.EAST, btnCreateNewGame, -244, SpringLayout.EAST,
        contentPane);
    if (client.isGameRunning()) {
      btnCreateNewGame.setEnabled(false);
    } else {
      btnCreateNewGame.setEnabled(true);
      btnCreateNewGame.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          // Showing the board
          dispose();
          try {
            Boolean isInviteActive = true;
            PlayerListGUI playerListGui = new PlayerListGUI(client, remoteGame, isInviteActive);
            playerListGui.setVisible(true);
          } catch (RemoteException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
          }
        }
      });
    }
    contentPane.add(btnCreateNewGame);

    JButton btnPlayerList = new JButton("Player List");
    sl_contentPane.putConstraint(SpringLayout.NORTH, btnPlayerList, 76, SpringLayout.SOUTH,
        btnCreateNewGame);
    sl_contentPane.putConstraint(SpringLayout.WEST, btnPlayerList, 275, SpringLayout.WEST,
        contentPane);
    sl_contentPane.putConstraint(SpringLayout.SOUTH, btnPlayerList, -330, SpringLayout.SOUTH,
        contentPane);
    sl_contentPane.putConstraint(SpringLayout.EAST, btnPlayerList, -254, SpringLayout.EAST,
        contentPane);
    btnPlayerList.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        try {
          Boolean isInviteActive = false;
          PlayerListGUI playerListGui = new PlayerListGUI(client, remoteGame, isInviteActive);
          playerListGui.setVisible(true);
        } catch (RemoteException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

        dispose();
      }
    });
    contentPane.add(btnPlayerList);

    JLabel lblUsername = new JLabel("SCRABBLE ONLINE");
    sl_contentPane.putConstraint(SpringLayout.NORTH, btnCreateNewGame, 108, SpringLayout.SOUTH,
        lblUsername);
    sl_contentPane.putConstraint(SpringLayout.NORTH, lblUsername, 49, SpringLayout.NORTH,
        contentPane);
    sl_contentPane.putConstraint(SpringLayout.WEST, lblUsername, 219, SpringLayout.WEST,
        contentPane);
    sl_contentPane.putConstraint(SpringLayout.EAST, lblUsername, -213, SpringLayout.EAST,
        contentPane);
    lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
    lblUsername.setFont(new Font("Verdana", Font.PLAIN, 26));
    lblUsername.setForeground(Color.ORANGE);
    contentPane.add(lblUsername);
    
    this.addWindowListener(new WindowListener() {
		@Override
		public void windowOpened(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosed(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowIconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeiconified(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowActivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowDeactivated(WindowEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void windowClosing(WindowEvent e) {
			// TODO Auto-generated method stub
			try {
				client.exitGame();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
    });
  }
 

}
