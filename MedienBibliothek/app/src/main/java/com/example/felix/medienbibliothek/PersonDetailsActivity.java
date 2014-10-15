package com.example.felix.medienbibliothek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


public class PersonDetailsActivity extends Activity
{
    TextView textViewPersonDetails;
    TableLayout tableLayoutPersonDetails;
    SQLController sqlController;
    String personDetailsID, personDetailsNachname, personDetailsVorname;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        sqlController = new SQLController(this);
        textViewPersonDetails = (TextView) findViewById(R.id.textViewPersonDetails);
        tableLayoutPersonDetails = (TableLayout) findViewById(R.id.tableLayoutPersonDetails);

        personDetailsID = getIntent().getStringExtra(PersonenActivity.EXTRA_MESSAGE_ID_DETAILS_PERSON);
        personDetailsNachname = getIntent().getStringExtra(PersonenActivity.EXTRA_MESSAGE_NACHNAME_DETAILS);
        personDetailsVorname = getIntent().getStringExtra(PersonenActivity.EXTRA_MESSAGE_VORNAME_DETAILS);

        textViewPersonDetails.setText(personDetailsID + " " + personDetailsNachname + " " + personDetailsVorname);
        sqlController.open();
        sqlController.fillTablePersonDetail(personDetailsID);
        tableLayoutPersonDetails.removeAllViews();
        buildTable();
    }


    public void buildTable()
    {

        sqlController.open();
        Cursor cursor = sqlController.readEntry_personDetail();
        int zeilen = cursor.getCount();
        if(zeilen == 0)
        {
            TextView textViewLeer = new TextView(this);
            textViewLeer.setText("hat keine Bücher ausgeliehen!");
            tableLayoutPersonDetails.addView(textViewLeer);
            sqlController.close();
        }
        else
        {
            createTableHeader();
            int spalten = cursor.getColumnCount();
            cursor.moveToFirst();
            for (int i = 0; i < zeilen; i++)
            {
                final TableRow zeile = new TableRow(this);
                zeile.setBackground(getResources().getDrawable(R.drawable.shape_unselected));

                zeile.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

                for (int j = 1; j < spalten; j++)
                {
                    TextView textView = new TextView(this);
                    textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    textView.setGravity(Gravity.CENTER);
                    textView.setTextSize(10);
                    textView.setPadding(0, 5, 0, 5);

                    textView.setText(cursor.getString(j));

                    zeile.addView(textView);
                }
                tableLayoutPersonDetails.addView(zeile);
                cursor.moveToNext();
            }
            sqlController.close();
        }
    }

    public void createTableHeader()
    {
        TextView textViewID, textViewBuchID, textViewBuchTitel, textViewBuchAutor, textViewDatumVon, textViewDatumBis;
        final TableRow head = new TableRow(this);
        head.setBackground(getResources().getDrawable(R.drawable.shape_tableheader));
        head.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewID = new TextView(this);
        textViewBuchID = new TextView(this);
        textViewBuchTitel = new TextView(this);
        textViewBuchAutor = new TextView(this);
        textViewDatumVon = new TextView(this);
        textViewDatumBis = new TextView(this);

        textViewID.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewBuchID.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewBuchTitel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewBuchAutor.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewDatumVon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewDatumBis.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        textViewID.setGravity(Gravity.CENTER);
        textViewBuchID.setGravity(Gravity.CENTER);
        textViewBuchTitel.setGravity(Gravity.CENTER);
        textViewBuchAutor.setGravity(Gravity.CENTER);
        textViewDatumVon.setGravity(Gravity.CENTER);
        textViewDatumBis.setGravity(Gravity.CENTER);

        textViewID.setTextSize(14);
        textViewBuchID.setTextSize(14);
        textViewBuchTitel.setTextSize(14);
        textViewBuchAutor.setTextSize(14);
        textViewDatumVon.setTextSize(14);
        textViewDatumBis.setTextSize(14);

        textViewID.setPadding(0, 5, 0, 5);
        textViewBuchID.setPadding(0, 5, 0, 5);
        textViewBuchTitel.setPadding(0, 5, 0, 5);
        textViewBuchAutor.setPadding(0, 5, 0, 5);
        textViewDatumVon.setPadding(0, 5, 0, 5);
        textViewDatumBis.setPadding(0, 5, 0, 5);

        textViewID.setText("ID");
        textViewBuchID.setText("ID");
        textViewBuchTitel.setText("Titel");
        textViewBuchAutor.setText("Autor");
        textViewDatumVon.setText("Von");
        textViewDatumBis.setText("Bis");

        textViewID.setTypeface(Typeface.DEFAULT_BOLD);
        textViewBuchID.setTypeface(Typeface.DEFAULT_BOLD);
        textViewBuchTitel.setTypeface(Typeface.DEFAULT_BOLD);
        textViewBuchAutor.setTypeface(Typeface.DEFAULT_BOLD);
        textViewDatumVon.setTypeface(Typeface.DEFAULT_BOLD);
        textViewDatumBis.setTypeface(Typeface.DEFAULT_BOLD);

        textViewID.setTextColor(getResources().getColor(android.R.color.white));
        textViewBuchID.setTextColor(getResources().getColor(android.R.color.white));
        textViewBuchTitel.setTextColor(getResources().getColor(android.R.color.white));
        textViewBuchAutor.setTextColor(getResources().getColor(android.R.color.white));
        textViewDatumVon.setTextColor(getResources().getColor(android.R.color.white));
        textViewDatumBis.setTextColor(getResources().getColor(android.R.color.white));

        //head.addView(textViewID);
        head.addView(textViewBuchID);
        head.addView(textViewBuchTitel);
        head.addView(textViewBuchAutor);
        head.addView(textViewDatumVon);
        head.addView(textViewDatumBis);

        tableLayoutPersonDetails.addView(head);
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sqlController.deleteFromPersonDetail();
        sqlController.close();
        startActivity(intent);
        this.finish();
    }
}
