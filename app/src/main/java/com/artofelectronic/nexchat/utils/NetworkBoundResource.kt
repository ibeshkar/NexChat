package com.artofelectronic.nexchat.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

fun <ResultType, RequestType> networkBoundResource(
    query: () -> Flow<ResultType>,
    fetch: suspend () -> RequestType,
    saveFetchResult: suspend (RequestType) -> Unit,
    shouldFetch: (ResultType) -> Boolean = { true }
): Flow<Resource<ResultType>> = flow {
    emit(Resource.Loading(null))
    query().collect { localData ->
        emit(Resource.Loading(localData))

        if (shouldFetch(localData)) {
            try {
                val remoteData = fetch()
                saveFetchResult(remoteData)
            } catch (e: Exception) {
                emit(Resource.Error(e, localData))
            }
        }

        emitAll(query().map { Resource.Success(it) })
    }
}