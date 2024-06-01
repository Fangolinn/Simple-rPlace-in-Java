package pl.wit.kowalcj1.java_place;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextArea;

public class ChatHistory extends JTextArea {
    Set<Message> messages = new HashSet<>();

    public void addMessage(Message message) {
        if (!messages.contains(message)) {
            messages.add(message);
            this.append(message.toString() + System.lineSeparator());
        }
    }
}
