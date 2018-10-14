/* adapted from https://www.macs.hw.ac.uk/cs/java-swing-guidebook/?name=JList&page=3 */

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
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import backend.IRemoteGame;

public class PlayerListGUI extends JFrame implements ActionListener {

  private JPanel contentPane;
  JList inviteList, playerList;
  DefaultListModel players, invited;
  JButton buttonin, buttonout, submit, back;
  private IClient client;
  private IRemoteGame remoteGame;

  /**
   * Create the frame.
   * 
   * @throws RemoteException
   */
  public PlayerListGUI(Client client, IRemoteGame remoteGame, Boolean isInviteActive)
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

    if (isInviteActive) {
      lblNewLabel = new JLabel("Create New Game - Invite Players");
      lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
      lblNewLabel.setFont(new Font("Verdana", Font.PLAIN, 40));
      lblNewLabel.setForeground(Color.ORANGE);
      contentPane.add(lblNewLabel, BorderLayout.PAGE_START);

      JPanel inviteBox = createDualListBox();

      contentPane.add(inviteBox, BorderLayout.CENTER);

      bottomPanel = new JPanel();
      bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

      submit = new JButton("Start Game");
      submit.addActionListener(this);
      bottomPanel.add(submit);
    } else {
      lblNewLabel = new JLabel("List of Clients");
      lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
      lblNewLabel.setFont(new Font("Verdana", Font.PLAIN, 40));
      lblNewLabel.setForeground(Color.ORANGE);
      contentPane.add(lblNewLabel, BorderLayout.PAGE_START);

      JPanel singleListBox = createSingleListBox();

      contentPane.add(singleListBox, BorderLayout.CENTER);

      bottomPanel = new JPanel();
      bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    back = new JButton("Back to Lobby");
    back.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          dispose();
          Lobby lobby = new Lobby(client, remoteGame);
          lobby.setVisible(true);
        } catch (RemoteException e1) {
          JOptionPane.showMessageDialog(null, "Server is down try again later, exiting the game...",
              "Error", JOptionPane.PLAIN_MESSAGE);
          System.exit(0);
        }
      }
    });

    bottomPanel.add(back);
    contentPane.add(bottomPanel, BorderLayout.SOUTH);

    this.addWindowListener(new WindowListener() {
      @Override
      public void windowOpened(WindowEvent e) {}

      @Override
      public void windowClosed(WindowEvent e) {}

      @Override
      public void windowIconified(WindowEvent e) {}

      @Override
      public void windowDeiconified(WindowEvent e) {}

      @Override
      public void windowActivated(WindowEvent e) {}

      @Override
      public void windowDeactivated(WindowEvent e) {}

      @Override
      public void windowClosing(WindowEvent e) {
        try {
          client.exitGame();
        } catch (RemoteException e1) {
          JOptionPane.showMessageDialog(null, "Error: " + e1, "Error", JOptionPane.PLAIN_MESSAGE);
          System.exit(0);
        }
      }
    });
  }

  public JPanel createDualListBox() throws RemoteException {
    ArrayList<IClient> clientList = client.getAllClientList();

    ArrayList<String> clientStringList = new ArrayList<>();

    // choose the player(s), ex index 0 and 1, choose all
    for (IClient client : clientList) {
      if ((client.getUniqueName()).equals(this.client.getUniqueName()) != true) {
        clientStringList.add(client.getUniqueName());
      }

    }

    String[] onlinePlayers = clientStringList.toArray(new String[0]);

    players = new DefaultListModel();
    invited = new DefaultListModel();

    for (int i = 0; i < onlinePlayers.length; i++) {
      players.addElement(onlinePlayers[i]);
    }

    playerList = new JList(players);
    playerList.setVisibleRowCount(10);
    playerList.setFixedCellHeight(20);
    playerList.setFixedCellWidth(140);
    playerList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    JScrollPane PlayerScrollList = new JScrollPane(playerList);

    inviteList = new JList(invited);
    inviteList.setVisibleRowCount(10);
    inviteList.setFixedCellHeight(20);
    inviteList.setFixedCellWidth(140);
    inviteList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

    JScrollPane inviteScrollList = new JScrollPane(inviteList);

    JPanel buttonPanel = new JPanel();

    buttonin = new JButton("Invite >>");
    buttonin.addActionListener(this);
    buttonPanel.add(buttonin);

    buttonout = new JButton("<< Remove");
    buttonout.addActionListener(this);
    buttonPanel.add(buttonout);

    buttonPanel.setBackground(new Color(51, 102, 153));

    JPanel dualListBox = new JPanel();
    dualListBox.setLayout(new BoxLayout(dualListBox, BoxLayout.LINE_AXIS));

    dualListBox.add(Box.createRigidArea(new Dimension(10, 0)));
    dualListBox.add(PlayerScrollList);
    dualListBox.add(Box.createRigidArea(new Dimension(5, 0)));
    dualListBox.add(buttonPanel);
    dualListBox.add(Box.createRigidArea(new Dimension(5, 0)));
    dualListBox.add(inviteScrollList);
    dualListBox.add(Box.createRigidArea(new Dimension(10, 0)));

    return dualListBox;
  }

  public JPanel createSingleListBox() throws RemoteException {
    ArrayList<IClient> clientList = client.getAllClientList();

    ArrayList<String> clientStringList = new ArrayList<>();
    int idx = 0;

    // choose the player(s), ex index 0 and 1, choose all
    for (IClient client : clientList) {
      clientStringList.add(client.getUniqueName());
    }

    String[] onlinePlayers = clientStringList.toArray(new String[0]);

    players = new DefaultListModel();

    for (int i = 0; i < onlinePlayers.length; i++) {

      players.addElement(onlinePlayers[i]);

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

  @Override
  public void actionPerformed(ActionEvent e) {
    int i = 0;

    // When the 'in' button is pressed,
    // we take the indices and values of the selected items
    // and output them to an array.

    if (e.getSource() == buttonin) {
      int[] fromindex = playerList.getSelectedIndices();
      Object[] from = playerList.getSelectedValues();

      // Then, for each item in the array, we add them to
      // the other list.
      for (i = 0; i < from.length; i++) {
        invited.addElement(from[i]);
      }

      // Finally, we remove the items from the first list.
      // We must remove from the bottom, otherwise we try to
      // remove the wrong objects.
      for (i = (fromindex.length - 1); i >= 0; i--) {
        players.remove(fromindex[i]);
      }
    }

    // If the out button is pressed, we take the indices and values of
    // the selected items and output them to an array.
    else if (e.getSource() == buttonout) {
      Object[] to = inviteList.getSelectedValues();
      int[] toindex = inviteList.getSelectedIndices();

      // Then, for each item in the array, we add them to
      // the other list.
      for (i = 0; i < to.length; i++) {
        players.addElement(to[i]);
      }

      // Finally, we remove the items from the first list.
      // We must remove from the bottom, otherwise we try to
      // remove the wrong objects.
      for (i = (toindex.length - 1); i >= 0; i--) {
        invited.remove(toindex[i]);
      }
    } else if (e.getSource() == submit) {
      Boolean isGameRunning = false;
      try {
        isGameRunning = remoteGame.isGameRunning();
      } catch (RemoteException e3) {
        JOptionPane.showMessageDialog(null, "Server is down try again later, exiting the game...",
            "Error", JOptionPane.PLAIN_MESSAGE);
        System.exit(0);
      }
      if (!isGameRunning) {
        ArrayList<String> clientPlayListName = new ArrayList<String>();
        try {
          clientPlayListName.add(this.client.getUniqueName());
        } catch (RemoteException e2) {
          JOptionPane.showMessageDialog(null, "Error: " + e2, "Error", JOptionPane.PLAIN_MESSAGE);
          System.exit(0);
        }
        for (int j = 0; j < invited.getSize(); j++) {
          String invt = (String) invited.get(j);
          clientPlayListName.add(invt);
        }

        try {
          dispose();
          client.createNewGame(remoteGame, clientPlayListName);
        } catch (RemoteException e1) {
          JOptionPane.showMessageDialog(null, "Problem creating a game", "Error",
              JOptionPane.PLAIN_MESSAGE);
          System.exit(0);
        }
      } else {
        JOptionPane.showMessageDialog(null, "Another game is running please wait until it is done");
      }
    }

  }
}
