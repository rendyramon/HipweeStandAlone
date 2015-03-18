package id.zelory.hipwee.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import id.zelory.hipwee.R;
import id.zelory.hipwee.model.Post;
import id.zelory.hipwee.model.PostDetail;
import id.zelory.hipwee.utils.Scraper;

public class ReadActivity extends ActionBarActivity
{
    private static final String TEXT_HTML = "text/html";
    private static final String BACKGROUND_COLOR = "#FFF";
    private static final String TEXT_COLOR = "#212121";
    private static final String BUTTON_COLOR = "#52A7DF";
    private static final String SUBTITLE_COLOR = "#727272";
    private static final String SUBTITLE_BORDER_COLOR = "solid #B6B6B6";
    private static final String CSS = "<head><link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'><style type='text/css'> "
            + "body {max-width: 100%; margin: 1.2em 0.3cm 0.3cm 0.2cm; font-family: 'Roboto', sans-serif; color: " + TEXT_COLOR + "; background-color:" + BACKGROUND_COLOR + "; line-height: 140%} "
            + "* {max-width: 100%; word-break: break-word}"
            + "h1 {font-weight: normal; line-height: 130%; text-align: center} "
            + "h2 {font-weight: bold; line-height: 130%; text-align: left} "
            + "h1 {font-size: 170%; margin-bottom: 0.1em;} "
            + "h2 {font-size: 150%} "
            + "a {color: #0099CC}"
            + "h1 a {color: inherit; text-decoration: none}"
            + "img {height: auto; display: block; margin-left: auto; margin-right: auto}"
            + "pre {white-space: pre-wrap;} "
            + "blockquote {margin: 0.8em 0 0.8em 1.2em; padding: 0} "
            + "p {margin: 0.8em 0 0.8em 0; text-align:justify} "
            + "p.subtitle {text-align: center; color: " + SUBTITLE_COLOR + "; border-top:1px " + SUBTITLE_BORDER_COLOR + "; border-bottom:1px " + SUBTITLE_BORDER_COLOR + "; padding-top:2px; padding-bottom:2px; font-weight:800 } "
            + "p.wp-caption-text{font-style: italic; text-align: left}"
            + "ul, ol {margin: 0 0 0.8em 0.6em; padding: 0 0 0 1em} "
            + "ul li, ol li {margin: 0 0 0.8em 0; padding: 0} "
            + "div.button-section {padding: 0.4cm 0; margin: 0; text-align: center} "
            + ".button-section p {margin: 0.1cm 0 0.2cm 0}"
            + ".button-section p.marginfix {margin: 0.5cm 0 0.5cm 0}"
            + "section {padding: 0px 0px;}"
            + "section > * { margin: 10px }"
            + ".card { position: relative; display: inline-block; vertical-align: top; background-color: #fff; box-shadow: 0 12px 15px 0 rgba(0, 0, 0, 0.24);}"
            + "li.item p {font: 200 12px/1.5 Georgia, Times New Roman, serif;}"
            + "li.item {padding: 0px; overflow: auto;}"
            + ".button-section input, .button-section a {font-family: sans-serif-light; font-size: 100%; color: #FFFFFF; background-color: " + BUTTON_COLOR + "; text-decoration: none; border: none; border-radius:0.2cm; padding: 0.3cm} "
            + "</style><meta name='viewport' content='width=device-width'/>"
            + "<link rel=\"import\" href=\"http://www.polymer-project.org/components/paper-ripple/paper-ripple.html\">"
            + " <script type=\"text/javascript\"> function loadUrl(url) { AndroidFunction.loadUrl(url); }</script></head>";
    private static final String BODY_START = "<body>";
    private static final String BODY_END = "</body>";
    private static final String TITLE_START = "<h1>";
    private static final String TITLE_MIDDLE = "";
    private static final String TITLE_END = "</h1>";
    private static final String SUBTITLE_START = "<p class='subtitle'>";
    private static final String SUBTITLE_END = "</p>";

    private WebView isi;
    private String url;
    private PostDetail postDetail;
    private ArrayList<Post> randPost;

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        isi = (WebView) findViewById(R.id.isi);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        onNewIntent(getIntent());
        url = getIntent().getStringExtra("url");
        isi.addJavascriptInterface(new JavaScriptInterface(this), "AndroidFunction");
        isi.getSettings().setJavaScriptEnabled(true);
        isi.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        logo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ReadActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }


    @Override
    protected void onNewIntent(final Intent intent)
    {
        Bundle extras = intent.getExtras();
        if(extras != null){
            if(extras.containsKey("url"))
            {
                url = extras.getString("url");
                new ReadPost().execute();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_read, menu);
        ImageView v = (ImageView) menu.findItem(R.id.share).getActionView();
        v.setPadding(0, 0, 0, 10);
        v.setImageResource(R.drawable.ic_action_share);
        v.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, Html.fromHtml(postDetail.getJudul()));
                intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(postDetail.getJudul()) + "\n" + url);
                startActivity(Intent.createChooser(intent, "Bagikan"));
            }
        });
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
        else if (id == R.id.share)
        {
            Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.action_refresh)
        {
            new ReadPost().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    public class JavaScriptInterface
    {
        Context mContext;

        JavaScriptInterface(Context c)
        {
            mContext = c;
        }

        @JavascriptInterface
        public void loadUrl(String url)
        {
            Intent intent = new Intent(ReadActivity.this, ReadActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }

    private class ReadPost extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ReadActivity.this);
            progressDialog.setMessage("Membuka artikel...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            postDetail = Scraper.read(url);
            randPost = Scraper.random();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            if (postDetail != null)
            {
                isi.loadData(generateHtml(postDetail.getJudul(), postDetail.getPenulis(), postDetail.getTanggal(), postDetail.getIsi(), randPost), TEXT_HTML, "UTF-8");
            }
            else
            {
                isi.loadData("Gagal membuka artikel!", TEXT_HTML, "UTF-8");
            }

            progressDialog.dismiss();
        }

        private String generateHtml(String judul, String penulis, String tanggal, String isi, ArrayList<Post> randPost)
        {

            return CSS + BODY_START + TITLE_START + "" + TITLE_MIDDLE + judul + TITLE_END + SUBTITLE_START + penulis + " - " + tanggal + SUBTITLE_END + "<div>" + isi + "</div>" + SUBTITLE_START + "<div><h2>Artikel Lainnya</h2>" + generateRandom(randPost) + "</div>" + SUBTITLE_START + "<center><b>&copy; Hipwee.com</b></center>" + SUBTITLE_END + BODY_END;
        }

        private String generateRandom(ArrayList<Post> posts)
        {
            String head = "<section>";
            String headContent = "<paper-ripple class=\"recenteringTouch\" fit><img src=\"";
            String mid = "\"/></paper-ripple><div class=\"content\"><div style=\"padding:12px; color:" + SUBTITLE_COLOR + "\">";
            String footContent = "</div></div></div>";
            String foot = "</section>";
            String randPost = head;

            for (int i = 0; i < posts.size(); i++)
            {
                randPost += "<div class=\"card\" onClick=\"loadUrl('" + posts.get(i).getAlamat() + "')\">";
                randPost += headContent;
                randPost += posts.get(i).getGambar();
                randPost += mid;
                randPost += posts.get(i).getJudul();
                randPost += footContent;
            }

            randPost += foot;

            return randPost;
        }
    }
}
