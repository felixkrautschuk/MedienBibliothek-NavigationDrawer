package com.example.felix.medienbibliothek;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter
{
    public TabsPagerAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public Fragment getItem(int i)
    {
        switch (i)
        {
            case 0:
                return new PersonenActivity();
            case 1:
                return new BuecherActivity();
            case 2:
                return new VerleihActivity();
        }
        return null;
    }

    public int getCount()
    {
        return 3;
    }


    @Override
    public int getItemPosition(Object object)
    {
        return POSITION_NONE;
    }
}
