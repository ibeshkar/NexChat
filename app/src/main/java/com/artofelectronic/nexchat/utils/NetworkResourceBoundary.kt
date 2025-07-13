package com.artofelectronic.nexchat.utils

import kotlinx.coroutines.flow.*

abstract class NetworkResourceBoundary<ResultType, RequestType> {

    fun asFlow(): Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading)

        val local = loadFromDb()
        if (local != null) {
            emit(Resource.Success(mapToResult(local)))
        }

        try {
            val remote = fetchFromRemote()
            saveRemoteData(remote)
            emit(Resource.Success(remote))
        } catch (e: Exception) {
            if (local == null) {
                emit(Resource.Error(e.localizedMessage ?: "Network error"))
            }
        }
    }

    protected abstract suspend fun fetchFromRemote(): ResultType
    protected abstract suspend fun saveRemoteData(data: ResultType)
    protected abstract suspend fun loadFromDb(): RequestType?
    protected abstract fun mapToResult(local: RequestType): ResultType
}