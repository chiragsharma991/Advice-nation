package com.project.nat.advice_nation.Base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.project.nat.advice_nation.Adapter.SubcategoryAdapter;
import com.project.nat.advice_nation.Fragment.AnKoins;
import com.project.nat.advice_nation.Fragment.HomeFragment;
import com.project.nat.advice_nation.Https.ApiResponse;
import com.project.nat.advice_nation.Https.GetApi;
import com.project.nat.advice_nation.Https.PostApi;
import com.project.nat.advice_nation.Model.Category;
import com.project.nat.advice_nation.Model.Subcategory;
import com.project.nat.advice_nation.R;
import com.project.nat.advice_nation.RecylerViewClick.RecyclerItemClickListener;
import com.project.nat.advice_nation.utils.BaseActivity;
import com.project.nat.advice_nation.utils.Constants;
import com.project.nat.advice_nation.utils.DialogUtils;
import com.project.nat.advice_nation.utils.NetworkUrl;
import com.project.nat.advice_nation.utils.pageindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.project.nat.advice_nation.R.anim.exit;
import static com.project.nat.advice_nation.R.id.progressBar;
import static com.project.nat.advice_nation.R.id.progressBarToolbar;
import static com.project.nat.advice_nation.R.id.viewPager;

public class DashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,HomeFragment.OnFragmentInteractionListener,ApiResponse
{

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ViewPager view_pager;
    private FragmentStatePagerAdapter mHeaderPagerAdapter;
    private CirclePageIndicator page_Indicator;
    private Handler handler=new Handler();
    private int delay=5000;
    private String TAG="DashboardActivity";
    private Context context;
    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_ANKOINS = "ankoins";
    public static String CURRENT_TAG = TAG_HOME;
    private DrawerLayout drawer;
    private Handler mHandler;
    private NavigationView navigationView;
    private ProgressBar progressBar;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private String referralCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardn);
        context=DashboardActivity.this;
        initialize();
        if (isOnline(context)) {
            callback(0);
        }
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment(getString(R.string.app_name));
        }

    }

    private void initialize()
    {
        gson=new Gson();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHandler = new Handler();

        page_Indicator = (CirclePageIndicator) findViewById(R.id.pageIndicator);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        view_pager=(ViewPager)findViewById(viewPager);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitle(getString(R.string.app_name));
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }


    private void callback(int id) {
        switch (id) {
            case 0:
                //http://ec2-13-126-97-168.ap-south-1.compute.amazonaws.com:8080/AdviseNation/api/users/85384665/referral
                long user = sharedPreferences.getLong("id", 0);
                String bearerToken = sharedPreferences.getString("bearerToken", "");
                Log.e(TAG, "bearerToken: " + bearerToken);
                String URL = NetworkUrl.URL + user + "/referral";
                String apiTag = NetworkUrl.URL_CATEGORY + user + "/referral";
                GetApi getApi = new GetApi(context, URL, bearerToken, apiTag, TAG, 0); //0 is for finish second api call
                break;

            default:
                break;
        }
    }

    @Override
    public void OnSucess(JSONObject response, int id) {

        switch (id) {
            case 0:
                Log.e(TAG, "OnSucess: "+response.toString() );
                try {
                    JSONObject jsonObject=new JSONObject(response.toString());
                    referralCode=jsonObject.getString("data");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("referralCode",referralCode);
                    editor.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }



    @Override
    public void OnFailed(int error, int id) {
        Log.e(TAG, "OnFailed: " + error);
        progressBar.setVisibility(View.GONE);
        switch (error) {
            case 000:
                Log.e(TAG, "OnFailed: "+getResources().getString(R.string.network_poor ));
                break;
            case 500:
                Log.e(TAG, "OnFailed: "+getResources().getString(R.string.error_500 ));
                break;
            default:
                Log.e(TAG, "OnFailed: "+getResources().getString(R.string.random_error ));
        }

    }


    private void loadHomeFragment(String title)
    {
        setToolbarTitle(title);
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null)
        {
            drawer.closeDrawers();
            return;
        }

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private void setToolbarTitle(String title) {
        //getSupportActionBar().setTitle(title);
        collapsingToolbarLayout.setTitle(title);

    }

    private Fragment getHomeFragment() {
        Log.e(TAG, "getHomeFragment: " );
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;

            case 2:
                // Ankoins fragment
                AnKoins anKoins = new AnKoins();
                return anKoins;

            default:
                return new HomeFragment();
        }
    }


    private Boolean exit = false;

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            if (exit)
            {
                finish();
            } else
            {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        exit = false;
                    }
                }, 3 * 1000);
            }
        }
    }

    public void setViewPagerAdapter(ViewPager viewPager, final ArrayList<Subcategory> carouseimages){

        mHeaderPagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return carouseimages.get(0).getData().size();
            }
            @Override
            public Fragment getItem(int position) {
                PagerFragment pagerFragment = new PagerFragment();
                Bundle bundle = new Bundle();
                String list=gson.toJson(carouseimages);
                bundle.putInt(Constants.Bundle_Pos, position);
                bundle.putString(Constants.KEY, list);
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
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            Login.startScreen(context);
            finish();
            return true;
        }
        if (id == R.id.invite) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at (Referral code ="+referralCode+" ): \n" +
                            "https://play.google.com/store/apps/details?id=com.google.android.apps.plus");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment(getString(R.string.app_name));

        } else if (id == R.id.ankoins) {
            navItemIndex = 2;
            CURRENT_TAG = TAG_ANKOINS;
            loadHomeFragment(getString(R.string.app_ankoins));

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Invite.startScreen(context);

        } else if (id == R.id.about) {

          About.startScreen(context);
        }
        else{
            loadHomeFragment(getString(R.string.app_name));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void startScreen(Context context)
    {
        context.startActivity(new Intent(context, DashboardActivity.class));
    }

    @Override
    public void onFragmentInteraction(boolean loader ,ArrayList<Subcategory> carouseimages) {
        progressBar.setVisibility(loader==true ? View.VISIBLE :View.GONE);
        if(carouseimages!=null) setViewPagerAdapter(view_pager ,carouseimages);


    }

}
