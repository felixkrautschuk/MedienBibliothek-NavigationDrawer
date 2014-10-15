package com.example.felix.medienbibliothek;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener
{
    public ViewPager viewPager;
    public TabsPagerAdapter tabsPagerAdapter;
    private ActionBar actionBar;
    private String[] tabs = { "Personen", "Bücher", "Verleih" };

    Intent intent_person_hinzufügen, intent_person_suchen, intent_buch_hinzufügen, intent_buch_suchen, intent_verleih_hinzufuegen, intent_viewpager_index;
    String person_nachname, person_vorname, person_suchbegriff;
    String buch_titel, buch_autor, buch_suchbegriff;
    String verleih_personID, verleih_buchID, verleih_datum_von, verleih_datum_bis;
    SQLController sqlController;
    int viewpagerIndex = 0;

    //NavigationDrawer
    private String[] navigationDrawerItemTitles;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ObjectDrawerItem[] objectDrawerItems;
    private DrawerItemCustomAdapter drawerItemCustomAdapter;
    private ActionBarDrawerToggle drawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Medienbibliothek");
        getActionBar().setIcon(R.drawable.app_icon);

        //Viewpager
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        tabsPagerAdapter = new TabsPagerAdapter(getFragmentManager());
        viewPager.setAdapter(tabsPagerAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab_name : tabs)
        {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                // on changing the page
                // make respected tab selected

                //tabsPagerAdapter.notifyDataSetChanged();
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {

            }
        });


        sqlController = new SQLController(this);

        intent_person_hinzufügen = getIntent();
        intent_person_suchen = getIntent();
        intent_buch_hinzufügen = getIntent();
        intent_buch_suchen = getIntent();
        intent_verleih_hinzufuegen = getIntent();
        intent_viewpager_index = getIntent();

        person_nachname = intent_person_hinzufügen.getStringExtra(PersonenHinzufuegenActivity.EXTRA_MESSAGE_NACHNAME);
        person_vorname = intent_person_hinzufügen.getStringExtra(PersonenHinzufuegenActivity.EXTRA_MESSAGE_VORNAME);
        person_suchbegriff = intent_person_suchen.getStringExtra(PersonSuchenActivity.EXTRA_MESSAGE_SUCHE_PERSON);

        buch_titel = intent_buch_hinzufügen.getStringExtra(BuecherHinzufuegenActivity.EXTRA_MESSAGE_TITEL);
        buch_autor = intent_buch_hinzufügen.getStringExtra(BuecherHinzufuegenActivity.EXTRA_MESSAGE_AUTOR);
        buch_suchbegriff = intent_buch_suchen.getStringExtra(BuecherSuchenActivity.EXTRA_MESSAGE_SUCHE_BUCH);

        verleih_personID = intent_verleih_hinzufuegen.getStringExtra(VerleihHinzufuegenActivity.EXTRA_MESSAGE_VERLEIH_PERSONID);
        verleih_buchID = intent_verleih_hinzufuegen.getStringExtra(VerleihHinzufuegenActivity.EXTRA_MESSAGE_VERLEIH_BUCHID);
        verleih_datum_von = intent_verleih_hinzufuegen.getStringExtra(VerleihHinzufuegenActivity.EXTRA_MESSAGE_VERLEIH_DATUM_VON);
        verleih_datum_bis = intent_verleih_hinzufuegen.getStringExtra(VerleihHinzufuegenActivity.EXTRA_MESSAGE_VERLEIH_DATUM_BIS);

        if(intent_viewpager_index.getStringExtra(BuecherHinzufuegenActivity.EXTRA_MESSAGE_VIEWPAGER_INDEX) != null)
        {
            viewpagerIndex = Integer.parseInt(intent_viewpager_index.getStringExtra(BuecherHinzufuegenActivity.EXTRA_MESSAGE_VIEWPAGER_INDEX));
        }

        if(intent_viewpager_index.getStringExtra(BuecherSuchenActivity.EXTRA_MESSAGE_SUCHE_VIEWPAGER_INDEX) != null)
        {
            viewpagerIndex = Integer.parseInt(intent_viewpager_index.getStringExtra(BuecherSuchenActivity.EXTRA_MESSAGE_SUCHE_VIEWPAGER_INDEX));
        }

        if(intent_viewpager_index.getStringExtra(BuecherUpdateActivity.EXTRA_MESSAGE_UPDATE_VIEWPAGER_INDEX) != null)
        {
            viewpagerIndex = Integer.parseInt(intent_viewpager_index.getStringExtra(BuecherUpdateActivity.EXTRA_MESSAGE_UPDATE_VIEWPAGER_INDEX));
        }

        if(intent_viewpager_index.getStringExtra(VerleihHinzufuegenActivity.EXTRA_MESSAGE_VERLEIH_VIEWPAGER_INDEX) != null)
        {
            viewpagerIndex = Integer.parseInt(intent_viewpager_index.getStringExtra(VerleihHinzufuegenActivity.EXTRA_MESSAGE_VERLEIH_VIEWPAGER_INDEX));
        }

        //Intent-Daten löschen damit beim drehen des Gerätes die Daten aus den anderen Activities
        //nicht nochmal in die Tabellen eingefügt werden
        getIntent().removeExtra(PersonenHinzufuegenActivity.EXTRA_MESSAGE_NACHNAME);
        getIntent().removeExtra(PersonenHinzufuegenActivity.EXTRA_MESSAGE_VORNAME);
        getIntent().removeExtra(PersonSuchenActivity.EXTRA_MESSAGE_SUCHE_PERSON);
        getIntent().removeExtra(BuecherHinzufuegenActivity.EXTRA_MESSAGE_TITEL);
        getIntent().removeExtra(BuecherHinzufuegenActivity.EXTRA_MESSAGE_AUTOR);
        getIntent().removeExtra(BuecherSuchenActivity.EXTRA_MESSAGE_SUCHE_BUCH);
        getIntent().removeExtra(VerleihHinzufuegenActivity.EXTRA_MESSAGE_VERLEIH_PERSONID);
        getIntent().removeExtra(VerleihHinzufuegenActivity.EXTRA_MESSAGE_VERLEIH_BUCHID);
        getIntent().removeExtra(VerleihHinzufuegenActivity.EXTRA_MESSAGE_VERLEIH_DATUM_VON);
        getIntent().removeExtra(VerleihHinzufuegenActivity.EXTRA_MESSAGE_VERLEIH_DATUM_BIS);
        getIntent().removeExtra(BuecherHinzufuegenActivity.EXTRA_MESSAGE_VIEWPAGER_INDEX);
        getIntent().removeExtra(BuecherSuchenActivity.EXTRA_MESSAGE_SUCHE_VIEWPAGER_INDEX);
        getIntent().removeExtra(BuecherUpdateActivity.EXTRA_MESSAGE_UPDATE_VIEWPAGER_INDEX);

        if(person_nachname != null && person_vorname!= null)
        {
            this.sqlController.open();
            this.sqlController.insertDataInPerson(person_nachname, person_vorname);
            Toast.makeText(this, "Person " + person_nachname + " " + person_vorname + " wurde erfolgreich hinzugefügt", Toast.LENGTH_SHORT).show();
        }

        if(buch_titel != null && buch_autor!= null)
        {
            sqlController.open();
            sqlController.insertDataInBuch(buch_titel, buch_autor);
            Toast.makeText(getApplicationContext(), "Buch "+ buch_titel + " wurde erfolgreich hinzugefügt", Toast.LENGTH_SHORT).show();
        }

        if(verleih_personID != null && verleih_buchID != null && verleih_datum_von != null && verleih_datum_bis != null)
        {
            sqlController.open();
            sqlController.insertDataInVerleih(verleih_personID, verleih_buchID, verleih_datum_von, verleih_datum_bis);
            sqlController.updateBuchWithIDStatusToVerliehen(verleih_buchID);
            Toast.makeText(getApplicationContext(), "Verleih wurde erfolgreich durchgeführt", Toast.LENGTH_SHORT).show();
        }

        navigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        objectDrawerItems = new ObjectDrawerItem[3];
        objectDrawerItems[0] = new ObjectDrawerItem(R.drawable.ic_personen, "Personen");
        objectDrawerItems[1] = new ObjectDrawerItem(R.drawable.ic_buecher, "Bücher");
        objectDrawerItems[2] = new ObjectDrawerItem(R.drawable.ic_ausleihen, "Verleih");

        drawerItemCustomAdapter = new DrawerItemCustomAdapter(this, R.layout.listview_item_row, objectDrawerItems);
        drawerList.setAdapter(drawerItemCustomAdapter);
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close)
        {
            public void onDrawerOpened(View drawerView)
            {
                getActionBar().setTitle(getTitle());
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view)
            {
                getActionBar().setTitle(getTitle());
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        viewPager.setCurrentItem(viewpagerIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (item.getItemId() == R.id.action_testdaten)
        {
            sqlController.open();
            sqlController.insertTestData();
            sqlController.close();
            return true;
        }
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft)
    {

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft)
    {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft)
    {

    }

    public String getCurrentSuchbegriffPerson()
    {
        return this.person_suchbegriff;
    }

    public String getCurrentSuchbegriffBuch()
    {
        return this.buch_suchbegriff;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
            selectItem(position);
        }

        private void selectItem(int position)
        {
            Fragment fragment = null;

            switch (position)
            {
                case 0:
                    viewPager.setCurrentItem(0);
                    System.out.println("000000000000000000000000000000000000000");
                    drawerLayout.closeDrawer(drawerList);
                    break;
                case 1:
                    viewPager.setCurrentItem(1);
                    System.out.println("111111111111111111111111111111111111111");
                    drawerLayout.closeDrawer(drawerList);
                    break;
                case 2:
                    viewPager.setCurrentItem(2);
                    System.out.println("222222222222222222222222222222222222222");
                    drawerLayout.closeDrawer(drawerList);
                    break;
            }

            if (fragment != null)
            {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                drawerList.setItemChecked(position, true);
                drawerList.setSelection(position);
                getActionBar().setTitle(navigationDrawerItemTitles[position]);
                drawerLayout.closeDrawer(drawerList);
            }
        }
    }
}