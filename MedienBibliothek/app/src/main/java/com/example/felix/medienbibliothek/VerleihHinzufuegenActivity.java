package com.example.felix.medienbibliothek;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class VerleihHinzufuegenActivity extends Activity implements View.OnClickListener
{
    EditText editTextPersonID, editTextBuchID, editTextDatumVon, editTextDatumBis;
    Button buttonVerleihHinzufuegen;
    SimpleDateFormat simpleDateFormat;
    public final static String EXTRA_MESSAGE_VERLEIH_PERSONID = "com.example.felix.medienbibliothek.MESSAGE_VERLEIH_PERSONID";
    public final static String EXTRA_MESSAGE_VERLEIH_BUCHID = "com.example.felix.medienbibliothek.MESSAGE_VERLEI_BUCHID";
    public final static String EXTRA_MESSAGE_VERLEIH_DATUM_VON = "com.example.felix.medienbibliothek.MESSAGE_VERLEI_DATUM_VON";
    public final static String EXTRA_MESSAGE_VERLEIH_DATUM_BIS = "com.example.felix.medienbibliothek.MESSAGE_VERLEI_DATUM_BIS";
    public final static String EXTRA_MESSAGE_VERLEIH_VIEWPAGER_INDEX = "com.example.felix.medienbibliothek.MESSAGE_VERLEIH_VIEWPAGER_INDEX";

    String personID, buchID, datum_von, datum_bis;
    DatePicker datePicker;
    SQLController sqlController;
    TableLayout tableLayoutPersonen, tableLayoutBuecher;
    Boolean buchVerfuegbar = false;

    private int year;
    private int month;
    private int day;
    static final int DATE_DIALOG_ID = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verleih_hinzufuegen);
        setTitle("Medienbibliothek");
        getActionBar().setDisplayHomeAsUpEnabled(true);

        sqlController = new SQLController(this);
        tableLayoutPersonen = (TableLayout) findViewById(R.id.tableLayoutPersonen);
        tableLayoutBuecher = (TableLayout) findViewById(R.id.tableLayoutBuecher);

        datePicker = (DatePicker) findViewById(R.id.datePickerDatumBis);
        simpleDateFormat = new SimpleDateFormat( "dd.MM.yyyy", Locale.GERMANY);

        editTextPersonID = (EditText) findViewById(R.id.textPerson_ID);
        editTextPersonID.setFocusable(false);

        editTextBuchID = (EditText) findViewById(R.id.textBuch_ID);
        editTextBuchID.setFocusable(false);

        editTextDatumVon = (EditText) findViewById(R.id.textDatum);
        editTextDatumVon.setFocusable(false);
        editTextDatumVon.setText(simpleDateFormat.format(new Date()).toString());

        editTextDatumBis = (EditText) findViewById(R.id.textDatumBis);
        editTextDatumBis.setFocusable(false);

        buttonVerleihHinzufuegen = (Button) findViewById(R.id.buttonVerleihHinzufuegen);
        buttonVerleihHinzufuegen.setOnClickListener(this);

        initDatePicker();

        buildTablesVerleihPersonen();
        buildTablesVerleihBuecher();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.verleih_hinzufuegen, menu);
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
            editTextPersonID.setText("");
            editTextBuchID.setText("");
            resetColor(tableLayoutPersonen);
            resetColor(tableLayoutBuecher);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buildTablesVerleihPersonen()
    {
        //PERSONEN
        createTableHeaderPersonen();
        sqlController.open();
        Cursor cursor = sqlController.readEntry_person();
        int zeilen = cursor.getCount();
        int spalten = cursor.getColumnCount();
        cursor.moveToFirst();
        for(int i = 0; i < zeilen; i++)
        {
            final TableRow zeile = new TableRow(this);
            zeile.setBackground(getResources().getDrawable(R.drawable.shape_unselected));

            zeile.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for(int j = 0; j < spalten; j++)
            {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(16);
                textView.setPadding(0, 5, 0, 5);

                textView.setText(cursor.getString(j));

                zeile.addView(textView);
            }
            zeile.setClickable(true);
            zeile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    resetColor(view);
                    TableRow tableRow=(TableRow)view;
                    tableRow.setBackground(getResources().getDrawable(R.drawable.shape_selected));
                    TextView textViewID= (TextView)tableRow.getChildAt(0);
                    personID = textViewID.getText().toString();
                    editTextPersonID.setText(personID);
                    view.setBackground(getResources().getDrawable(R.drawable.shape_selected));
                }
            });
            tableLayoutPersonen.addView(zeile);
            cursor.moveToNext();
        }
        sqlController.close();
    }

    public void buildTablesVerleihBuecher()
    {
        //BÜCHER
        createTableHeaderBuecher();
        sqlController.open();
        Cursor cursor = sqlController.readEntry_buch();
        int zeilen = cursor.getCount();
        int spalten = cursor.getColumnCount();
        cursor.moveToFirst();
        for(int i = 0; i < zeilen; i++)
        {
            final TableRow zeile = new TableRow(this);
            zeile.setBackground(getResources().getDrawable(R.drawable.shape_unselected));

            zeile.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for(int j = 0; j < spalten; j++)
            {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT));
                textView.setGravity(Gravity.CENTER);
                textView.setTextSize(16);
                textView.setPadding(0, 5, 0, 5);

                textView.setText(cursor.getString(j));

                zeile.addView(textView);
            }
            zeile.setClickable(true);
            zeile.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    resetColor(view);
                    TableRow tableRow=(TableRow)view;
                    tableRow.setBackground(getResources().getDrawable(R.drawable.shape_selected));
                    TextView textViewID= (TextView)tableRow.getChildAt(0);
                    TextView textViewStatus = (TextView) tableRow.getChildAt(3);
                    buchID = textViewID.getText().toString();
                    editTextBuchID.setText(buchID);
                    view.setBackground(getResources().getDrawable(R.drawable.shape_selected));
                }
            });
            TextView textViewStatus = (TextView) zeile.getChildAt(3);
            String status = textViewStatus.getText().toString();
            if(status.equals("verliehen"))
            {
                buchVerfuegbar = false;
            }
            else if(status.equals("verfügbar"))
            {
                buchVerfuegbar = true;
            }
            if(buchVerfuegbar == true)
            {
                tableLayoutBuecher.addView(zeile);
            }
            cursor.moveToNext();
        }
        sqlController.close();
    }

    public void resetColor(View v)
    {
        for(int i = 1; i < tableLayoutPersonen.getChildCount(); i++)
        {
            tableLayoutPersonen.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.shape_unselected));
        }

        for(int i = 1; i < tableLayoutBuecher.getChildCount(); i++)
        {
            tableLayoutBuecher.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.shape_unselected));
        }
    }

    public void createTableHeaderPersonen()
    {
        TextView textviewHeadPersonID, textViewHeadPersonNachname, textViewPersonVorname;
        final TableRow head = new TableRow(this);
        head.setBackground(getResources().getDrawable(R.drawable.shape_tableheader));
        head.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        textviewHeadPersonID = new TextView(this);
        textViewHeadPersonNachname = new TextView(this);
        textViewPersonVorname = new TextView(this);

        textviewHeadPersonID.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewHeadPersonNachname.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewPersonVorname.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


        textviewHeadPersonID.setGravity(Gravity.CENTER);
        textViewHeadPersonNachname.setGravity(Gravity.CENTER);
        textViewPersonVorname.setGravity(Gravity.CENTER);

        textviewHeadPersonID.setTextSize(18);
        textViewHeadPersonNachname.setTextSize(18);
        textViewPersonVorname.setTextSize(18);

        textviewHeadPersonID.setPadding(0, 5, 0, 5);
        textViewHeadPersonNachname.setPadding(0, 5, 0, 5);
        textViewPersonVorname.setPadding(0, 5, 0, 5);

        textviewHeadPersonID.setText("ID");
        textViewHeadPersonNachname.setText("Nachname");
        textViewPersonVorname.setText("Vorname");

        textviewHeadPersonID.setTypeface(Typeface.DEFAULT_BOLD);
        textViewHeadPersonNachname.setTypeface(Typeface.DEFAULT_BOLD);
        textViewPersonVorname.setTypeface(Typeface.DEFAULT_BOLD);

        textviewHeadPersonID.setTextColor(getResources().getColor(android.R.color.white));
        textViewHeadPersonNachname.setTextColor(getResources().getColor(android.R.color.white));
        textViewPersonVorname.setTextColor(getResources().getColor(android.R.color.white));

        head.addView(textviewHeadPersonID);
        head.addView(textViewHeadPersonNachname);
        head.addView(textViewPersonVorname);

        tableLayoutPersonen.addView(head);
    }

    public void createTableHeaderBuecher()
    {
        TextView textViewHeadBuchID, textViewHeadBuchTitel, textViewHeadBuchAutor, textViewHeadBuchStatus;
        final TableRow head = new TableRow(this);
        head.setBackground(getResources().getDrawable(R.drawable.shape_tableheader));
        head.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewHeadBuchID = new TextView(this);
        textViewHeadBuchTitel = new TextView(this);
        textViewHeadBuchAutor = new TextView(this);
        textViewHeadBuchStatus = new TextView(this);

        textViewHeadBuchID.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewHeadBuchTitel.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewHeadBuchAutor.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewHeadBuchStatus.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));

        textViewHeadBuchID.setGravity(Gravity.CENTER);
        textViewHeadBuchTitel.setGravity(Gravity.CENTER);
        textViewHeadBuchAutor.setGravity(Gravity.CENTER);
        textViewHeadBuchStatus.setGravity(Gravity.CENTER);

        textViewHeadBuchID.setTextSize(18);
        textViewHeadBuchTitel.setTextSize(18);
        textViewHeadBuchAutor.setTextSize(18);
        textViewHeadBuchStatus.setTextSize(18);

        textViewHeadBuchID.setPadding(0, 5, 0, 5);
        textViewHeadBuchTitel.setPadding(0, 5, 0, 5);
        textViewHeadBuchAutor.setPadding(0, 5, 0, 5);
        textViewHeadBuchStatus.setPadding(0, 5, 0, 5);

        textViewHeadBuchID.setText("ID");
        textViewHeadBuchTitel.setText("Titel");
        textViewHeadBuchAutor.setText("Autor");
        textViewHeadBuchStatus.setText("Status");

        textViewHeadBuchID.setTypeface(Typeface.DEFAULT_BOLD);
        textViewHeadBuchTitel.setTypeface(Typeface.DEFAULT_BOLD);
        textViewHeadBuchAutor.setTypeface(Typeface.DEFAULT_BOLD);
        textViewHeadBuchStatus.setTypeface(Typeface.DEFAULT_BOLD);

        textViewHeadBuchID.setTextColor(getResources().getColor(android.R.color.white));
        textViewHeadBuchTitel.setTextColor(getResources().getColor(android.R.color.white));
        textViewHeadBuchAutor.setTextColor(getResources().getColor(android.R.color.white));
        textViewHeadBuchStatus.setTextColor(getResources().getColor(android.R.color.white));

        head.addView(textViewHeadBuchID);
        head.addView(textViewHeadBuchTitel);
        head.addView(textViewHeadBuchAutor);
        head.addView(textViewHeadBuchStatus);

        tableLayoutBuecher.addView(head);
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.buttonVerleihHinzufuegen)
        {
            Intent intent = new Intent(this, MainActivity.class);
            personID = editTextPersonID.getText().toString();
            buchID = editTextBuchID.getText().toString();
            datum_von = editTextDatumVon.getText().toString();
            datum_bis = editTextDatumBis.getText().toString();

            if(!personID.equals("") && !buchID.equals(""))
            {
                intent.putExtra(EXTRA_MESSAGE_VERLEIH_PERSONID, personID);
                intent.putExtra(EXTRA_MESSAGE_VERLEIH_BUCHID, buchID);
                intent.putExtra(EXTRA_MESSAGE_VERLEIH_DATUM_VON, datum_von);
                intent.putExtra(EXTRA_MESSAGE_VERLEIH_DATUM_BIS, datum_bis);
                intent.putExtra(EXTRA_MESSAGE_VERLEIH_VIEWPAGER_INDEX, "2");
                startActivity(intent);
                this.finish();
            }
            else
            {
                Toast.makeText(this, "Es fehlen Informationen!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initDatePicker()
    {
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        Date date = null;

        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.MILLISECOND, calendar.MILLISECOND - 1000);
        datePicker.setMinDate(calendar.getTimeInMillis());
        datePicker.init(year, month, day + 1, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker viewDatePicker, int newYear, int newMonth, int newDay) {
                newMonth++;
                Date date = null;
                try
                {
                    date = simpleDateFormat.parse(newDay + "." + newMonth + "." + newYear);
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                editTextDatumBis.setText(simpleDateFormat.format(date));
            }
        });

        day++;
        month++;
        try
        {
            date = simpleDateFormat.parse(day+"."+month+"."+year);
            editTextDatumBis.setText(simpleDateFormat.format(date));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(EXTRA_MESSAGE_VERLEIH_VIEWPAGER_INDEX, "2");
        startActivity(intent);
        this.finish();
    }
}
