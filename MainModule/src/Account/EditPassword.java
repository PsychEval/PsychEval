
import javax.swing.*;
import java.awt.*;        // Using AWT container and component classes
import java.awt.event.*;  // Using AWT event classes and listener interfaces
import javax.swing.border.EmptyBorder;

public class EditPassword extends Frame implements ActionListener {

    private Label enterOldPass;
    private Label enterNewPass;
    private Label reenterNewPass;
    private JPasswordField enterOldPassword;
    private JPasswordField enterNewPassword;
    private JPasswordField reenterNewPassword;
    private Button submitPassword;
    private String submittedPassword;
    private Label successMessage;
    private JFrame frame;
    private JPanel windowPane;
    private BoxLayout boxLayout;

    private void makeFrame() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        windowPane = new JPanel();
        boxLayout = new BoxLayout(windowPane, BoxLayout.Y_AXIS);
        windowPane.setLayout(boxLayout);
        windowPane.setBorder(new EmptyBorder(new Insets(150, 200, 150, 200)));


        enterOldPass = new Label("Enter Old Password:");
        enterNewPass = new Label("Enter New Password:");
        reenterNewPass = new Label("Reenter New Password:");
        enterOldPassword = new JPasswordField(20);
        enterNewPassword = new JPasswordField(20);
        reenterNewPassword = new JPasswordField(20);
//        enterOldPassword.setEditable(true);
//        enterNewPassword.setEditable(true);
//        reenterNewPassword.setEditable(true);

        windowPane.add(enterOldPass);
        windowPane.add(enterOldPassword);
        windowPane.add(enterNewPass);
        windowPane.add(enterNewPassword);
        windowPane.add(reenterNewPass);
        windowPane.add(reenterNewPassword);

        successMessage = new Label("");
        successMessage.setPreferredSize(new Dimension(275, 50));
        successMessage.setVisible(true);
        windowPane.add(successMessage);

        submitPassword = new Button("Submit");
        windowPane.add(submitPassword);
        submitPassword.addActionListener(this);

        frame.add(windowPane);
        frame.pack();
        frame.setVisible(true);
    }


    public EditPassword() {

    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        char [] oldPassArr = enterOldPassword.getPassword();
        char [] newPassArr = enterNewPassword.getPassword();
        char [] reenteredNewPassArr = reenterNewPassword.getPassword();
        String oldPassString = String.copyValueOf(oldPassArr);
        String newPassString = String.copyValueOf(newPassArr);
        String reenteredNewPassString = String.copyValueOf(reenteredNewPassArr);
        //clear the text on gui
        enterOldPassword.setText("");
        enterNewPassword.setText("");
        reenterNewPassword.setText("");

        if(oldPassString.equals(newPassString)) {
            successMessage.setText("New password must be different from old");
        }
        else if(!newPassString.equals(reenteredNewPassString)) {
            successMessage.setText("Password is not the same as re-entered password");
        }
        else {
            //take username and change password in db
            successMessage.setText("Success!");
        }

        oldPassString = null;
        newPassString = null;
        reenteredNewPassString = null;
    }

    public static void main(String[] args) {
        EditPassword ep = new EditPassword();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ep.makeFrame();
            }
        });
        return;
    }


}