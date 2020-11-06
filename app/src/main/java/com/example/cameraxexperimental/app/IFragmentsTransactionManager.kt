package com.example.cameraxexperimental.app

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

interface IFragmentsTransactionsManager {
    fun replaceFragment(fragment: Fragment, tag: String, @IdRes container: Int,
                        addToBackStack: Boolean = false, enterAnims: Pair<Int, Int>? = null,
                        exitAnims: Pair<Int, Int>? = null)

    fun addFragment(fragment: Fragment, tag: String, @IdRes container: Int, addToBackStack: Boolean = false, anims: Pair<Int, Int>? = null)
}