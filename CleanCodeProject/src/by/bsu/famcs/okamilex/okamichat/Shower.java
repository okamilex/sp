package by.bsu.famcs.okamilex.okamichat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class Shower {
    private List<Message> messageList;
    private PrintWriter logWriter;
    protected BufferedReader reader;

    public Shower(List<Message> messageList, PrintWriter logWriter,BufferedReader reader) {
        this.messageList = messageList;
        this.logWriter = logWriter;
        this.reader = reader;
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
    public void showTme() {
        Date timeFrom = null;
        Date timeTo = null;
        System.out.println("Input: time-from: dd/mm/yyyy hh:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        try {
            timeFrom = format.parse(reader.readLine());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Can't read this");
        }
        System.out.println("Input: time-to: dd/mm/yyyy hh:mm:ss");
        try {
            timeTo = format.parse(reader.readLine());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Can't read this");
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
}
