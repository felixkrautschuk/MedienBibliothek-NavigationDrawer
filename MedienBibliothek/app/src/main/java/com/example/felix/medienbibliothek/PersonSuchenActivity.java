package com.example.felix.medienbibliothek;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class PersonSuchenActivity extends Activity implements View.OnClickListener
{
    public final static String EXTRA_MESSAGE_SUCHE_PERSON = "com.example.felix.medienbibliothek.MESSAGE1_SUCHE_PERSON";
    EditText textSuchPattern;
    Button buttonSuchePerson;
    String suchbegriff;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_suchen);
        setTitle("Medienbibliothek");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        textSuchPattern = (EditText) findViewById(R.id.textSuchPattern);
        buttonSuchePerson = (Button) findViewById(R.id.buttonSuchePerson);
        buttonSuchePerson.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        if(view.getId() == R.id.buttonSuchePerson)
        {
            Intent intent = new Intent(this, MainActivity.class);
            suchbegriff = textSuchPattern.getText().toString();

            if(!suchbegriff.equals(""))
            {
                intent.putExtra(EXTRA_MESSAGE_SUCHE_PERSON, suchbegriff);
                startActivity(intent);
                this.finish();
            }
            else
            {
                Toast.makeText(this, "Bitte Suchbegriff eingeben!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }
}
