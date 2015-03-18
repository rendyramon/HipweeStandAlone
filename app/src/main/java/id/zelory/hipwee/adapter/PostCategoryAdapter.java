package id.zelory.hipwee.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import id.zelory.hipwee.R;
import id.zelory.hipwee.model.Post;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by zetbaitsu on 21/02/2015.
 */
public class PostCategoryAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Post> posts;
    
    public PostCategoryAdapter(Context context, ArrayList<Post> posts)
    {
        this.context = context;
        this.posts = posts;
    }
    
    @Override
    public int getCount()
    {
        return posts.size();
    }

    @Override
    public Object getItem(int position)
    {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        Post post = posts.get(position);
        ImgHolder imgHolder = randHolder();
        String kategori = "";
        try
        {
            kategori = post.getAlamat().substring(22);
            int x = kategori.indexOf("/");
            kategori = kategori.substring(0, x);
        }
        catch (Exception e)
        {
            kategori = "gagal";
        }

        int color;
        int bg;
        int bgSmall;
        switch (kategori)
        {
            case "hiburan":
                color = R.color.hiburan;
                bg = R.drawable.bghiburan;
                bgSmall = R.color.bghiburan;
                break;
            case "tips":
                color = R.color.tips;
                bg = R.drawable.bgtips;
                bgSmall = R.color.bgtips;
                break;
            case "hubungan":
                color = R.color.hubungan;
                bg = R.drawable.bghubungan;
                bgSmall = R.color.bghubungan;
                break;
            case "motivasi":
                color = R.color.motivasi;
                bg = R.drawable.bgmotivasi;
                bgSmall = R.color.bgmotivasi;
                break;
            case "travel":
                color = R.color.travel;
                bg = R.drawable.bgtravel;
                bgSmall = R.color.bgtravel;
                break;
            case "sukses":
                color = R.color.sukses;
                bg = R.drawable.bgsukses;
                bgSmall = R.color.bgsukses;
                break;
            case "feature":
                color = R.color.feature;
                bg = R.drawable.bgfeature;
                bgSmall = R.color.bgfeature;
                break;
            case "style":
                color = R.color.style;
                bg = R.drawable.bgstyle;
                bgSmall = R.color.bgstyle;
                break;
            default:
                color = R.color.primary;
                bg = R.color.primary_dark;
                bgSmall = bg;
                break;
        }
        
        if (post.getDeskripsi().equals(""))
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_big, parent, false);
            
            holder = new ViewHolder();
            holder.gambar = (ImageView) convertView.findViewById(R.id.gambar);
            holder.judul = (TextView) convertView.findViewById(R.id.judul);
            holder.kategori = (TextView) convertView.findViewById(R.id.kategori);
            holder.background = (LinearLayout) convertView.findViewById(R.id.background);
            
            Picasso.with(context).load(post.getGambar()).placeholder(imgHolder.holder).error(imgHolder.holder).into(holder.gambar);
            holder.judul.setText(Html.fromHtml(post.getJudul()));
            holder.kategori.setText(kategori.toUpperCase());
            
            holder.background.setBackgroundResource(color);
            holder.kategori.setBackgroundResource(bg);

            if (kategori.equals("gagal"))
            {
                holder.judul.setVisibility(View.GONE);
                holder.kategori.setVisibility(View.GONE);
                holder.background.setVisibility(View.GONE);
            }
        }
        else
        {
            if (convertView == null || convertView.getTag() == null)
            {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_small, parent, false);

                holder = new ViewHolder();
                holder.gambar = (ImageView) convertView.findViewById(R.id.gambar);
                holder.judul = (TextView) convertView.findViewById(R.id.judul);
                holder.deskripsi = (TextView) convertView.findViewById(R.id.deskripsi);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) convertView.getTag();
            }
            Picasso.with(context).load(post.getGambar()).transform(new CropCircleTransformation()).placeholder(imgHolder.bulet).error(imgHolder.bulet).into(holder.gambar);
            holder.judul.setText(Html.fromHtml(post.getJudul()));
            holder.judul.setBackgroundResource(bgSmall);
            holder.deskripsi.setText(Html.fromHtml(post.getDeskripsi()));
            holder.deskripsi.setBackgroundResource(bgSmall);
        }
        
        
        
        return convertView;
    }
    
    private class ViewHolder
    {
        ImageView gambar;
        TextView judul, kategori, deskripsi;
        LinearLayout background;
    }

    private ImgHolder randHolder()
    {
        Random rand = new Random();
        int ran = rand.nextInt(2) + 1;
        ImgHolder imgHolder = new ImgHolder();

        if (ran == 1)
        {
            imgHolder.holder = R.drawable.holder2;
            imgHolder.bulet = R.drawable.sasa;
        }
        else
        {
            imgHolder.holder = R.drawable.holder1;
            ran = rand.nextInt(2) + 1;
            if (ran == 1)
                imgHolder.bulet = R.drawable.sasi;
            else
                imgHolder.bulet = R.drawable.sasu;
        }

        return imgHolder;
    }

    private class ImgHolder
    {
        int holder;
        int bulet;
    }
}
