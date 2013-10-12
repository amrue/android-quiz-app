package edu.sjsu.cs175.hw03;

import edu.sjsu.cs175.hw03.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        final Question question = (Question) getIntent().getSerializableExtra(
                "question");
        TextView textView = (TextView) findViewById(R.id.questionTextHeader);
        textView.setText(question.getText());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                QuestionActivity.this, R.layout.choicetext_item, question.getChoices());;
                
        ListView listView = (ListView) findViewById(R.id.choices);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
              int position, long id) {
                question.answer(position);
                Context context = getApplicationContext();
                CharSequence text = question.getStatus() == Question.Status.CORRECT ? "Good job!" : "Try again!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show(); 
                Intent result = new Intent();
                result.putExtra("response", position);
                setResult(Activity.RESULT_OK, result);
                finish();              
            }});
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
