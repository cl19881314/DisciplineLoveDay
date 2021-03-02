package com.chen.discipline.love.day.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chen.discipline.love.day.MyApplication
import com.chen.discipline.love.day.R
import com.chen.discipline.love.day.ui.adapter.ContactsAdapter
import com.chen.discipline.love.day.ui.adapter.RemindAdapter
import kotlinx.android.synthetic.main.fragment_dashboard.view.*

class RemindFragment : Fragment() {
    private lateinit var adapter: RemindAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        var root =inflater.inflate(R.layout.fragment_dashboard, container, false)
        root.rvData.layoutManager = LinearLayoutManager(context)
        adapter =  RemindAdapter()
        root.rvData.adapter = adapter
        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val daoSession = MyApplication.getInstance().daoSession
        var personMessageDaoDao = daoSession.personMessageDao
        adapter.addAllPerson(personMessageDaoDao.loadAll())
    }
}