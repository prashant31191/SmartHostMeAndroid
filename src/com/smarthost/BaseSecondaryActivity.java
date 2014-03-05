
package com.smarthost;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.SimpleDrawerListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.smarthost.ui.adapters.DrawerListAdapter;
import com.smarthost.util.DrawerUtils;
import com.smarthost.util.Navigation;

/**
 * Convenience class with helper methods for setting the ActionBar title and
 * icon. Also provides access to the Navigation Drawer.
 * <p>
 * It also manages the handling of the "up" navigation, recreating the parent
 * MainActivity task if necessary.
 * </p>
 * <p>
 * Inherited classes should call {@link #initActionBar(String, int)} or
 * {@link #initActionBar(String, int, android.content.Intent)} and
 * {@link #initDrawer()} if the Navigation Drawer is present.
 * </p>
 */
public class BaseSecondaryActivity extends BaseTrackerActivity {

    public static final String FRAGMENT_TAG = "content";

    private Intent mUpIntent;

    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerView;
    private ListView mDrawerList;
    private View mDrawerBtnSettings;

    private Handler mHandler;
    private Runnable mPendingLaunch;

    private int mIconRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
    }

    protected void initViews(String title, int icon) {
        setContentView(R.layout.activity_base_secondary);
        initActionBar(title, icon);
        initDrawer();
    }

    protected Fragment getFragment() {
        return getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    protected void addFragment(Fragment fragment) {
        if (getFragment() == null)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, fragment, FRAGMENT_TAG).commit();
    }

    protected void initViews(String title, int icon, Fragment fragment) {
        initViews(title, icon);
        addFragment(fragment);
    }

    /**
     * Setup the Action Bar with custom title.
     *
     * @param title The title String.
     * @param iconRes The Drawable resource containing the icon.
     */
    protected void initActionBar(String title, int iconRes) {
        setTitle(title);

        final ActionBar actionBar = getActionBar();

        if (iconRes > 0) {
            mIconRes = iconRes;
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_HOME_AS_UP
                    | ActionBar.DISPLAY_SHOW_HOME);
            actionBar.setIcon(iconRes);
        } else {
            mIconRes = R.drawable.ic_launcher;
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE
                    | ActionBar.DISPLAY_HOME_AS_UP);
        }
    }

    /**
     * Setup the Action Bar with a title but no icon.
     *
     * @param title The title String.
     */
    protected void initActionBar(String title) {
        initActionBar(title, 0);
    }

    /**
     * Setup the Action Bar with custom title and intent to be used as "up".
     * This will not use the default icon.
     *
     * @param title The title String.
     * @param iconRes The Drawable resource containing the icon.
     * @param upIntent The Intent used for Up navigation.
     */
    protected void initActionBar(String title, int iconRes, Intent upIntent) {
        mUpIntent = upIntent;
        initActionBar(title, iconRes);
    }

    protected void initDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_container);
        if (mDrawerLayout == null)
            throw new IllegalStateException(
                    "Unable to find Navigation Drawer with ID drawer_container");

        mDrawerView = (RelativeLayout) findViewById(R.id.drawer_view);

        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerList.setAdapter(new DrawerListAdapter(this));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerLayout.setDrawerListener(new DrawerListener());

        mDrawerBtnSettings = findViewById(R.id.drawer_settings);

        refreshActionBar();
        refreshDrawer();
    }

    protected void disableDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerView);
    }

    /**
     * Update the selected item.
     */
    protected void refreshDrawer() {
        mDrawerList.setItemChecked(Navigation.getId(this), true);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mDrawerLayout != null) {
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerView);
            menu.setGroupVisible(0, !drawerOpen);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                if (!getSupportFragmentManager().popBackStackImmediate()) {
                    if (mUpIntent == null) {
                        // TODO Determine the proper top-level Activity
                        mUpIntent = new Intent(this, MyActivity.class);
                    }

                    handleUp();
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {

        getActionBar().setTitle(title);

        super.setTitle(title);
    }

    /**
     * Called when a navigation item is selected from the Drawer.
     *
     * @param which One of {@link #NAV_TRACK_NOW}, {@link #NAV_SCHEDULE},
     *            {@link #NAV_WORKOUTS} or {@link #NAV_PROGRESS}
     */
    protected void onDrawerItemClick(final int which) {

        Toast.makeText(this, "Coming Soon!", Toast.LENGTH_SHORT).show();
        closeDrawer();
        return;

//        mPendingLaunch = DrawerUtils.buildLaunchRunnable(this, which);
//        mDrawerList.setItemChecked(which, true);
//        closeDrawer();
    }

    /**
     * Called from XML
     *
     * @param target The view clicked.
     */
    public void onSettingsClick(View target) {
        closeDrawer();
        Toast.makeText(this, "Comings Soon", Toast.LENGTH_SHORT).show();
//        // Use a pending runnable to avoid janky drawer animations.
//        mPendingLaunch = new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = SettingsActivity.getLaunchIntent(BaseSecondaryActivity.this);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//            }
//        };
    }

    protected void closeDrawer() {
        mDrawerLayout.closeDrawer(mDrawerView);
    }

    /**
     * Set the {@link Intent} to be used for navigating "up".
     *
     * @param intent The Intent of the parent Activity.
     */
    protected void setUpIntent(Intent intent) {
        mUpIntent = intent;
    }

    /**
     * Update the title and invalidate the options menu
     */
    protected void refreshActionBar() {
        boolean isDrawerOpen = mDrawerLayout != null
                && mDrawerLayout.isDrawerOpen(mDrawerView);
//        getActionBar().setTitle(isDrawerOpen ? getString(R.string.app_name) : getTitle());
        getActionBar().setIcon(isDrawerOpen ? R.drawable.ic_launcher : mIconRes);
        invalidateOptionsMenu();
    }

    protected boolean isDrawerOpen() {
        return mDrawerLayout != null
                && mDrawerLayout.isDrawerOpen(mDrawerView);
    }


    /**
     * Navigate up, recreating the Intent if necessary.
     */
    private void handleUp() {
        if (NavUtils.shouldUpRecreateTask(this, mUpIntent)) {

            // This activity is not part of the application's task, so
            // create a new task with a synthesized back stack.
            TaskStackBuilder.create(this).addNextIntent(mUpIntent)
                    .startActivities();

            finish();

        } else {

            // This activity is part of the application's task, so simply
            // navigate up to the hierarchical parent activity.
            //
            // Note: The MainActivity launch mode should be set to singleTop
            // or singleInstance to prevent reloading.
            NavUtils.navigateUpTo(this, mUpIntent);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onDrawerItemClick(position);
        }
    }

    private class DrawerListener extends SimpleDrawerListener {

        @Override
        public void onDrawerOpened(View arg0) {
            refreshActionBar();
        }

        @Override
        public void onDrawerClosed(View arg0) {
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
            float translationX = DrawerUtils.PARALAX_OFFSET * (1 - slideOffset);
            mDrawerList.setTranslationX(translationX);
            mDrawerBtnSettings.setTranslationX(translationX);
            notifyDrawerSlide(slideOffset);
        }
    }

    public void notifyDrawerSlide(float slideOffset){

    }

}
