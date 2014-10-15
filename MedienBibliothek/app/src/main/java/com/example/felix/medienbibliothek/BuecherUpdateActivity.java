package com.example.felix.medienbibliothek;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class BuecherUpdateActivity extends Activity implements View.OnClickListener
{
    EditText editTextUpdateTitel, editTextUpdateAutor;
    Button buttonAenderungenSpeichern;
    String itemContextID = null;
    String itemContextTitel = null;
    String itemContextAutor = null;
    SQLController sqlController;
    Intent buch_update_intent;
    public final static String EXTRA_MESSAGE_UPDATE_VIEWPAGER_INDEX = "com.example.felix.medienbibliothek.MESSAGE_UPDATE_VIEWPAGER_INDEX";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buecher_update);
        setTitle("Medienbibliothek");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        editTextUpdateTitel = (EditText) findViewById(R.id.textBuchTitel);
        editTextUpdateAutor = (EditText) findViewById(R.id.textBuchAutor);
        buttonAenderungenSpeichern = (Button) findViewById(R.id.buttonAenderungenSpeichern);
        buttonAenderungenSpeichern.setOnClickListener(this);
        sqlController = new SQLController(this);
        buch_update_intent = getIntent();

        itemContextID = buch_update_intent.getStringExtra(BuecherActivity.EXTRA_MESSAGE_ID_UPDATE_BUCH);
        itemContextTitel = buch_update_intent.getStringExtra(BuecherActivity.EXTRA_MESSAGE_TITEL_UPDATE);
        itemContextAutor = buch_update_intent.getStringExtra(BuecherActivity.EXTRA_MESSAGE_AUTOR_UPDATE);

        getIntent().removeExtra(BuecherActivity.EXTRA_MESSAGE_ID_UPDATE_BUCH);
        getIntent().removeExtra(BuecherActivity.EXTRA_MESSAGE_TITEL_UPDATE);
        getIntent().removeExtra(BuecherActivity.EXTRA_MESSAGE_AUTOR_UPDATE);

        editTextUpdateTitel.setText(itemContextTitel);
        editTextUpdateAutor.setText(itemContextAutor);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.buecher_update, menu);
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
            editTextUpdateTitel.setText("");
            editTextUpdateAutor.setText("");
            return true;
        }
        if(id == android.R.id.home)
        {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view)
    {
        String itemContextTitel_updated = editTextUpdateTitel.getText().toString();
        String itemContextAutor_updated = editTextUpdateAutor.getText().toString();
        if(!itemContextTitel_updated.equals("") && !itemContextAutor_updated.equals(""))
        {
            sqlController.open();
            sqlController.updateBuchWithID(itemContextID, itemContextTitel_updated, itemContextAutor_updated);
            sqlController.close();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(EXTRA_MESSAGE_UPDATE_VIEWPAGER_INDEX, "1");
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Buch wurde erfolgreich geändert!", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else
        {
            Toast.makeText(this, "Es fehlen Informationen!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(EXTRA_MESSAGE_UPDATE_VIEWPAGER_INDEX, "1");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }
}
