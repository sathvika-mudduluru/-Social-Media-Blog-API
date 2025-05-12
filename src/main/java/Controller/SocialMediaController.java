package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import DAO.AccountDAO;
import DAO.MessageDAO;

import java.util.List;
import java.util.Map;

public class SocialMediaController {



    private final AccountDAO accountDAO = new AccountDAO();
    private final MessageDAO messageDAO = new MessageDAO();

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // 1. Register
        app.post("/register", this::registerHandler);

        // 2. Login
        app.post("/login", this::loginHandler);

        // 3. Post a message
        app.post("/messages", this::postMessageHandler);

        // 4. Get all messages
        app.get("/messages", this::getAllMessagesHandler);

        // 5. Get message by ID
        app.get("/messages/{id}", this::getMessageByIdHandler);

        // 6. Delete message by ID
        app.delete("/messages/{id}", this::deleteMessageHandler);

        // 7. Update message by ID
        app.patch("/messages/{id}", this::updateMessageHandler);

        // 8. Get all messages for a user
        app.get("/accounts/{id}/messages", this::getMessagesByUserHandler);

        return app;
    }

    // ===== HANDLERS =====

    private void registerHandler(Context ctx) {
        Account a = ctx.bodyAsClass(Account.class);
        if (a.getUsername().isBlank() || a.getPassword().length() < 4 || accountDAO.existsByUsername(a.getUsername())) {
            ctx.status(400); return;
        }
        Account created = accountDAO.create(a);
        ctx.json(created);
    }

    private void loginHandler(Context ctx) {
        Account input = ctx.bodyAsClass(Account.class);
        Account stored = accountDAO.findByUsername(input.getUsername());
        if (stored == null || !stored.getPassword().equals(input.getPassword())) {
            ctx.status(401); return;
        }
        ctx.json(stored);
    }

    private void postMessageHandler(Context ctx) {
        Message msg = ctx.bodyAsClass(Message.class);
        if (msg.getMessage_text().isBlank() || msg.getMessage_text().length() > 255 || !accountDAO.existsById(msg.getPosted_by())) {
            ctx.status(400); return;
        }
        msg.setTime_posted_epoch(1669947792L);
        Message saved = messageDAO.create(msg);
        ctx.json(saved);
    }

    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageDAO.getAll();
        ctx.json(messages);
    }

    private void getMessageByIdHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Message m = messageDAO.findById(id);
        if (m != null) ctx.json(m);
        else ctx.json(""); // empty response if not found
    }

    private void deleteMessageHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Message deleted = messageDAO.deleteById(id);
        if (deleted != null) ctx.json(deleted);
        else ctx.json(""); // empty response if already gone
    }

    private void updateMessageHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Map<String, String> updates = ctx.bodyAsClass(Map.class);
        String newText = updates.get("message_text");
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            ctx.status(400); return;
        }
        Message updated = messageDAO.updateText(id, newText);
        if (updated != null) ctx.json(updated);
        else ctx.status(400);
    }

    private void getMessagesByUserHandler(Context ctx) {
        int userId = Integer.parseInt(ctx.pathParam("id"));
        List<Message> messages = messageDAO.findByUserId(userId);
        ctx.json(messages);
    }
}