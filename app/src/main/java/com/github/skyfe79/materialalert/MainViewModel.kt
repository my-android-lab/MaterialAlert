package com.github.skyfe79.materialalert

import android.app.Application
import com.github.skyfe79.android.reactcomponentkit.redux.Output
import com.github.skyfe79.android.reactcomponentkit.redux.State
import com.github.skyfe79.android.reactcomponentkit.viewmodel.RootAndroidViewModelType


sealed class Route {
    object None: Route()
    data class BasicAlert(val title: String, val message: String): Route()
}

data class MainViewState(
    var route: Route = Route.None
): State()


class MainViewModel(application: Application): RootAndroidViewModelType<MainViewState>(application) {

    var route: Output<Route> = Output(Route.None)

    override fun setupStore() {
        store
            .set(
                initialState = MainViewState(),
                middlewares = arrayOf(::handleRoute)
            )
    }

    override fun on(newState: MainViewState) {
        route.acceptWithoutCompare(newState.route)
    }
}