package com.downstairs101.camera.view

import android.util.Size
import android.view.View
import com.downstairs101.camera.view.camera.Camera

class PreviewSize {

    fun getFitsPreviewSize(view: View, cameraSettings: Camera.CameraSettings): Size {
        val supportedSizes = cameraSettings.getSupportedSizes()
        var size: Size? = null

        if (supportedSizes.any { matchesAspectRatio(it) }) {
            size = selectSuccessorViewSize(view, supportedSizes)
            size ?: selectPredecessorViewSize(view, supportedSizes)
        }

        return size ?: supportedSizes.first()
    }

    private fun selectSuccessorViewSize(view: View, supportedSizes: List<Size>): Size? {
        return supportedSizes
            .filter { matchesAspectRatio(it) && sizeIsBiggerThanView(view, it) }
            .minBy { area(it.width, it.height) }
    }

    private fun selectPredecessorViewSize(view: View, supportedSizes: List<Size>): Size? {
        return supportedSizes
            .filter { matchesAspectRatio(it) && sizeIsMinorThanView(view, it) }
            .maxBy { it.width * it.height }
    }

    private fun area(width: Int, height: Int) = width * height

    private fun sizeIsBiggerThanView(view: View, size: Size) = size.width >= view.width && size.height >= view.height

    private fun sizeIsMinorThanView(view: View, size: Size) = size.width <= view.width && size.height <= view.height

    private fun matchesAspectRatio(size: Size) = size.width * aspectRatio().height == size.height * aspectRatio().width

    private fun aspectRatio() = Size(1, 1)
}