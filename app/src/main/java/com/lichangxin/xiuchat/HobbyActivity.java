package com.lichangxin.xiuchat;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class HobbyActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private PieChart pieChart;

    // 设置 Toolbar 和事件
    private void setBar() {
        toolbar = findViewById(R.id.hobby_toolbar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("兴趣（用户画像）");

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }
    }

    private void showChart(PieChart pieChart, PieData pieData) {
        pieChart.setHoleRadius(60f);  // 半径
        pieChart.setTransparentCircleRadius(64f); // 半透明圈

        pieChart.setDrawCenterText(true); // 饼状图中间可以添加文字

        pieChart.setDrawHoleEnabled(true);

        pieChart.setRotationAngle(90); // 初始旋转角度

        pieChart.setRotationEnabled(true); // 可以手动旋转

        pieChart.setUsePercentValues(true);  // 显示成百分比
        pieChart.setCenterText("用户画像");  // 饼状图中间的文字

        //设置数据
        pieChart.setData(pieData);

        Legend mLegend = pieChart.getLegend();  // 设置比例图
        mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  // 最右边显示
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        pieChart.animateXY(1000, 1000);  // 设置动画
    }

    private PieData getPieData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<>();  // xVals 用来表示每个饼块上的内容

        for (int i = 0; i < count; i++) {
            xValues.add("Quarterly" + (i + 1));  // 饼块上显示成 Quarterly1, Quarterly2, Quarterly3, Quarterly4
        }

        ArrayList<PieEntry> yValues = new ArrayList<>();  // yVals 用来表示封装每个饼块的实际数据

        // 饼图数据
        float quarterly1 = 14;
        float quarterly2 = 14;
        float quarterly3 = 34;
        float quarterly4 = 38;

        yValues.add(new PieEntry(quarterly1, 0));
        yValues.add(new PieEntry(quarterly2, 1));
        yValues.add(new PieEntry(quarterly3, 2));
        yValues.add(new PieEntry(quarterly4, 3));

        // y 轴的集合
        PieDataSet pieDataSet = new PieDataSet(yValues, "标签属性");
        pieDataSet.setSliceSpace(0f); // 设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<>();

        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        float px = 5 * (metrics.densityDpi / 160f);

        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        PieData pieData = new PieData(pieDataSet);

        return pieData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.hobby_layout);

        setBar();

        pieChart = findViewById(R.id.pie_chart);

        PieData pieData = getPieData(4, 100);

        showChart(pieChart, pieData);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}
