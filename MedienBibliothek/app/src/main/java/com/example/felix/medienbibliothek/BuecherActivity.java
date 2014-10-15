package com.example.felix.medienbibliothek;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class BuecherActivity extends Fragment
{
    TableLayout tableLayout;
    SQLController sqlController;
    Boolean suchBegriffGefunden = false;
    String suchbegriff;

    public String itemContextID = null;
    String itemContextTitel = null;
    String itemContextAutor = null;

    public final static String EXTRA_MESSAGE_ID_UPDATE_BUCH = "com.example.felix.medienbibliothek.MESSAGE_ID_UPDATE_BUCH";
    public final static String EXTRA_MESSAGE_TITEL_UPDATE = "com.example.felix.medienbibliothek.MESSAGE_TITEL_UPDATE";
    public final static String EXTRA_MESSAGE_AUTOR_UPDATE = "com.example.felix.medienbibliothek.MESSAGE_AUTOR_UPDATE";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_buecher, container, false);
        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        setHasOptionsMenu(true);


        sqlController = new SQLController(getActivity());
        tableLayout = (TableLayout) getView().findViewById(R.id.tableLayoutBuecher);

        tableLayout.removeAllViews();
        buildTable();


        suchbegriff = ((MainActivity)getActivity()).getCurrentSuchbegriffBuch();

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
        inflater.inflate(R.menu.buecher, menu);
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

        int id = item.getItemId();
        if (id == R.id.action_neu)
        {
            startActivity(new Intent(getActivity(), BuecherHinzufuegenActivity.class));
            return true;
        }

        if(id == R.id.action_suchen)
        {
            startActivity(new Intent(getActivity(), BuecherSuchenActivity.class));
            return true;
        }

        if(item.getItemId() == R.id.action_clearAllData)
        {
            sqlController.deleteAllBuecherOhnePerson();
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
        inflater.inflate(R.menu.tablerow_buecher, menu);
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
                Intent intent_buch = new Intent(getActivity(), BuecherUpdateActivity.class);
                intent_buch.putExtra(EXTRA_MESSAGE_ID_UPDATE_BUCH, itemContextID);
                intent_buch.putExtra(EXTRA_MESSAGE_TITEL_UPDATE, itemContextTitel);
                intent_buch.putExtra(EXTRA_MESSAGE_AUTOR_UPDATE, itemContextAutor);
                System.out.println(itemContextID + " " + itemContextTitel + " " + itemContextAutor);
                getActivity().startActivity(intent_buch);
                return true;
            case R.id.action_delete:
                if(sqlController.checkIfBuchCanBeDeleted(itemContextID) == true)
                {
                    sqlController.open();
                    sqlController.deleteSingleItemFromBuch(itemContextID);
                    sqlController.close();
                    tableLayout.removeAllViews();
                    itemContextID = null;
                    buildTable();
                }
                else
                {
                    Toast.makeText(getActivity(), "Buch wird noch ausgeliehen!", Toast.LENGTH_SHORT).show();
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
        Cursor cursor = sqlController.readEntry_buch();
        int zeilen = cursor.getCount();
        int spalten = cursor.getColumnCount();
        cursor.moveToFirst();
        for(int i = 0; i < zeilen; i++)
        {
            final TableRow zeile = new TableRow(getActivity());
            zeile.setBackground(getResources().getDrawable(R.drawable.shape_unselected));

            zeile.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            for(int j = 0; j < spalten; j++)
            {
                TextView textView = new TextView(getActivity());
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
                    TextView textViewID= (TextView)tableRow.getChildAt(0);
                    TextView textViewTitel = (TextView) tableRow.getChildAt(1);
                    TextView textViewAutor = (TextView) tableRow.getChildAt(2);
                    //Toast.makeText(getActivity(),textViewID.getText().toString(),Toast.LENGTH_SHORT).show();
                    itemContextID = textViewID.getText().toString();
                    itemContextTitel = textViewTitel.getText().toString();
                    itemContextAutor = textViewAutor.getText().toString();
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
        TextView textViewHeadBuchID, textViewHeadBuchTitel, textViewHeadBuchAutor, textViewHeadBuchStatus;
        final TableRow head = new TableRow(getActivity());
        head.setBackground(getResources().getDrawable(R.drawable.shape_tableheader));
        head.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewHeadBuchID = new TextView(getActivity());
        textViewHeadBuchTitel = new TextView(getActivity());
        textViewHeadBuchAutor = new TextView(getActivity());
        textViewHeadBuchStatus = new TextView(getActivity());

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

        tableLayout.addView(head);
    }
}
