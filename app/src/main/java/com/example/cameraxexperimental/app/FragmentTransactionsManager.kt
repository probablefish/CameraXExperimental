package com.example.cameraxexperimental.app

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import javax.inject.Inject


class FragmentsTransactionsManager @Inject constructor(private val fm: FragmentManager) :
    IFragmentsTransactionsManager {

    override fun replaceFragment(fragment: Fragment, tag: String, @IdRes container: Int,
                                 addToBackStack: Boolean, enterAnims: Pair<Int, Int>?,
                                 exitAnims: Pair<Int, Int>?
    ) {
        val fragmentTransition = fm.beginTransaction()
        if (enterAnims != null && exitAnims != null) {
            fragmentTransition.setCustomAnimations(enterAnims.first, enterAnims.second, exitAnims.first, exitAnims.second)
        }

        fragmentTransition
            .replace(container, fragment, tag)

        if (addToBackStack) {
            fragmentTransition.addToBackStack(tag)
        }
        fragmentTransition.commit()
    }

    override fun addFragment(fragment: Fragment, tag: String, @IdRes container: Int, addToBackStack: Boolean, anims: Pair<Int, Int>?) {
        val fragmentTransition = fm.beginTransaction()

        if (anims != null) {
            fragmentTransition.setCustomAnimations(anims.first, anims.second, anims.first, anims.second)
        }

        fragmentTransition
            .add(container, fragment, tag)

        if (addToBackStack) {
            fragmentTransition.addToBackStack(tag)
        }
        fragmentTransition.commit()
    }
}