package edu.sjsu.cs175.hw03;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import edu.sjsu.cs175.hw03.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String QUESTION_TEXT_URL = "http://horstmann.com/sjsu/fall2013/cs175/hw03/quiz.xml";
    Quiz result = new Quiz();      
    Question question = new Question();
    int s = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        new AsyncTask<String, Void, Quiz>() {
            @Override
            protected Quiz doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    result.readFromXml(url.openStream());
                    //Scanner in = new Scanner(url.openStream());
                    //result.read(in);                    
                    //in.close();
                } catch (IOException e) {
                    Log.e(getClass().toString(), e.getMessage());
                }
                return result;
            }
            @Override
            protected void onPostExecute(final Quiz quiz) {
                MainActivity.this.result = quiz;
                ArrayAdapter<Question> adapter = new ArrayAdapter<Question>(
                        MainActivity.this, R.layout.questiontext_item, quiz.getQuestions()) {
                            @Override
                            public View getView(int position, View v, ViewGroup parent) {
                                LayoutInflater inflater = LayoutInflater.from(getContext());;
                                Question q = getItem(position);
                                if (v == null) v = inflater.inflate(R.layout.questiontext_item, null);
                                TextView textView = (TextView) v.findViewById(R.id.questionText);
                                textView.setText(q.getText());
                                if (q.getStatus() == Question.Status.INCORRECT)
                                    textView.setTextColor(Color.RED);
                                else if (q.getStatus() == Question.Status.CORRECT)
                                    textView.setTextColor(Color.GREEN);
                                else 
                                    textView.setTextColor(Color.BLACK);
                                return v;
                              }
                            };
                ListView listView = (ListView) findViewById(R.id.questions);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                      int position, long id) {
                        Question q = quiz.getQuestions().get(position);
                        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                        intent.putExtra("question", q);
                        startActivityForResult(intent,position);
                    }});
            }
        }.execute(QUESTION_TEXT_URL);
        
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        
        //int val = data.getIntExtra("pos", 0);
        //int iVal = (int) getIntent().getIntExtra("neg", 0);
        //int cVal = data.getIntExtra("color", Color.GRAY);
        if(resultCode == RESULT_OK) {
            int response = data.getIntExtra("response", -1);
           result.getQuestions().get(requestCode).answer(response);
            ListView listView = (ListView) findViewById(R.id.questions);
            listView.invalidateViews();
            TextView tv = (TextView) findViewById(R.id.textView1);
            tv.setText("Score: "+result.getScore());        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
