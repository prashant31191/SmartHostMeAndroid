package com.smarthost;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.smarthost.ui.adapters.DrawerListAdapter;
import com.smarthost.util.DrawerUtils;
import com.smarthost.util.Navigation;
import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TabPageIndicator;

/**
 * Base class for top-level Activities that sets up a Navigation Drawer,
 * ViewPager and TabPageIndicator.
 *
 * @author paulburke (ipaulpro)
 */
public class BaseActivity extends BaseTrackerActivity {


    protected DrawerLayout mDrawerLayout;
    protected ActionBarDrawerToggle mDrawerToggle;
    protected ListView mDrawerList;
    protected ViewGroup mDrawerView;
    protected View mDrawerBtnSettings;

    private ViewPager mViewPager;
    private TabPageIndicator mTabsView;
    private PagerAdapter mPagerAdapter;

    private Handler mHandler;
    private Runnable mPendingLaunch;

    private int[] mMenuItemIds;

    public interface OnVerticalSwipeListener {
        public static final int SCROLL_DOWN = 1;
        public static final int SCROLL_UP = -1;

        public void onSwipe(boolean isSwipeUp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
    }

    /**
     * Setup the Drawer, ActionBar, and ViewPager
     *
     * @param title The Activity title.
     * @param pagerAdapter The adapter to use for the ViewPager.
     */
    protected void initViews(String title, PagerAdapter pagerAdapter) {
        setContentView(R.layout.activity_base);
        setTitle(title);

        initDrawer();
        initActionBar();
        initPagerAndTabs(pagerAdapter);
    }

    public static final String FRAGMENT_TAG = "content";

    protected void initViews(String title, int icon, Fragment fragment) {
        setContentView(R.layout.activity_base_secondary);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, fragment, FRAGMENT_TAG).commit();
        setTitle(title);
        initDrawer();
        initActionBar();
    }


    protected void setTitle(String title){
        super.setTitle(title);
        getActionBar().setTitle(title);
    }

    private void initPagerAndTabs(PagerAdapter pagerAdapter) {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = pagerAdapter;
        mViewPager.setAdapter(mPagerAdapter);

        mTabsView = (TabPageIndicator) findViewById(R.id.tabs);
        mTabsView.setViewPager(mViewPager);
    }

    public void initActionBar(String title) {
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        setTitle(title);
        refreshActionBar();
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_HOME
                | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setIcon(R.drawable.ic_launcher);

        refreshActionBar();
    }

    private void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_container);
        mDrawerView = (ViewGroup) findViewById(R.id.drawer_view);

        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerList.setAdapter(new DrawerListAdapter(this));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = createDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerBtnSettings = findViewById(R.id.drawer_settings);

        refreshDrawer();
    }

    private ActionBarDrawerToggle createDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                refreshActionBar();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Check if there's a launch pending. Prevents a janky Drawer.
                if (mPendingLaunch != null) {
                    mHandler.post(mPendingLaunch);
                    mPendingLaunch = null;
                } else {
                    refreshActionBar();
                }
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);


                float delta = 1 - slideOffset;
                float translationX = DrawerUtils.PARALAX_OFFSET * delta;
                mDrawerList.setTranslationX(translationX);
                mDrawerBtnSettings.setTranslationX(translationX);

                setActionBarItemsAlpha(delta);
            }

        };
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        refreshActionBar();

        int size = menu.size();
        mMenuItemIds = new int[size];
        for (int i = 0; i < size; i++) {
            mMenuItemIds[i] = menu.getItem(i).getItemId();
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    protected PageIndicator getTabsView() {
        return mTabsView;
    }

    protected ViewPager getViewPager() {
        return mViewPager;
    }

    protected PagerAdapter getPagerAdapter() {
        return mPagerAdapter;
    }

    protected DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    protected ViewGroup getDrawerView() {
        return mDrawerView;
    }

    protected boolean isDrawerOpen() {
        return mDrawerLayout.isDrawerOpen(mDrawerView);
    }

    protected void openDrawer() {
        mDrawerLayout.openDrawer(mDrawerView);
        // This can happen before the listener has a chance to catch it
        setActionBarItemsAlpha(0);
    }

    /**
     * Update the title and invalidate the options menu
     */
    protected void refreshActionBar() {
        if (isDrawerOpen()) {
            setActionBarItemsAlpha(0);
        } else {
            getActionBar().setTitle(getTitle());
        }
    }

    /**
     * Update the selected item.
     */
    protected void refreshDrawer() {
        mDrawerList.setItemChecked(Navigation.getId(this), true);
    }


    protected void onDrawerItemClick(int which) {
        // Override if subclass needs to listen for Drawer click events
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectDrawerItem(position);
        }
    }


    protected void selectDrawerItem(final int which) {
        if (which == Navigation.getId(this))
            return;

        onDrawerItemClick(which);
        mPendingLaunch = DrawerUtils.buildLaunchRunnable(this, which);

        findViewById(R.id.content).animate().alpha(0f);

        mDrawerLayout.closeDrawer(mDrawerView);
    }

    /**
     * Called from XML
     *
     * @param target The view clicked.
     */
    public void onSettingsClick(View target) {

//        Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();

        Intent intent = SettingsActivity.getLaunchIntent(BaseActivity.this);
        startActivity(intent);
        mDrawerLayout.closeDrawer(mDrawerView);
    }

    protected void setActionBarItemsAlpha(float delta) {
        if (mPendingLaunch == null) {
/*
            this makes the title fade and turn into "heavy" when the drawer is opened
            but it's not going to fly here because not all activities use the title view
            TextView title = (TextView) findViewById(titleId);
            if (title != null)
            {
                String heavy = getString(R.string.app_name);
                title.setAlpha(delta);

                if (delta <= 1 && delta > .5f){
                    float alpha = (delta - .5f)*2;
                    title.setAlpha(alpha);

                    if(!title.getText().equals(getTitle()))
                        title.setText(getTitle());

                }  else if (delta < .5f){
                    float alpha = 1 - ((delta)*2);
                    title.setAlpha(alpha);

                    if(!title.getText().equals(heavy))
                        title.setText(heavy);
                }

            }
*/

            // This isn't pretty, but it's oh, so pretty
//            final int titleId =
//                    Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
//            View title = findViewById(titleId);
//            if (title != null  && getActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD)
//                title.setAlpha(delta);
            if (mMenuItemIds != null) {
                for (int id : mMenuItemIds) {
                    View v = findViewById(id);
                    if (v != null) {
                        v.setAlpha(delta);
                        if (delta == 1)
                            v.setEnabled(true);
                        else if (delta == 0)
                            v.setEnabled(false);
                    }
                }
            }
        }
    }

}
