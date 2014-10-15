package com.example.felix.medienbibliothek;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class PersonenHinzufuegenActivity extends Activity implements View.OnClickListener
{
    EditText textViewNachname, textViewVorname;
    Button buttonPersonHinzufuegen;
    public final static String EXTRA_MESSAGE_NACHNAME = "com.example.felix.medienbibliothek.MESSAGE_NACHNAME";
    public final static String EXTRA_MESSAGE_VORNAME = "com.example.felix.medienbibliothek.MESSAGE_VORNAME";
    String nachname, vorname;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personen_hinzufuegen);
        setTitle("Medienbibliothek");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setIcon(R.drawable.app_icon);

        textViewNachname = (EditText) findViewById(R.id.textNachname);
        textViewVorname = (EditText) findViewById(R.id.textVorname);
        buttonPersonHinzufuegen = (Button) findViewById(R.id.buttonPersonHinzufuegen);
        buttonPersonHinzufuegen.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.personen_hinzufuegen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_clear)
        {
            textViewNachname.setText("");
            textViewVorname.setText("");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view)
    {
        if(view.getId() == R.id.buttonPersonHinzufuegen)
        {

            Intent intent = new Intent(this, MainActivity.class);
            nachname = textViewNachname.getText().toString();
            vorname = textViewVorname.getText().toString();

            if(!nachname.equals("") && !vorname.equals(""))
            {
                intent.putExtra(EXTRA_MESSAGE_NACHNAME, nachname);
                intent.putExtra(EXTRA_MESSAGE_VORNAME, vorname);
                startActivity(intent);
                this.finish();
            }
            else
            {
                Toast.makeText(this, "Es fehlen Informationen!", Toast.LENGTH_SHORT).show();
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
