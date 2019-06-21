package com.github.skyfe79.materialalert

import com.github.skyfe79.android.reactcomponentkit.redux.Action
import com.github.skyfe79.android.reactcomponentkit.redux.State
import io.reactivex.Observable

fun MainViewModel.handleRoute(state: State, action: Action): Observable<State> {
    val mainViewState = (state as? MainViewState) ?: return Observable.just(state)

    return when (action) {
        is ShowBasicAlertAction -> {
            Observable.just(mainViewState.copy(route = Route.BasicAlert(action.title, action.message)))
        }
        else -> {
            Observable.just(mainViewState.copy(route = Route.None))
        }
    }
}