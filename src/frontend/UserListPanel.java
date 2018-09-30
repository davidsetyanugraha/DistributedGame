package frontend;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class UserListPanel extends JPanel {
  private static final long serialVersionUID = -154678996694424775L;
  private JList<String> userList;
  private ArrayList<String> userArray = new ArrayList();
  private DefaultListModel<String> listModel;
  private ClientFrame parentFrm;

  public UserListPanel(ClientFrame Frm) {
    this.parentFrm = Frm;
    this.userList = new JList();
    JScrollPane scroll = new JScrollPane();
    this.listModel = new DefaultListModel();
    for (String str : this.userArray) {
      this.listModel.addElement(str);
    }
    this.userList.setModel(this.listModel);
    this.userList.setVisibleRowCount(5);
    this.userList.addMouseListener(new MouseAdapter() {
      // public void mouseClicked(MouseEvent mevt) {
      // JList<String> list = (JList) mevt.getSource();
      // if (mevt.getClickCount() == 2) {
      // int index = list.locationToIndex(mevt.getPoint());
      // if ((index > 0) && (UserListPanel.this.parentFrm.getServerThread() != null)) {
      // Object[] options = {"Yes", "No"};
      // int decision = JOptionPane.showOptionDialog(UserListPanel.this.parentFrm.getFrmClient(),
      // "Do you want to kick out " + (String) UserListPanel.this.userArray.get(index),
      // "Tips", 0, 3, null, options, options[0]);
      // if (decision == 0) {
      // CopyOnWriteArrayList<User> clients =
      // UserListPanel.this.parentFrm.getServerThread().getClients();
      // for (User client : clients) {
      // if (((String) UserListPanel.this.userArray.get(index))
      // .equals(client.getUserName())) {
      // JSONSerializer serializer = new JSONSerializer();
      // try {
      // DataOutputStream out =
      // new DataOutputStream(client.getClientSocket().getOutputStream());
      // SocketMsg kickoutMsg = new SocketMsg();
      // kickoutMsg.setOperation("kick");
      // kickoutMsg.setData("you have been kicked out by manager!");
      // out.writeUTF(serializer.serialize(kickoutMsg));
      // } catch (IOException e) {
      // System.out.println(e.getMessage());
      // }
      // UserListPanel.this.parentFrm.getServerThread().getClients().remove(client);
      // UserListPanel.this.parentFrm.getUserListPanel()
      // .deleteUser((String) UserListPanel.this.userArray.get(index));
      // SocketMsg msg = new SocketMsg();
      // msg.setOperation("deleteUser");
      // msg.setData(client.getUserName());
      // UserListPanel.this.parentFrm.getCanvasPanel().sendMsg(msg);
      // break;
      // }
      // }
      // }
      // }
      // }
      // }
    });
    scroll.setPreferredSize(new Dimension(200, 100));
    scroll.setViewportView(this.userList);
    setLayout(new BorderLayout());
    add(scroll, "Center");
  }

  public ArrayList<String> getUserArray() {
    return this.userArray;
  }

  public void setUserArray(ArrayList<String> userArray) {
    this.userArray = userArray;
  }

  public void addUser(String newUser) {
    if (!this.userArray.contains(newUser)) {
      this.userArray.add(newUser);
      this.listModel.addElement(newUser);
    }
  }

  public void deleteUser(String delUser) {
    int index = this.userArray.indexOf(delUser);
    if ((index > 0) && (this.listModel.contains(delUser))) {
      this.listModel.remove(index);
      this.userArray.remove(delUser);
    }
  }

  public void clear() {
    this.listModel.clear();
    this.userArray.clear();
  }
}
