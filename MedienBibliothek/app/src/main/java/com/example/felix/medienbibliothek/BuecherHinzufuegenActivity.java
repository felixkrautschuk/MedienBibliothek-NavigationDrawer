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


public class BuecherHinzufuegenActivity extends Activity implements View.OnClickListener
{
    EditText textViewTitel, textViewAutor;
    Button buttonBuchHinzufuegen;
    public final static String EXTRA_MESSAGE_TITEL = "com.example.felix.medienbibliothek.MESSAGE_TITEL";
    public final static String EXTRA_MESSAGE_AUTOR = "com.example.felix.medienbibliothek.MESSAGE_AUTOR";
    public final static String EXTRA_MESSAGE_VIEWPAGER_INDEX = "com.example.felix.medienbibliothek.MESSAGE_VIEWPAGER_INDEX";
    String titel, autor;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buecher_hinzufuegen);
        setTitle("Medienbibliothek");
        getActionBar().setIcon(R.drawable.app_icon);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        textViewTitel = (EditText) findViewById(R.id.textBuchTitel);
        textViewAutor = (EditText) findViewById(R.id.textBuchAutor);
        buttonBuchHinzufuegen = (Button) findViewById(R.id.buttonBuchHinzufuegen);
        buttonBuchHinzufuegen.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.buecher_hinzufuegen, menu);
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
            textViewTitel.setText("");
            textViewAutor.setText("");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view)
    {
        if(view.getId() == R.id.buttonBuchHinzufuegen)
        {
            Intent intent = new Intent(this, MainActivity.class);
            titel = textViewTitel.getText().toString();
            autor = textViewAutor.getText().toString();

            if(!titel.equals("") && !autor.equals(""))
            {
                intent.putExtra(EXTRA_MESSAGE_TITEL, titel);
                intent.putExtra(EXTRA_MESSAGE_AUTOR, autor);
                intent.putExtra(EXTRA_MESSAGE_VIEWPAGER_INDEX, "1");
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
        intent.putExtra(EXTRA_MESSAGE_VIEWPAGER_INDEX, "1");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }
}
