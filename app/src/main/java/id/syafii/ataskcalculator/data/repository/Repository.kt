/*
 * Created by Muhamad Syafii
 * Friday, 22/9/2023
 * Copyright (c) 2023.
 * All Rights Reserved
 */

package id.syafii.ataskcalculator.data.repository

import com.google.mlkit.vision.common.InputImage
import id.syafii.ataskcalculator.data.Response

interface Repository {
    suspend fun calculate(image: InputImage) : Response<String>
}