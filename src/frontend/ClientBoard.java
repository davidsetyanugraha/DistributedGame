package frontend;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ClientBoard {
  JFrame frmClient;
  private JFileChooser fileChooser;
  private JDialog popUpDialog;
  private JTextArea popUpMsg;
  private JButton yesBtn;
  private JButton noBtn;
  private UserListPanel userListPanel;
  private File openedFile = null;
  private int userChoice = -1;
  private JTextField tfMsg;
  private JTextArea taMsgHis;
  private JMenuBar menuBar;
  private int serverPort = 23333;
  private static Pane backPanel;
  private static IClient client;
  private static File file;
  private static Login login;
  private final static int VOTE_ACCEPT = 0;
  private static JMenu mnScore;
  private static JMenuItem displayScoreMenu;

  public ClientBoard(IClient client) {
    this.client = client;
    try {
      initialize();
    } catch (RemoteException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void destroyCurrentBoard() {
    this.frmClient.setVisible(false);
  }

  private void initialize() throws RemoteException {
    this.frmClient = new JFrame();
    this.frmClient
        .setTitle("Scrabble Game - DSCraftsman, PlayerName: " + this.client.getUniqueName());
    this.frmClient.setBounds(100, 100, 800, 800);
    this.frmClient.setDefaultCloseOperation(3);

    this.fileChooser = new JFileChooser();

    this.popUpDialog = new JDialog();
    this.popUpDialog.setMinimumSize(new Dimension(350, 175));
    this.popUpDialog.setModal(true);
    this.popUpDialog.setVisible(false);
    this.popUpMsg = new JTextArea();
    this.popUpMsg.setColumns(22);
    this.popUpMsg.setEditable(false);
    this.popUpMsg.setRows(3);

    this.yesBtn = new JButton();
    this.yesBtn.setText("yes");
    this.yesBtn.setSize(5, 2);
    this.yesBtn.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        ClientBoard.this.yesBtnClicked();
      }
    });
    this.noBtn = new JButton();
    this.noBtn.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        ClientBoard.this.noBtnClicked();
      }
    });
    this.noBtn.setSize(5, 2);
    this.noBtn.setText("no");

    GridBagLayout gridBagLayout = new GridBagLayout();
    gridBagLayout.columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 259, 52};
    gridBagLayout.rowHeights = new int[10];
    gridBagLayout.columnWeights =
        new double[] {1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D};
    gridBagLayout.rowWeights =
        new double[] {0.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 0.0D, 1.0D};
    this.frmClient.getContentPane().setLayout(gridBagLayout);

    JPanel topPanel = new JPanel();
    GridBagLayout topLayout = new GridBagLayout();
    topLayout.columnWeights = new double[] {};
    topPanel.setLayout(topLayout);
    GridBagConstraints gbc_topPanel = new GridBagConstraints();
    gbc_topPanel.insets = new Insets(0, 0, 5, 0);
    gbc_topPanel.anchor = 17;
    gbc_topPanel.gridwidth = 11;
    gbc_topPanel.fill = 1;
    gbc_topPanel.gridx = 0;
    gbc_topPanel.gridy = 0;
    this.frmClient.getContentPane().add(topPanel, gbc_topPanel);

    JToolBar toolBar = new JToolBar();
    toolBar.setFloatable(false);
    GridBagConstraints toolbarCon = new GridBagConstraints();
    toolbarCon.gridx = 0;
    toolbarCon.gridy = 0;
    toolbarCon.anchor = 17;

    // TODO Pane board
    this.backPanel = new Pane(this, this.client);

    // another window
    GridBagConstraints gbc_backPanel = new GridBagConstraints();
    gbc_backPanel.weighty = 7.0D;
    gbc_backPanel.weightx = 7.0D;
    gbc_backPanel.insets = new Insets(0, 0, 0, 5);
    gbc_backPanel.gridwidth = 22;
    gbc_backPanel.gridheight = 22;
    gbc_backPanel.fill = 1;
    gbc_backPanel.gridx = 0;
    gbc_backPanel.gridy = 1;

    this.frmClient.getContentPane().add(this.backPanel.getGui(), gbc_backPanel);

    this.menuBar = new JMenuBar();
    this.frmClient.setJMenuBar(this.menuBar);
    JMenu mnFile = new JMenu("option");
    this.menuBar.add(mnFile);

    JMenuItem closeMenu = new JMenuItem("Exit");
    closeMenu.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        try {
          client.exit();
        } catch (RemoteException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    });
    mnFile.add(closeMenu);

    this.mnScore = new JMenu("score");
    this.menuBar.add(mnScore);

    GridBagConstraints gbc_listPanel = new GridBagConstraints();
    gbc_listPanel.insets = new Insets(0, 0, 5, 0);
    gbc_listPanel.fill = 1;
    gbc_listPanel.gridwidth = 2;
    gbc_listPanel.gridheight = 3;
    gbc_listPanel.weighty = 3.0D;
    gbc_listPanel.weightx = 2.0D;
    gbc_listPanel.gridx = 9;
    gbc_listPanel.gridy = 1;

    JMenu mnPlayerList = new JMenu("players");
    this.menuBar.add(mnPlayerList);

    JMenuItem DisplayPlayerMenu = new JMenuItem("display players");
    DisplayPlayerMenu.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        JOptionPane.showOptionDialog(null, userListPanel, "Player List", JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE, null, null, null);
      }
    });

    mnPlayerList.add(DisplayPlayerMenu);

    // this.frmClient.getContentPane().add(this.userListPanel, gbc_listPanel);
  }



  public void closeMenuClicked(MouseEvent e) {
    // endNetClicked(e);
    this.frmClient.dispose();
    System.exit(0);
  }

  public void showFileExistDlg(File f) {
    double xpos = this.frmClient.getLocationOnScreen().getX()
        + this.frmClient.getSize().getWidth() / 2.0D - this.popUpDialog.getSize().getWidth() / 2.0D;
    double ypos =
        this.frmClient.getLocationOnScreen().getY() + this.frmClient.getSize().getHeight() / 2.0D
            - this.popUpDialog.getSize().getHeight() / 2.0D;
    this.popUpDialog.setLocation((int) xpos, (int) ypos);
    this.popUpMsg
        .setText("File : \"" + f.getName() + "\" already exists.\nDo you want to overwrite?");
    this.popUpDialog.setTitle("Overwrite Confirmation");
    GridBagConstraints msgCon = new GridBagConstraints();
    msgCon.gridx = 0;
    msgCon.gridy = 0;
    msgCon.gridwidth = 2;
    msgCon.gridheight = 1;
    GridBagConstraints yesBtnCon = new GridBagConstraints();
    yesBtnCon.gridx = 0;
    yesBtnCon.gridy = 1;

    yesBtnCon.fill = 0;
    GridBagConstraints noBtnCon = new GridBagConstraints();
    noBtnCon.gridx = 1;
    noBtnCon.gridy = 1;

    noBtnCon.fill = 0;
    GridBagLayout gridLayout = new GridBagLayout();
    this.popUpDialog.getContentPane().setLayout(gridLayout);
    this.popUpDialog.getContentPane().add(this.popUpMsg, msgCon);
    this.popUpDialog.getContentPane().add(this.yesBtn, yesBtnCon);
    this.popUpDialog.getContentPane().add(this.noBtn, noBtnCon);
    this.popUpDialog.setVisible(true);
  }

  public void yesBtnClicked() {
    this.userChoice = 1;
    this.popUpDialog.setVisible(false);
  }

  public void noBtnClicked() {
    this.userChoice = 0;
    this.popUpDialog.setVisible(false);
  }

  public Pane getPane() {
    return this.backPanel;
  }

  public void setPane(Pane backPanel) {
    this.backPanel = backPanel;
  }

  public JTextArea getTaMsgHis() {
    return this.taMsgHis;
  }

  public void setTaMsgHis(JTextArea taMsgHis) {
    this.taMsgHis = taMsgHis;
  }

  public JMenuBar getMenuBar() {
    return this.menuBar;
  }

  public void setMenuBar(JMenuBar menuBar) {
    this.menuBar = menuBar;
  }

  public JFrame getFrmClient() {
    return this.frmClient;
  }

  public UserListPanel getUserListPanel() {
    return this.userListPanel;
  }

  public void setUserListPanel(UserListPanel userListPanel) {
    this.userListPanel = userListPanel;
  }

  // Reading JSON data.
  public static void renderBasedOnJson(String json, int state, ArrayList<String> votingWords)
      throws JSONException {
    JSONObject data = new JSONObject(json);

    // render words
    // getting words that are available
    JSONArray wordsAvail = (JSONArray) data.get("word");

    for (int i = 0; i < wordsAvail.length(); i++) {
      JSONObject word = wordsAvail.getJSONObject(i);
      int x = word.getInt("x");
      int y = word.getInt("y");
      String ch = word.getString("ch");
      backPanel.setChar(x, y, ch);
    }

    // render player
    // getting players that are available

    JSONArray playerAvail = (JSONArray) data.get("player");
    backPanel.updateScoreBoard(playerAvail);
    renderDisplayScoreMenu(playerAvail);

    if (state == client.STATE_WAIT) {
      // disable buttons
      backPanel.renderMessage("Wait! It's other turn..");
      backPanel.isVoteAndPassShown(false);
      backPanel.hideVotingYesAndNoVote();
    } else if (state == client.STATE_INSERTION) {
      // enable buttons
      backPanel.renderMessage("It's your turn.. insert word, then vote or pass");
      backPanel.isVoteAndPassShown(true);
      backPanel.hideVotingYesAndNoVote();
    } else if (state == client.STATE_VOTING_SHOW) {
      // wait for voting, show buttons
      backPanel.renderMessage("Let's Vote! ");
      backPanel.isVoteAndPassShown(false);
      backPanel.renderVotingMessage(votingWords);
    } else if (state == client.STATE_VOTING_WAIT) {
      // wait for voting
      backPanel.renderMessage("Please wait other votes.. ");
      backPanel.isVoteAndPassShown(false);
      backPanel.hideVotingYesAndNoVote();
    } else if (state == client.STATE_END) {
      backPanel.renderMessage("GAME END!");
      backPanel.isVoteAndPassShown(false);
      backPanel.hideVotingYesAndNoVote();
    }
  }

  private static void renderDisplayScoreMenu(JSONArray playerAvail) {
    mnScore.removeAll();

    displayScoreMenu = new JMenuItem("display scoreboard");
    displayScoreMenu.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        JOptionPane.showMessageDialog(null, "Scores: " + backPanel.getScoreMessage());
      }
    });

    mnScore.add(displayScoreMenu);
  }
}
