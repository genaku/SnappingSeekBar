package hu.mesys.snappingseekbar.library.utils;

/**
 * Created by Mezei József on 2017. 03. 07..
 */

public class SeekbarUtils {

    public static float sectionFactor(float indicatorCount){
        return  100f / (indicatorCount - 1f);
    }
}
