/**
 * Classe de gestion de la base de données pour l'application de relevé terrain.
 * Cette classe gère la création, la mise à jour, et les opérations CRUD (Create, Read, Update, Delete) sur la base de données SQLite.
 */
package ensa.application01.releveterrain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Cette classe `DatabaseHelper` est responsable de la gestion de la base de données SQLite utilisée dans l'application de relevé terrain.
 * Elle permet la création initiale de la base, la mise à jour des versions et la manipulation des données via les opérations CRUD.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Constantes pour la base de données
    /** Nom de la base de données */
    private static final String DATABASE_NAME = "ReleveTerrain.db";
    /** Version de la base de données */
    private static final int DATABASE_VERSION = 1;

    // Constantes pour la table et les colonnes
    /** Nom de la table où les entrées seront stockées */
    private static final String TABLE_ENTRIES = "entries";
    /** ID unique pour chaque entrée */
    private static final String COLUMN_ID = "id";
    /** Nom du site ou de l'emplacement */
    private static final String COLUMN_SITE_NAME = "site_name";
    /** Date à laquelle l'entrée a été réalisée */
    private static final String COLUMN_DATE = "date";
    /** Coordonnées GPS du site ou de l'emplacement */
    private static final String COLUMN_COORDINATES = "coordinates";
    /** Description détaillée du site */
    private static final String COLUMN_DESCRIPTION = "description";
    /** Type de terrain associé au site */
    private static final String COLUMN_TERRAIN_TYPE = "terrain_type";
    /** Observations collectées sur le site */
    private static final String COLUMN_OBSERVATIONS = "observations";
    /** Condition actuelle du site */
    private static final String COLUMN_CONDITION = "condition";

    /**
     * Constructeur pour initialiser la base de données avec le contexte de l'application.
     *
     * @param context Le contexte de l'application, généralement utilisé pour accéder aux ressources du système.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Méthode appelée lors de la création initiale de la base de données.
     * Elle crée la table nécessaire pour stocker les données liées aux relevés terrain.
     *
     * @param db L'instance SQLiteDatabase permettant d'exécuter des commandes SQL.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_ENTRIES = "CREATE TABLE " + TABLE_ENTRIES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SITE_NAME + " TEXT," +
                COLUMN_DATE + " TEXT," +
                COLUMN_COORDINATES + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_TERRAIN_TYPE + " TEXT," +
                COLUMN_OBSERVATIONS + " TEXT," +
                COLUMN_CONDITION + " TEXT)";
        db.execSQL(CREATE_TABLE_ENTRIES);
    }

    /**
     * Méthode appelée lors de la mise à jour de la base de données si la version change.
     * Cette méthode supprime la table existante et la recrée avec la nouvelle version.
     *
     * @param db         L'instance SQLiteDatabase permettant d'exécuter des commandes SQL.
     * @param oldVersion Version précédente de la base de données.
     * @param newVersion Nouvelle version de la base de données.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        onCreate(db);
    }

    /**
     * Insère une nouvelle entrée dans la table `entries`.
     *
     * @param siteName    Nom du site ou de l'emplacement.
     * @param date        Date de l'entrée.
     * @param coordinates Coordonnées GPS du site.
     * @param description Description détaillée du site.
     * @param observations Observations collectées sur le site.
     * @param terrainType Type de terrain associé au site.
     * @param condition   Condition actuelle du site.
     * @return L'ID de la ligne insérée, ou -1 si l'insertion a échoué.
     */
    public long insertEntry(String siteName, String date, String coordinates, String description, String observations,
                            String terrainType, String condition) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SITE_NAME, siteName);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_COORDINATES, coordinates);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_TERRAIN_TYPE, terrainType);
        values.put(COLUMN_OBSERVATIONS, observations);
        values.put(COLUMN_CONDITION, condition);

        return db.insert(TABLE_ENTRIES, null, values);
    }

    /**
     * Récupère toutes les entrées stockées dans la base de données.
     * Les colonnes sélectionnées incluent un alias `_id` pour la compatibilité avec les adaptateurs de l'interface utilisateur.
     *
     * @return Un curseur contenant toutes les données des entrées.
     */
    public Cursor getAllEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT id as _id, site_name, date, coordinates, description, terrain_type, observations, condition FROM " + TABLE_ENTRIES, null);
    }
}
