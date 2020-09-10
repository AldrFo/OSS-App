package ru.mpei.domain_tasks

import io.reactivex.Single
import kekmech.ru.common_network.EmptyRequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import ru.mpei.domain_tasks.dto.TasksItem

interface TasksApi {

    // TODO(Implement request to server)
    @POST("")
    fun get(
            @Body body: EmptyRequestBody = EmptyRequestBody
    ): Single<List<TasksItem>>
}