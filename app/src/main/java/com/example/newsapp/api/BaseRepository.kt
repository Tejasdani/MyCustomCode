package com.example.newsapp.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

/**
 * Function that executes the given function on Dispatchers.IO context and switch to Dispatchers.Main context when an error occurs
 * @param callFunction is the function that is returning the wanted object. It must be a suspend function. Eg:
 * override suspend fun loginUser(body: LoginUserBody, emitter: RemoteErrorEmitter): LoginUserResponse? = safeApiCall( { authApi.loginUser(body)} , emitter)
 * @param emitter is the interface that handles the error messages. The error messages must be displayed on the MainThread, or else they would throw an Exception.
 */


@Suppress("UNCHECKED_CAST")
suspend inline fun <T> safeApiCall(
    crossinline callFunction: suspend () -> Response<T>
): Resource<T?> {
    try {
        val myObject = withContext(Dispatchers.IO) {
            callFunction.invoke()
        }
        Timber.e("Is response is success", myObject.isSuccessful)
        return if (myObject.isSuccessful) {
            Resource.Success(myObject.body() as T)
        } else {
            Resource.Error(
                myObject.body(), ErrorType.UNKNOWN, myObject.message() ?: "Some Exception Occurred"
            )
        }

    } catch (e: Exception) {
        e.printStackTrace()
        when (e) {
            is HttpException -> {
                return if (e.code() == Error.UN_AUTHORISED_ERROR) {
                    val error = Error().apply {
                        message = Error.SESSION_EXPIRE
                    }
                    Resource.UnAuthorised(null, ErrorType.SESSION_EXPIRED, "")

                } else {
                    val body = e.response()?.errorBody()
                    Resource.Error(null, ErrorType.UNKNOWN, "")
                }
            }
            is SocketTimeoutException -> {
                return Resource.Error(null, ErrorType.TIMEOUT, "")
            }
            is IOException -> {
                return Resource.Error(null, ErrorType.NETWORK, "")
            }
            is ConnectException -> {
                return Resource.Error(null, ErrorType.SERVER_ERROR, "")
            }
            is ClassCastException -> {
                return Resource.Error(null, ErrorType.CLASS_CAST_EXCEPTION, "")
            }
            else -> {
                return Resource.Error(null, ErrorType.UNKNOWN, "")
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
suspend inline fun <T> safeMultiApiCalls(
    crossinline callFunction: suspend () -> List<Response<T>?>
): List<Resource<T>> {
    try {
        val responseList: List<Response<T>?> = withContext(Dispatchers.IO) {
            callFunction.invoke()
        }

        val resourceList = ArrayList<Resource<T>>()
        responseList.map { response ->
            if (response?.isSuccessful == true && response.body() != null) {
                resourceList.add(Resource.Success(response.body() as T))
            } else {
                resourceList.add(Resource.Error(null, ErrorType.UNKNOWN))
            }
        }

        return resourceList
    } catch (e: Exception) {
        e.printStackTrace()
        when (e) {
            is HttpException -> {
                if (e.code() == Error.UN_AUTHORISED_ERROR) {
                    val error = Error().apply {
                        message = Error.SESSION_EXPIRE
                    }
                    Resource.UnAuthorised(null, ErrorType.SESSION_EXPIRED, "")

                } else {
                    val body = e.response()?.errorBody()
                    Resource.Error(null, ErrorType.UNKNOWN, "")
                }
            }
            is SocketTimeoutException -> {
                Resource.Error(null, ErrorType.TIMEOUT, "")
            }
            is IOException -> {
                Resource.Error(null, ErrorType.NETWORK, "")
            }
            is ClassCastException -> {
                Resource.Error(null, ErrorType.CLASS_CAST_EXCEPTION, "")
            }
            else -> {
                Resource.Error(null, ErrorType.UNKNOWN, "")
            }
        }
        return listOf()
    }
}

@Suppress("UNCHECKED_CAST")
fun <T> Resource<T>?.isRequestCallSuccess(
    success: (T) -> Unit = {},
    failure: (T?, ErrorType) -> Unit = { _, _ -> },
    unAuthorised: (T?, ErrorType) -> Unit = { _, _ -> }
) {
    val resData = this
    if (this is Resource.Success<*>) {
        if (isRequestSuccess(this.data as ApiResult<*>)) {
            success(resData?.data as T)
        } else {
            failure(resData?.data, resData?.errorType ?: ErrorType.UNKNOWN)
        }
    } else if (this is Resource.Error<*>) {
        failure(resData?.data, resData?.errorType!!)
    }/*else if (this is Resource.UnAuthorised<*>) {
        unAuthorised(resData?.data, resData?.errorType!!)
    } */ else {
        failure(resData?.data, resData?.errorType!!)
    }
}

suspend fun <T> Resource<T>?.isRequestCallSuspendSuccess(
    success: suspend (T) -> Unit = {},
    failure: suspend (T?, ErrorType) -> Unit = { _, _ -> },
    unAuthorised: suspend (T?, ErrorType) -> Unit = { _, _ -> }
) {
    val resData = this
    if (this is Resource.Success<*>) {
        if (isRequestSuccess(this.data as ApiResult<*>)) {
            success(resData?.data as T)
        } else {
            failure(resData?.data, resData?.errorType ?: ErrorType.UNKNOWN)
        }
    } else if (this is Resource.Error<*>) {
        failure(resData?.data, resData?.errorType!!)
    }/*else if (this is Resource.UnAuthorised<*>) {
        unAuthorised(resData?.data, resData?.errorType!!)
    }*/ else {
        failure(resData?.data, resData?.errorType!!)
    }
}

private fun isRequestSuccess(receivedData: ApiResult<*>?): Boolean {
    if (receivedData == null) return false
    /* val gson = Gson()
     val tmp = gson.toJson(receivedData)
     val myObject: ApiResult<*> = gson.fromJson(tmp, ApiResult::class.java)*/
    return receivedData.errorType.equals("success", true)
}