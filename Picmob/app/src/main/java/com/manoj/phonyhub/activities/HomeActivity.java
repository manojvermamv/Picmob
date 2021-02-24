package com.manoj.phonyhub.activities;

import android.animation.LayoutTransition;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.manoj.phonyhub.HelperMethod;
import com.manoj.phonyhub.MyApp;
import com.manoj.phonyhub.R;
import com.manoj.phonyhub.fragments.CategoriesFragment;
import com.manoj.phonyhub.fragments.HomeFragment;
import com.manoj.phonyhub.fragments.ProfileFragment;

import java.util.Objects;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final long exitDelay = 2000L;
    private final String Home_Fragment_Tag = "homeFragment";
    boolean backStackFragment;
    Toolbar toolbar;
    TabLayout tabLayout;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Context context;
    Context context_global = MyApp.getContext();
    private HomeFragment homeFragment;
    private boolean recentlyBackPressed = false;
    private final Runnable exitRunnable = new Runnable() {
        @Override
        public void run() {
            recentlyBackPressed = false;
        }
    };
    private Handler exitHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbarHome);
        toolbar.setTitle("Wallpapers");
        //toolbar.setTitleTextColor(getResources().getColor(R.color.textColorLight));
        setSupportActionBar(toolbar);

        // initialize Navigation Drawer & Fresco
        Fresco.initialize(this);
        initNavigationDrawer();

        tabLayout = findViewById(R.id.tabLayoutHome);
        frameLayout = findViewById(R.id.frameContainer);

        // Creating tabs with name and icon
        TabLayout.Tab homeTab = tabLayout.newTab();
        homeTab.setText("Home");

        TabLayout.Tab categoriesTab = tabLayout.newTab();
        categoriesTab.setText("Categories");

        TabLayout.Tab profileTab = tabLayout.newTab();
        profileTab.setText("Profile");

        // Setting home tab as default tab with selected icon color & Adding to recently created tabs in TabLayout
        tabLayout.addTab(homeTab, true);  //homeTab.getIcon().setColorFilter(Color.parseColor("#FDFDFD"), PorterDuff.Mode.SRC_IN);
        tabLayout.addTab(categoriesTab);
        tabLayout.addTab(profileTab);

        if (savedInstanceState != null) {
            // fragment may exist , Now look up the instance that already exists by Tag
            homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(Home_Fragment_Tag);
        } else if (homeFragment == null) {
            // only create fragment if they haven't been instantiated already
            homeFragment = new HomeFragment();
        }

        if (!homeFragment.isInLayout()) {
            // Setting HomeFragment as default fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameContainer, homeFragment, Home_Fragment_Tag).addToBackStack("HomeBackStack")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
        }

        backStackFragment = getSupportFragmentManager().getBackStackEntryCount() > 0;

        // Performing action on tab selected
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        if (backStackFragment) {
                            getSupportFragmentManager().popBackStack("HomeBackStack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            //getSupportFragmentManager().popBackStack();
                        } else {
                            fragment = new HomeFragment();
                        }
                        toolbar.setTitle("Wallpapers");
                        break;
                    case 1:
                        fragment = new CategoriesFragment();
                        toolbar.setTitle(categoriesTab.getText());
                        break;
                    case 2:
                        fragment = new ProfileFragment();
                        toolbar.setTitle(profileTab.getText());
                        break;
                }
                if (fragment != null) {
                    //getSupportFragmentManager().saveFragmentInstanceState(fragment);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }


    @Override
    protected void onResume() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            //getSupportFragmentManager().popBackStack("ImageDialogBackStack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (getSupportFragmentManager().findFragmentByTag("ImageDialog") != null) {
            if (backStackFragment) {
                //getSupportFragmentManager().popBackStack("HomeBackStack", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.home_menu_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        //SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search wallpapers");

        int searchBarId = searchView.getContext().getResources().getIdentifier("android:id/search_bar", null, null);
        LinearLayout searchBar = (LinearLayout) searchView.findViewById(androidx.appcompat.R.id.search_bar);
        searchBar.setLayoutTransition(new LayoutTransition());
        //searchView.setLayoutTransition(new LayoutTransition());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
                searchIntent.putExtra("Search", query.toLowerCase());
                startActivity(searchIntent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.toolbarHome) {
            HelperMethod.toast("ToolBarHome");
        } else if (id == R.id.navigation_user_profile) {
            HelperMethod.toast("Profile");
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.navigation_item_wallpapers) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.navigation_item_video_wallpapers) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.navigation_item_ringtones) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.navigation_item_notification_sound) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, TestActivity.class));
        } else if (id == R.id.navigation_item_upload) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, EndlessScrollingActivity.class));
        } else if (id == R.id.navigation_item_favorite) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, FavoritesActivity.class));
        } else if (id == R.id.navigation_item_settings) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.navigation_item_help) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (id == R.id.navigation_item_information) {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, AboutActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        if (tabLayout.getSelectedTabPosition() == 0) {
            if (recentlyBackPressed) {
                exitHandler.removeCallbacks(exitRunnable);
                exitHandler = null;
                super.onBackPressed();
                finish();
            } else {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    recentlyBackPressed = true;
                    HelperMethod.customToast("Press again to exit", "short", this);
                    exitHandler.postDelayed(exitRunnable, exitDelay);
                }
            }
        } else {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                Objects.requireNonNull(tabLayout.getTabAt(0)).select();
            }
        }
    }

    public void initNavigationDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout_root);
        navigationView = findViewById(R.id.navigation_view_home);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING) {
                    if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        //HelperMethod.toast("drawer stared opening");
                    } else {
                        //HelperMethod.toast("drawer stared closing");
                    }
                }
            }
        };

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_round_24);

        // initialize Navigation Items & set OnClick Listener on it
        LinearLayout navigationProfile, navigationWallpaper, navigationVideoWall, navigationRingtone, navigationNotification, navigationUpload, navigationFavorites, navigationSettings, navigationHelp, navigationInfo;
        navigationProfile = findViewById(R.id.navigation_user_profile);
        navigationWallpaper = findViewById(R.id.navigation_item_wallpapers);
        navigationVideoWall = findViewById(R.id.navigation_item_video_wallpapers);
        navigationRingtone = findViewById(R.id.navigation_item_ringtones);
        navigationNotification = findViewById(R.id.navigation_item_notification_sound);
        navigationUpload = findViewById(R.id.navigation_item_upload);
        navigationFavorites = findViewById(R.id.navigation_item_favorite);
        navigationSettings = findViewById(R.id.navigation_item_settings);
        navigationHelp = findViewById(R.id.navigation_item_help);
        navigationInfo = findViewById(R.id.navigation_item_information);
        navigationProfile.setOnClickListener(HomeActivity.this);
        navigationWallpaper.setOnClickListener(HomeActivity.this);
        navigationVideoWall.setOnClickListener(HomeActivity.this);
        navigationRingtone.setOnClickListener(HomeActivity.this);
        navigationNotification.setOnClickListener(HomeActivity.this);
        navigationUpload.setOnClickListener(HomeActivity.this);
        navigationFavorites.setOnClickListener(HomeActivity.this);
        navigationSettings.setOnClickListener(HomeActivity.this);
        navigationHelp.setOnClickListener(HomeActivity.this);
        navigationInfo.setOnClickListener(HomeActivity.this);

        // Removing navigation drawer help menu
        navigationHelp.setVisibility(View.GONE);

    }

//    public void showPopupMenu(View view) {
//        PopupMenu popup = new PopupMenu(this, view);
//        popup.getMenuInflater().inflate(R.menu.main_menu, popup.getMenu());
//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.menuTest1:
//                        Intent test1Intent = new Intent(HomeActivity.this, Test1Activity.class);
//                        startActivity(test1Intent);
//                        break;
//                }
//                return true;
//            }
//        });
//        popup.show();
//    }

}