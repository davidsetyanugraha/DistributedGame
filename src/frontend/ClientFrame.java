package frontend;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;
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
import javax.swing.border.TitledBorder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import backend.IRemoteGame;

public class ClientFrame {
  private JFrame frmClient;
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
  private User user = null;
  private ArrayList<User> userList;
  private int serverPort = 23333;
  private static Pane backPanel;
  private static Client client;
  private static File file;

  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Registry registry = LocateRegistry.getRegistry("localhost");
          IRemoteGame remoteGame = (IRemoteGame) registry.lookup("GameServer");

          /** Create Random Name */
          Random r = new Random(); // just create one and keep it around
          String alphabet = "abcdefghijklmnopqrstuvwxyz";

          final int N = 10;
          StringBuilder sb = new StringBuilder();
          for (int i = 0; i < N; i++) {
            sb.append(alphabet.charAt(r.nextInt(alphabet.length())));
          }

          // TODO NAME MUST UNIQUE!
          String randomName = sb.toString();
          /** End Create Random Name */

          client = new Client(randomName);
          client.joinGame(remoteGame);

          ClientFrame clientFrame = new ClientFrame(client);
          clientFrame.frmClient.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  public ClientFrame(Client user) {
    // try {
    initialize();

    /** TODO MOVE THIS read json IMP */

    // file = new File(
    // "/Users/davidsetyanugraha/Master/Semester1/DistributedSystem/Assignment/Assignment2/SourceCode/DistributedSystemProject2/src/example-get.json");
    // readFile();
    // } catch (FileNotFoundException | JSONException e) {
    // e.printStackTrace();
    // }
  }

