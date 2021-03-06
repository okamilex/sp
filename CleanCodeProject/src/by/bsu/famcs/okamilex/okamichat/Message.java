/**
 * Created by alex_ on 14.02.2016.
 */
package by.bsu.famcs.okamilex.okamichat;
import java.util.Date;

public class Message {
    private int id;
    private String author;
    private Date timestamp;
    private String message;

    public Message(int id, String author, Date timestamp, String message) {
        this.id = id;
        this.message = message;
        this.author = author;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "id : " + id + " | " + timestamp + " | " + author + ": " + message;
    }

    private static Message ourInstance = new Message();

    public static Message getInstance() {
        return ourInstance;
    }

    private Message() {
    }
}
