package com.example.rydehomeuser.utils

import androidx.viewpager.widget.PagerAdapter
import android.view.View
import androidx.annotation.NonNull
import java.nio.file.Files.size
import android.view.ViewGroup
import androidx.annotation.IdRes


class CommonPagerAdapter : androidx.viewpager.widget.PagerAdapter() {
    private val pageIds = arrayListOf<Int>()

    fun insertViewId(@IdRes pageId: Int) {
        pageIds.add(pageId)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return container.findViewById(pageIds.get(position))
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return pageIds.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}