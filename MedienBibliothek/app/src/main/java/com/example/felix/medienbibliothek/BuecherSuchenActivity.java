package com.example.felix.medienbibliothek;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class BuecherSuchenActivity extends Activity implements View.OnClickListener
{
    public final static String EXTRA_MESSAGE_SUCHE_BUCH = "com.example.felix.medienbibliothek.MESSAGE_SUCHE_BUCH";
    public final static String EXTRA_MESSAGE_SUCHE_VIEWPAGER_INDEX = "com.example.felix.medienbibliothek.MESSAGE_SUCHE_VIEWPAGER_INDEX";
    EditText textSuchPattern;
    Button buttonSucheBuch;
    String suchbegriff;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buecher_suchen);
        setTitle("Medienbibliothek");
        getActionBar().setIcon(R.drawable.app_icon);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        textSuchPattern = (EditText) findViewById(R.id.textSuchPattern);
        buttonSucheBuch = (Button) findViewById(R.id.buttonSucheBuch);
        buttonSucheBuch.setOnClickListener(this);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE_SUCHE_VIEWPAGER_INDEX, "1");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }

    public void onClick(View view)
    {
        if(view.getId() == R.id.buttonSucheBuch)
        {
            Intent intent = new Intent(this, MainActivity.class);
            suchbegriff = textSuchPattern.getText().toString();

            if(!suchbegriff.equals(""))
            {
                intent.putExtra(EXTRA_MESSAGE_SUCHE_BUCH, suchbegriff);
                intent.putExtra(EXTRA_MESSAGE_SUCHE_VIEWPAGER_INDEX, "1");
                startActivity(intent);
                this.finish();
            }
            else
            {
                Toast.makeText(this, "Bitte Suchbegriff eingeben!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
