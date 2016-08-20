/*
 * ////////   //////    //    ///////    //       // //////
 * //     //  //      //  //  //    //   ////   //// // 
 * //     //  //     //    // //     //  // // // // //
 * ////////   /////  //////// //      // //  //   // ///// 
 * //   //    /////  //    // //      // //       // /////
 * //    //   //     //    // //     //  //       // // 
 * //     //  //     //    // //    //   //       // //
 * //     //  ////// //    // ///////    //       // ///////
 *
 * By Jonathan Herrera
 * Last update 22:25 07/01/2016
 * ABOUT
 * Creating a simple message board where users can post text and see text that others have posted
 * There are many things that I still wanted to do but could not get to.
 * HOW TO
 * The program successfully establishes a connection to a database
 * The GUI will open where you will NEED to enter a Name and Message to upload to the DB
 * The Name and Message text cannot conatain special characters such as ' ; / ) etc.
 * Although the message is uploaded, it does not display so the app must close then reopen to display new messages
 * however, you can constantly upload messages, it just will not show
 * MISSING FEATURES
 * Includes auto refreshing the page to show the most up to date messages, incorporate user's time of upload,
 * changing the size of everything, add images, etc., but the GUI took longer than expected, connect this to
 * an actual database, and port this onto my website
 * 
 */
package messageboard;

//the following are imported for use with JFrame
    import javax.swing.*;
    import java.awt.*;
    //this library adds components to change the style (l
    import java.awt.event.*;
    //despite importing the whole awt library, I have to specify this is all 
    import java.util.Calendar;
    import java.text.SimpleDateFormat;
    //these two are used for getting the time and the then displaying it

public class MessageBoard extends JFrame {
    //PANELS
        static JPanel inputPanel = new JPanel(new SpringLayout());
        //this is to style the layout of the window
//        static JPanel messagePanel = new JPanel(new GridLayout(50, 2)); 
//        //this is used for the messages that will be displayed
        
        Container contentPane = getContentPane();
        //this is embedded in the Java library
        
    //LAYOUTS
        static SpringLayout springLayout = new SpringLayout();
        //this layout organizes each object by specifying a set distance from another object/window
        GridLayout gridLayout = new GridLayout();
    
    //PANEL COMPONENTS
        //INPUT PANEL
        JLabel screenName = new JLabel("Name: ");
        JTextField nameField = new JTextField(30);
        JLabel msg = new JLabel("Message: ");
        JTextArea messageField = new JTextArea("", TA_ROWS, TA_COLS);
            //creates a textbox to type in with 120 char limit
            private static final int TA_ROWS = 6;
            private static final int TA_COLS = 30;
            //making the rows and columns a final variable so it doesnt 
        JButton submit = new JButton("Submit");
        //creates a submit button        
        
        //MESSAGE PANEL        
        static JLabel post;
        static DefaultListModel dlm = new DefaultListModel();
        //the layout for the list
        static JList messageList = new JList(dlm);
        //the list that will contain the messages
        JScrollPane messagePanel = new JScrollPane(messageList);
        //Im recreating the messagePanel as a scroll pane so I can actually scroll in it
        static Font customFont = new Font("Serif", Font.PLAIN, 16);
    
    MessageBoard() {
        super("nChat");
        //inheriting the properties of the JFrame class
        //also names the window
        setSize(500, 800);
//        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        contentPane.setLayout(springLayout);
        //this creates the Spring layout to be used
            
        add(messagePanel);
        messagePanel.setBackground(Color.CYAN);

        
        add(inputPanel);
        //the top portion will display the message box for the user to type in
        //to actually add the interactivity
            inputPanel.setBackground(Color.GREEN);
            inputPanel.setLayout(springLayout);
        
        createGUI();
        GUILayout();
        
        setVisible(true);
        //displays the window
    }
    
