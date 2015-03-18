package id.zelory.hipwee.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;

import java.util.ArrayList;

import id.zelory.hipwee.R;
import id.zelory.hipwee.adapter.MenuAdapter;
import id.zelory.hipwee.adapter.PostCategoryAdapter;
import id.zelory.hipwee.model.Post;
import id.zelory.hipwee.utils.Scraper;
import id.zelory.hipwee.utils.Utils;
import it.sephiroth.android.library.floatingmenu.FloatingActionItem;
import it.sephiroth.android.library.floatingmenu.FloatingActionMenu;

public class CategoryActivity extends ActionBarActivity implements AdapterView.OnItemClickListener,
                                                                   SearchView.OnQueryTextListener,
                                                                   SwipeRefreshLayout.OnRefreshListener
{
    private String url;
    private ArrayList<Post> posts;
    private JazzyListView jazzyListView;
    private DialogPlus dialogPlus;
    private SearchView searchView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        jazzyListView = (JazzyListView) findViewById(R.id.list_item);
        jazzyListView.setOnItemClickListener(this);
        jazzyListView.setTransitionEffect(JazzyHelper.CARDS);

        url = getIntent().getStringExtra("url");
        if (url.contains(Post.SEARCH))
            keyword = url.substring(25);
        url = url.replaceAll(" ", "%20");

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primary_dark, R.color.accent, R.color.icons);
        swipeRefreshLayout.setOnRefreshListener(this);

        ImageView logo = (ImageView) findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        new DownloadData().execute();

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                initMenu();
            }
        }, 200);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (Utils.randInt(1, 10) == 5)
        {
            showAd();
        }
    }

    private void initMenu()
    {
        int action_item_padding = getResources().getDimensionPixelSize(R.dimen.float_action_item_padding);

        FloatingActionItem item1 = new FloatingActionItem.Builder(0)
                .withResId(R.drawable.tombol)
                .withDelay(50)
                .withPadding(action_item_padding)
                .build();

        FloatingActionMenu.Builder builder = new FloatingActionMenu
                .Builder(this)
                .withThreshold(R.dimen.float_action_threshold)
                .withGap(R.dimen.float_action_item_gap)
                .withHorizontalPadding(R.dimen.float_action_h_padding)
                .withVerticalPadding(R.dimen.float_action_v_padding)
                .withGravity(FloatingActionMenu.Gravity.RIGHT | FloatingActionMenu.Gravity.BOTTOM)
                .withDirection(FloatingActionMenu.Direction.Horizontal)
                .animationDuration(200)
                .animationInterpolator(new DecelerateInterpolator())
                .addItem(item1)
                .visible(false);

        FloatingActionMenu actionMenu = builder.build();
        actionMenu.show(true, true);

        actionMenu.setOnItemClickListener(new FloatingActionMenu.OnItemClickListener()
        {
            @Override
            public void onItemClick(FloatingActionMenu menu, int id)
            {
                if (dialogPlus != null && dialogPlus.isShowing())
                    dialogPlus.dismiss();
                else
                    showMenu();
            }
        });
    }

    private void showMenu()
    {
        dialogPlus = new DialogPlus.Builder(this)
                .setContentHolder(new GridHolder(3))
                .setHeader(R.layout.header)
                .setFooter(R.layout.footer)
                .setCancelable(true)
                .setGravity(DialogPlus.Gravity.BOTTOM)
                .setAdapter(new MenuAdapter(this))
                .setOnItemClickListener(new OnItemClickListener()
                {
                    @Override
                    public void onItemClick(DialogPlus dialogPlus, Object o, View view, int i)
                    {
                        String url;
                        switch (i)
                        {
                            case 0:
                                url = Post.HIBURAN;
                                break;
                            case 1:
                                url = Post.HIBURAN;
                                break;
                            case 2:
                                url = Post.TIPS;
                                break;
                            case 3:
                                url = Post.HUBUNGAN;
                                break;
                            case 4:
                                url = Post.MOTIVASI;
                                break;
                            case 5:
                                url = Post.SUKSES;
                                break;
                            case 6:
                                url = Post.TRAVEL;
                                break;
                            case 7:
                                url = Post.FEATURE;
                                break;
                            default:
                                url = Post.STYLE;
                                break;
                        }

                        if (i > 0)
                        {
                            Intent intent = new Intent(CategoryActivity.this, CategoryActivity.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        dialogPlus.dismiss();
                    }
                })
                .create();
        dialogPlus.show();

    }

    private void showAd()
    {
        final InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.video_ad_unit_id));
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        mInterstitialAd.loadAd(adRequestBuilder.build());
        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {
                super.onAdLoaded();
                mInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode)
            {
                super.onAdFailedToLoad(errorCode);
                final InterstitialAd interstitialAd = new InterstitialAd(CategoryActivity.this);
                interstitialAd.setAdUnitId(getString(R.string.loading_ad_unit_id));
                AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
                interstitialAd.loadAd(adRequestBuilder.build());
                interstitialAd.setAdListener(new AdListener()
                {
                    @Override
                    public void onAdLoaded()
                    {
                        super.onAdLoaded();
                        interstitialAd.show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_category, menu);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Masukan kata kunci");
        searchView.setPadding(0, 0, 0, 10);
        ImageView v = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        v.setImageResource(R.drawable.ic_action_search);
        v = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        v.setImageResource(R.drawable.ic_action_close);
        TextView textView = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textView.setTextColor(getResources().getColor(R.color.secondary_text));
        textView.setHintTextColor(getResources().getColor(R.color.secondary_text));
        searchView.setOnQueryTextListener(this);
        if (keyword != null)
        {
            searchView.setIconified(false);
            searchView.clearFocus();
            searchView.setQuery(keyword, false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            AlertDialog dialog = new AlertDialog.Builder(this).setPositiveButton("CLOSE", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {

                }
            }).setTitle("About").setIcon(R.mipmap.ic_launcher).create();
            dialog.setMessage("hipwee.com v1.0.0 - 2015.\nDeveloped by Zelory.");
            dialog.show();

        }
        else if (id == R.id.search)
        {
            searchView.onActionViewExpanded();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        String url = ((Post) (parent.getAdapter().getItem(position))).getAlamat();
        if (url.equals(""))
        {
            Toast.makeText(this, "Pencarian Gagal", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(this, ReadActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String s)
    {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra("url", Post.SEARCH + s);
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s)
    {
        return false;
    }

    @Override
    public void onRefresh()
    {
        new DownloadData().execute();
    }

    private class DownloadData extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            if (!swipeRefreshLayout.isRefreshing())
            {
                progressDialog = new ProgressDialog(CategoryActivity.this);

                if (keyword != null)
                    progressDialog.setMessage("Mencari artikel...");
                else
                    progressDialog.setMessage("Memuat artikel...");

                progressDialog.setCancelable(true);
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            posts = null;

            if (keyword == null)
                while (posts == null)
                {
                    posts = Scraper.scrapThis(url);
                }
            else
                while (posts == null)
                {
                    posts = Scraper.search(keyword);
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            PostCategoryAdapter adapter = new PostCategoryAdapter(CategoryActivity.this, posts);
            jazzyListView.setAdapter(adapter);

            if (progressDialog != null)
                progressDialog.dismiss();

            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
