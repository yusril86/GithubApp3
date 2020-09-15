package com.pareandroid.githubapp.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.pareandroid.githubapp.ui.fragment.DetailFollowFragment
import com.pareandroid.githubapp.R

class PagerFollowAdapter(private val mContext : Context, frm : FragmentManager) : FragmentPagerAdapter(frm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var username: String? = null

    private val tabTitle= arrayOf(
        R.string.detail_followers,
        R.string.detail_following
    )

    override fun getItem(position: Int): Fragment {
        return if (position == 0){
            DetailFollowFragment.newInstance(username,"followers")
        }else{

            DetailFollowFragment.newInstance(username,"following")
        }
    }

    override fun getCount(): Int =2

    override fun getPageTitle(position: Int): CharSequence? = mContext.resources.getString(tabTitle[position])

}