package com.lichangxin.xiuchat;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class ViewSlideActivity extends AppCompatActivity {
    private View view1;
    private View view2;
    private View view3;
    private ViewPager viewPager;
    private ImageView imageViews[];
    private ImageView imageView;
    private ArrayList<View> viewList;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)  。
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 。
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /* 初始化 ViewPager */
    private void initViewPager() {
        ViewGroup container = (ViewGroup) findViewById(R.id.ll_container);

        imageViews = new ImageView[viewList.size()];
        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // 设置每个小圆点距离做左边的的间距
        margin.setMargins(px2dip(this, 20), 0, 0, 0);

        for (int i = 0; i < viewList.size(); i++) {
            imageView = new ImageView(ViewSlideActivity.this);

            // 设置每个小圆点的宽高
            imageView.setLayoutParams(new LinearLayout.LayoutParams(px2dip(this, 10), px2dip(this,10)));
            imageViews[i] = imageView;

            if (i == 0) {
                // 默认选中第一个圆点
                imageViews[i].setBackgroundResource(R.drawable.shape_point_white);
            } else {
                // 其他设为未选中状态
                imageViews[i].setBackgroundResource(R.drawable.shape_point_gray);
            }
            container.addView(imageView, margin);
        }

        viewPager = findViewById(R.id.view_pager);

        // 添加适配器数据
        viewPager.setAdapter(new ViewPagerAdapter(viewList));
        viewPager.setCurrentItem(0);

        // 监听滑动事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d("ViewSlideActivity", "" + position);
                for (int i = 0; i < viewList.size(); i++) {
                    imageViews[i].setBackgroundResource(R.drawable.shape_point_gray);
                }
                imageViews[position].setBackgroundResource(R.drawable.shape_point_white);
            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_page);

        LayoutInflater inflater = getLayoutInflater().from(this);

        view1 = inflater.inflate(R.layout.view_page1, null);
        view2 = inflater.inflate(R.layout.view_page2, null);
        view3 = inflater.inflate(R.layout.view_page3, null);

        viewList = new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        initViewPager();
    }

    /* 数据适配器 */
    public class ViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public ViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));
        }
    }
}
