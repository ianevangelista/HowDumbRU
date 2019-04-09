package Controllers;

import java.util.Properties;
import java.util.regex.Pattern;
import javax.mail.*;
import javax.mail.internet.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;

import static Controllers.ControllerHome.getUserName;

public class ControllerEmail {

    // Fxml elements
    @FXML
    public TextArea feedback;
    public TextField email;
    public Label errorMessageEmailInvalid;

    // Set up for java email
    private Properties props = new Properties();
    private Session session = Session.getDefaultInstance(props);
    private MimeMessage message = new MimeMessage(session);
    private Transport transport = null;

    private void sendFeedback(){
        // Set up email details for the HDRU gmail account
        String sendTo = "howdumbru.game@gmail.com";
        String host = "smtp.gmail.com";
        String username = "howdumbru.game@gmail.com";
        String password = "Junierkul";

        // Set up properties for the email connection
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", host);

        try{
            // Add email of the user sending the feedback and set recepient as the HDRU gmail address
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));

            // Set email subject and contents to the feedback
            message.setSubject("Feedback from How Dumb R U?");
            String melding = feedback.getText() + "\nSendt from: " + email.getText();
            message.setText(melding);

            // Send the email
            transport = session.getTransport("smtp");
            transport.connect(host, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (MessagingException e) {
            // If the email fails
            e.printStackTrace();
        }
        catch (Exception e){
            // If something else fails
            e.printStackTrace();
        }
    }

    private void sendConfirm(){
        // Set up email details for the HDRU gmail account
        String sendTo = email.getText();
        String host = "smtp.gmail.com";
        String username = "howdumbru.game@gmail.com";
        String password = "Junierkul";

        // Set up properties for the email connection
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.password", password);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", host);

        try{
            // Add email of the user sending the feedback and set recepient as the HDRU gmail address
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(sendTo));

            // Set email subject and contents to the feedback
            message.setSubject("Received feedback from How Dumb R U?");
            message.setText("Feedback received! We will come back to you as soon as possible.\n" +
                    "\nHere's your feedback:\n" + feedback.getText() +
                    "\n\nFrom: How Dumb R U?");

            // Send the email
            transport = session.getTransport("smtp");
            transport.connect(host, username, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        }
        catch (MessagingException e) {
            // If the email fails
            e.printStackTrace();
        }
        catch (Exception e){
            // If something else fails
            e.printStackTrace();
        }
    }

    public void feedback(ActionEvent event){
        if(!checkEmail()) {
            // If the email is invalid
            errorMessageEmailInvalid.setVisible(true);
        } else{
            // Send feedback to HDRU and a confirmation mail to the user
            sendFeedback();
            sendConfirm();
            // Go to game if logged inn or main if not
            if(getUserName()!= null) ChangeScene.change(event, "/Scenes/Game.fxml");
            else ChangeScene.change(event, "/Scenes/Main.fxml");
        }
    }

    public void sceneHome(ActionEvent event) { //back button
        // Go to game if logged inn or main if not
        if(getUserName()!= null) ChangeScene.change(event, "/Scenes/Game.fxml");
        else ChangeScene.change(event, "/Scenes/Main.fxml");
    }

    private boolean checkEmail(){
        // Get email from textfield
        String getEmail = email.getText();
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        // Check if the email is valid with regex
        Pattern pat = Pattern.compile(emailRegex);
        if (getEmail == null)
            return false;
        return pat.matcher(getEmail).matches();
    }
}
