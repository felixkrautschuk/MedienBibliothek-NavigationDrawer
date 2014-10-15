package com.example.felix.medienbibliothek;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SQLController
{
    private Datenbank_Helper datenbank_helper;
    private Context context;
    private SQLiteDatabase sqLiteDatabase;

    public SQLController(Context context)
    {
        this.context = context;
    }

    public SQLController open() throws SQLException
    {
        datenbank_helper = new Datenbank_Helper(context);
        sqLiteDatabase = datenbank_helper.getWritableDatabase();
        return this;
    }

    public void deleteFromPersonDetail()
    {
        open();
        sqLiteDatabase.execSQL("delete from " + Datenbank_Helper.TABELLE_PERSONDETAIL);
        sqLiteDatabase.execSQL("delete from sqlite_sequence where name='"+Datenbank_Helper.TABELLE_PERSONDETAIL+"'");
    }

    public void close()
    {
        datenbank_helper.close();
    }


    public void insertDataInPerson(String nachname, String vorname)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Datenbank_Helper.PERSON_NACHNAME, nachname);
        contentValues.put(Datenbank_Helper.PERSON_VORNAME, vorname);
        sqLiteDatabase.insert(Datenbank_Helper.TABELLE_PERSONEN, null, contentValues);
    }

    public void insertDataInBuch(String titel, String autor)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Datenbank_Helper.BUCH_TITEL, titel);
        contentValues.put(Datenbank_Helper.BUCH_AUTOR, autor);
        contentValues.put(Datenbank_Helper.BUCH_STATUS, "verfügbar");
        sqLiteDatabase.insert(Datenbank_Helper.TABELLE_BUCH, null, contentValues);
    }

    public void insertDataInVerleih(String personID, String buch_ID, String datum_von, String datum_bis)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Datenbank_Helper.VERLEIH_PERSON_ID, personID);
        contentValues.put(Datenbank_Helper.VERLEIH_BUCH_ID, buch_ID);
        contentValues.put(Datenbank_Helper.VERLEIH_DATUM_VON, datum_von);
        contentValues.put(Datenbank_Helper.VERLEIH_DATUM_BIS, datum_bis);
        sqLiteDatabase.insert(Datenbank_Helper.TABELLE_VERLEIH, null, contentValues);
    }

    public void insertDataInPersonDetail(String verleih_buch_id, String buchTitel, String buchAutor, String verleih_datum_von, String verleih_datum_bis)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Datenbank_Helper.DETAIL_BUCH_ID, verleih_buch_id);
        contentValues.put(Datenbank_Helper.DETAIL_BUCH_TITEL, buchTitel);
        contentValues.put(Datenbank_Helper.DETAIL_BUCH_AUTOR, buchAutor);
        contentValues.put(Datenbank_Helper.DETAIL_VERLEIH_DATUM_VON, verleih_datum_von);
        contentValues.put(Datenbank_Helper.DETAIL_VERLEIH_DATUM_BIS, verleih_datum_bis);
        sqLiteDatabase.insert(Datenbank_Helper.TABELLE_PERSONDETAIL, null, contentValues);
    }

    public void deleteFromPersonen()
    {
        sqLiteDatabase.execSQL("delete from personen");
        sqLiteDatabase.execSQL("delete from sqlite_sequence where name='"+Datenbank_Helper.TABELLE_PERSONEN+"'");
        Toast.makeText(context, "Daten wurden erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
    }

    public void deleteSingleItemFromPersonen(String idOfItemThatShouldBeDeleted)
    {
        sqLiteDatabase.execSQL("delete from personen where person_id ="+idOfItemThatShouldBeDeleted);
        Toast.makeText(context, "Person wurde erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
    }

    public void deleteFromBuch()
    {
        sqLiteDatabase.execSQL("delete from buch");
        sqLiteDatabase.execSQL("UPDATE SQLITE_SEQUENCE SET SEQ=0 WHERE NAME='"+Datenbank_Helper.TABELLE_BUCH+"'");
        Toast.makeText(context, "Daten wurden erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
    }

    public void deleteSingleItemFromBuch(String idOfItemThatShouldBeDeleted)
    {
        sqLiteDatabase.execSQL("delete from buch where buch_id ="+idOfItemThatShouldBeDeleted);
        Toast.makeText(context, "Buch wurde erfolgreich gelöscht", Toast.LENGTH_SHORT).show();
    }

    public void deleteFromVerleih()
    {
        sqLiteDatabase.execSQL("delete from verleih");
        sqLiteDatabase.execSQL("delete from sqlite_sequence where name='"+Datenbank_Helper.TABELLE_VERLEIH+"'");
        Toast.makeText(context, "Alle Bücher wurden erfolgreich zurückgegeben", Toast.LENGTH_SHORT).show();
    }

    public void deleteSingleItemFromVerleih(String idOfItemThatShouldBeDeleted)
    {
        sqLiteDatabase.execSQL("delete from verleih where verleih_id = " + idOfItemThatShouldBeDeleted);
        Toast.makeText(context, "Buch wurde erfolgreich zurückgegeben", Toast.LENGTH_SHORT).show();
    }

    public void updatePersonWithID(String id, String nachname_neu, String vorname_neu)
    {
        sqLiteDatabase.execSQL("UPDATE " + Datenbank_Helper.TABELLE_PERSONEN + " SET " + Datenbank_Helper.PERSON_NACHNAME + " = '" + nachname_neu + "', " + Datenbank_Helper.PERSON_VORNAME + " = '" + vorname_neu + "' WHERE " + Datenbank_Helper.PERSON_ID + " = " + id);
    }

    public void updateBuchWithID(String id, String titel_neu, String autor_neu)
    {
        sqLiteDatabase.execSQL("UPDATE " + Datenbank_Helper.TABELLE_BUCH + " SET " + Datenbank_Helper.BUCH_TITEL + " = '" + titel_neu + "', " + Datenbank_Helper.BUCH_AUTOR + " = '" + autor_neu + "' WHERE " + Datenbank_Helper.BUCH_ID + " = " + id);
    }

    public void updateBuchWithIDStatusToVerliehen(String id)
    {
        sqLiteDatabase.execSQL("UPDATE " + Datenbank_Helper.TABELLE_BUCH + " SET " + Datenbank_Helper.BUCH_STATUS + " = 'verliehen' WHERE " + Datenbank_Helper.BUCH_ID + " = " + id);
    }

    public void updateBuchWithIDStatusToVerfuegbar(String id)
    {
        sqLiteDatabase.execSQL("UPDATE " + Datenbank_Helper.TABELLE_BUCH + " SET " + Datenbank_Helper.BUCH_STATUS + " = 'verfügbar' WHERE " + Datenbank_Helper.BUCH_ID + " = " + id);
    }

    public void updateBuecherStatusToVerfuegbar()
    {
        sqLiteDatabase.execSQL("UPDATE " + Datenbank_Helper.TABELLE_BUCH + " SET " + Datenbank_Helper.BUCH_STATUS + " = 'verfügbar'");
    }

    /*
    public List<Person> getAllPersonenWithPattern(String pattern)
    {
        List<Person> personList = new ArrayList<Person>();
        String selectQuery = "SELECT  * FROM " + Datenbank_Helper.TABELLE_PERSONEN + " WHERE " + Datenbank_Helper.PERSON_ID + " = " + pattern + " or " + Datenbank_Helper.PERSON_NACHNAME + " = " + pattern + " or " + Datenbank_Helper.PERSON_VORNAME + " = " + pattern;

        Cursor cursor;
        if(selectQuery != null)
        {
            cursor = sqLiteDatabase.rawQuery(selectQuery, null);
            if (cursor.moveToFirst())
            {
                do
                {
                    Person contact = new Person();
                    contact.setIndex(Integer.parseInt(cursor.getString(0)));
                    contact.setNachname(cursor.getString(1));
                    contact.setVorname(cursor.getString(2));
                    personList.add(contact);
                }
                while (cursor.moveToNext());
            }
        }
        return personList;
    }
    */

    public Cursor readEntry_person()
    {
        String[] allColumns = new String[] { Datenbank_Helper.PERSON_ID, Datenbank_Helper.PERSON_NACHNAME, Datenbank_Helper.PERSON_VORNAME};
        Cursor cursor = sqLiteDatabase.query(Datenbank_Helper.TABELLE_PERSONEN, allColumns, null, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor readEntry_buch()
    {
        String[] allColumns = new String[] { Datenbank_Helper.BUCH_ID, Datenbank_Helper.BUCH_TITEL, Datenbank_Helper.BUCH_AUTOR, Datenbank_Helper.BUCH_STATUS};
        Cursor cursor = sqLiteDatabase.query(Datenbank_Helper.TABELLE_BUCH, allColumns, null, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor readEntry_verleih()
    {
        String[] allColumns = new String[] { Datenbank_Helper.VERLEIH_ID, Datenbank_Helper.VERLEIH_PERSON_ID, Datenbank_Helper.VERLEIH_BUCH_ID, Datenbank_Helper.VERLEIH_DATUM_VON, Datenbank_Helper.VERLEIH_DATUM_BIS};
        Cursor cursor = sqLiteDatabase.query(Datenbank_Helper.TABELLE_VERLEIH, allColumns, null, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor readEntry_personDetail()
    {
        String[] allColumns = new String[] { Datenbank_Helper.DETAIL_ID, Datenbank_Helper.DETAIL_BUCH_ID, Datenbank_Helper.DETAIL_BUCH_TITEL, Datenbank_Helper.DETAIL_BUCH_AUTOR, Datenbank_Helper.DETAIL_VERLEIH_DATUM_VON, Datenbank_Helper.DETAIL_VERLEIH_DATUM_BIS};
        Cursor cursor = sqLiteDatabase.query(Datenbank_Helper.TABELLE_PERSONDETAIL, allColumns, null, null, null, null, null);
        if (cursor != null)
        {
            cursor.moveToFirst();
        }
        return cursor;
    }


    public void fillTablePersonDetail(String personDetailsID)
    {
        this.open();
        deleteFromPersonDetail();
        Cursor cursor = sqLiteDatabase.rawQuery("select verleih.verleih_buch_id, buch.titel, buch.autor, verleih.datum_von, verleih.datum_bis from verleih inner join buch on verleih.verleih_buch_id = buch.buch_id where verleih.verleih_person_id = ?", new String[]{personDetailsID});

        ArrayList temp = new ArrayList();
        if (cursor != null)
        {
            if (cursor.moveToFirst())
            {
                do
                {
                    temp.add(cursor.getString(cursor.getColumnIndex("verleih_buch_id")));
                    temp.add(cursor.getString(cursor.getColumnIndex("titel")));
                    temp.add(cursor.getString(cursor.getColumnIndex("autor")));
                    temp.add(cursor.getString(cursor.getColumnIndex("datum_von")));
                    temp.add(cursor.getString(cursor.getColumnIndex("datum_bis")));

                    insertDataInPersonDetail(cursor.getString(cursor.getColumnIndex("verleih_buch_id")), cursor.getString(cursor.getColumnIndex("titel")), cursor.getString(cursor.getColumnIndex("autor")), cursor.getString(cursor.getColumnIndex("datum_von")), cursor.getString(cursor.getColumnIndex("datum_bis")));
                }
                while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    public boolean checkIfPersonCanBeDeleted(String personID)
    {
        this.open();
        Cursor cursor = sqLiteDatabase.rawQuery("select verleih.verleih_buch_id, buch.titel, buch.autor, verleih.datum_von, verleih.datum_bis from verleih inner join buch on verleih.verleih_buch_id = buch.buch_id where verleih.verleih_person_id = ?", new String[]{personID});

        if (cursor != null && cursor.getCount()>0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void deleteAllPersonenOhneBuch()
    {
        this.open();
        Cursor cursor = sqLiteDatabase.rawQuery("Select person_id from personen", null);
        if (cursor.moveToFirst())
        {
            do
            {
                String personIDTest = cursor.getString(cursor.getColumnIndex("person_id"));
                if(checkIfPersonCanBeDeleted(personIDTest) == true)
                {
                    sqLiteDatabase.execSQL("delete from personen where person_id ="+personIDTest);
                }
            }
            while (cursor.moveToNext());
        }
        this.close();
    }

    public boolean checkIfBuchCanBeDeleted(String buchID)
    {
        this.open();
        deleteFromPersonDetail();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from verleih where verleih.verleih_buch_id = ?", new String[]{buchID});

        if (cursor != null && cursor.getCount()>0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void deleteAllBuecherOhnePerson()
    {
        this.open();
        Cursor cursor = sqLiteDatabase.rawQuery("Select buch_id from buch ", null);
        if (cursor.moveToFirst())
        {
            do
            {
                String buchIDTest = cursor.getString(cursor.getColumnIndex("buch_id"));
                if(checkIfBuchCanBeDeleted(buchIDTest) == true)
                {
                    sqLiteDatabase.execSQL("delete from buch where buch_id ="+buchIDTest);
                }
            }
            while (cursor.moveToNext());
        }
        this.close();
    }


    public void insertTestData()
    {
        //vorhandene Daten der Tabellen löschen
        sqLiteDatabase.execSQL("delete from personen");
        sqLiteDatabase.execSQL("delete from sqlite_sequence where name='"+Datenbank_Helper.TABELLE_PERSONEN+"'");
        sqLiteDatabase.execSQL("delete from buch");
        sqLiteDatabase.execSQL("update sqlite_sequence set seq=0 where name='"+Datenbank_Helper.TABELLE_BUCH+"'");
        sqLiteDatabase.execSQL("delete from verleih");
        sqLiteDatabase.execSQL("delete from sqlite_sequence where name='"+Datenbank_Helper.TABELLE_VERLEIH+"'");


        //Personen
        sqLiteDatabase.execSQL("insert into personen (person_id, nachname, vorname) values (1, 'Krautschuk', 'Felix')");
        sqLiteDatabase.execSQL("insert into personen (person_id, nachname, vorname) values (2, 'Herzog', 'Benjamin')");
        sqLiteDatabase.execSQL("insert into personen (person_id, nachname, vorname) values (3, 'Noack', 'Markus')");
        sqLiteDatabase.execSQL("insert into personen (person_id, nachname, vorname) values (4, 'Knothe', 'Christian')");
        sqLiteDatabase.execSQL("insert into personen (person_id, nachname, vorname) values (5, 'Krautschuk', 'Susan')");
        sqLiteDatabase.execSQL("insert into personen (person_id, nachname, vorname) values (6, 'Richter', 'Vincent')");
        sqLiteDatabase.execSQL("insert into personen (person_id, nachname, vorname) values (7, 'Loschke', 'Kevin')");
        sqLiteDatabase.execSQL("insert into personen (person_id, nachname, vorname) values (8, 'Horn', 'Tobias')");
        sqLiteDatabase.execSQL("insert into personen (person_id, nachname, vorname) values (9, 'Sturm', 'Felix')");
        sqLiteDatabase.execSQL("insert into personen (person_id, nachname, vorname) values (10, 'Horn', 'Werner')");

        sqLiteDatabase.execSQL("insert into buch (buch_id, titel, autor, status) values (1, 'Theorie', 'B. Hollas', 'verliehen')");
        sqLiteDatabase.execSQL("insert into buch (buch_id, titel, autor, status) values (2, 'Mathe', 'K. Neumann', 'verfügbar')");
        sqLiteDatabase.execSQL("insert into buch (buch_id, titel, autor, status) values (3, 'Prog', 'A. Beck', 'verfügbar')");
        sqLiteDatabase.execSQL("insert into buch (buch_id, titel, autor, status) values (4, 'Java', 'B. Steppan', 'verliehen')");
        sqLiteDatabase.execSQL("insert into buch (buch_id, titel, autor, status) values (5, 'HTML5', 'G. Born', 'verfügbar')");
        sqLiteDatabase.execSQL("insert into buch (buch_id, titel, autor, status) values (6, 'OpenGL', 'M. Apetri', 'verfügbar')");
        sqLiteDatabase.execSQL("insert into buch (buch_id, titel, autor, status) values (7, 'Otto', 'O. Waalkes', 'verfügbar')");
        sqLiteDatabase.execSQL("insert into buch (buch_id, titel, autor, status) values (8, 'Faust I', 'J.W. Goethe', 'verfügbar')");
        sqLiteDatabase.execSQL("insert into buch (buch_id, titel, autor, status) values (9, 'KI', 'B. Hollas', 'verliehen')");
        sqLiteDatabase.execSQL("insert into buch (buch_id, titel, autor, status) values (10, 'Faust II', 'J.W. Goethe', 'verliehen')");

        sqLiteDatabase.execSQL("insert into verleih (verleih_id, verleih_person_id, verleih_buch_id, datum_von, datum_bis) values(1, '1', '1', '07.09.2014', '14.09.2014')");
        sqLiteDatabase.execSQL("insert into verleih (verleih_id, verleih_person_id, verleih_buch_id, datum_von, datum_bis) values(2, '1', '4', '01.07.2014', '10.09.2014')");
        sqLiteDatabase.execSQL("insert into verleih (verleih_id, verleih_person_id, verleih_buch_id, datum_von, datum_bis) values(3, '3', '9', '12.04.2014', '25.07.2014')");
        sqLiteDatabase.execSQL("insert into verleih (verleih_id, verleih_person_id, verleih_buch_id, datum_von, datum_bis) values(4, '7', '10', '21.06.2014', '31.12.2014')");
    }
}
