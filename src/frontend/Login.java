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

public class Login extends JFrame {

  private JPanel contentPane;
  private JTextField userNameArea;
  private JPasswordField passwordArea;

  /**
   * Create the frame.
   * 
   * @param remoteGame
   * @param client
   */
  public Login(IRemoteGame remoteGame) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 350, 250);
    contentPane = new JPanel();
    contentPane.setBackground(new Color(51, 102, 153));
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(null);

    JLabel Minimise = new JLabel("-");
    Minimise.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        setState(JFrame.ICONIFIED);
      }
    });
    Minimise.setHorizontalAlignment(SwingConstants.CENTER);
    Minimise.setFont(new Font("Arial", Font.BOLD, 28));
    Minimise.setForeground(Color.WHITE);
    Minimise.setBounds(282, 3, 27, 40);
    contentPane.add(Minimise);

    JLabel Exit = new JLabel("X");
    Exit.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        System.exit(0);
      }
    });
    Exit.setHorizontalAlignment(SwingConstants.CENTER);
    Exit.setFont(new Font("Arial", Font.BOLD, 17));
    Exit.setForeground(Color.WHITE);
    Exit.setBounds(314, 5, 36, 38);
    contentPane.add(Exit);

    JLabel Header = new JLabel("SCRABBLE ONLINE");
    Header.setHorizontalAlignment(SwingConstants.CENTER);
    Header.setFont(new Font("Verdana", Font.PLAIN, 26));
    Header.setForeground(Color.ORANGE);
    Header.setBounds(0, 42, 350, 40);
    contentPane.add(Header);

    JLabel lblUsername = new JLabel("Username ");
    lblUsername.setForeground(Color.WHITE);
    lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 12));
    lblUsername.setBounds(60, 102, 69, 23);
    contentPane.add(lblUsername);

    JLabel lblPassword = new JLabel("Password ");
    lblPassword.setForeground(Color.WHITE);
    lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
    lblPassword.setBounds(60, 137, 69, 23);
    contentPane.add(lblPassword);

    userNameArea = new JTextField();
    userNameArea.setForeground(new Color(255, 255, 255));
    userNameArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
    userNameArea.setBackground(new Color(102, 102, 102));
    userNameArea.setBounds(129, 103, 168, 20);
    contentPane.add(userNameArea);
    userNameArea.setColumns(10);

    passwordArea = new JPasswordField();
    passwordArea.setForeground(new Color(255, 255, 255));
    passwordArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
    passwordArea.setBackground(new Color(102, 102, 102));
    passwordArea.setBounds(129, 138, 168, 20);
    contentPane.add(passwordArea);

    JButton loginBtn = new JButton("Login");
    loginBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        // when button login is pressed
        char[] pass = passwordArea.getPassword();
        String passString = new String(pass);
        try {
          // when button login is pressed, check the username and password
          // client can join the remoteGame if the validation is okay.

          if (remoteGame.isLoginValid(userNameArea.getText(), passString)) {

            if (!remoteGame.isClientLoggedIn(userNameArea.getText())) {
              Client client = new Client(userNameArea.getText());
              client.joinClientList(remoteGame);
              // dispose login frame
              dispose();

              Lobby lobby = new Lobby(client, remoteGame);
              lobby.setVisible(true);
            } else {
              JOptionPane.showMessageDialog(null,
                  "Username has been used. Please try another username", "Error",
                  JOptionPane.PLAIN_MESSAGE);
            }
          } else {
            JOptionPane.showMessageDialog(null,
                "Username or password not valid. Please retype again", "Error",
                JOptionPane.PLAIN_MESSAGE);
          }

        } catch (RemoteException e1) {
          JOptionPane.showMessageDialog(null, "Server is down try again later, exiting the game...",
              "Error", JOptionPane.PLAIN_MESSAGE);
          System.exit(0);
        }
      }
    });
    loginBtn.setBackground(new Color(255, 255, 255));
    loginBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
    loginBtn.setBounds(70, 172, 90, 29);
    contentPane.add(loginBtn);

    JButton cancelBtn = new JButton("Cancel");
    cancelBtn.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        System.exit(0);
      }
    });
    cancelBtn.setFont(new Font("Tahoma", Font.PLAIN, 13));
    cancelBtn.setBounds(179, 172, 90, 29);
    contentPane.add(cancelBtn);

    JLabel register = new JLabel("Click here to create a new account");
    register.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        dispose();
        Register regis = new Register(remoteGame);
        regis.setVisible(true);
        regis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      }
    });
    register.setFont(new Font("Tahoma", Font.PLAIN, 12));
    register.setForeground(new Color(255, 255, 255));
    register.setHorizontalAlignment(SwingConstants.CENTER);
    register.setBounds(80, 213, 195, 16);
    contentPane.add(register);
    setUndecorated(true);
  }
}
