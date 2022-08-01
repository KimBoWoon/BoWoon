package com.data.lol.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 코루틴을 사용하여 백그라운드로 작업을 실행할 때 사용하는 함수
 *
 * @param block 백그라운드로 진행시킬 작업
 * @param success 작업이 성공적으로 마무리 되었을 때
 * @param error 작업이 실패 했을 때
 */
fun <T, R> coroutineIOCallbackTo(
    block: suspend (() -> T),
    success: ((T?) -> R)? = null,
    error: ((Throwable) -> R)? = null
) {
    CoroutineScope(Dispatchers.Main).launch {
        runCatching {
            withContext(Dispatchers.IO) {
                block.invoke()
            }
        }.onSuccess {
            success?.invoke(it)
        }.onFailure { e ->
            Log.printStackTrace(e)
            error?.invoke(e)
        }
    }
}

/**
 * 코루틴을 사용하여 메인 스레드에서 작업을 실행할 때 사용하는 함수
 *
 * @param block 메인 스레드로 진행시킬 작업
 * @param success 작업이 성공적으로 마무리 되었을 때
 * @param error 작업이 실패 했을 때
 */
fun <T, R> coroutineMainCallbackTo(
    block: suspend (() -> T),
    success: ((T?) -> R)? = null,
    error: ((Throwable) -> R)? = null
) {
    CoroutineScope(Dispatchers.Main).launch {
        runCatching {
            block.invoke()
        }.onSuccess {
            success?.invoke(it)
        }.onFailure { e ->
            Log.printStackTrace(e)
            error?.invoke(e)
        }
    }
}