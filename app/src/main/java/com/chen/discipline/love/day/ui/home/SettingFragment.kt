package com.chen.discipline.love.day.ui.home

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chen.discipline.love.day.MyApplication
import com.chen.discipline.love.day.R
import com.chen.discipline.love.day.greendao.PersonMessage
import com.chen.discipline.love.day.greendao.PersonMessageDao
import com.chen.discipline.love.day.ui.adapter.ContactsAdapter
import kotlinx.android.synthetic.main.fragment_notifications.view.*

class SettingFragment : Fragment() {
    private lateinit var personMessageDaoDao: PersonMessageDao
    private lateinit var adapter: ContactsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        root.txtAddMsg.setOnClickListener(View.OnClickListener { txtAddMsg() })
        root.rvData.layoutManager = LinearLayoutManager(context)
        adapter =  ContactsAdapter()
        root.rvData.adapter = adapter
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val daoSession = MyApplication.getInstance().daoSession
        personMessageDaoDao = daoSession.personMessageDao
        adapter.addAllPerson(personMessageDaoDao.loadAll())
    }

    private fun txtAddMsg() {
        val editText = EditText(context)
        val dialog =
            AlertDialog.Builder(context!!).setTitle("添加")
                .setView(editText)
                .setNegativeButton("取消") { dialogInterface, i -> dialogInterface.dismiss() }
                .setPositiveButton("确定") { dialogInterface, i ->
                    var name = editText.text.toString()
                    if (TextUtils.isEmpty(name)){
                        Toast.makeText(context, "请输入人员名称", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    val list = personMessageDaoDao.queryBuilder()
                        .where(PersonMessageDao.Properties.Name.eq(name)).list()
                    if (list.size > 0){
                        Toast.makeText(context,"联系人已经存在了",Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }
                    val entity =
                        PersonMessage()
                    entity.name = name
                    personMessageDaoDao!!.insert(entity)
                    adapter.addPerson(entity)
                }
                .show()
    }
}