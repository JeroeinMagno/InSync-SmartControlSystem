package com.example.insync_smartcontrolsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import android.content.res.ColorStateList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private TextView currentTimeTextView;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView roomNameTextView;
    private ImageView roomImageView;
    private TextView profileNameTextView;  // Profile name TextView

    private MaterialButton selectedButton = null; // Track the selected button

    // Color values for selected and unselected buttons
    private static final int SELECTED_COLOR = 0xFFC4A372; // Active color
    private static final int UNSELECTED_COLOR = 0xFFF3D19F; // Inactive color
    private static final int COLOR_ON = 0xFF4CAF50;  // Green
    private static final int COLOR_OFF = 0xFFFFFFFF; // White

    private MaterialCardView device1Card, device2Card, device3Card, device4Card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Enable edge-to-edge layout
            EdgeToEdge.enable(this);

            // Set up the view and layout
            setContentView(R.layout.activity_dashboard);

            // Initialize the current time TextView
            currentTimeTextView = findViewById(R.id.current_time);

            firestore = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();





            // Hide the action bar if present
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }

            // Set padding for system bars (edge-to-edge effect)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dashboard), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });



            // Initialize DrawerLayout and NavigationView
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);

            View headerView = navigationView.getHeaderView(0);
            profileNameTextView = headerView.findViewById(R.id.profile_name);

            // Initialize room-related views
            roomNameTextView = findViewById(R.id.room_name);
            roomImageView = findViewById(R.id.room_image);

            // Initialize the device cards
            device1Card = findViewById(R.id.device1_card);
            device2Card = findViewById(R.id.device2_card);
            device3Card = findViewById(R.id.device3_card);
            device4Card = findViewById(R.id.device4_card);

            // Set up the toggle for each device
            setupDeviceToggle(device1Card, R.id.device1_status_light, R.id.on_off_device1_text);
            setupDeviceToggle(device2Card, R.id.device2_status_light, R.id.on_off_device2_text);
            setupDeviceToggle(device3Card, R.id.device3_status_light, R.id.on_off_device3_text);
            setupDeviceToggle(device4Card, R.id.device4_status_light, R.id.on_off_device4_text);

            // Set up buttons for each room
            MaterialButton bedroomButton = findViewById(R.id.bedroom_btn);
            MaterialButton diningRoomButton = findViewById(R.id.diningroom_btn);
            MaterialButton kitchenButton = findViewById(R.id.kitchen_btn);
            MaterialButton livingRoomButton = findViewById(R.id.livingroom_btn);

            // Set up the hamburger button to open the drawer
            ImageButton hamburgerButton = findViewById(R.id.hamburger_button);
            hamburgerButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

            // Set up a listener for navigation item selection
            navigationView.setNavigationItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.overview) {
                    Intent intent = new Intent(DashboardActivity.this, OverviewActivity.class);
                    startActivity(intent);
                } else if (id == R.id.sensors) {
                    Intent intent = new Intent(DashboardActivity.this, SensorsActivity.class);
                    startActivity(intent);
                } else if (id == R.id.networking) {
                    Intent intent = new Intent(DashboardActivity.this, NetworkingActivity.class);
                    startActivity(intent);
                } else if (id == R.id.cloud_computing) {
                    Intent intent = new Intent(DashboardActivity.this, CloudComputingActivity.class);
                    startActivity(intent);
                } else if (id == R.id.security) {
                    Intent intent = new Intent(DashboardActivity.this, SecurityActivity.class);
                    startActivity(intent);
                } else if (id == R.id.data_management) {
                    Intent intent = new Intent(DashboardActivity.this, DataManagementActivity.class);
                    startActivity(intent);
                } else if (id == R.id.edge_computing) {
                    Intent intent = new Intent(DashboardActivity.this, EdgeComputingActivity.class);
                    startActivity(intent);
                } else if (id == R.id.analytics) {
                    Intent intent = new Intent(DashboardActivity.this, AnalyticsActivity.class);
                    startActivity(intent);
                } else if (id == R.id.applications) {
                    Intent intent = new Intent(DashboardActivity.this, ApplicationsActivity.class);
                    startActivity(intent);
                } else if (id == R.id.standards) {
                    Intent intent = new Intent(DashboardActivity.this, StandardsActivity.class);
                    startActivity(intent);
                } else if (id == R.id.about) {
                    Intent intent = new Intent(DashboardActivity.this, AboutActivity.class);
                    startActivity(intent);
                } else if (id == R.id.logout) {
                    new AlertDialog.Builder(DashboardActivity.this)
                            .setMessage("Are you sure you want to log out?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", (dialog, id1) -> {
                                // Perform log out action here (e.g., FirebaseAuth sign out)
                                FirebaseAuth.getInstance().signOut();  // Uncomment this if using FirebaseAuth

                                Toast.makeText(DashboardActivity.this, "Logged out", Toast.LENGTH_SHORT).show();

                                // Redirect to LoginActivity after log out
                                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();  // Close the current activity so that the user can't go back to the Dashboard
                            })
                            .setNegativeButton("No", null)
                            .show();
                } else {
                    Toast.makeText(DashboardActivity.this, "Unknown option selected", Toast.LENGTH_SHORT).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });


            // Set up button click listeners for changing room content
            livingRoomButton.setOnClickListener(v -> {
                selectButton(livingRoomButton);
                roomNameTextView.setText("Living Room");
                showDevices();
                roomImageView.setImageResource(R.drawable.livingroom_bg);
            });

            bedroomButton.setOnClickListener(v -> {
                selectButton(bedroomButton);
                roomNameTextView.setText("Bedroom");
                hideDevices();
                roomImageView.setImageResource(R.drawable.bedroom_bg);
            });

            diningRoomButton.setOnClickListener(v -> {
                selectButton(diningRoomButton);
                roomNameTextView.setText("Dining Room");
                hideDevices();
                roomImageView.setImageResource(R.drawable.diningroom_bg);
            });

            kitchenButton.setOnClickListener(v -> {
                selectButton(kitchenButton);
                roomNameTextView.setText("Kitchen");
                hideDevices();
                roomImageView.setImageResource(R.drawable.kitchen_bg);
            });

            // Set Living Room as the default room
            selectButton(livingRoomButton);
            roomNameTextView.setText("Living Room");
            roomImageView.setImageResource(R.drawable.livingroom_bg);

            // Update profile name in the navigation drawer
            updateProfileName();
            // Update the current time every second
            updateCurrentTime();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCurrentTime() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Get the current time formatted as "hh:mm:ss a"
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                String currentTime = sdf.format(new Date());

                // Update the TextView with the current time
                currentTimeTextView.setText(currentTime);

                // Repeat every second
                handler.postDelayed(this, 1000);
            }
        };

        handler.post(runnable);
    }

    // Method to update the profile name
    private void updateProfileName() {
        // Retrieve the username from the Intent passed from SignupActivity if available
        String username = getIntent().getStringExtra("USERNAME");

        if (username != null) {
            // If username is passed via Intent, display it
            if (profileNameTextView != null) {
                profileNameTextView.setText(username);
            }
        } else {
            // Otherwise, fetch the username from Firestore (for logged-in users)
            if (auth.getCurrentUser() != null) {
                String userId = auth.getCurrentUser().getUid();
                DocumentReference userRef = firestore.collection("users").document(userId);

                userRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String profileName = task.getResult().getString("name");
                        if (profileName != null) {
                            if (profileNameTextView != null) {
                                profileNameTextView.setText(profileName); // Update the TextView
                            } else {
                                Log.e("DashboardActivity", "profileNameTextView is null");
                            }
                        } else {
                            Log.e("DashboardActivity", "Profile name is null");
                        }
                    } else {
                        Log.e("DashboardActivity", "Failed to load profile name: " + task.getException());
                        Toast.makeText(DashboardActivity.this, "Failed to load profile name.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.e("DashboardActivity", "User is not logged in");
            }
        }
    }

    // Method for setting up device toggle functionality
    private void setupDeviceToggle(MaterialCardView deviceCard, int circleId, int textId) {
        View statusCircle = deviceCard.findViewById(circleId);
        TextView statusText = deviceCard.findViewById(textId);

        // Set an OnClickListener for toggling
        deviceCard.setOnClickListener(v -> {
            if ("OFF".contentEquals(statusText.getText())) {
                statusCircle.setBackgroundTintList(ColorStateList.valueOf(COLOR_ON));
                statusText.setText("ON");
            } else {
                statusCircle.setBackgroundTintList(ColorStateList.valueOf(COLOR_OFF));
                statusText.setText("OFF");
            }
        });
    }

    // Method to highlight the selected button and update the UI accordingly
    private void selectButton(MaterialButton selectedButton) {
        if (this.selectedButton != null) {
            this.selectedButton.setTextColor(ColorStateList.valueOf(0xFFF3D19F));
        }
        this.selectedButton = selectedButton;
        this.selectedButton.setTextColor(ColorStateList.valueOf(SELECTED_COLOR));
    }

    // Method to show device cards for the living room
    private void showDevices() {
        device1Card.setVisibility(View.VISIBLE);
        device2Card.setVisibility(View.VISIBLE);
        device3Card.setVisibility(View.VISIBLE);
        device4Card.setVisibility(View.VISIBLE);
    }

    // Method to hide device cards for non-living room sections
    private void hideDevices() {
        device1Card.setVisibility(View.GONE);
        device2Card.setVisibility(View.GONE);
        device3Card.setVisibility(View.GONE);
        device4Card.setVisibility(View.GONE);
    }



}

