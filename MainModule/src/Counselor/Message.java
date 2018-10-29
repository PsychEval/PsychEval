package Counselor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Message {
    private String timeStamp;
    private String message;
    public Message(String message) {
        this.message = message;
        this.timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
    }
    public String getMessage() {
        return message;
    }
    public String getTimeStamp() {
        return timeStamp;
    }
}
