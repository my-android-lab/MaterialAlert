package com.github.skyfe79.materialalert

import com.github.skyfe79.android.reactcomponentkit.redux.Action

data class ShowBasicAlertAction(
    val title: String,
    val message: String
): Action