  private void initialize() {
    this.frmClient = new JFrame();
    this.frmClient.setTitle("Scrabble Game - DSCraftsman");
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
        ClientFrame.this.yesBtnClicked();
      }
    });
    this.noBtn = new JButton();
    this.noBtn.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        ClientFrame.this.noBtnClicked();
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
    JMenu mnFile = new JMenu("file");
    this.menuBar.add(mnFile);

    JMenuItem newMenu = new JMenuItem("New");
    newMenu.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        // ClientFrame.this.newMenuClicked(e);
      }
    });
    mnFile.add(newMenu);

    JMenuItem openMenu = new JMenuItem("Open");
    openMenu.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        // ClientFrame.this.openMenuClicked(e);
      }
    });
    mnFile.add(openMenu);

    JMenuItem saveMenu = new JMenuItem("Save");
    saveMenu.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        // ClientFrame.this.saveMenuClicked(e);
      }
    });
    mnFile.add(saveMenu);

    JMenuItem saveAsMenu = new JMenuItem("Save as");
    saveAsMenu.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        // ClientFrame.this.saveAsMenuClicked(e);
      }
    });
    mnFile.add(saveAsMenu);

    JMenuItem closeMenu = new JMenuItem("Close");
    closeMenu.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        ClientFrame.this.closeMenuClicked(e);
      }
    });
    mnFile.add(closeMenu);

    JMenu mnNetwork = new JMenu("network");
    this.menuBar.add(mnNetwork);

    JMenuItem endNetMenu = new JMenuItem("disconnect from network");
    endNetMenu.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        // ClientFrame.this.endNetClicked(e);
      }
    });

    JMenuItem createMenu = new JMenuItem("create a new game network");
    createMenu.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        ClientFrame clientFrame = new ClientFrame(client);
        Pane game = new Pane(clientFrame, client);
        game.setVisible(true);
      }
    });
    mnNetwork.add(createMenu);

    JMenuItem connectNet = new JMenuItem("connect network~");
    mnNetwork.add(connectNet);
    mnNetwork.add(endNetMenu);

    JMenu mnScore = new JMenu("score");
    this.menuBar.add(mnScore);

    JMenuItem DisplayScoreMenu = new JMenuItem("display scoreboard");
    DisplayScoreMenu.addMouseListener(new MouseAdapter() {
      public void mouseReleased(MouseEvent e) {
        JOptionPane.showMessageDialog(null, "Your Score: " + backPanel.getScore());
      }
    });

    mnScore.add(DisplayScoreMenu);

    this.userListPanel = new UserListPanel(this);
    try {
      this.userListPanel.addUser(client.getUniqueName());
    } catch (RemoteException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    this.userListPanel.setBorder(new TitledBorder(null, "Player List", 4, 2, null, null));

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

    /*
     * this.taMsgHis = new JTextArea(""); GridBagConstraints gbc_taMsgHis = new
     * GridBagConstraints(); gbc_taMsgHis.insets = new Insets(0, 0, 5, 5); gbc_taMsgHis.gridx = 8;
     * gbc_taMsgHis.gridy = 4; frmClient.getContentPane().add(taMsgHis, gbc_taMsgHis);
     * this.taMsgHis.setEditable(false); JScrollPane msgPane = new JScrollPane();
     * msgPane.setViewportBorder(new TitledBorder(null, "Message History", 4, 2, null, null));
     * GridBagConstraints gbc_msgPane = new GridBagConstraints(); gbc_msgPane.insets = new Insets(0,
     * 0, 5, 0); gbc_msgPane.weighty = 5.0D; gbc_msgPane.weightx = 1.5D; gbc_msgPane.gridwidth = 2;
     * gbc_msgPane.gridheight = 5; gbc_msgPane.fill = 1; gbc_msgPane.gridx = 9; gbc_msgPane.gridy =
     * 4;
     * 
     * this.frmClient.getContentPane().add(msgPane, gbc_msgPane);
     * 
     * this.tfMsg = new JTextField(); GridBagConstraints gbc_tfMsg = new GridBagConstraints();
     * gbc_tfMsg.insets = new Insets(0, 0, 0, 5); gbc_tfMsg.weightx = 1.3D; gbc_tfMsg.weighty =
     * 1.0D; gbc_tfMsg.fill = 2; gbc_tfMsg.gridx = 9; gbc_tfMsg.gridy = 9;
     * 
     * this.tfMsg.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent
     * e) { // ClientFrame.this.sendMsgClicked(); } });
     * this.frmClient.getContentPane().add(this.tfMsg, gbc_tfMsg); this.tfMsg.setColumns(10);
     * 
     * JButton btnSendMsg = new JButton("send"); GridBagConstraints gbc_btnSendMsg = new
     * GridBagConstraints(); gbc_btnSendMsg.weighty = 1.0D; gbc_btnSendMsg.weightx = 0.5D;
     * gbc_btnSendMsg.gridx = 10; gbc_btnSendMsg.gridy = 9; btnSendMsg.addActionListener(new
     * ActionListener() { public void actionPerformed(ActionEvent e) { //
     * ClientFrame.this.sendMsgClicked(); } }); this.frmClient.getContentPane().add(btnSendMsg,
     * gbc_btnSendMsg);
     */
    this.userList = new ArrayList();
  }

  /**
   * start new game public void newMenuClicked(MouseEvent e) { Object[] options = { "Yes", "No" };
   * int decision = JOptionPane.showOptionDialog(this.frmClient, "Do you want to create a new game",
   * "Tips", 0, 3, null, options, options[0]); if (decision == 0) { this.backPanel.reset();
   * this.backPanel.regame(); if (this.serverThread != null) { SocketMsg msg = new SocketMsg();
   * msg.setOperation("newCanvas"); this.backPanel.sendMsg(msg); } } }
   * 
   */

  /**
   * open a exsited game public void openMenuClicked(MouseEvent e) {
   * this.fileChooser.setAcceptAllFileFilterUsed(false); this.fileChooser.setFileFilter(new
   * FileFilter(FileFilter.CanvasFilterType.COF_OPEN)); if
   * (this.fileChooser.showOpenDialog(this.frmClient) == 0) { File f =
   * this.fileChooser.getSelectedFile(); if (f.exists()) { try { ObjectInputStream in = new
   * ObjectInputStream(new FileInputStream(f.getPath())); List<CanvasShape> sh =
   * (List)in.readObject(); in.close(); this.canvasPanel.reset(); this.canvasPanel.repaint();
   * this.canvasPanel.setShapes((CopyOnWriteArrayList)sh); this.canvasPanel.repaint();
   * this.openedFile = this.fileChooser.getSelectedFile(); if (this.serverThread != null) {
   * SocketMsg msg = new SocketMsg(); msg.setOperation("newCanvas"); this.canvasPanel.sendMsg(msg);
   * } for (CanvasShape shape : sh) { SocketMsg msg = new SocketMsg(); msg.setOperation("add");
   * JSONSerializer serializer = new JSONSerializer(); String data = serializer.serialize(shape);
   * msg.setData(data); this.canvasPanel.sendMsg(msg); } } catch (FileNotFoundException ex) {
   * ex.printStackTrace(); } catch (IOException ex) { ex.printStackTrace(); } catch
   * (ClassNotFoundException ex) { ex.printStackTrace(); } } }
   * this.fileChooser.resetChoosableFileFilters(); }
   */

  /**
   * save game public void saveMenuClicked(MouseEvent e) { if (this.openedFile == null) {
   * this.fileChooser.setAcceptAllFileFilterUsed(false); this.fileChooser.setFileFilter(new
   * CanvasFileFilter(CanvasFileFilter.CanvasFilterType.COF_SAVE)); if
   * (this.fileChooser.showSaveDialog(this.frmClient) == 0) { File f =
   * this.fileChooser.getSelectedFile(); if (f.exists()) { showFileExistDlg(f); } else {
   * this.userChoice = 1; } if (this.userChoice == 1) { try { ObjectOutputStream out = new
   * ObjectOutputStream(new FileOutputStream(f.getPath()));
   * out.writeObject(this.canvasPanel.getShapes()); out.close(); } catch (FileNotFoundException ex)
   * { ex.printStackTrace(); } catch (IOException ex) { ex.printStackTrace(); } } this.userChoice =
   * -1; } } else { try { ObjectOutputStream out = new ObjectOutputStream(new
   * FileOutputStream(this.openedFile.getPath())); out.writeObject(this.canvasPanel.getShapes());
   * out.close(); } catch (FileNotFoundException ex) { ex.printStackTrace(); } catch (IOException
   * ex) { ex.printStackTrace(); } } this.fileChooser.resetChoosableFileFilters(); }
   */

  /**
   * save as public void saveAsMenuClicked(MouseEvent e) { CanvasFileFilter filter = new
   * CanvasFileFilter(CanvasFileFilter.CanvasFilterType.SAVE);
   * this.fileChooser.setAcceptAllFileFilterUsed(false); this.fileChooser.setFileFilter(filter); if
   * (this.fileChooser.showSaveDialog(this.frmClient) == 0) { try { File f =
   * this.fileChooser.getSelectedFile(); if (f.exists()) { showFileExistDlg(f); } else {
   * this.userChoice = 1; } if (this.userChoice == 1) { String ext =
   * filter.getExtension(f.getPath()); BufferedImage img = this.canvasPanel.getImage();
   * ImageIO.write(img, ext, f); } } catch (IOException ex) { ex.printStackTrace(); }
   * this.userChoice = -1; } this.fileChooser.resetChoosableFileFilters(); }
   */
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

  /*
   * create connect function public void createNetClicked(MouseEvent e) {
   * 
   * }
   * 
   */

  /**
   * connect function
   * 
   * public void conNetClicked(MouseEvent e) { double x_pos = (this.frmClient.getBounds().getMinX()
   * + this.frmClient.getBounds().getMaxX()) / 7.0D * 2.0D; double y_pos =
   * (this.frmClient.getBounds().getMinY() + this.frmClient.getBounds().getMaxY()) / 5.0D * 2.0D;
   * this.conNetFrame.setLocation((int)x_pos, (int)y_pos); this.conNetFrame.setVisible(true); }
   */

  /**
   * end connection function
   *
   * public void endNetClicked(MouseEvent e) { if ((this.serverThread != null) || (this.clientThread
   * != null)) { Object[] options = { "end anyway", "nope" }; int decision =
   * JOptionPane.showOptionDialog(this.frmClient, "would you like to disconnect", "disconnect from
   * network", 0, 3, null, options, options[0]); if (decision == 0) { if (this.serverThread != null)
   * { SocketMsg msg = new SocketMsg(); msg.setOperation("disconnect"); msg.setData("the manager has
   * disconnected"); this.canvasPanel.sendMsg(msg);
   * 
   * CopyOnWriteArrayList<User> clients = this.serverThread.getClients(); try { for (User client :
   * clients) { if (client.getClientSocket() != null) { client.getClientSocket().close(); } }
   * this.serverThread.getCanvasServer().close(); this.serverThread = null; this.user = null; }
   * catch (IOException ioe) { System.out.println(ioe.getMessage()); } clients.removeAll(clients); }
   * else if (this.clientThread != null) { try { this.clientThread.getClient().close();
   * this.clientThread = null; getMenuBar().getMenu(0).setEnabled(true); } catch (IOException ioe) {
   * System.out.println(ioe.getMessage()); } } getUserListPanel().clear(); } } else {
   * JOptionPane.showMessageDialog(this.frmClient, "you don't have a network"); } }
   */

  public void yesBtnClicked() {
    this.userChoice = 1;
    this.popUpDialog.setVisible(false);
  }

  public void noBtnClicked() {
    this.userChoice = 0;
    this.popUpDialog.setVisible(false);
  }

  public User getUser() {
    return this.user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Pane getPane() {
    return this.backPanel;
  }

  public void setPane(Pane backPanel) {
    this.backPanel = backPanel;
  }

  /**
   * if Socket Thread use in connection
   * 
   * public void setServerThread(SocketThread serverThread) { this.serverThread = serverThread; }
   * 
   * public SocketClient getClientThread() { return this.clientThread; }
   * 
   * public void setClientThread(SocketClient clientThread) { this.clientThread = clientThread; }
   */

  public JTextArea getTaMsgHis() {
    return this.taMsgHis;
  }

  public void setTaMsgHis(JTextArea taMsgHis) {
    this.taMsgHis = taMsgHis;
  }

  /**
   * 
   * public void sendMsgClicked() { if ((this.serverThread == null) && (this.clientThread == null))
   * { JOptionPane.showMessageDialog(this.frmClient, "please start or connect to a network first",
   * "error", 0); } else if (!this.tfMsg.getText().trim().equals("")) { String msgContent =
   * this.tfMsg.getText(); SocketMsg msg = new SocketMsg(); msg.setOperation("chat");
   * msg.setData(msgContent); msg.setUserName(getUser().getUserName()); JSONSerializer serializer =
   * new JSONSerializer(); String msgJson = serializer.serialize(msg); if (this.serverThread !=
   * null) { CopyOnWriteArrayList<User> clients = getServerThread().getClients(); if
   * (!clients.isEmpty()) { int i = 0; for (User client : clients) { if (i == 0) { i++; } else { try
   * { DataOutputStream out = new DataOutputStream(client.getClientSocket().getOutputStream());
   * out.writeUTF(msgJson); } catch (IOException e) { System.out.println(e.getMessage()); } } } } }
   * else if (this.clientThread != null) { try { DataOutputStream out = new
   * DataOutputStream(getClientThread().getClient().getOutputStream()); out.writeUTF(msgJson); }
   * catch (IOException e) { System.out.println(e.getMessage()); } }
   * this.taMsgHis.append(this.user.getUserName() + "(me)" + ": " + msgContent + "\n");
   * this.tfMsg.setText(""); } }
   */

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
  public static void renderBasedOnJson(String json) throws JSONException {
    JSONObject data = new JSONObject(json);

    // getting words that are available
    JSONArray wordsAvail = (JSONArray) data.get("word");

    for (int i = 0; i < wordsAvail.length(); i++) {
      JSONObject word = wordsAvail.getJSONObject(i);
      int x = word.getInt("x");
      int y = word.getInt("y");
      String ch = word.getString("ch");
      backPanel.setChar(x, y, ch);
    }
  }

}
