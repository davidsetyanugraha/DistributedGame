package frontend;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import backend.IRemoteGame;

public class Register extends JFrame {

  private JPanel regPane;
  private JTextField firstNameArea;
  private JTextField lastNameArea;
  private JTextField userNameArea;
  private JPasswordField passwordArea;
  private JPasswordField confirmationArea;

  /**
   * Create the frame.
   */
  public Register(IRemoteGame remoteGame) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 340, 260);
    regPane = new JPanel();
    regPane.setBackground(new Color(51, 102, 153));
    regPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(regPane);
    regPane.setLayout(null);

    JLabel welcomeMsg = new JLabel("Welcome new player! Tell us about yourself.");
    welcomeMsg.setHorizontalAlignment(SwingConstants.LEFT);
    welcomeMsg.setForeground(Color.ORANGE);
    welcomeMsg.setFont(new Font("Verdana", Font.PLAIN, 12));
    welcomeMsg.setBounds(26, 29, 269, 33);
    regPane.add(welcomeMsg);

    JLabel firstNameLbl = new JLabel("Last Name    ");
    firstNameLbl.setForeground(Color.WHITE);
    firstNameLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
    firstNameLbl.setBounds(26, 94, 109, 23);
    regPane.add(firstNameLbl);

    firstNameArea = new JTextField();
    firstNameArea.setForeground(Color.WHITE);
    firstNameArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
    firstNameArea.setColumns(10);
    firstNameArea.setBackground(new Color(102, 102, 102));
    firstNameArea.setBounds(142, 64, 168, 20);
    regPane.add(firstNameArea);

    JLabel Minimise_1 = new JLabel("-");
    Minimise_1.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        setState(JFrame.ICONIFIED);
      }
    });
    Minimise_1.setHorizontalAlignment(SwingConstants.CENTER);
    Minimise_1.setForeground(Color.WHITE);
    Minimise_1.setFont(new Font("Arial", Font.BOLD, 28));
    Minimise_1.setBounds(275, 0, 27, 47);
    regPane.add(Minimise_1);

    JLabel Exit = new JLabel("X");
    Exit.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        System.exit(0);
      }
    });
    Exit.setHorizontalAlignment(SwingConstants.CENTER);
    Exit.setForeground(Color.WHITE);
    Exit.setFont(new Font("Arial", Font.BOLD, 17));
    Exit.setBounds(307, 0, 33, 52);
    regPane.add(Exit);

    JLabel lastNameLbl = new JLabel("First Name   ");
    lastNameLbl.setForeground(Color.WHITE);
    lastNameLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
    lastNameLbl.setBounds(26, 63, 109, 23);
    regPane.add(lastNameLbl);

    lastNameArea = new JTextField();
    lastNameArea.setForeground(Color.WHITE);
    lastNameArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
    lastNameArea.setColumns(10);
    lastNameArea.setBackground(new Color(102, 102, 102));
    lastNameArea.setBounds(142, 94, 168, 20);
    regPane.add(lastNameArea);

    JLabel userNameLbl = new JLabel("Username   ");
    userNameLbl.setForeground(Color.WHITE);
    userNameLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
    userNameLbl.setBounds(26, 123, 109, 23);
    regPane.add(userNameLbl);

    userNameArea = new JTextField();
    userNameArea.setForeground(Color.WHITE);
    userNameArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
    userNameArea.setColumns(10);
    userNameArea.setBackground(new Color(102, 102, 102));
    userNameArea.setBounds(142, 124, 168, 20);
    regPane.add(userNameArea);

    JLabel passwordLbl = new JLabel("Password             ");
    passwordLbl.setForeground(Color.WHITE);
    passwordLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
    passwordLbl.setBounds(26, 153, 109, 23);
    regPane.add(passwordLbl);

    JLabel confirmationLbl = new JLabel("Confirm Password ");
    confirmationLbl.setForeground(Color.WHITE);
    confirmationLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
    confirmationLbl.setBounds(26, 188, 109, 23);
    regPane.add(confirmationLbl);

    passwordArea = new JPasswordField();
    passwordArea.setForeground(Color.WHITE);
    passwordArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
    passwordArea.setBackground(new Color(102, 102, 102));
    passwordArea.setBounds(142, 156, 168, 20);
    regPane.add(passwordArea);

    confirmationArea = new JPasswordField();
    confirmationArea.setForeground(Color.WHITE);
    confirmationArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
    confirmationArea.setBackground(new Color(102, 102, 102));
    confirmationArea.setBounds(142, 186, 168, 20);
    regPane.add(confirmationArea);

    JButton btnConfirm = new JButton("Confirm");
    btnConfirm.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (getPass().equals(getConfirmPass())) {
          try {
            if (remoteGame.isLoginValid(userNameArea.getText())) {
              // Error when username has already existed
              JOptionPane.showMessageDialog(null, "Username exists. Please create another username",
                  "Error", JOptionPane.PLAIN_MESSAGE);
            } else {
              // Successful registering
              remoteGame.appendJsonClient(getUserName(), getPass(), getFirstName(), getLastName());
            }
          } catch (RemoteException e1) {
            e1.printStackTrace();
          }
        } else {
          // Error when confirmation doesn't match password
          JOptionPane.showMessageDialog(null,
              "Confirmation does not match password. Please retype again", "Error",
              JOptionPane.PLAIN_MESSAGE);
        }
      }
    });
    btnConfirm.setFont(new Font("Tahoma", Font.PLAIN, 13));
    btnConfirm.setBackground(Color.WHITE);
    btnConfirm.setBounds(71, 223, 90, 29);
    regPane.add(btnConfirm);

    JButton button_1 = new JButton("Cancel");
    button_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
    button_1.setBounds(178, 223, 90, 29);
    regPane.add(button_1);

    setUndecorated(true);
  }

  public String getFirstName() {
    String name = firstNameArea.getText();
    return name;
  }

  public String getLastName() {
    String name = lastNameArea.getText();
    return name;
  }

  public String getUserName() {
    String name = userNameArea.getText();
    return name;
  }

  public String getPass() {
    String name = new String(passwordArea.getPassword());
    return name;
  }

  public String getConfirmPass() {
    String name = new String(confirmationArea.getPassword());
    return name;
  }
}
