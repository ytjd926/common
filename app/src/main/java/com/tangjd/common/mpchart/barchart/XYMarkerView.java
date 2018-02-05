
package com.tangjd.common.mpchart.barchart;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.tangjd.common.R;

import java.text.DecimalFormat;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class XYMarkerView extends MarkerView {

    private TextView tvContent;
    private IAxisValueFormatter xAxisValueFormatter;

    private DecimalFormat format;
    private String mUnit;

    public XYMarkerView(Context context, IAxisValueFormatter xAxisValueFormatter, String unit) {
        super(context, R.layout.custom_marker_view);
        mUnit = unit;
        this.xAxisValueFormatter = xAxisValueFormatter;
        tvContent = (TextView) findViewById(R.id.tvContent);
        format = new DecimalFormat("###.0");
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    private float mValueY = 0;

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        tvContent.setText(xAxisValueFormatter.getFormattedValue(e.getX(), null) + ": " + format.format(e.getY()) + mUnit);
        mValueY = e.getY();
        super.refreshContent(e, highlight);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
        if (mValueY != 0) {
            super.draw(canvas, posX, posY);
        }
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
