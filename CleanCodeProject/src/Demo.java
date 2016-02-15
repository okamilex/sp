/**
 * Created by alex_ on 14.02.2016.
 */
import javax.json.*;
import java.io.*;
import java.lang.*;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.util.logging.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.sql.Time;
import java.sql.Timestamp;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Demo {

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    static ArrayList<Message> messageList = new ArrayList<Message>();
    static String option = "";
    static String name = "";
    static String filename = "";
    static int maxID = 0;
    static boolean wantToExit = false;
    static boolean fromDelete = false;



public static void main(String[] args) throws IOException {



    System.out.println("Input: your name");
    name = reader.readLine();
    System.out.println("Input: 'help' for help");
    while (!wantToExit) {
        option = reader.readLine();
        switch (option) {
            case "add": {
                System.out.println("Input: message:");
                String messageText = reader.readLine();
                maxID++;
                int id = maxID;
                Date date = new Date();
                Message message = new Message(id, name, date, messageText);
                messageList.add(message);
                System.out.println("Message was successfully added.");
                break;
            }
            case "delete": {
                while (true) {
                    System.out.print("Input: message id:");

                    int id = Integer.valueOf(reader.readLine());
                    boolean isDeleted = false;
                    for (int i = 0; i < messageList.size(); i++) {
                        if (messageList.get(i).getId() == id) {
                            messageList.remove(i);
                            System.out.println("Message is successfully deleted");
                            isDeleted = true;
                        }
                    }
                    if (isDeleted == false) {
                        System.out.println("This id isn't match id of any existed message. Do you want to try again? Y/N");
                        String answer = reader.readLine();
                        if (answer.equals("n")) {
                            break;
                        }
                    } else {
                        if(filename != "")
                        {
                            fromDelete = true;
                            option = "save";
                        }
                        break;
                    }
                }
                break;
            }
            case "exit": {
                wantToExit = true;
                break;
            }
            case "help": {
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
                break;
            }
            case "open": {
                System.out.println("Input: filename: ('chat', 'history')");
                filename = reader.readLine();
                if (filename == "") {
                    filename = "chatHistory";
                }
                filename = filename + ".json";

                try {
                    String jsonAllLines = Files.readAllLines(Paths.get(filename)).toString();
                    JsonReader jsonReader = Json.createReader(new StringReader(jsonAllLines));
                    JsonArray jsonArray = jsonReader.readArray();

                    if (jsonArray.size() == 0) {
                        System.out.println("File is empty.");
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
                            if(id > maxID)
                            {
                                maxID = id;
                            }
                            messageList.add(message);
                        }
                        for (Message it : messageList) {
                            System.out.println(it.toString());
                        }
                        break;
                    }
                }
                catch (JsonException e) {
                    System.out.println(e.getMessage());
                }
                catch (NoSuchFileException e)
                {
                    System.out.println("There isn't file with filename: "+e.getMessage());
                }
                break;




            }
            case "save": {
                if (!messageList.isEmpty()) {
                    if (!fromDelete) {
                        System.out.println("Input: filename: ('chat', 'history')");
                        filename = reader.readLine();
                        if (filename == "") {
                            filename = "chatHistory";
                        }
                    filename = filename + ".json";
                    }
                    fromDelete = false;
                    File jsonFile = new File(filename);
                    FileWriter fileWriter = new FileWriter(jsonFile);
                    JsonWriter jsonWriter = Json.createWriter(fileWriter);
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                    for (int i = 0; i < messageList.size(); i++) {
                        arrayBuilder.add(
                                Json.createObjectBuilder()
                                        .add("id", messageList.get(i).getId())
                                        .add("author", messageList.get(i).getAuthor())
                                        .add("timestamp", messageList.get(i).getTimestamp().getTime())
                                        .add("message", messageList.get(i).getMessage())
                                        .build());
                    }
                    JsonArray jsonArray = arrayBuilder.build();
                    jsonWriter.writeArray(jsonArray);
                    jsonWriter.close();
                    System.out.println("History was saved.");
                    break;
                } else
                    System.out.println("There isn't any message.");
                break;
            }
            case "search": {
                System.out.println("Input: Search by(author/key word/regular expression):");
                String searchOption = reader.readLine();
                switch (searchOption) {
                    case "author":
                    case "1": {
                        boolean thereIsMessage = false;
                        System.out.println("Input: author name");
                        String nameForSearch = reader.readLine();
                        for (Message it : messageList) {
                            if (it.getAuthor().equals(nameForSearch)) {
                                System.out.println(it.toString());
                                thereIsMessage = true;
                            }
                        }
                        if (thereIsMessage == false) {
                            System.out.println("There isn't any message from this author");
                        }
                        break;
                    }
                    case "key word":
                    case "2": {
                        boolean thereIsMessage = false;
                        System.out.println("Input: key word");
                        String keyWord = reader.readLine();
                        for (Message it : messageList) {
                            if (it.getMessage().contains(keyWord)) {
                                System.out.println(it.toString());
                                thereIsMessage = true;
                            }
                        }
                        if (thereIsMessage == false) {
                            System.out.println("There isn't any message with this word");
                        }
                        break;
                    }
                    case "regular expression":
                    case "3": {
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
                        if (thereIsMessage == false) {
                            System.out.println("There isn't any message with this regular expression");
                        }
                        break;
                    }
                    case "all":
                    default: {
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
                        if (thereIsMessage == false) {
                            System.out.println("There isn't any message from this author");
                        }


                        break;
                    }
                }
                break;
            }
            case "setName": {
                System.out.println("Input: name");
                name = reader.readLine();
            }
            case "show": {
                if (messageList.size() > 0) {
                    for (Message it : messageList) {
                        System.out.println(it.toString());
                    }
                    break;
                } else {
                    System.out.println("There isn't any message");
                    break;
                }
            }
            case "showTumbler": {
                break;
            }
            case "showTime": {
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
                    if (iterator.getTimestamp().after(timeFrom) && iterator.getTimestamp().before(timeTo)) {
                        System.out.println(iterator.toString());
                        thereIsMessage = true;
                    }
                }
                if (thereIsMessage == false)
                    System.out.println("There isn't any message in this period of time");
                break;

            }
            default: {
                int id = 0;
                if (!messageList.isEmpty()) {
                    id = messageList.get(messageList.size() - 1).getId() + 1;
                }
                Date date = new Date();
                Message message = new Message(id, name, date, option);
                messageList.add(message);
                System.out.println("Message was successfully added.");
                break;
            }
        }

    }
}
}
