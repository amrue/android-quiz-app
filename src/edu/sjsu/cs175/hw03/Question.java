package edu.sjsu.cs175.hw03;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;

import org.xml.sax.SAXException;

import android.graphics.Color;

public class Question implements Serializable {
    private String text;
    private List<String> choices = new ArrayList<String>();
    private int correctChoice;
    enum Status { UNANSWERED, INCORRECT, CORRECT };
    private Status status = Status.UNANSWERED; 
    
    public Question() {
    } 
    
    public Question(String text, String... answers) {
        this.text = text;
        int i = 0;
        for (String line : answers) {
            if (line.startsWith("*")) {
                correctChoice = i;
                line = line.substring(1);
            }
            choices.add(line);
            i++;
        }
    }

    public void addChoice(String choice, boolean isCorrect) {
        if (isCorrect) correctChoice = choices.size();
        choices.add(choice);        
    }

    public List<String> getChoices() {
        return Collections.unmodifiableList(choices);
    }

    public String getText() {
        return text;
    }
    

    public Status getStatus() { return status; }
    public void answer(int choice) { status = choice == correctChoice ? Status.CORRECT : Status.INCORRECT; } 

}
