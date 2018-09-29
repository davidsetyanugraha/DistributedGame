package client;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.*;
import java.net.URL;
import javax.imageio.ImageIO;

public class Pane extends JPanel{

	private int strokeSize = 1;
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));
    private JButton[][]  boardSquares = new JButton[22][22];
    private JPanel board;
    private final JLabel message = new JLabel(
            "Ready to play");
    private static final String COLS = "ABCDEFGHIJKLMNOPQRSTUV";
    

    public Pane(ClientFrame clientFrame){
    	
        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));
        JToolBar tools = new JToolBar();
        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);
        
        tools.addSeparator();
        tools.add(message);
        
        JButton btnSubmit = new JButton("Submit");
        btnSubmit.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		//call submit
        		//call calculate score
        	}
        });
        tools.add(btnSubmit);
        
        

        board = new JPanel() {

            /**
             * Override the preferred size to return the largest it can, in
             * a square shape.  Must (must, must) be added to a GridBagLayout
             * as the only component (it uses the parent as a guide to size)
             * with no GridBagConstaint (so it is centered).
             */
            @Override
            public final Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                Dimension prefSize = null;
                Component c = getParent();
                if (c == null) {
                    prefSize = new Dimension(
                            (int)d.getWidth(),(int)d.getHeight());
                } else if (c!=null &&
                        c.getWidth()>d.getWidth() &&
                        c.getHeight()>d.getHeight()) {
                    prefSize = c.getSize();
                } else {
                    prefSize = d;
                }
                int w = (int) prefSize.getWidth();
                int h = (int) prefSize.getHeight();
                // the smaller of the two sizes
                int s = (w>h ? h : w);
                return new Dimension(s,s);
            }
        };

        RelativeLayout rl = new RelativeLayout(RelativeLayout.Y_AXIS);
        rl.setRoundingPolicy( RelativeLayout.FIRST );
        rl.setFill(true);
        board.setLayout( rl );

        board.setBorder(new CompoundBorder(
                new EmptyBorder(8,8,8,8),
                new LineBorder(Color.BLACK)
                ));
        // Set the BG to be ochre
        Color ochre = new Color(204,119,34);
        board.setBackground(SystemColor.activeCaption);
        JPanel boardConstrain = new JPanel(new GridBagLayout());
        boardConstrain.setBackground(SystemColor.desktop);
        boardConstrain.add(board);
        gui.add(boardConstrain);


        // create the board squares        

        Insets buttonMargin = new Insets(0, 0, 0, 0);
