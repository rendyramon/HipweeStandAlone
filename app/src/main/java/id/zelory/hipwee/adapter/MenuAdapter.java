package id.zelory.hipwee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import id.zelory.hipwee.R;

/**
 * Created by zetbaitsu on 28/02/2015.
 */
public class MenuAdapter extends BaseAdapter
{
    private LayoutInflater layoutInflater;

    public MenuAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 9;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
                view = layoutInflater.inflate(R.layout.item_menu, parent, false);
           

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        switch (position) {
            case 0:
                viewHolder.textView.setText("POPULER");
                viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
                break;
            case 1:
                viewHolder.textView.setText("HIBURAN");
                viewHolder.imageView.setImageResource(R.mipmap.menu_hiburan);
                break;
            case 2:
                viewHolder.textView.setText("TIPS");
                viewHolder.imageView.setImageResource(R.mipmap.menu_tips);
                break;
            case 3:
                viewHolder.textView.setText("HUBUNGAN");
                viewHolder.imageView.setImageResource(R.mipmap.menu_hubungan);
                break;
            case 4:
                viewHolder.textView.setText("MOTIVASI");
                viewHolder.imageView.setImageResource(R.mipmap.menu_motivasi);
                break;
            case 5:
                viewHolder.textView.setText("SUKSES");
                viewHolder.imageView.setImageResource(R.mipmap.menu_sukses);
                break;
            case 6:
                viewHolder.textView.setText("TRAVEL");
                viewHolder.imageView.setImageResource(R.mipmap.menu_travel);
                break;
            case 7:
                viewHolder.textView.setText("FEATURE");
                viewHolder.imageView.setImageResource(R.mipmap.menu_feature);
                break;
            default:
                viewHolder.textView.setText("STYLE");
                viewHolder.imageView.setImageResource(R.mipmap.menu_style);
                break;
        }

        return view;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
