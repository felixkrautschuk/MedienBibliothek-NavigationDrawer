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


public class PersonUpdateActivity extends Activity implements View.OnClickListener
{
    EditText editTextUpdateVorname, editTextUpdateNachname;
    Button buttonAenderungenSpeichern;
    String itemContextID = null;
    String itemContextNachname = null;
    String itemContextVorname = null;
    SQLController sqlController;
    Intent person_update_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_update);
        setTitle("Medienbibliothek");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        editTextUpdateNachname = (EditText) findViewById(R.id.textNachname);
        editTextUpdateVorname = (EditText) findViewById(R.id.textVorname);
        buttonAenderungenSpeichern = (Button) findViewById(R.id.buttonAenderungenSpeichern);
        buttonAenderungenSpeichern.setOnClickListener(this);
        sqlController = new SQLController(this);
        person_update_intent = getIntent();

        itemContextID = person_update_intent.getStringExtra(PersonenActivity.EXTRA_MESSAGE_ID_UPDATE_PERSON);
        itemContextNachname = person_update_intent.getStringExtra(PersonenActivity.EXTRA_MESSAGE_NACHNAME_UPDATE);
        itemContextVorname = person_update_intent.getStringExtra(PersonenActivity.EXTRA_MESSAGE_VORNAME_UPDATE);

        getIntent().removeExtra(PersonenActivity.EXTRA_MESSAGE_ID_UPDATE_PERSON);
        getIntent().removeExtra(PersonenActivity.EXTRA_MESSAGE_NACHNAME_UPDATE);
        getIntent().removeExtra(PersonenActivity.EXTRA_MESSAGE_VORNAME_UPDATE);

        editTextUpdateNachname.setText(itemContextNachname);
        editTextUpdateVorname.setText(itemContextVorname);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.person_update, menu);
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
            editTextUpdateNachname.setText("");
            editTextUpdateVorname.setText("");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view)
    {
        String itemContextNachname_updated = editTextUpdateNachname.getText().toString();
        String itemContextVorname_updated = editTextUpdateVorname.getText().toString();
        if(!itemContextNachname_updated.equals("") && !itemContextVorname_updated.equals(""))
        {
            sqlController.open();
            sqlController.updatePersonWithID(itemContextID, itemContextNachname_updated, itemContextVorname_updated);
            sqlController.close();
            startActivity(new Intent(this, MainActivity.class));
            Toast.makeText(getApplicationContext(), "Person wurde erfolgreich geändert!", Toast.LENGTH_SHORT).show();
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
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }
}
