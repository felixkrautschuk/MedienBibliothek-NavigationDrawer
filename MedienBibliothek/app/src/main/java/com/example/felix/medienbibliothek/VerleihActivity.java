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

public class VerleihActivity extends Fragment
{
    public TableLayout tableLayout;
    SQLController sqlController;
    public String itemContextID = null;
    String itemContextPersonID = null;
    String itemContextBuchID = null;
    String itemContextDatum_von = null;
    String itemContextDatum_bis = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.activity_verleih, container, false);
        sqlController = new SQLController(getActivity());
        return rootView;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        setHasOptionsMenu(true);

        tableLayout = (TableLayout) getView().findViewById(R.id.tableLayoutVerleih);

        tableLayout.removeAllViews();

        buildTable();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.verleih, menu);
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
            startActivity(new Intent(getActivity(), VerleihHinzufuegenActivity.class));
            return true;
        }

        if(item.getItemId() == R.id.action_suchen)
        {
            //startActivity(new Intent(getActivity(), VerleihSuchenActivity.class));
            return true;
        }

        if(item.getItemId() == R.id.action_clearAllData)
        {
            sqlController.open();
            sqlController.updateBuecherStatusToVerfuegbar();
            sqlController.deleteFromVerleih();
            sqlController.close();
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
        inflater.inflate(R.menu.tablerow_verleih, menu);
    }

    public boolean onContextItemSelected(MenuItem item)
    {
        if( getUserVisibleHint() == false )
        {
            return false;
        }
        switch(item.getItemId())
        {
            case R.id.action_rueckgabe:
                sqlController.open();
                sqlController.updateBuchWithIDStatusToVerfuegbar(itemContextBuchID);
                sqlController.deleteSingleItemFromVerleih(itemContextID);
                sqlController.close();
                tableLayout.removeAllViews();
                itemContextID = null;
                itemContextPersonID = null;
                itemContextBuchID = null;
                buildTable();

                //startActivity(new Intent(getActivity(), MainActivity.class));
                //BuecherActivity fragment = new BuecherActivity();
                //fragment.getFragmentManager().beginTransaction().detach(fragment).commit();
                //fragment.getFragmentManager().beginTransaction().attach(fragment).commit();

                //BuecherActivity buecherActivity = (BuecherActivity)((MainActivity)getActivity()).getTabsPagerAdapter().getItem(1);
                //buecherActivity.buildTable();


                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void buildTable()
    {
        createTableHeader();
        sqlController.open();
        Cursor cursor = sqlController.readEntry_verleih();
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
                    tableRow.setBackground(getResources().getDrawable(R.drawable.shape_selected));
                    TextView textViewID= (TextView)tableRow.getChildAt(0);
                    TextView textViewPersonID = (TextView) tableRow.getChildAt(0);
                    TextView textViewBuchID = (TextView) tableRow.getChildAt(1);
                    TextView textViewDatumVon = (TextView) tableRow.getChildAt(2);
                    TextView textViewDatumBis = (TextView) tableRow.getVirtualChildAt(3);
                    //Toast.makeText(getActivity(),textViewID.getText().toString(),Toast.LENGTH_SHORT).show();
                    itemContextID = textViewID.getText().toString();
                    itemContextPersonID = textViewPersonID.getText().toString();
                    itemContextBuchID = textViewBuchID.getText().toString();
                    itemContextDatum_von = textViewDatumVon.getText().toString();
                    itemContextDatum_bis = textViewDatumBis.getText().toString();
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
        for(int i = 1; i < tableLayout.getChildCount(); i++)
        {
            tableLayout.getChildAt(i).setBackground(getResources().getDrawable(R.drawable.shape_unselected));
        }
    }

    public void createTableHeader()
    {
        TextView textViewHeadID, textViewHeadPersonID, textViewHeadBuchID, textViewHeadDatumVon, textViewHeadDatumBis;
        final TableRow head = new TableRow(getActivity());
        head.setBackground(getResources().getDrawable(R.drawable.shape_tableheader));
        head.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewHeadID = new TextView(getActivity());
        textViewHeadPersonID = new TextView(getActivity());
        textViewHeadBuchID = new TextView(getActivity());
        textViewHeadDatumVon = new TextView(getActivity());
        textViewHeadDatumBis = new TextView(getActivity());

        textViewHeadID.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewHeadPersonID.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewHeadBuchID.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewHeadDatumVon.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        textViewHeadDatumBis.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));


        textViewHeadID.setGravity(Gravity.CENTER);
        textViewHeadPersonID.setGravity(Gravity.CENTER);
        textViewHeadBuchID.setGravity(Gravity.CENTER);
        textViewHeadDatumVon.setGravity(Gravity.CENTER);
        textViewHeadDatumBis.setGravity(Gravity.CENTER);

        textViewHeadID.setTextSize(18);
        textViewHeadPersonID.setTextSize(18);
        textViewHeadBuchID.setTextSize(18);
        textViewHeadDatumVon.setTextSize(18);
        textViewHeadDatumBis.setTextSize(18);

        textViewHeadID.setPadding(0, 5, 0, 5);
        textViewHeadPersonID.setPadding(0, 5, 0, 5);
        textViewHeadBuchID.setPadding(0, 5, 0, 5);
        textViewHeadDatumVon.setPadding(0, 5, 0, 5);
        textViewHeadDatumBis.setPadding(0, 5, 0, 5);

        textViewHeadID.setText("");
        textViewHeadPersonID.setText("PID");
        textViewHeadBuchID.setText("BID");
        textViewHeadDatumVon.setText("Von");
        textViewHeadDatumBis.setText("Bis");

        textViewHeadID.setTypeface(Typeface.DEFAULT_BOLD);
        textViewHeadPersonID.setTypeface(Typeface.DEFAULT_BOLD);
        textViewHeadBuchID.setTypeface(Typeface.DEFAULT_BOLD);
        textViewHeadDatumVon.setTypeface(Typeface.DEFAULT_BOLD);
        textViewHeadDatumBis.setTypeface(Typeface.DEFAULT_BOLD);

        textViewHeadID.setTextColor(getResources().getColor(android.R.color.white));
        textViewHeadPersonID.setTextColor(getResources().getColor(android.R.color.white));
        textViewHeadBuchID.setTextColor(getResources().getColor(android.R.color.white));
        textViewHeadDatumVon.setTextColor(getResources().getColor(android.R.color.white));
        textViewHeadDatumBis.setTextColor(getResources().getColor(android.R.color.white));

        head.addView(textViewHeadID);
        head.addView(textViewHeadPersonID);
        head.addView(textViewHeadBuchID);
        head.addView(textViewHeadDatumVon);
        head.addView(textViewHeadDatumBis);

        tableLayout.addView(head);
    }
}