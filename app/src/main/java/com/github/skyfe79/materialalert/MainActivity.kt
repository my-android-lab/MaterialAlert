package com.github.skyfe79.materialalert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.github.skyfe79.android.reactcomponentkit.rx.AutoDisposeBag
import com.github.skyfe79.android.reactcomponentkit.rx.disposedBy
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick

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