//        for (int ii = 0; ii < boardSquares.length; ii++) {
//            for (int jj = 0; jj < boardSquares[ii].length; jj++) {
//                JButton a = new JButton();
//            	a.addActionListener(new ActionListener(){
//            	public void actionPerformed(ActionEvent ia){
//            		String input = "¡¤";
//            		a.setText(input);
//            }});
//            }
//            }
        for (int ii = 0; ii < boardSquares.length; ii++) {
            for (int jj = 0; jj < boardSquares[ii].length; jj++) {
        		
                JButton b = new JButton();
                b.setMargin(buttonMargin);
                
                boardSquares[10][10] = new JButton(String.valueOf("*"));
                b.setText(" ");
                
            	final int i = ii;
            	final int j = jj;
//                int a = b.getHorizontalTextPosition();
//                int d = b.getVerticalTextPosition();

                b.addActionListener(new ActionListener(){
                	public void actionPerformed(ActionEvent e){
                		// call input window
                		String input;
                		input = JOptionPane.showInputDialog(null, "Enter the Character");
            			
                		if (input.length()>1){
                			// exception
                			JOptionPane.showMessageDialog(null,"Invalid Input, Please Try Again!","Error",JOptionPane.PLAIN_MESSAGE);
                			input = " ";
                		}
                		
//                		else if (!b.getText().isEmpty() || b.getText().equals("*")){
//                			JOptionPane.showMessageDialog(null,"Letter Already Exist, Try Again.","Error",JOptionPane.PLAIN_MESSAGE);
//                			input = b.getText();
//                		}
                		
                		//input position invalidation               		
                		else if (i != 0 & j !=0){
                			int x,y;
                			boolean findLetter = false;
                		for(x=j-1;x<=j+1;x++){
                			for(y=i-1;y<=i+1;y++){                		
                				if(!boardSquares[x][y].getText().equalsIgnoreCase(" ")){
                					findLetter = true;
                					}
                				}
                			}
                			if(findLetter == false){
                				JOptionPane.showMessageDialog(null,"Please Input Letter in Adjacent Positions","Error",JOptionPane.PLAIN_MESSAGE);
                				input = " ";
                			}
                		}
                		else if (i == 0 && j != 0){
                			int x,y;
                			boolean findLetter = false;
                			for(x=j-1;x<=j+1;x++){
                				for(y=i;y<=i+1;y++){
                					if(!boardSquares[x][y].getText().equalsIgnoreCase(" ")){
                						findLetter = true;
                						}
                					}
                				}
                			if(findLetter == false){
                				JOptionPane.showMessageDialog(null,"Please Input Letter in Adjacent Positions","Error",JOptionPane.PLAIN_MESSAGE);
                				input = " ";
                				}
                			}
                		else if (i != 0 & j ==0){
                			int x,y;
                			boolean findLetter = false;
                			for(x=j;x<=j+1;x++){
                				for(y=i-1;y<=i+1;y++){
                					if(!boardSquares[x][y].getText().equalsIgnoreCase(" ")){
                						findLetter = true;
                						}
                					}
                				}
                			if(findLetter == false){
                				JOptionPane.showMessageDialog(null,"Please Input Letter in Adjacent Positions","Error",JOptionPane.PLAIN_MESSAGE);
                				input = " ";
                			}
                			}
                		else if (i == 0 & j ==0){
                			int x,y;
                			boolean findLetter = false;
                			for(x=j;x<=j+1;x++){
                				for(y=i;y<=i+1;y++){
                					if(!boardSquares[x][y].getText().equalsIgnoreCase(" ")){
                						findLetter = true;
                					}
                				}
                			}
                			if(findLetter == false){
                				JOptionPane.showMessageDialog(null,"Please Input Letter in Adjacent Positions","Error",JOptionPane.PLAIN_MESSAGE);
                				input = " ";
                			}
                			}

                		b.setText(input);
                	}
                });
                
                
                if ((jj % 2 == 1 && ii % 2 == 1)
                        || (jj % 2 == 0 && ii % 2 == 0)) {
                    b.setBackground(Color.WHITE);
                } else {
                    b.setBackground(Color.GRAY);
                }
                boardSquares[jj][ii] = b;
            }
        }
        
    
        //fill the board

        RelativeLayout topRL = new RelativeLayout(RelativeLayout.X_AXIS);
        topRL.setRoundingPolicy( RelativeLayout.FIRST );
        topRL.setFill(true);
        JPanel top = new JPanel( topRL );
        top.setOpaque(false);
        board.add(top, new Float(1));

        top.add(new JLabel(""), new Float(1));

        // fill the top row
        for (int ii = 0; ii < 21; ii++) {
            JLabel label = new JLabel(COLS.substring(ii, ii + 1), SwingConstants.CENTER);
            top.add(label, new Float(1));
        }
        // fill the black non-pawn piece row
        for (int ii = 0; ii < 21; ii++) {

            RelativeLayout rowRL = new RelativeLayout(RelativeLayout.X_AXIS);
            rowRL.setRoundingPolicy( RelativeLayout.FIRST );
            rowRL.setFill(true);
            JPanel row = new JPanel( rowRL );
            row.setOpaque(false);
            board.add(row, new Float(1));

            for (int jj = 0; jj < 21; jj++) {
                switch (jj) {
                    case 0:
                        row.add(new JLabel("" + (22-(ii + 1)), SwingConstants.CENTER), new Float(1));
                    default:
                        row.add(boardSquares[jj][ii], new Float(1));
                }
            }
        }
    }
    
    public void setStrokeSize(int size)
    {
      this.strokeSize = size;
    }
    public final JComponent getboard() {
        return board;
    }

    public final JComponent getGui() {
        return gui;

    }
}