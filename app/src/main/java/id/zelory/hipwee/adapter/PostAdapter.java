package id.zelory.hipwee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import id.zelory.hipwee.R;
import id.zelory.hipwee.model.Post;
import jp.wasabeef.picasso.transformations.ColorFilterTransformation;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;

/**
 * Created by zetbaitsu on 16/02/2015.
 */
public class PostAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Post> posts;

    public PostAdapter(Context context, ArrayList<Post> posts)
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
        final ViewHolder holder;
        ImgHolder imgHolder = randHolder();
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_main, parent, false);
            holder = new ViewHolder();
            holder.judul = (TextView) convertView.findViewById(R.id.judul);
            holder.gambar = (ImageView) convertView.findViewById(R.id.gambar);
            holder.bulet = (ImageView) convertView.findViewById(R.id.bulet);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        Post post = posts.get(position);

        String kategori = post.getAlamat().substring(22);
        int x = kategori.indexOf("/");
        kategori = kategori.substring(0, x);

        holder.judul.setText(Html.fromHtml(post.getJudul()));

        switch (kategori)
        {
            case "hiburan":
                Picasso.with(context).load(post.getGambar()).transform(new GrayscaleTransformation()).transform(new ColorFilterTransformation(Color.argb(150, 159, 56, 58))).placeholder(imgHolder.holder).error(imgHolder.holder).into(holder.gambar);
                break;
            case "tips":
                Picasso.with(context).load(post.getGambar()).transform(new GrayscaleTransformation()).transform(new ColorFilterTransformation(Color.argb(150, 255, 204, 0))).placeholder(imgHolder.holder).error(imgHolder.holder).into(holder.gambar);
                break;
            case "hubungan":
                Picasso.with(context).load(post.getGambar()).transform(new GrayscaleTransformation()).transform(new ColorFilterTransformation(Color.argb(150, 0, 182, 157))).placeholder(imgHolder.holder).error(imgHolder.holder).into(holder.gambar);
                break;
            case "motivasi":
                Picasso.with(context).load(post.getGambar()).transform(new GrayscaleTransformation()).transform(new ColorFilterTransformation(Color.argb(150, 51, 0, 102))).placeholder(imgHolder.holder).error(imgHolder.holder).into(holder.gambar);
                break;
            case "sukses":
                Picasso.with(context).load(post.getGambar()).transform(new GrayscaleTransformation()).transform(new ColorFilterTransformation(Color.argb(150, 104, 159, 56))).placeholder(imgHolder.holder).error(imgHolder.holder).into(holder.gambar);
                break;
            case "travel":
                Picasso.with(context).load(post.getGambar()).transform(new GrayscaleTransformation()).transform(new ColorFilterTransformation(Color.argb(150, 203, 6, 12))).placeholder(imgHolder.holder).error(imgHolder.holder).into(holder.gambar);
                break;
            case "feature":
                Picasso.with(context).load(post.getGambar()).transform(new GrayscaleTransformation()).transform(new ColorFilterTransformation(Color.argb(150, 77, 77, 77))).placeholder(imgHolder.holder).error(imgHolder.holder).into(holder.gambar);
                break;
            case "style":
                Picasso.with(context).load(post.getGambar()).transform(new GrayscaleTransformation()).transform(new ColorFilterTransformation(Color.argb(150, 11, 119, 169))).placeholder(imgHolder.holder).error(imgHolder.holder).into(holder.gambar);
                break;
        }
        Picasso.with(context).load(post.getGambar()).transform(new CropCircleTransformation()).placeholder(imgHolder.bulet).error(imgHolder.bulet).into(holder.bulet);
        return convertView;
    }

    private class ViewHolder
    {
        TextView judul;
        ImageView gambar;
        ImageView bulet;
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
