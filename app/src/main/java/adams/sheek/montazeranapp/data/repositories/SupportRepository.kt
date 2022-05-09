package adams.sheek.montazeranapp.data.repositories

import adams.sheek.montazeranapp.data.datasource.remote.SupportService
import adams.sheek.montazeranapp.utils.extension.Toaster
import com.skydoves.whatif.whatIfNotNull
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class SupportRepository(
    private val service: SupportService,
    private val coroutineDispatcher: CoroutineDispatcher
) : Repository {

    fun sendMessage(
        message: String,
        onStart: () -> Unit,
        onComplete: () -> Unit
    ) = flow {
        try {
            service.sendMessage(message).body().whatIfNotNull {
                if (it.errors == null) {
                    emit(it.success)
                } else {
                    Toaster.show(it.errors)
                }
                emit(it.success)
            }
        } catch (e: Exception) {
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(coroutineDispatcher)
}