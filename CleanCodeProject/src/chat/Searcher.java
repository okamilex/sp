package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.regex.PatternSyntaxException;

public class Searcher {
    private List<Message> messageList;
    private PrintWriter logWriter;
    protected BufferedReader reader;

    public Searcher(List<Message> messageList, PrintWriter logWriter,BufferedReader reader) {
        this.messageList = messageList;
        this.logWriter = logWriter;
        this.reader = reader;
        search();
    }

    public void search() {
        System.out.println("Input: Search by(author/key word/regular expression/all):");
        String searchOption = "";
        try {
            searchOption = reader.readLine();
        } catch (IOException e) {
            System.out.println("Can't read this");
        }
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

    public void searchAll() {
        boolean thereIsMessage = false;
        System.out.println("search for:");
        String forSearch;
        forSearch = "";
        boolean incorrectExpression = false;
        try {
            forSearch = reader.readLine();
        } catch (IOException e) {
            System.out.println("Can't read this");
        }
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
            try {
                Pattern p = Pattern.compile(forSearch);
                Matcher m = p.matcher(it.getMessage());
                if (m.find()) {
                    thereIsMessage = true;
                    System.out.println(it.toString());
                }
            } catch (PatternSyntaxException e) {
                if (!incorrectExpression) {
                    System.out.println("Incorrect expression: \n" + e.getMessage());
                    incorrectExpression = true;
                }
            }

        }
        if (!thereIsMessage) {
            System.out.println("There isn't any message from this author");
            logWriter.write((new Date()).toString() + " | " + "Search by all: " + forSearch + " |Nothing was found " + "" + "\n");
        } else {
            logWriter.write((new Date()).toString() + " | " + "Search by all: " + forSearch + " |Successfully " + "" + "\n");
        }
    }
    public void searchByRegularExpression() {
        boolean thereIsMessage = false;
        boolean incorrectExpression = false;
        System.out.println("Input: regular expression");

        String expression;
        expression = "";
        try {
            expression = reader.readLine();
        } catch (IOException e) {
            System.out.println("Can't read this");
        }

        for (Message it : messageList) {
            try {
                Pattern p = Pattern.compile(expression);
                Matcher m = p.matcher(it.getMessage());
                if (m.find()) {
                    thereIsMessage = true;
                    System.out.println(it.toString());
                }
            } catch (PatternSyntaxException e) {
                System.out.println("Incorrect expression: \n" + e.getMessage());
                incorrectExpression = true;
            }

        }
        if ((!thereIsMessage) && (!incorrectExpression)) {
            System.out.println("There isn't any message with this regular expression");
            logWriter.write((new Date()).toString() + " | " + "Search By Regular Expression: " + expression + " |There isn't any message with this regular expression " + "" + "\n");
        } else {
            logWriter.write((new Date()).toString() + " | " + "Search By Regular Expression: " + expression + " |Successfully " + "" + "\n");
        }
    }
    public void searchByKeyWord() {
        boolean thereIsMessage = false;
        System.out.println("Input: key word");

        String keyWord;
        keyWord = "";
        try {
            keyWord = reader.readLine();
        } catch (IOException e) {
            System.out.println("Can't read this");
        }
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
    public void searchByAuthor() {
        boolean thereIsMessage = false;
        System.out.println("Input: author name");
        String nameForSearch;
        nameForSearch = "";
        try {
            nameForSearch = reader.readLine();
        } catch (IOException e) {
            System.out.println("Can't read this");
        }

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
}

