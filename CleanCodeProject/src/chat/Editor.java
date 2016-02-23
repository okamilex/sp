package chat;


import javax.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class Editor {
    private List<Message> messageList;
    private PrintWriter logWriter;
    protected BufferedReader reader;
    protected String filename;
    private int addCount;
    private String name;

    public Editor(List<Message> messageList, PrintWriter logWriter, BufferedReader reader,  int addCount, String name) {
        this.messageList = messageList;
        this.logWriter = logWriter;
        this.reader = reader;
        this.addCount = addCount;
        this.name = name;
    }

    public void save() {
        int count = 0;
        if (!messageList.isEmpty()) {
            filename = "";
            System.out.println("Input: filename: ('chat', 'history')");
            try {
                filename = reader.readLine();
            } catch (IOException e) {
                System.out.println("Can't read this");
            }


            if (filename.equals("")) {
                String logXFileP = (new Date()).toString();
                StringTokenizer k = new StringTokenizer(logXFileP, ":", false);
                while (k.hasMoreTokens()) {
                    filename = filename + k.nextToken();
                    if (k.hasMoreTokens()) {
                        filename = filename + " ";
                    }
                }
            }
            filename = "history/" + filename + ".json";

            File jsonFile = new File(filename);
            try {
                FileWriter fileWriter = new FileWriter(jsonFile);

                JsonWriter jsonWriter = Json.createWriter(fileWriter);
                JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                for (Message aMessageList : messageList) {
                    arrayBuilder.add(
                            Json.createObjectBuilder()
                                    .add("id", aMessageList.getId())
                                    .add("author", aMessageList.getAuthor())
                                    .add("timestamp", aMessageList.getTimestamp().getTime())
                                    .add("message", aMessageList.getMessage())
                                    .build());
                    count++;
                }
                JsonArray jsonArray = arrayBuilder.build();
                jsonWriter.writeArray(jsonArray);
                jsonWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("save error");
            }
            System.out.println("History was saved.");
            logWriter.write((new Date()).toString() + " | " + "Save: " + filename + " |Successfully: " + count + " saved" + "\n");
        } else {
            System.out.println("There isn't any message.");
            logWriter.write((new Date()).toString() + " | " + "Save: " + filename + " |There isn't any message. " + "" + "\n");
        }
    }
    public void open() {
        int count = 0;
        System.out.println("Input: filename: ('chat', 'history')");
        String filenameHere;
        filenameHere = "";
        try {
            filenameHere = reader.readLine();
        } catch (IOException e) {
            System.out.println("Can't read this");
        }
        if (!filenameHere.equals("") || filename.equals("")) {
            filename = "history/" + filenameHere + ".json";
        }
        try {
            String jsonAllLines = Files.readAllLines(Paths.get(filename)).toString();
            JsonReader jsonReader = Json.createReader(new StringReader(jsonAllLines));
            JsonArray jsonArray = jsonReader.readArray();

            if (jsonArray.size() == 0) {
                System.out.println("File is empty.");
                logWriter.write((new Date()).toString() + " | " + "Open: " + filename + " |Empty file. " + "" + "\n");
            } else {
                JsonArray arrMes = jsonArray.getJsonArray(0);
                jsonReader.close();
                messageList.clear();
                for (int i = 0; i < arrMes.size(); i++) {
                    JsonObject newObj = arrMes.getJsonObject(i);
                    int id = newObj.getInt("id");
                    Message message = new Message(
                            id,
                            newObj.getString("author"),
                            new Date(newObj.getJsonNumber("timestamp").longValue()),
                            newObj.getString("message"));
                    count++;
                    System.out.println(message.toString());
                    messageList.add(message);
                }
                logWriter.write((new Date()).toString() + " | " + "Open: " + filename + " |Successfully: " + count + " opened" + "\n");
            }
        } catch (JsonException e) {
            System.out.println(e.getMessage());
            logWriter.write((new Date()).toString() + " | " + "Open: " + e.getMessage() + " |Exception " + "" + "\n");
        } catch (NoSuchFileException e) {
            System.out.println("There isn't file with filename: " + e.getMessage());
            logWriter.write((new Date()).toString() + " | " + "Open: " + e.getMessage() + " |There isn't file with filename " + "" + "\n");
        } catch (IOException e) {
            System.out.println("open error");
        }
    }
    public void delete() {
        while (true) {
            System.out.print("Input: message id:");
            int id = -1;
            try {
                id = Integer.valueOf(reader.readLine());
            } catch (IOException e) {
                System.out.println("Can't read this");
            }
            boolean isDeleted = false;
            for (int i = 0; i < messageList.size(); i++) {
                if (messageList.get(i).getId() == id) {
                    logWriter.write("Delete message: " + messageList.get(i).getMessage());
                    messageList.remove(i);
                    System.out.println((new Date()).toString() + " | " + "Message is successfully deleted");
                    logWriter.write(" |Successfully " + "" + "\n");
                    isDeleted = true;
                }
            }
            if (!isDeleted) {
                System.out.println("This id isn't match id of any existed message. Do you want to try again? Y/N");
                logWriter.write((new Date()).toString() + " | " + "Delete message: " + id + " |Wasn't found " + "" + "\n");
                String answer = "";
                try {
                    answer = reader.readLine();
                } catch (IOException e) {
                    System.out.println("Can't read this");
                }
                if (answer.equals("n")) {
                    logWriter.write("and exit \n");
                    break;
                }
                logWriter.write("and we will retry \n");
            } else {
                break;
            }
        }
    }
    public void add(String messageText) {
        int id = 0;
        if (!messageList.isEmpty()) {
            id = messageList.get(messageList.size() - 1).getId() + 1;
        }
        Date time = new Date();
        Message message = new Message(id, name, time, messageText);
        messageList.add(message);
        addCount++;
        System.out.println("Message was successfully added.");
        logWriter.write((new Date()).toString() + " | " + "Add message: " + messageText + " |Successfully " + "" + "\n");
    }

}
