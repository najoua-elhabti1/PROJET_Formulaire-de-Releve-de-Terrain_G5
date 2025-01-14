/**
 * MainActivity - Activité principale de l'application pour le relevé terrain.
 * Cette classe permet de capturer des coordonnées GPS, saisir des informations générales,
 * des observations, des mesures, et de manipuler ces données via les opérations CRUD (Create, Read, Update, Delete).
 * Fonctionnalités principales :
 * - Capture des coordonnées GPS du périphérique.
 * - Saisie d'informations détaillées concernant le site (nom, date, description).
 * - Sélection du type de terrain via un menu déroulant.
 * - Enregistrement des données capturées dans une base de données locale.
 * - Visualisation des entrées enregistrées.
 * - Gestion des conditions de site à l'aide de checkboxes.
 */
package ensa.application01.releveterrain;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Activité principale de l'application pour le relevé terrain.
 * Cette classe permet la création initiale de la base, la mise à jour des versions et la manipulation des données via les opérations CRUD.
 * Elle facilite également la capture des coordonnées GPS, l'enregistrement des observations, et la gestion des conditions de site.
 */
public class MainActivity extends AppCompatActivity {

    /** Code de demande de permission pour la localisation. */
    private static final int LOCATION_REQUEST_CODE = 100;

    /** Client pour les services de localisation. */
    private FusedLocationProviderClient fusedLocationProviderClient;

    /** Objet définissant les paramètres de la requête de localisation. */
    private LocationRequest locationRequest;

    // Champs de saisie pour les informations du site
    /** Champ de saisie pour le nom du site. */
    private EditText editTextSiteName;

    /** Champ de saisie pour la date. */
    private EditText editTextDate;

    /** Champ de saisie pour la description. */
    private EditText editTextDescription;

    /** Champ de saisie pour les observations. */
    private EditText editTextObservations;

    /** Menu déroulant pour sélectionner le type de terrain. */
    private Spinner spinnerTerrainType;

    // Checkboxes pour les options de condition du site
    /** Case à cocher pour indiquer un bon état. */
    private CheckBox checkBoxGoodCondition;

    /** Case à cocher pour indiquer un état endommagé. */
    private CheckBox checkBoxDamaged;

    /** Case à cocher pour indiquer un état moyen. */
    private CheckBox checkBoxMoy;

    // Boutons pour capturer les coordonnées, enregistrer et afficher les entrées
    /** Bouton pour capturer les coordonnées GPS. */
    private Button buttonCaptureCoordinates;

    /** Bouton pour enregistrer l'entrée actuelle. */
    private Button buttonSaveEntry;

    /** Bouton pour afficher les entrées sauvegardées. */
    private Button buttonViewEntries;

    /** Zone de texte pour afficher les coordonnées GPS capturées. */
    private TextView textViewCoordinates;

    /** Instance de l'assistant pour la gestion de la base de données. */
    private DatabaseHelper databaseHelper;

    /**
     * Méthode appelée lors de la création de l'activité.
     * Initialisation de l'interface utilisateur et des services requis.
     *
     * @param savedInstanceState État sauvegardé de l'activité, s'il y en a un.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des éléments de l'interface utilisateur
        editTextSiteName = findViewById(R.id.editTextSiteName);
        editTextDate = findViewById(R.id.editTextDate);
        textViewCoordinates = findViewById(R.id.textViewCoordinates);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerTerrainType = findViewById(R.id.spinnerTerrainType);

        // Configuration du spinner pour le type de terrain
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.terrain_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTerrainType.setAdapter(adapter);

        editTextObservations = findViewById(R.id.editTextObservations);
        checkBoxGoodCondition = findViewById(R.id.checkBoxGoodCondition);
        checkBoxDamaged = findViewById(R.id.checkBoxDamaged);
        checkBoxMoy = findViewById(R.id.checkBoxMoy);
        buttonCaptureCoordinates = findViewById(R.id.buttonCaptureCoordinates);
        buttonSaveEntry = findViewById(R.id.buttonSave);
        buttonViewEntries = findViewById(R.id.buttonViewEntries);

        // Gestion des interactions entre les cases à cocher
        setupCheckBoxListeners();

        // Initialisation du client de localisation
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationPermission();

        // Configuration des actions des boutons
        buttonCaptureCoordinates.setOnClickListener(v -> getCurrentLocation());
        buttonSaveEntry.setOnClickListener(v -> saveEntry());
        buttonViewEntries.setOnClickListener(v -> viewSavedEntries());

        // Initialisation de la base de données
        databaseHelper = new DatabaseHelper(this);
    }

    /**
     * Configure la gestion des interactions entre les cases à cocher.
     */
    private void setupCheckBoxListeners() {
        checkBoxGoodCondition.setOnClickListener(v -> {
            if (checkBoxGoodCondition.isChecked()) {
                checkBoxDamaged.setChecked(false);
                checkBoxMoy.setChecked(false);
            }
        });

        checkBoxDamaged.setOnClickListener(v -> {
            if (checkBoxDamaged.isChecked()) {
                checkBoxGoodCondition.setChecked(false);
                checkBoxMoy.setChecked(false);
            }
        });

        checkBoxMoy.setOnClickListener(v -> {
            if (checkBoxMoy.isChecked()) {
                checkBoxGoodCondition.setChecked(false);
                checkBoxDamaged.setChecked(false);
            }
        });
    }

