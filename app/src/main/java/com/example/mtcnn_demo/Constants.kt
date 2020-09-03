package com.example.mtcnn_demo

import android.os.Environment
import java.io.File

object Constants {

    val LOG_DEBUG = BuildConfig.DEBUG

    const val FACE_SHAPE_MODEL_PATH = "shape_predictor_68_face_landmarks.dat"

    /**
     * getFaceShapeModelPath
     *
     * @return default face shape model path
     */
    fun getFaceShapeModelPath(): String {
        val sdcard = Environment.getExternalStorageDirectory()
        return sdcard.getAbsolutePath() + File.separator + FACE_SHAPE_MODEL_PATH
    }
}