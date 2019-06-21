# MaterialAlert

Material Alert Dialog Example with AndroidReactComponentKit

![](./art/result.gif)


## Action

```kotlin
data class ShowBasicAlertAction(
    val title: String,
    val message: String
): Action
```

## Middleware

```kotlin
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
```

## MainViewModel & State

```kotlin
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
```

## MainActivity

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private val disposeBag: AutoDisposeBag by lazy {
        AutoDisposeBag(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        handleClickEvent()
        handleViewModelOutputs()
    }

    private fun handleClickEvent() {
        showAlert.onClick {
            viewModel.dispatch(ShowBasicAlertAction(
                title = "Hello",
                message = "This is Material Alert Dialog"
            ))
        }
    }

    private fun handleViewModelOutputs() {
        viewModel
            .route
            .asObservable()
            .skip(1)
            .subscribe {
                handleRoute(it)
            }
            .disposedBy(disposeBag)
    }

    private fun handleRoute(route: Route) {
        when (route) {
            is Route.None -> Unit
            is Route.BasicAlert -> showBasicAlert(route.title, route.message)
        }
    }

    private fun showBasicAlert(title: String, message: String) {
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}
```