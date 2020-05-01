package hu.mesys.snappingseekbar.library.views

import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.ProgressBar

/**
 * User: tobiasbuchholz
 * Date: 28.07.14 | Time: 14:18
 */
class ProgressBarAnimation(
        private val progressBar: ProgressBar,
        private val from: Float,
        to: Float
) : Animation() {

    private val distance = to - from

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        super.applyTransformation(interpolatedTime, t)
        val value = from + distance * interpolatedTime
        progressBar.progress = value.toInt()
    }
}