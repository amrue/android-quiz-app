package edu.sjsu.cs175.hw03;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class Quiz {
    List<Question> questions = new ArrayList<Question>();
    
    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }
    
    public void readFromXml(InputStream in) throws IOException {
        class QuestionHandler extends BasicHandler {
            private boolean correctChoice;
            Question q = new Question();
            @Override
            public void startElement(String uri, String localName,
                    String qName, Attributes attributes) throws SAXException {
                super.startElement(uri, localName, qName, attributes);
                    correctChoice = "true".equals(attributes.getValue("value"));
            }
            @Override
            public void endElement(String uri, String localName, String qName)
                    throws SAXException {
                if (qName.equals("text")) 
                    questions.add(new Question(lastString()));
                else if (qName.equals("choice")) 
                    questions.get(questions.size() - 1).addChoice(lastString(), correctChoice);
            }
        }
        
        try {       
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser(); 
            parser.parse(in, new QuestionHandler());
        } catch (Exception ex) {
            IOException ioEx = new IOException(ex.getMessage());
            ioEx.initCause(ex);
            throw ioEx;
        }       
        
        
    }
    public int getScore() {
        int score = 0;
        for (Question q : questions) 
            if (q.getStatus() == Question.Status.CORRECT) score++;
        return score;
    }
    
}
