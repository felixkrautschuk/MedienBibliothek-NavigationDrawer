package com.example.felix.medienbibliothek;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Datenbank_Helper extends SQLiteOpenHelper
{
    //Datenbankinformationen
    static final String DATENBANK_NAME = "BIBLIOTHEK.DB";
    static final int DATENBANK_VERSION = 1;

    //Tabelleninformationen Personen
    public static final String TABELLE_PERSONEN = "personen";
    public static final String PERSON_ID = "person_id";
    public static final String PERSON_VORNAME = "vorname";
    public static final String PERSON_NACHNAME = "nachname";

    //Tabelleninformationen Buch
    public static final String TABELLE_BUCH = "buch";
    public static final String BUCH_ID = "buch_id";
    public static final String BUCH_TITEL = "titel";
    public static final String BUCH_AUTOR = "autor";
    public static final String BUCH_STATUS = "status";

    //Tabelleninformationen Verleih
    public static final String TABELLE_VERLEIH = "verleih";
    public static final String VERLEIH_ID = "verleih_id";
    public static final String VERLEIH_PERSON_ID = "verleih_person_id";
    public static final String VERLEIH_BUCH_ID = "verleih_buch_id";
    public static  final  String VERLEIH_DATUM_VON = "datum_von";
    public static  final  String VERLEIH_DATUM_BIS = "datum_bis";

    //Tabelleninformation PersonDetail
    public static final String TABELLE_PERSONDETAIL = "persondetail";
    public static final String DETAIL_ID = "detail_id";
    public static final String DETAIL_BUCH_ID = "detail_buch_id";
    public static final String DETAIL_BUCH_TITEL = "detail_buch_titel";
    public static final String DETAIL_BUCH_AUTOR = "detail_buch_autor";
    public static final String DETAIL_VERLEIH_DATUM_VON = "detail_verleih_datum_von";
    public static final String DETAIL_VERLEIH_DATUM_BIS = "detail_verleih_datum_bis";

    //Tabllen-CREATE-Statement
    private static final String CREATE_TABLE_PERSON = "create table " + TABELLE_PERSONEN
            + "("
            + PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + PERSON_NACHNAME + " TEXT NOT NULL ,"
            + PERSON_VORNAME + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_BUCH =
            "create table " + TABELLE_BUCH
            + "("
            + BUCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + BUCH_TITEL + " TEXT NOT NULL ,"
            + BUCH_AUTOR + " TEXT NOT NULL ,"
            + BUCH_STATUS + " TEXT NOT NULL);";

    private static final String CREATE_TABLE_VERLEIH = "create table " + TABELLE_VERLEIH
            + "("
            + VERLEIH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + VERLEIH_PERSON_ID + " TEXT NOT NULL ,"
            + VERLEIH_BUCH_ID +   " TEXT NOT NULL ,"
            + VERLEIH_DATUM_VON + " TEXT NOT NULL ,"
            + VERLEIH_DATUM_BIS + " TEXT NOT NULL) ;";


    private static final String CREATE_TABLE_PERSONDETAIL = "create table " + TABELLE_PERSONDETAIL
            + "("
            + DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DETAIL_BUCH_ID + " TEXT NOT NULL ,"
            + DETAIL_BUCH_TITEL + " TEXT NOT NULL ,"
            + DETAIL_BUCH_AUTOR + " TEXT NOT NULL ,"
            + DETAIL_VERLEIH_DATUM_VON + " TEXT NOT NULL ,"
            + DETAIL_VERLEIH_DATUM_BIS + " TEXT NOT NULL);";


    public Datenbank_Helper(Context context)
    {
        super(context, DATENBANK_NAME, null, DATENBANK_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        //erstelle Tabellen Person und Buch
        sqLiteDatabase.execSQL(CREATE_TABLE_PERSON);
        sqLiteDatabase.execSQL(CREATE_TABLE_BUCH);
        sqLiteDatabase.execSQL(CREATE_TABLE_VERLEIH);
        sqLiteDatabase.execSQL(CREATE_TABLE_PERSONDETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
        //lösche alte Tabelle falls sie existiert
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABELLE_PERSONEN);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABELLE_BUCH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABELLE_VERLEIH);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABELLE_PERSONDETAIL);

        //und erstelle die Tabellen erneut
        onCreate(sqLiteDatabase);
    }
}
