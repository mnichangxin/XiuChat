package com.lichangxin.xiuchat;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewSlideActivity extends AppCompatActivity {
    private View view1;
    private View view2;
    private View view3;
    private ViewPager viewPager;
    private ImageView imageViews[];
    private ArrayList<View> viewList;

    private void initViewPager() {
        LinearLayout container = (LinearLayout) findViewById(R.id.ll_container);

        imageViews = new ImageView[viewList.size()];
        LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // 设置每个小圆点距离做左边的的间距
        margin.setMargins(15, 0, 0, 0);

        for (int i = 0; i < viewList.size(); i++) {
            ImageView imageView = new ImageView(ViewSlideActivity.this);

            // 设置每个小圆点的宽高
            imageView.setLayoutParams(new LinearLayout.LayoutParams(5, 5));
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

        viewPager.setAdapter(new ViewPagerAdapter(viewList));
        viewPager.setCurrentItem(0);

        // 监听滑动事件
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < viewList.size(); i++) {
                    imageViews[position].setBackgroundResource(R.drawable.shape_point_white);
                    if (position != i) {
                        imageViews[position].setBackgroundResource(R.drawable.shape_point_gray);
                    }
                }
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
