package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

// GUI for inviting player
// ATTENTION
// for backends: please look for TODO tags to connect to server

public class InvitePlayerGUI extends JFrame implements ActionListener {

  private JPanel contentPane;
  JList inviteList, playerList;
  DefaultListModel players, invited;
  JButton buttonin, buttonout, submit;
  private Client client;
  private IRemoteGame remoteGame;

  /**
   * Create the frame.
   * 
   * @throws RemoteException
   */
  public InvitePlayerGUI(Client client, IRemoteGame remoteGame) throws RemoteException {

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

    JLabel lblNewLabel = new JLabel("Create New Game - Invite Players");
    lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
    lblNewLabel.setFont(new Font("Verdana", Font.PLAIN, 40));
    lblNewLabel.setForeground(Color.ORANGE);
    contentPane.add(lblNewLabel, BorderLayout.PAGE_START);

    JPanel inviteBox = createDualListBox();

    contentPane.add(inviteBox, BorderLayout.CENTER);

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    submit = new JButton("Submit");
    submit.addActionListener(this);
    bottomPanel.add(submit);
    contentPane.add(bottomPanel, BorderLayout.SOUTH);


  }

  public JPanel createDualListBox() throws RemoteException {
    // TODO temporary data will be swapped by real player list from backend
    ArrayList<IClient> clientList = client.getAllClientList();

    ArrayList<String> clientStringList = new ArrayList<>();
    int idx = 0;

    // choose the player(s), ex index 0 and 1, choose all
    for (IClient client : clientList) {
      clientStringList.add(client.getUniqueName());
    }

    String[] onlinePlayers = clientStringList.toArray(new String[0]);

    // String onlinePlayers[] = {"Milk", "Cheese", "Bread", "Butter", "Beans", "Soup", "Bacon",
    // "Chicken", "Curry Sauce", "Chocolate"};

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
      if (invited.isEmpty() != true) {
        // TODO submit user that is invited to backend
        ArrayList<String> clientPlayListName = null;
        for (int j = 0; j < players.getSize(); j++) {
          clientPlayListName.add((String) players.get(j));
        }

        try {
          client.createNewGame(remoteGame, clientPlayListName);
        } catch (RemoteException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }

        ClientBoard clientBoard = new ClientBoard(client);
        clientBoard.frmClient.setVisible(true);
      } else {
        JOptionPane.showMessageDialog(null, "invite list is empty, playing solo...");
        // TODO no one is invited show gameboard, playing alone
      }
    }

  }
}
