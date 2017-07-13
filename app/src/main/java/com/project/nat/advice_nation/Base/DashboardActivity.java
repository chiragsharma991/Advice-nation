package com.project.nat.advice_nation.Base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.Constants;
import com.project.nat.advice_nation.utils.DialogUtils;
import com.project.nat.advice_nation.utils.pageindicator.CirclePageIndicator;

import static com.project.nat.advice_nation.R.id.viewPager;

public class DashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ViewPager view_pager;
    private FragmentStatePagerAdapter mHeaderPagerAdapter;
    private CirclePageIndicator page_Indicator;
    private Handler handler=new Handler();
    private int delay=5000;
    private String TAG="DashboardActivity";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardn);
        context=DashboardActivity.this;
        initialize();

    }

    private void initialize()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        page_Indicator = (CirclePageIndicator) findViewById(R.id.pageIndicator);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        view_pager=(ViewPager)findViewById(viewPager);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.app_name));
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setViewPagerAdapter(view_pager);
    }



    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
            overridePendingTransition(R.anim.frist_to_second, R.anim.second_to_frist);
        }
    }

    public void setViewPagerAdapter(ViewPager viewPager){

        mHeaderPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 4;
            }
            @Override
            public Fragment getItem(int position) {
                PagerFragment pagerFragment = new PagerFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.Bundle_Pos, position);
                pagerFragment.setArguments(bundle);

                return pagerFragment;
            }
            @Override
            public Parcelable saveState() {return null;}

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return null;
            }
        };
        viewPager.setAdapter(mHeaderPagerAdapter);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(0);
        page_Indicator.setViewPager(viewPager);
        handler.postDelayed(runnable, delay);
    }

    Runnable runnable = new Runnable() {
        public void run() {
            Log.e(TAG, "runable: " );

        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.about) {

          About.startScreen(context);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, DashboardActivity.class));
    }

    public void onClickButton(View view){
        SubcategoryActivity.startScreen(context);
     //   overridePendingTransition(R.anim.start, R.anim.exit);
    }
}
