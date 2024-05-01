package u_cece.todolist;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class ServerAPI {

    private String hostBaseURL;
    private final Gson gson = new Gson();

    private URL makeSubURL(String s) throws Exception {
        return new URI(hostBaseURL + s).toURL();
    }

    public ServerAPI(String host) {
        hostBaseURL = host;
    }

    public void setHost(String host) {
        hostBaseURL = host;
    }

    public String getHost() {
        return hostBaseURL;
    }

    private static class AddTaskPayload {
        private String name;
        private String description;
        public AddTaskPayload() {
        }
        public AddTaskPayload(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    public void addTask(String name, String description) throws Exception {
        sendPostWithPayload("/tasks/add",
                new AddTaskPayload(name, description));
    }

    private static class RemoveTaskPayload {
        private String name;
        public RemoveTaskPayload() {
        }
        public RemoveTaskPayload(String name) {
            this.name = name;
        }
    }

    public void removeTask(String name) throws Exception {
        sendPostWithPayload("/tasks/remove",
                new RemoveTaskPayload(name));
    }

    private void sendPostWithPayload(String to, Object payload) throws Exception {
        URL url = makeSubURL(to);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setReadTimeout(5000);
        conn.setConnectTimeout(5000);

        String json = gson.toJson(payload);

        byte[] payloadBytes = json.getBytes();
        conn.setFixedLengthStreamingMode(payloadBytes.length);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.connect();
        conn.getResponseCode();
        try (OutputStream os = conn.getOutputStream()) {
            os.write(payloadBytes);
        }
        StringBuilder response = new StringBuilder();
        try (InputStream is = conn.getInputStream();
             InputStreamReader isr = new InputStreamReader(is)) {
            Scanner scanner = new Scanner(isr);
            while (scanner.hasNextLine())
                response.append(scanner.nextLine());
        }
        JsonElement element = gson.toJsonTree(response.toString());
        JsonObject object = element.getAsJsonObject();
        if (!object.get("success").getAsBoolean())
            throw new Exception(object.get("reason").toString());
    }
}
