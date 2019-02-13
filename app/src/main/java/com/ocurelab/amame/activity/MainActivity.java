package com.ocurelab.amame.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.ocurelab.amame.R;
import com.ocurelab.amame.fragment.Dominique;
import com.ocurelab.amame.fragment.ForumFragment;
import com.ocurelab.amame.fragment.MainFragment;
import com.ocurelab.amame.fragment.ServiceFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout=findViewById(R.id.main_activity);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState==null){
            MainFragment mainFragment= new MainFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,mainFragment).commit();
        }

        navigationView=findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        switch (id){
            case R.id.conseil:
                MainFragment mainFragment= new MainFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,mainFragment).commit();
                break;
            case R.id.service:
                ServiceFragment serviceFragment= new ServiceFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,serviceFragment).commit();
                break;
            case R.id.forum:
                ForumFragment forumFragment = new ForumFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,forumFragment).commit();
                break;
            case R.id.domi:
                Dominique dominique = new Dominique();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainFrame,dominique).commit();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}
