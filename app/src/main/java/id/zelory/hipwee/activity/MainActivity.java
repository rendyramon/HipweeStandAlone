package id.zelory.hipwee.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import id.zelory.hipwee.R;
import id.zelory.hipwee.adapter.MainViewPagerAdapter;
import id.zelory.hipwee.adapter.MenuAdapter;
import id.zelory.hipwee.model.Post;
import id.zelory.hipwee.service.NotificationService;
import id.zelory.hipwee.utils.Utils;
import it.sephiroth.android.library.floatingmenu.FloatingActionItem;
import it.sephiroth.android.library.floatingmenu.FloatingActionMenu;

public class MainActivity extends ActionBarActivity implements SearchView.OnQueryTextListener
{
    private ViewPager viewPager;
    private SearchView searchView;
    private DialogPlus dialogPlus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!Utils.isMyServiceRunning(NotificationService.class, this))
        {
            startService(new Intent(this, NotificationService.class));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        MainViewPagerAdapter adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        checkIntent();

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

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        showAd();
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
                final InterstitialAd interstitialAd = new InterstitialAd(MainActivity.this);
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
                            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                            intent.putExtra("url", url);
                            startActivity(intent);
                        }
                        dialogPlus.dismiss();
                    }
                })
                .create();
        dialogPlus.show();

    }

    private void checkIntent()
    {
        if (getIntent().getData() != null)
        {
            Uri data = getIntent().getData();
            String scheme = data.getScheme();
            String fullPath = data.getEncodedSchemeSpecificPart();

            String url = scheme + ":" + fullPath;
            Log.d("zet", url);
            boolean isKategori = false;
            if (url.contains(Post.URL_HIBURAN))
            {
                url = Post.HIBURAN;
                isKategori = true;

            }
            else if (url.contains(Post.URL_TIPS))
            {
                url = Post.TIPS;
                isKategori = true;

            }
            else if (url.contains(Post.URL_HUBUNGAN))
            {
                url = Post.HUBUNGAN;
                isKategori = true;

            }
            else if (url.contains(Post.URL_MOTIVASI))
            {
                url = Post.MOTIVASI;
                isKategori = true;

            }
            else if (url.contains(Post.URL_SUKSES))
            {
                url = Post.SUKSES;
                isKategori = true;

            }
            else if (url.contains(Post.URL_TRAVEL))
            {
                url = Post.TRAVEL;
                isKategori = true;

            }
            else if (url.contains(Post.URL_FEATURE))
            {
                url = Post.FEATURE;
                isKategori = true;

            }
            else if (url.contains(Post.URL_STYLE))
            {
                url = Post.STYLE;
                isKategori = true;

            }
            else if (url.contains(Post.SEARCH))
            {
                isKategori = true;
            }

            if (isKategori)
            {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
            else
            {
                if (url.matches(Post.ARTIKEL_HIBURAN + ".*") ||
                        url.matches(Post.ARTIKEL_TIPS + ".*") ||
                        url.matches(Post.ARTIKEL_HUBUNGAN + ".*") ||
                        url.matches(Post.ARTIKEL_MOTIVASI + ".*") ||
                        url.matches(Post.ARTIKEL_SUKSES + ".*") ||
                        url.matches(Post.ARTIKEL_TRAVEL + ".*") ||
                        url.matches(Post.ARTIKEL_FEATURE + ".*") ||
                        url.matches(Post.ARTIKEL_STYLE + ".*"))
                {
                    Intent intent = new Intent(MainActivity.this, ReadActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void toPage(int index)
    {
        viewPager.setCurrentItem(index);
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
}
