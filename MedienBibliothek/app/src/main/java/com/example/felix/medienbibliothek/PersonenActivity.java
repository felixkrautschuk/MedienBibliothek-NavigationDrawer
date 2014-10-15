package com.example.felix.medienbibliothek;
import android.app.Fragment;
import android.graphics.Typeface;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TableRow.LayoutParams;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class PersonenActivity extends Fragment
{
    String suchbegriff;
    public TableLayout tableLayout;
    SQLController sqlController;
    Boolean suchBegriffGefunden = false;
    public String itemContextID = null;
    String itemContextNachname = null;
    String itemContextVorname = null;

    public final static String EXTRA_MESSAGE_ID_UPDATE_PERSON = "com.example.felix.medienbibliothek.MESSAGE_ID_UPDATE_PERSON";
    public final static String EXTRA_MESSAGE_NACHNAME_UPDATE = "com.example.felix.medienbibliothek.MESSAGE_NACHNAME_UPDATE";
    public final static String EXTRA_MESSAGE_VORNAME_UPDATE = "com.example.felix.medienbibliothek.MESSAGE_VORNAME_UPDATE";

    public final static String EXTRA_MESSAGE_ID_DETAILS_PERSON = "com.example.felix.medienbibliothek.MESSAGE_ID_DETAILS_PERSON";
    public final static String EXTRA_MESSAGE_NACHNAME_DETAILS = "com.example.felix.medienbibliothek.MESSAGE_NACHNAME_DETAILS";
    public final static String EXTRA_MESSAGE_VORNAME_DETAILS = "com.example.felix.medienbibliothek.MESSAGE_VORNAME_DETAILS";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_personen, container, false);
        sqlController = new SQLController(getActivity());

        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        setHasOptionsMenu(true);

        tableLayout = (TableLayout) getView().findViewById(R.id.tableLayoutPersonen);

        tableLayout.removeAllViews();

        buildTable();

        suchbegriff = ((MainActivity)getActivity()).getCurrentSuchbegriffPerson();
        if(suchbegriff != null)
        {
            for(int i = 0; i < tableLayout.getChildCount(); i++)
            {
                View parentRow = tableLayout.getChildAt(i);
                if(parentRow instanceof TableRow)
                {
                    for(int j = 0; j < ((TableRow) parentRow).getChildCount(); j++)
                    {
                        TextView textView = (TextView) ((TableRow) parentRow).getChildAt(j);
                        if(suchbegriff.equals(textView.getText().toString()) == true)
                        {
                            parentRow.setBackground(getResources().getDrawable(R.drawable.shape_selected));
                            suchBegriffGefunden = true;
                        }
                    }
                }
            }
            if(suchBegriffGefunden == false)
            {
                Toast.makeText(getActivity(), "Keine Treffer...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.personen, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if( getUserVisibleHint() == false )
        {
            return false;
        }
        if (item.getItemId() == R.id.action_neu)
        {
            startActivity(new Intent(getActivity(), PersonenHinzufuegenActivity.class));
            return true;
        }

        if(item.getItemId() == R.id.action_suchen)
        {
            startActivity(new Intent(getActivity(), PersonSuchenActivity.class));
            return true;
        }

        if(item.getItemId() == R.id.action_clearAllData)
        {
            sqlController.deleteAllPersonenOhneBuch();
            tableLayout.removeAllViews();
            buildTable();
            return true;
        }

        if(item.getItemId() == R.id.action_update)
        {
            tableLayout.removeAllViews();
            buildTable();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.tablerow_personen, menu);
    }

    public boolean onContextItemSelected(MenuItem item)
    {
        if( getUserVisibleHint() == false )
        {
            return false;
        }
        switch(item.getItemId())
        {
            case R.id.action_update:
                Intent intent_update = new Intent(this.getActivity(), PersonUpdateActivity.class);
                intent_update.putExtra(EXTRA_MESSAGE_ID_UPDATE_PERSON, itemContextID);
                intent_update.putExtra(EXTRA_MESSAGE_NACHNAME_UPDATE, itemContextNachname);
                intent_update.putExtra(EXTRA_MESSAGE_VORNAME_UPDATE, itemContextVorname);
                startActivity(intent_update);
                return true;

            case R.id.action_details:
                Intent intent_details = new Intent(this.getActivity(), PersonDetailsActivity.class);
                intent_details.putExtra(EXTRA_MESSAGE_ID_DETAILS_PERSON, itemContextID);
                intent_details.putExtra(EXTRA_MESSAGE_NACHNAME_DETAILS, itemContextNachname);
                intent_details.putExtra(EXTRA_MESSAGE_VORNAME_DETAILS, itemContextVorname);
                startActivity(intent_details);
                return true;

            case R.id.action_delete:
                if(sqlController.checkIfPersonCanBeDeleted(itemContextID) == true)
                {
                    sqlController.open();
                    sqlController.deleteSingleItemFromPersonen(itemContextID);
                    sqlController.close();
                    tableLayout.removeAllViews();
                    itemContextID = null;
                    itemContextNachname = null;
                    itemContextVorname = null;
                    buildTable();
                }
                else
                {
                    Toast.makeText(getActivity(), "Person hat noch Bücher ausgeliehen!", Toast.LENGTH_SHORT).show();
                    return false;
                }

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void buildTable()
    {
        createTableHeader();
        sqlController.open();
        Cursor cursor = sqlController.readEntry_person();
        int zeilen = cursor.getCount();
        int spalten = cursor.getColumnCount();
        cursor.moveToFirst();
        for(int i = 0; i < zeilen; i++)
        {
            final TableRow zeile = new TableRow(getActivity());
            zeile.setBackground(getResources().getDrawable(R.drawable.shape_unselected));

            zeile.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for(int j = 0; j < spalten; j++)
            {
                TextView textView = new TextView(getActivity());
                textView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT));
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
                    TextView textViewNachname = (TextView) tableRow.getChildAt(1);
                    TextView textViewVorname = (TextView) tableRow.getChildAt(2);
                    //Toast.makeText(getActivity(),textViewID.getText().toString(),Toast.LENGTH_SHORT).show();
                    itemContextID = textViewID.getText().toString();
                    itemContextNachname = textViewNachname.getText().toString();
                    itemContextVorname = textViewVorname.getText().toString();
                    view.setBackground(getResources().getDrawable(R.drawable.shape_selected));
                    registerForContextMenu(zeile);
                    zeile.showContextMenu();
                }
            });
            tableLayout.addView(zeile);
            cursor.moveToNext();
        }
        sqlController.close();
    }

    public void resetColor(View v)
    {
        //Tableheader unberührt lassen --> beginne mit 1
        for(int i = 1; i < tableLayout.getChildCount(); i++)
        {
            tableLayout.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.shape_unselected));
        }
    }

    public void createTableHeader()
    {
        TextView textviewHeadPersonID, textViewHeadPersonNachname, textViewPersonVorname;
        final TableRow head = new TableRow(getActivity());
        head.setBackground(getResources().getDrawable(R.drawable.shape_tableheader));
        head.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        textviewHeadPersonID = new TextView(getActivity());
        textViewHeadPersonNachname = new TextView(getActivity());
        textViewPersonVorname = new TextView(getActivity());

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

        tableLayout.addView(head);
    }
}
