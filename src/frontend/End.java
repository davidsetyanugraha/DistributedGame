package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import backend.IRemoteGame;

public class End extends JFrame {

  private JPanel contentPane;
  JList inviteList, playerList;
  DefaultListModel players, invited;
  JButton buttonin, buttonout, submit, back;
  private Client client;
  private IRemoteGame remoteGame;
  private ArrayList<String> clientStringList;

  /**
   * Create the frame.
   * 
   * @throws RemoteException
   */
  public End(Client client, IRemoteGame remoteGame, Map<String, Integer> score_board)
      throws RemoteException {

    this.remoteGame = remoteGame;
    this.client = client;
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 800, 800);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setBackground(new Color(51, 102, 153));
    setContentPane(contentPane);
    contentPane.setOpaque(true);
    contentPane.setLayout(new BorderLayout(0, 0));

    JLabel lblNewLabel;
    JPanel bottomPanel;

    this.clientStringList = new ArrayList<>();

    // choose the player(s), ex index 0 and 1, choose all
    int rank = 1;
    String firstRankName = "";
    for (Map.Entry<String, Integer> entry : score_board.entrySet()) {
      clientStringList.add(rank + ") " + entry.getKey() + " | Score: " + entry.getValue());
      if (rank == 1)
        firstRankName = entry.getKey();
      rank = rank + 1;
    }

    lblNewLabel = new JLabel(firstRankName + " is the winner!");
    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
    lblNewLabel.setFont(new Font("Verdana", Font.PLAIN, 40));
    lblNewLabel.setForeground(Color.ORANGE);
    contentPane.add(lblNewLabel, BorderLayout.PAGE_START);

    JPanel singleListBox = createSingleListBox();

    contentPane.add(singleListBox, BorderLayout.CENTER);

    bottomPanel = new JPanel();
    bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

    back = new JButton("Back to Lobby");
    back.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          dispose();
          Lobby lobby = new Lobby(client, remoteGame);
          lobby.setVisible(true);
        } catch (RemoteException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });

    bottomPanel.add(back);
    contentPane.add(bottomPanel, BorderLayout.SOUTH);
    
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

  public JPanel createSingleListBox() throws RemoteException {

    String[] onlinePlayers = clientStringList.toArray(new String[0]);

    players = new DefaultListModel();

    for (int i = -1; i < onlinePlayers.length; i++) {
      if (i == -1) {
        players.addElement("SCOREBOARD");
      } else {
        players.addElement(onlinePlayers[i]);
      }
    }

    playerList = new JList(players);
    playerList.setVisibleRowCount(10);
    playerList.setFixedCellHeight(20);
    playerList.setFixedCellWidth(140);
    playerList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    JScrollPane PlayerScrollList = new JScrollPane(playerList);
    JPanel buttonPanel = new JPanel();

    buttonPanel.setBackground(new Color(51, 102, 153));

    JPanel singleListBox = new JPanel();
    singleListBox.setLayout(new BoxLayout(singleListBox, BoxLayout.LINE_AXIS));

    singleListBox.add(Box.createRigidArea(new Dimension(10, 0)));
    singleListBox.add(PlayerScrollList);

    return singleListBox;
  }
}
