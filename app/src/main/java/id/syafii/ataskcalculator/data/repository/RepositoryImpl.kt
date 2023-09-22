/*
 * Created by Muhamad Syafii
 * Friday, 22/9/2023
 * Copyright (c) 2023.
 * All Rights Reserved
 */

package id.syafii.ataskcalculator.data.repository

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognizer
import id.syafii.ataskcalculator.data.Response
import javax.inject.Inject
import kotlinx.coroutines.tasks.await
class RepositoryImpl @Inject constructor(
    private val recognizer: TextRecognizer
): Repository {
    override suspend fun calculate(image: InputImage): Response<String> {
        return try {
            val text = recognizer.process(image).await().text
            Response.Success(text)
        } catch (e: Exception) {
            Response.Error(e)
        }
    }

}