    void createGUI() {
        //the message input area
        inputPanel.add(screenName);
        inputPanel.add(nameField);
        inputPanel.add(msg);
        inputPanel.add(messageField);
            messageField.setWrapStyleWord(true);
            messageField.setLineWrap(true);
            //these settings make it so that the text the user inputs wil automatically move 
            //to the next line. Doesn't prevent more that 4 rows of text however...
        inputPanel.add(submit);
        //the following line is to perform an action when the submit button is clicked
            submit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                //when the submit button is clicked then the following method will occur
                    submitPressed(contentPane);
                }
            });
    }
    
    public void submitPressed(Container contentPane) {
        Calendar cal = Calendar.getInstance();
        //this gets the time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        
        String userName = nameField.getText();
        String userMessage = messageField.getText();
        //getting the text from the message boxes and sending them to the server method
        String time = sdf.format(cal.getTime());
        serverConnect.postMessage(time, userName, userMessage);
        nameField.setText("");
        messageField.setText("");
        //after submitting, the fields will be cleared. Prevents spam because I require a name AND message :p
        dlm.clear();
        //this clears the list everytime it is posted
        serverConnect.initialDisplay();
        //this redisplays the messages
    }
    
    public static void displayMessage(String time, String name, String message) {
        //the server on launch will get the messages and display them
        String post = new String(time + ": " + name + " says " + message);
        dlm.addElement(post);
        //displays the name and message onto the messagePanel
    }
    
    void GUILayout() {
    //LAYOUT POSITIONING
        //PANEL LAYOUTS
        //Input Panel
            springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, inputPanel, 0, SpringLayout.HORIZONTAL_CENTER, contentPane);
            springLayout.putConstraint(SpringLayout.SOUTH, inputPanel, 0, SpringLayout.SOUTH, contentPane);
            springLayout.putConstraint(SpringLayout.SOUTH, inputPanel, 165, SpringLayout.NORTH, contentPane);
        //Message Panel
            springLayout.putConstraint(SpringLayout.EAST, messagePanel, 0, SpringLayout.EAST, contentPane);
            springLayout.putConstraint(SpringLayout.WEST, messagePanel, 0, SpringLayout.WEST, contentPane);
            springLayout.putConstraint(SpringLayout.NORTH, messagePanel, 0, SpringLayout.SOUTH, inputPanel);
            springLayout.putConstraint(SpringLayout.SOUTH, messagePanel, 0, SpringLayout.SOUTH, contentPane);
        //INPUT PANEL COMPONENTS
        //Name Textfield
            springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, nameField, 5, SpringLayout.HORIZONTAL_CENTER, inputPanel);
            springLayout.putConstraint(SpringLayout.NORTH, nameField, 5, SpringLayout.NORTH, inputPanel);
        //"Name:"
            springLayout.putConstraint(SpringLayout.EAST, screenName, -10, SpringLayout.WEST, nameField);
            //(the ___ side of the, object is, offset __px of the, N/E/S/W side of the, object)
                //sides include N,E,S,W, BASELINE, HORIZONTAL_/VERTICAL_CENTER
            springLayout.putConstraint(SpringLayout.NORTH, screenName, 5, SpringLayout.NORTH, inputPanel);
        //Messagefield
            springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, messageField, 0, SpringLayout.HORIZONTAL_CENTER, nameField);
            springLayout.putConstraint(SpringLayout.NORTH, messageField, 5, SpringLayout.SOUTH, nameField);
        //"Message:"
            springLayout.putConstraint(SpringLayout.EAST, msg, -10, SpringLayout.WEST, messageField);
            springLayout.putConstraint(SpringLayout.NORTH, msg, 0, SpringLayout.NORTH, messageField);

        //Submit Button
            springLayout.putConstraint(SpringLayout.NORTH, submit, 5, SpringLayout.SOUTH, messageField);
            springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, submit, 0, SpringLayout.HORIZONTAL_CENTER, messageField);
        //Posts
//            layout.putConstraint(SpringLayout.NORTH, postDisplay, 30, SpringLayout.SOUTH, submit);
//            layout.putConstraint(SpringLayout.WEST, postDisplay, 0, SpringLayout.WEST, msg);
    
    }

    public static void main(String[] args) {

        serverConnect.initialDisplay();
        //putting this first will grab all messages initially on the DB so it is up to date
        MessageBoard gui = new MessageBoard();       

    }

}