    /**
     * Demande les permissions de localisation à l'utilisateur.
     * Si les permissions sont déjà accordées, configure la requête de localisation.
     */
    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        } else {
            setupLocationRequest();
        }
    }

    /**
     * Configure la requête de localisation.
     */
    private void setupLocationRequest() {
        locationRequest = new LocationRequest.Builder(30000) // Mise à jour toutes les 30 secondes
                .setMinUpdateIntervalMillis(5000) // Intervalle minimum de mise à jour
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY) // Priorité équilibrée
                .build();
    }

    /**
     * Récupère la localisation actuelle du périphérique.
     * Affiche les coordonnées GPS capturées dans une zone de texte.
     */
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            textViewCoordinates.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
                        } else {
                            Toast.makeText(this, "Impossible d'obtenir la localisation.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Erreur lors de l'obtention de la localisation.", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Permission de localisation refusée.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Enregistre les informations de l'utilisateur dans la base de données locale.
     * Affiche un message de confirmation ou d'erreur selon le succès de l'enregistrement.
     */
    private void saveEntry() {
        String siteName = editTextSiteName.getText().toString();
        String date = editTextDate.getText().toString();
        String coordinates = textViewCoordinates.getText().toString();
        String description = editTextDescription.getText().toString();
        String observations = editTextObservations.getText().toString();
        String terrainType = spinnerTerrainType.getSelectedItem().toString();

        String condition = determineCondition();
        // Validation des champs obligatoires
        if (siteName.isEmpty()) {
            editTextSiteName.setError("Le nom du site est obligatoire");
            editTextSiteName.requestFocus();
            return;
        }

        if (date.isEmpty()) {
            editTextDate.setError("La date du relevé est obligatoire");
            editTextDate.requestFocus();
            return;
        }

        if (description.isEmpty()) {
            editTextDescription.setError("La description est obligatoire");
            editTextDescription.requestFocus();
            return;
        }
        if (observations.isEmpty()) {
            editTextDescription.setError("Les observations sont obligatoires");
            editTextDescription.requestFocus();
            return;
        }
        if (terrainType.isEmpty()) {
            editTextDescription.setError("Le type du terrain obligatoire");
            editTextDescription.requestFocus();
            return;
        }
        if (determineCondition().isEmpty()) {
            editTextDescription.setError("L'etat des l'infrastructures obligatoire");
            editTextDescription.requestFocus();
            return;
        }
        long id = databaseHelper.insertEntry(siteName, date, coordinates, description, observations, terrainType, condition);
        if (id != -1) {
            Toast.makeText(this, "Entrée enregistrée avec succès!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erreur lors de l'enregistrement.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Détermine l'état du site en fonction des cases à cocher sélectionnées.
     * @return L'état sélectionné : "Bon état", "Endommagé", "Moyenne" ou "Non spécifié".
     */
    private String determineCondition() {
        if (checkBoxGoodCondition.isChecked()) {
            return "Bon état";
        } else if (checkBoxDamaged.isChecked()) {
            return "Endommagé";
        } else if (checkBoxMoy.isChecked()) {
            return "Moyenne";
        }
        return "Non spécifié";
    }

    /**
     * Lance une nouvelle activité pour afficher les entrées sauvegardées.
     */
    private void viewSavedEntries() {
        Intent intent = new Intent(MainActivity.this, ViewListActivity.class);
        startActivity(intent);
    }

    /**
     * Gère le résultat des permissions demandées (localisation).
     * Configure la requête de localisation si la permission est accordée.
     *
     * @param requestCode Code de la demande.
     * @param permissions Tableau des permissions demandées.
     * @param grantResults Résultats des permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setupLocationRequest();
        } else {
            Toast.makeText(this, "Permission de localisation refusée.", Toast.LENGTH_SHORT).show();
        }
    }
}
