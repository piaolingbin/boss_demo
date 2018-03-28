package lecho.lib.hellocharts.formatter;

import lecho.lib.hellocharts.model.SliceValue;

public interface PieChartValueFormatter {

    public int formatChartValue(char[] formattedValue, SliceValue value);
    public int formatChartValue2(char[] formattedValue, SliceValue value);
}
