package id.zelory.hipwee.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import id.zelory.hipwee.fragment.MainFragment;
import id.zelory.hipwee.model.Post;

/**
 * Created by zetbaitsu on 16/02/2015.
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter
{
    public MainViewPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        MainFragment mainFragment = new MainFragment();
        switch (position)
        {
            case 0:
                mainFragment.setUrl(Post.TOP_7);
                break;
            case 1:
                mainFragment.setUrl(Post.TOP_30);
                break;
            case 2:
                mainFragment.setUrl(Post.TOP_ALL);
                break;
            default:
                break;
        }
        return mainFragment;
    }

    @Override
    public int getCount()
    {
        return 3;
    }
}
