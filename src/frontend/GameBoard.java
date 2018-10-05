package frontend;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.json.JSONArray;
import org.json.JSONException;



public class GameBoard extends JPanel{
	
	private static Client client;
	
	private final JPanel gui = new JPanel(new BorderLayout(3, 3));
	private final JLabel message = new JLabel("Ready to play");
	
	
	
	public GameBoard(ClientFrame clientFrame, Client client) {
		this.client = client;
		// set up the main GUI
	    gui.setBorder(new EmptyBorder(5, 5, 5, 5));
	    JToolBar tools = new JToolBar();
	    tools.setFloatable(false);
	    gui.add(tools, BorderLayout.PAGE_START);
	    
	    tools.addSeparator();
	    tools.add(message);
		
	}
	

}
