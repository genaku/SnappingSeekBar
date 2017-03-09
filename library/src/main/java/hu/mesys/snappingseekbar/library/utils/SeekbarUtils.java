package hu.mesys.snappingseekbar.library.utils;

/**
 * Created by Mezei József on 2017. 03. 07..
 */

public class SeekbarUtils {

    public static float sectionFactor(float indicatorCount) {
        return 100f / (indicatorCount - 1f);
    }

    public static boolean isSetValue(float value) {
        return value > -1;
    }

    public static boolean isSetValue(float[] value) {
        return value[0] > -1 || value[1] > -1 || value[2] > -1 || value[3] > -1;
    }

    public static float[] normalizeValues(float[] values) {
        for (int i = 0; i < values.length; i++)
            if (values[i] == -1) values[i] = 0;

        return values;
    }
}
