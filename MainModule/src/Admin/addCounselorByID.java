import javax.swing.*;
import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import javax.swing.border.EmptyBorder;

public class addCounselorByID implements ActionListener {

    private Label insertID;
    private JTextField counselorID;

    private Label successMessage;
    private JFrame frame;
    private JPanel windowPane;
    private BoxLayout boxLayout;
    private Button submitID;
    private String counselorIDString;

    private void makeFrame() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        windowPane = new JPanel();
        boxLayout = new BoxLayout(windowPane, BoxLayout.Y_AXIS);
        windowPane.setLayout(boxLayout);
        windowPane.setBorder(new EmptyBorder(new Insets(150, 200, 150, 200)));


        insertID = new Label("Enter Counselor ID:");
        windowPane.add(insertID);

        //textfield
        counselorID = new JTextField(10);
        counselorID.setMaximumSize(new Dimension(Integer.MAX_VALUE, counselorID.getMinimumSize().height));
        windowPane.add(counselorID);

        successMessage = new Label("");
        successMessage.setPreferredSize(new Dimension(275, 50));
        windowPane.add(successMessage);

        submitID = new Button("Submit");
        windowPane.add(submitID);
        submitID.addActionListener(this);

        frame.add(windowPane);
        frame.pack();
        frame.setVisible(true);
    }


    public addCounselorByID() {

    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        counselorIDString = counselorID.getText();
        counselorID.setText("");

        successMessage.setText("is it working");

        //do database stuff

        counselorIDString = null;

    }

    public static void main(String[] args) {
        addCounselorByID a = new addCounselorByID();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                a.makeFrame();
            }
        });
        return;
    }


}