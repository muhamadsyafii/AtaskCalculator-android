/*
 * Created by Muhamad Syafii
 * , 22/9/2023
 * Copyright (c) 2023.
 * All Rights Reserved
 */

package id.syafii.ataskcalculator.module

import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.syafii.ataskcalculator.data.repository.Repository
import id.syafii.ataskcalculator.data.repository.RepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideTextRecognizer() = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @Provides
    fun provideRepository(recognizer: TextRecognizer): Repository = RepositoryImpl(recognizer)
}