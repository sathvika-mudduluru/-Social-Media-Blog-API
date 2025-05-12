package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public Message createMessage(Message msg) {
        return messageDAO.create(msg);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAll();
    }

    public Message getMessageById(int id) {
        return messageDAO.findById(id);
    }

    public Message deleteMessageById(int id) {
        return messageDAO.deleteById(id);
    }

    public Message updateMessage(int id, String newText) {
        return messageDAO.updateText(id, newText);
    }

    public List<Message> getMessagesByUserId(int accountId) {
        return messageDAO.findByUserId(accountId);
    }
}
