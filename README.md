Android酷炫欢迎页播放视频,仿蚂蜂窝自由行和慕课网
>今天无意间看到了蚂蜂窝自由行的app，启动页很酷炫。我记得以前慕课网有个版本的app欢迎页也是播放视频的。
今天就顺手写一个，代码比较简单，高手请略过。

先看效果图：

 ![效果图](https://github.com/linglongxin24/WelcomeVideoPager/blob/master/screenshorts/effect.gif?raw=true)

#一.资源准备

三个比较短小的视频：[视频下载](https://github.com/linglongxin24/WelcomeVideoPager/tree/master/app/src/main/res/raw)

#二.开始编写代码

 * 1.在项目的res下新建一个raw文件夹，放入准备好的这三个视频
 
 * 2.自定义播放视频的CustomVideoView
  在这个自定义View里面提供一个播放视频的方法。用户只需要传入播放路径就可以了，并且可一循环播放。

```java
package cn.bluemobi.dylan.welcomevideopager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.VideoView;

/**
 * 可以播放视频的View
 * Created by yuandl on 2016-11-10.
 */

public class CustomVideoView extends VideoView {
    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec), View.MeasureSpec.getSize(heightMeasureSpec));
    }

    /**
     * 播放视频
     *
     * @param uri 播放地址
     */
    public void playVideo(Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Uri can not be null");
        }
        /**设置播放路径**/
        setVideoURI(uri);
        /**开始播放**/
        start();
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                /**设置循环播放**/
                mp.setLooping(true);
            }
        });
        setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return true;
            }
        });
    }
}
```

 * 3.建立没个欢迎页面的Fragment去加载自定义视频View的视图

```java
package cn.bluemobi.dylan.welcomevideopager;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by yuandl on 2016-11-10.
 */

public class GuildFragment extends Fragment {

    private CustomVideoView customVideoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        customVideoView = new CustomVideoView(getContext());
        /**获取参数，根据不同的参数播放不同的视频**/
        int index = getArguments().getInt("index");
        Uri uri;
        if (index == 1) {
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.guide_1);
        } else if (index == 2) {
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.guide_2);
        } else {
            uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.guide_3);
        }
        /**播放视频**/
        customVideoView.playVideo(uri);
        return customVideoView;
    }

    /**
     * 记得在销毁的时候让播放的视频终止
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customVideoView != null) {
            customVideoView.stopPlayback();
        }
    }
}

```
 * 4.界面布局
 
```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <android.support.v4.view.ViewPager
        android:id="@+id/vp"
        android:layout_width="match_parent"
        android:layout_height="match_parent"></android.support.v4.view.ViewPager>

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"
        android:layout_marginBottom="130dp"
        android:orientation="horizontal">

        <ImageView
            android:id="@+id/iv1"
            android:layout_width="10dp"
            android:layout_height="10dp"
            android:src="@mipmap/dot_focus" />

        <ImageView
            android:id="@+id/iv2"
            android:layout_width="10dp"
            android:layout_height="10dp"
            android:layout_marginLeft="10dp"
            android:src="@mipmap/dot_normal" />

        <ImageView
            android:id="@+id/iv3"
            android:layout_width="10dp"
            android:layout_height="10dp"
            android:layout_marginLeft="10dp"
            android:src="@mipmap/dot_normal" />
    </LinearLayout>

    <Button
        android:id="@+id/bt_start"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_centerHorizontal="true"
        android:layout_marginBottom="50dp"
        android:background="@mipmap/bt_start"
        android:textColor="@android:color/white"
        android:visibility="gone" />

</RelativeLayout>
```
 
 * 5.给界面添加Fragment
 
```java
package cn.bluemobi.dylan.welcomevideopager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
     * 初始化数据,添加三个Fragment
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

    /**
     * 设置ViewPager的适配器和滑动监听
     */
    private void initView() {
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
        vp.addOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * ViewPager适配器
     */
    private class MyPageAdapter extends FragmentPagerAdapter {


        public MyPageAdapter(FragmentManager fm) {
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

    /**
     * ViewPager滑动页面监听器
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 根据页面不同动态改变红点和在最后一页显示立即体验按钮
         *
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            bt_start.setVisibility(View.GONE);
            iv1.setImageResource(R.mipmap.dot_normal);
            iv2.setImageResource(R.mipmap.dot_normal);
            iv3.setImageResource(R.mipmap.dot_normal);
            if (position == 0) {
                iv1.setImageResource(R.mipmap.dot_focus);
            } else if (position == 1) {
                iv2.setImageResource(R.mipmap.dot_focus);
            } else {
                iv3.setImageResource(R.mipmap.dot_focus);
                bt_start.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}

```
#三.[GitHub](https://github.com/linglongxin24/WelcomeVideoPager)
 
>注意：如果视频是有声音的话是会有问题的，由于Fragment的预加载机制。具体解决方案请看这里[Android中ViewPager+Fragment取消(禁止)预加载延迟加载(懒加载)问题解决方案](http://blog.csdn.net/linglongxin24/article/details/53205878) 
在本Demo中可以将GuildFragment替换为Guild2Fragment可以查看解决后的效果。