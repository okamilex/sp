package chat;


import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;

public class ChatConsole {
    protected Date startTime;
    private PrintWriter logWriter;
    private List<Message> messageList;
    protected BufferedReader reader;
    protected String logFile;
    protected String option;
    private String name;
    private int addCount = 0;
    private boolean wantToExit = false;


    public ChatConsole()  {
        messageList = new ArrayList<>();
        reader = new BufferedReader(new InputStreamReader(System.in));
        startWork();

        optionReader();
    }

    public void startWork() {
        startTime = new Date();
        String logXFileP = startTime.toString();
        StringTokenizer k = new StringTokenizer(logXFileP, ":", false);
        logFile = "";
        while (k.hasMoreTokens()) {
            logFile = logFile + k.nextToken();
            if (k.hasMoreTokens()) {
                logFile = logFile + " ";
            }
        }
        logFile = "log/" + logFile + ".txt";//".logfile";
        try {
            logWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));
        } catch (IOException e) {
            System.out.println("No logfile");
        }
        logWriter.write("___________" + startTime.toString() + " Started" + "___________" + "\n");
        setNameHere();
    }
    public void optionReader()  {
        System.out.println("Input: 'help' for help");
        while (!wantToExit) {
            option = "";
            try {
                option = reader.readLine();
            } catch (IOException e) {
                System.out.println("Can't read this");
            }
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
                    new Editor(messageList,logWriter,reader,addCount,name).open();
                    break;
                }
                case "delete": {
                    new Editor(messageList,logWriter,reader,addCount,name).delete();
                    break;

                }
                case "save": {
                    new Editor(messageList,logWriter,reader,addCount,name).save();
                    break;
                }
                case "search": {
                    new Searcher(messageList,logWriter,reader);
                    break;
                }
                case "setName": {
                    setNameHere();
                }
                case "show": {
                    new Shower(messageList,logWriter,reader).show();
                    break;
                }
                case "showTumbler": {
                    break;
                }
                case "showTime": {
                    new Shower(messageList,logWriter,reader).showTme();
                    break;

                }
                default: {
                    if (option.length() > 140) {
                        System.out.println("Warning!!! Message is too long. We recommend to use lathe than 140 characters.");
                        logWriter.write((new Date()).toString() + " | " + "Warning!!! Message " + option + " is too long." + "" + "\n");

                        logWriter.flush();
                        return;
                    } else {
                        new Editor(messageList,logWriter,reader,addCount,name).add(option);
                    }

                    break;
                }
            }

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
    public void setNameHere() {
        name = "";
        System.out.println("Input: name");
        try {
            name = reader.readLine();
        } catch (IOException e) {
            System.out.println("Can't read this");
        }

        logWriter.write((new Date()).toString() + " | " + "Set name: " + name + " |Successfully " + "" + "\n");
    }
    public void exit() {
        wantToExit = true;
        Date exitTime = new Date();
        logWriter.write("___________" + exitTime.toString() + " Exit   " + "___________\n");
        logWriter.write(addCount + "message was added");
        try {
            reader.close();
            logWriter.close();
        } catch (IOException e){
            System.out.println("Reader close error");
        }

    }


}
