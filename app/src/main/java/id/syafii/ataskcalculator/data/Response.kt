/*
 * Created by Muhamad Syafii
 * Friday, 22/9/2023
 * Copyright (c) 2023.
 * All Rights Reserved
 */

package id.syafii.ataskcalculator.data

sealed class Response<out R> {

    object Loading: Response<Nothing>()

    data class Success<out T>(val data: T) : Response<T>()
    data class Error(val e: Exception) : Response<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$e]"
            is Loading -> "Loading"
        }
    }
}