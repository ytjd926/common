package com.tangjd.common.mpchart.barchart;

import android.content.Context;
import android.graphics.RectF;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.MPPointF;
import com.tangjd.common.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangjd on 2018/2/2.
 */

public class BarChartHelper {
    private Context mContext;
    private BarChart mChart;
    private RectF mOnValueSelectedRectF = new RectF();
    private int mBarColor;

    public BarChartHelper(Context context, BarChart barChart) {
        mContext = context;
        mChart = barChart;
    }

    /**
     * @param unit                单位
     * @param showXAxisLabelCount x坐标label显示的个数，如：共30个柱状图形，可以设置显示10个label等，使显示效果不至于太密集
     * @param xAxisLabelFormatter x坐标label需要显示的内容可以自定义，
     *                            回调如下：
     *                            public String getFormattedValue(float value, AxisBase axis) {
     *                            }
     *                            注意value的起始值为1！
     */
    public void initBarChart(String unit, int showXAxisLabelCount, IAxisValueFormatter xAxisLabelFormatter) {
        initBarChart(unit, showXAxisLabelCount, mContext.getResources().getColor(R.color.colorPrimary), xAxisLabelFormatter);
    }

    /**
     * @param unit                单位
     * @param showXAxisLabelCount x坐标label显示的个数，如：共30个柱状图形，可以设置显示10个label等，使显示效果不至于太密集
     * @param barColor            柱状图形的颜色
     * @param xAxisLabelFormatter x坐标label需要显示的内容可以自定义，
     *                            回调如下：
     *                            public String getFormattedValue(float value, AxisBase axis) {
     *                            }
     *                            注意value的起始值为1！
     */
    public void initBarChart(String unit, int showXAxisLabelCount, int barColor, IAxisValueFormatter xAxisLabelFormatter) {
        mBarColor = barColor;
        mChart.setNoDataText("没有数据");
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                RectF bounds = mOnValueSelectedRectF;
                mChart.getBarBounds((BarEntry) e, bounds);
                MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);
                MPPointF.recycleInstance(position);
            }

            @Override
            public void onNothingSelected() {

            }
        });

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be drawn
//        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(showXAxisLabelCount);
        xAxis.setValueFormatter(xAxisLabelFormatter);

        IAxisValueFormatter custom = new YAxisLabelFormatter(unit);

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setLabelCount(8, false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setGridLineWidth(0.6f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
//        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        // 不显示rightAxis
        rightAxis.setEnabled(false);

        // 图标底部的说明
        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // 不显示Legend
        l.setEnabled(false);

        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        // 点击柱状图后的PopupView
        XYMarkerView mv = new XYMarkerView(mContext, xAxisLabelFormatter, unit);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart

//        setData(12, 50);
//
//        // setting data
//        mSeekBarY.setProgress(50);
//        mSeekBarX.setProgress(12);
//
//        mSeekBarY.setOnSeekBarChangeListener(this);
//        mSeekBarX.setOnSeekBarChangeListener(this);
//
//        // mChart.setDrawLegend(false);
    }

    /**
     * SparseArray<ChartBean> key starts from 1
     */
    public void setData(List<Float> beans) {
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < beans.size(); i++) {
            yValues.add(new BarEntry(i + 1, beans.get(i) == null ? 0 : beans.get(i)));
        }

        BarDataSet set1;
        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yValues);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yValues, "");

            set1.setDrawIcons(false);
            set1.setDrawValues(false);
            set1.setColors(mBarColor);
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            final BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            mChart.setData(data);

            mChart.invalidate();
        }
    }

    public class YAxisLabelFormatter implements IAxisValueFormatter {
        private DecimalFormat mFormat;
        private String mUnit;

        public YAxisLabelFormatter(String unit) {
            mUnit = unit;
            mFormat = new DecimalFormat("###,###,###,##0.0");
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value) + " " + mUnit;
        }
    }

//    public class DayAxisValueFormatter implements IAxisValueFormatter {
//
//        @Override
//        public String getFormattedValue(float value, AxisBase axis) {
//            if (value > 28) {
//                return "";
//            }
//            return ((int) value) + "日";
//        }
//    }

    public void setLabelRotationAngle(float angle) {
        mChart.getXAxis().setLabelRotationAngle(angle);
    }
}
