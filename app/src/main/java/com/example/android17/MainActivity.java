package com.example.android17;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private Button newGame;
    private Button replayGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        newGame = findViewById(R.id.newGame);
        replayGame = findViewById(R.id.replayGame);
        newGame.setOnClickListener((V)->startNewGame());
    }


    private void startNewGame() {
//        Bundle bundle = new Bundle();

//        bundle.putInt(AddEditMovie.MOVIE_INDEX, pos);
//        bundle.putString(AddEditMovie.MOVIE_NAME, movie.name);
//        bundle.putString(AddEditMovie.MOVIE_YEAR, movie.year);
//        bundle.putString(AddEditMovie.MOVIE_DIRECTOR, movie.director);
//        Intent intent = new Intent(this, AddEditMovie.class);
//        intent.putExtras(bundle);
//        startActivityForResult(intent, EDIT_MOVIE_CODE);
          Intent intent = new Intent(this, PlayGame.class);
          startActivity(intent);
    }
}
