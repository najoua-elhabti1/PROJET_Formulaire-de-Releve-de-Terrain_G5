/**
 * ViewListActivity - Activité permettant d'afficher la liste des relevés de terrain sauvegardés.
 * Cette classe récupère les entrées de la base de données et les affiche dans une `ListView`.
 */
package ensa.application01.releveterrain;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Classe représentant l'activité permettant d'afficher les relevés de terrain sous forme de liste.
 */
public class ViewListActivity extends AppCompatActivity {

    // ListView pour afficher les entrées
    /** ListView pour afficher les entrées. */
    private ListView listViewEntries;

    // Helper pour interagir avec la base de données
    /** Helper pour interagir avec la base de données. */
    private DatabaseHelper databaseHelper;

    /**
     * Méthode appelée lors de la création de l'activité.
     * Initialise la vue et charge les entrées depuis la base de données.
     *
     * @param savedInstanceState Instance sauvegardée de l'activité (peut contenir des données pour recréer l'état de l'activité).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        // Initialisation des composants
        listViewEntries = findViewById(R.id.listViewEntries);
        databaseHelper = new DatabaseHelper(this);

        // Charger et afficher les entrées
        loadEntries();
    }

    /**
     * Charge les entrées de la base de données et les affiche dans la `ListView`.
     * Si aucune entrée n'est disponible, un message s'affiche.
     */
    private void loadEntries() {
        // Récupération de toutes les entrées depuis la base de données
        Cursor cursor = databaseHelper.getAllEntries();
        if (cursor.getCount() > 0) {
            // Colonnes à afficher depuis la base de données
            String[] from = {
                    "site_name", "date", "coordinates", "description",
                    "terrain_type", "observations", "condition",
            };

            // Identifiants des vues dans le fichier XML `item_entry.xml`
            int[] to = {
                    R.id.editTextSiteName, R.id.editTextDate, R.id.textViewCoordinates,
                    R.id.editTextDescription, R.id.spinnerTerrainType, R.id.editTextObservations,
                    R.id.checkBoxCondition
            };

            // Création et configuration de l'adaptateur pour la liste
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.item_entry,
                    cursor,
                    from,
                    to,
                    0
            );

            // Liaison de l'adaptateur avec la `ListView`
            listViewEntries.setAdapter(adapter);
        } else {
            // Affichage d'un message si aucune entrée n'est trouvée
            Toast.makeText(this, "Aucune entrée trouvée.", Toast.LENGTH_SHORT).show();
        }
    }
}
