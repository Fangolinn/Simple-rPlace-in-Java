package pl.wit.kowalcj1.java_place;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable{
    private UUID uuid;
    private String username;
    private String content;

    public Message(String username, String content){
        this.uuid = UUID.randomUUID();
        this.username = username;
        this.content = content;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", username, content);
    }
}
