package chat;

import javax.json.*;
import java.io.*;
import java.lang.*;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ChatConsole {
    Date startTime = new Date();
    String logFile = "";
    PrintWriter logWriter;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    List<Message> messageList = new ArrayList<>();
    String option = "";
    String name = "";
    String filename = "";
    int maxID = 0;
    boolean wantToExit = false;
    boolean fromDelete = false;


    public ChatConsole() throws IOException {
        String logXFileP = startTime.toString();
        StringTokenizer k = new StringTokenizer(logXFileP, ":", false);
        while (k.hasMoreTokens()) {
            logFile = logFile + k.nextToken();
            if (k.hasMoreTokens()) {
                logFile = logFile + " ";
            }
        }
        logFile = "log/" + logFile + ".txt";//".logfile";
        logWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));
        logWriter.write("___________" + startTime.toString() + " Started" + "___________" + "\n");

        setNameHere();
        System.out.println("Input: 'help' for help");
        while (!wantToExit) {
            option = reader.readLine();
            switch (option) {

                case "exit": {
                    exit();
                    break;
                }
                case "help": {
                    help();
                    break;
                }
                case "open": {
                    open();
                    break;
                }
                case "delete": {
                    delete();
                    break;

                }
                case "save": {
                    save();
                    break;
                }
                case "search": {
                    search();
                    break;
                }
                case "setName": {
                    setNameHere();
                }
                case "show": {
                    show();
                    break;
                }
                case "showTumbler": {
                    break;
                }
                case "showTime": {
                    showTme();
                    break;

                }
                default: {
                    if (option.length() > 140) {
                        System.out.println("Warning!!! Message is too long. We recommend to use lathe than 140 characters.");
                        logWriter.write((new Date()).toString() + " | " + "Warning!!! Message " + option + " is too long." + "" + "\n");

                        logWriter.flush();
                        return;
                    } else {
                        add(option);
                    }

                    break;
                }
            }

        }
        logWriter.close();

    }

    public void save() throws IOException {
        if (!messageList.isEmpty()) {

            System.out.println("Input: filename: ('chat', 'history')");
            filename = reader.readLine();
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

            fromDelete = false;
            File jsonFile = new File(filename);
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
            }
            JsonArray jsonArray = arrayBuilder.build();
            jsonWriter.writeArray(jsonArray);
            jsonWriter.close();
            System.out.println("History was saved.");
            logWriter.write((new Date()).toString() + " | " + "Save: " + filename + " |Successfully " + "" + "\n");
        } else {
            System.out.println("There isn't any message.");
            logWriter.write((new Date()).toString() + " | " + "Save: " + filename + " |Successfully " + "" + "\n");
        }
    }
    public void open() throws IOException {
        System.out.println("Input: filename: ('chat', 'history')");
        String filenameHere = reader.readLine();
        if (!filenameHere.equals("") || filename.equals("")) {
            filename = "history" + filenameHere + ".json";
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
                maxID = 0;
                for (int i = 0; i < arrMes.size(); i++) {
                    JsonObject newObj = arrMes.getJsonObject(i);
                    int id = newObj.getInt("id");
                    Message message = new Message(
                            id,
                            newObj.getString("author"),
                            new Date(newObj.getJsonNumber("timestamp").longValue()),
                            newObj.getString("message"));
                    if (id > maxID) {
                        maxID = id;
                    }
                    System.out.println(message.toString());
                    messageList.add(message);
                }
                logWriter.write((new Date()).toString() + " | " + "Open: " + filename + " |Successfully " + "" + "\n");
            }
        } catch (JsonException e) {
            System.out.println(e.getMessage());
            logWriter.write((new Date()).toString() + " | " +"Open: " + e.getMessage() + " |Exception " + "" + "\n");
        } catch (NoSuchFileException e) {
            System.out.println("There isn't file with filename: " + e.getMessage());
            logWriter.write((new Date()).toString() + " | " +"Open: " + e.getMessage() + " |There isn't file with filename " + "" + "\n");
        }
    }


    public void delete() throws IOException {
        while (true) {
            System.out.print("Input: message id:");

            int id = Integer.valueOf(reader.readLine());
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
                String answer = reader.readLine();
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
    public void add(String messageText) throws IOException {
        maxID++;
        int id = maxID;
        Date time = new Date();
        Message message = new Message(id, name, time, messageText);
        messageList.add(message);
        System.out.println("Message was successfully added.");
        logWriter.write((new Date()).toString() + " | " + "Add message: " + messageText + " |Successfully " + "" + "\n");
    }

    public void search() throws IOException {
        System.out.println("Input: Search by(author/key word/regular expression/all):");
        String searchOption = reader.readLine();
        switch (searchOption) {
            case "author":
            case "1": {
                searchByAuthor();
                break;
            }
            case "key word":
            case "2": {
                searchByKeyWord();
                break;
            }
            case "regular expression":
            case "3": {
                searchByRegularExpression();
                break;
            }
            case "all":
            default: {
                searchAll();


                break;
            }
        }
    }
    public void searchAll() throws IOException {
        boolean thereIsMessage = false;
        System.out.println("search for:");
        String forSearch = reader.readLine();
        for (Message it : messageList) {
            if (it.getAuthor().equals(forSearch)) {
                System.out.println(it.toString());
                thereIsMessage = true;
                continue;
            }
            if (it.getMessage().contains(forSearch)) {
                System.out.println(it.toString());
                thereIsMessage = true;
                continue;
            }
            Pattern p = Pattern.compile(forSearch);
            Matcher m = p.matcher(it.getMessage());
            if (m.find()) {
                thereIsMessage = true;
                System.out.println(it.toString());
            }
        }
        if (!thereIsMessage) {
            System.out.println("There isn't any message from this author");
            logWriter.write((new Date()).toString() + " | " + "Search by all: " + forSearch + " |Nothing was found " + "" + "\n");
        } else {
            logWriter.write((new Date()).toString() + " | " + "Search by all: " + forSearch + " |Successfully " + "" + "\n");
        }
    }
    public void searchByRegularExpression() throws IOException {
        boolean thereIsMessage = false;
        System.out.println("Input: regular expression");
        String expression = reader.readLine();

        for (Message it : messageList) {
            Pattern p = Pattern.compile(expression);
            Matcher m = p.matcher(it.getMessage());
            if (m.find()) {
                thereIsMessage = true;
                System.out.println(it.toString());
            }

        }
        if (!thereIsMessage) {
            System.out.println("There isn't any message with this regular expression");
            logWriter.write((new Date()).toString() + " | " + "Search By Regular Expression: " + expression + " |There isn't any message with this regular expression " + "" + "\n");
        } else {
            logWriter.write((new Date()).toString() + " | " + "Search By Regular Expression: " + expression + " |Successfully " + "" + "\n");
        }
    }
    public void searchByKeyWord() throws IOException {
        boolean thereIsMessage = false;
        System.out.println("Input: key word");
        String keyWord = reader.readLine();
        logWriter.write("Search By Key Word: " + keyWord + " |Successfully " + "" + "\n");
        for (Message it : messageList) {
            if (it.getMessage().contains(keyWord)) {
                System.out.println(it.toString());
                thereIsMessage = true;
            }
        }
        if (!thereIsMessage) {
            System.out.println("There isn't any message with this word");
            logWriter.write((new Date()).toString() + " | " + "Search By Key Word: " + keyWord + " |There isn't any message with this word " + "" + "\n");
        } else {
            logWriter.write((new Date()).toString() + " | " + "Search By Key Word: " + keyWord + " |Successfully " + "" + "\n");
        }

    }
    public void searchByAuthor() throws IOException {
        boolean thereIsMessage = false;
        System.out.println("Input: author name");
        String nameForSearch = reader.readLine();

        for (Message it : messageList) {
            if (it.getAuthor().equals(nameForSearch)) {
                System.out.println(it.toString());
                thereIsMessage = true;
            }
        }
        if (!thereIsMessage) {
            System.out.println("There isn't any message from this author");
            logWriter.write((new Date()).toString() + " | " + "Search By Author: " + nameForSearch + " |There isn't any message from this author " + "" + "\n");
        } else {
            logWriter.write((new Date()).toString() + " | " + "Search By Author: " + nameForSearch + " |Successfully " + "" + "\n");
        }
    }

    public void show() {
        if (messageList.size() > 0) {
            for (Message it : messageList) {
                System.out.println(it.toString());
            }
            logWriter.write((new Date()).toString() + " | " + "Show: " + " |Successfully " + "" + "\n");
        } else {
            System.out.println("There isn't any message");
            logWriter.write((new Date()).toString() + " | " + "Show: " + " |There isn't any message " + "" + "\n");
        }
    }
    public void showTme() throws IOException {
        Date timeFrom = null;
        Date timeTo = null;
        System.out.println("Input: time-from: dd/mm/yyyy hh:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            timeFrom = format.parse(reader.readLine());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Input: time-to: dd/mm/yyyy hh:mm:ss");
        try {
            timeTo = format.parse(reader.readLine());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        boolean thereIsMessage = false;
        for (Message iterator : messageList) {
            if (timeFrom != null && timeTo != null && iterator.getTimestamp().after(timeFrom) && iterator.getTimestamp().before(timeTo)) {
                System.out.println(iterator.toString());
                thereIsMessage = true;
            }
        }
        if (!thereIsMessage) {
            System.out.println("There isn't any message in this period of time");
            assert timeFrom != null;
            assert timeTo != null;
            logWriter.write((new Date()).toString() + " | " + "Search in time: " + timeFrom.toString() + "-" + timeTo.toString() + " |There isn't any message in this period of time " + "" + "\n");
        } else {
            logWriter.write((new Date()).toString() + " | " + "Search in time: " + timeFrom.toString() + "-" + timeTo.toString() + " |Successfully " + "" + "\n");
        }
    }

    public void help() {
        logWriter.write((new Date()).toString() + " | " + "Help: " + " |Successfully " + "" + "\n");
        System.out.println("Input: 'add'         to add a message");
        System.out.println("Input: 'delete'      to delete a message");
        System.out.println("Input: 'exit'        to exit");
        System.out.println("Input: 'help'        for help");
        System.out.println("Input: 'open'        to load a dialog from file");
        System.out.println("Input: 'save'        to save your dialog into file");
        System.out.println("Input: 'search'      to search for a message");
        System.out.println("Input: 'setName'     to set up your name");
        System.out.println("Input: 'show'        for message history");
        System.out.println("Input: 'showTime'    for message history of a time period");
        System.out.println("Input: 'showTumbler' to turn on/turn off auto-history");
        System.out.println("Input: <message>     to add a message");
    }
    public void setNameHere() throws IOException {
        System.out.println("Input: name");
        name = reader.readLine();
        logWriter.write((new Date()).toString() + " | " + "Set name: " + name + " |Successfully " + "" + "\n");
    }
    public void exit() {
        wantToExit = true;
        Date exitTime = new Date();
        logWriter.write("___________" + exitTime.toString() + " Exit   " + "___________");
    }
}
