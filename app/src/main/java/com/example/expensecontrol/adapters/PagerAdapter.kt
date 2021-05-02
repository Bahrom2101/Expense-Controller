package com.example.expensecontrol.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.expensecontrol.PagerFragment
import com.example.expensecontrol.models.CategoryStatistics

class PagerAdapter(var list: List<CategoryStatistics>, fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(
        fragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Fragment {
        return PagerFragment.newInstance(list[position].title)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence {
        return list[position].title
    }
}