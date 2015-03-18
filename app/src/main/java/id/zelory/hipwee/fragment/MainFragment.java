package id.zelory.hipwee.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.JazzyListView;

import java.util.ArrayList;

import id.zelory.hipwee.R;
import id.zelory.hipwee.activity.MainActivity;
import id.zelory.hipwee.activity.ReadActivity;
import id.zelory.hipwee.adapter.PostAdapter;
import id.zelory.hipwee.model.Post;
import id.zelory.hipwee.utils.Scraper;
import id.zelory.hipwee.utils.Utils;
import it.carlom.stikkyheader.core.StikkyHeaderBuilder;
import it.carlom.stikkyheader.core.StikkyHeaderListView;
import it.carlom.stikkyheader.core.animator.AnimatorBuilder;
import it.carlom.stikkyheader.core.animator.HeaderStikkyAnimator;

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener,
                                                      AdapterView.OnItemLongClickListener,
                                                      View.OnClickListener,
                                                      SwipeRefreshLayout.OnRefreshListener
{
    private String url;
    private JazzyListView listView;
    private ArrayList<Post> posts;
    private PostAdapter adapter;
    private ImageView imageView;
    private ImageView gambarTop;
    private TextView judulTop;
    private TextView next;
    private TextView prev;
    private SwipeRefreshLayout swipeRefreshLayout;

    public MainFragment()
    {

    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        imageView = (ImageView) view.findViewById(R.id.header_image);
        gambarTop = (ImageView) view.findViewById(R.id.gambarTop);
        judulTop = (TextView) view.findViewById(R.id.judulTop);
        next = (TextView) view.findViewById(R.id.next);
        prev = (TextView) view.findViewById(R.id.prev);
        prev.setText("<<");
        prev.setOnClickListener(this);
        next.setOnClickListener(this);

        listView = (JazzyListView) view.findViewById(R.id.list_item);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        listView.setTransitionEffect(JazzyHelper.CARDS);
        gambarTop.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        swipeRefreshLayout.setColorSchemeResources(R.color.primary, R.color.primary_dark, R.color.accent, R.color.icons);
        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        StikkyHeaderListView stikkyHeaderListView = (StikkyHeaderListView) StikkyHeaderBuilder.stickTo(listView).setHeader(R.id.header, (ViewGroup) getView())
                .minHeightHeaderRes(R.dimen.min_height_header).animator(new ParallaxStikkyAnimator())
                .build();

        stikkyHeaderListView.setSwipeRefreshLayout(swipeRefreshLayout);

        if (url == null)
            url = Post.TOP_30;

        if (url.equals(Post.TOP_30))
        {
            gambarTop.setImageResource(R.drawable.top30);
            judulTop.setText("Top 30 Hari Terakhir");
            prev.setVisibility(View.VISIBLE);
        }
        else if (url.equals(Post.TOP_ALL))
        {
            gambarTop.setImageResource(R.drawable.top_all);
            judulTop.setText("Top Sepanjang Waktu");
            next.setVisibility(View.GONE);
            prev.setVisibility(View.VISIBLE);
        }
        if (savedInstanceState != null)
        {
            posts = savedInstanceState.getParcelableArrayList(url);
        }
        if (posts == null)
        {
            new DownloadData().execute();
        }
        else
        {
            adapter = new PostAdapter(getActivity(), posts);
            listView.setAdapter(adapter);
            Picasso.with(getActivity()).load(((Post) adapter.getItem(Utils.randInt(0, 9))).getGambar()).placeholder(R.drawable.holder1).error(R.drawable.holder1).into(imageView);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(url, posts);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent intent = new Intent(getActivity(), ReadActivity.class);
        intent.putExtra("url", ((Post) (parent.getAdapter().getItem(position))).getAlamat());
        startActivity(intent);
    }

    @Override
    public void onClick(View v)
    {
        if (v.getId() == R.id.gambarTop)
        {
            MainActivity activity = (MainActivity) getActivity();
            switch (url)
            {
                case Post.TOP_7:
                    activity.toPage(1);
                    break;
                case Post.TOP_30:
                    activity.toPage(2);
                    break;
                default:
                    activity.toPage(0);
                    break;
            }
        }
        else if (v.getId() == R.id.prev)
        {
            MainActivity activity = (MainActivity) getActivity();
            switch (url)
            {
                case Post.TOP_7:
                    activity.toPage(0);
                    break;
                case Post.TOP_30:
                    activity.toPage(0);
                    break;
                default:
                    activity.toPage(1);
                    break;
            }
        }
        else if (v.getId() == R.id.next)
        {
            MainActivity activity = (MainActivity) getActivity();
            switch (url)
            {
                case Post.TOP_7:
                    activity.toPage(1);
                    break;
                case Post.TOP_30:
                    activity.toPage(2);
                    break;
                default:
                    activity.toPage(2);
                    break;
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
    {
        String judul = ((Post) (parent.getAdapter().getItem(position))).getJudul();
        Toast.makeText(getActivity(), judul, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onRefresh()
    {
        new DownloadData().execute();
    }

    private class ParallaxStikkyAnimator extends HeaderStikkyAnimator
    {
        @Override
        public AnimatorBuilder getAnimatorBuilder()
        {
            View mHeader_image = getHeader().findViewById(R.id.header_image);

            return AnimatorBuilder.create().applyVerticalParallax(mHeader_image);
        }
    }

    private class DownloadData extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            if (url.equals(Post.TOP_7) && !swipeRefreshLayout.isRefreshing())
            {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Memuat artikel...");
                progressDialog.setCancelable(true);
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            posts = null;
            while (posts == null)
            {
                posts = Scraper.scrapThis(url);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            adapter = new PostAdapter(getActivity(), posts);
            listView.setAdapter(adapter);

            Picasso.with(getActivity()).load(((Post) adapter.getItem(Utils.randInt(0, 9))).getGambar()).placeholder(R.drawable.holder1).error(R.drawable.holder1).into(imageView);
            if (progressDialog != null)
                progressDialog.dismiss();

            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
