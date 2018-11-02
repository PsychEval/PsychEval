package Account;

import java.util.List;

public class MessageInfo {
    String counselorEmail;
    String parentEmail;
    List<Message> messages;
    public MessageInfo(String counselorEmail, String parentEmail, List<Message> messages) {
        this.counselorEmail = counselorEmail;
        this.parentEmail = parentEmail;
        this.messages = messages;
    }
    public String getCounselorEmail() {
        return counselorEmail;
    }
    public String getParentEmail() {
        return parentEmail;
    }
    public List<Message> getMessages() {
        return messages;
    }
}
