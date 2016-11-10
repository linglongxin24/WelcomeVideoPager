package cn.bluemobi.dylan.welcomevideopager;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager vp;
    private ImageView iv1;
    private ImageView iv2;
    private ImageView iv3;
    private Button bt_start;
    private List<Fragment> fragments;

    private void assignViews() {
        vp = (ViewPager) findViewById(R.id.vp);
        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        bt_start = (Button) findViewById(R.id.bt_start);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignViews();
        initData();
        initView();

    }

    /**
     * 初始化数据
     */
    private void initData() {
        fragments = new ArrayList<>();

        Fragment fragment1 = new GuildFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("index", 1);
        fragment1.setArguments(bundle1);
        fragments.add(fragment1);

        Fragment fragment2 = new GuildFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("index", 2);
        fragment2.setArguments(bundle2);
        fragments.add(fragment2);

        Fragment fragment3 = new GuildFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("index", 3);
        fragment3.setArguments(bundle3);
        fragments.add(fragment3);
    }


    private void initView() {
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(new MyPageAdpater(getSupportFragmentManager()));
        vp.addOnPageChangeListener(new MyPageChangeListener());
    }

    private class MyPageAdpater extends FragmentPagerAdapter {


        public MyPageAdpater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            bt_start.setVisibility(View.GONE);
            iv1.setImageResource(R.mipmap.dot_normal);
            iv2.setImageResource(R.mipmap.dot_normal);
            iv3.setImageResource(R.mipmap.dot_normal);
            if (position == 0) {
                iv1.setImageResource(R.mipmap.dot_focus);
            }else if(position==1){
                iv2.setImageResource(R.mipmap.dot_focus);
            }else {
                iv3.setImageResource(R.mipmap.dot_focus);
                bt_start.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
