package Account;

public class Message {
    String sentByWho;
    String message;
    public Message(String message, String sentByWho) {
        this.message = message;
        this.sentByWho = sentByWho;
    }
    public String getMessage() {
        return message;
    }
    public String getSentByWho() {
        return sentByWho;
    }
}
