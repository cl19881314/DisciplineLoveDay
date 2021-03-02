package com.chen.discipline.love.day.ui.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.chen.discipline.love.day.MyApplication
import com.chen.discipline.love.day.R
import com.chen.discipline.love.day.greendao.PersonLoveDay
import com.chen.discipline.love.day.greendao.PersonLoveDayDao
import com.chen.discipline.love.day.greendao.PersonMessage
import com.chen.discipline.love.day.greendao.PersonMessageDao
import com.chen.discipline.love.day.utils.CalculationDaysUtil
import kotlinx.android.synthetic.main.item_contacts_layout.view.*
import kotlinx.android.synthetic.main.item_edit_love_day_layout.view.*
import kotlinx.android.synthetic.main.item_love_day_layout.view.*

/**
 * @author Chenhong
 * @date 2020/3/13.
 * @des
 */
class RemindAdapter : RecyclerView.Adapter<RemindAdapter.ContactsHolder>() {
    private var mDataList = ArrayList<PersonMessage>()

    class ContactsHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contacts_layout, parent, false)
        return ContactsHolder(view)
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    override fun onBindViewHolder(holder: ContactsHolder, position: Int) {
        val name = mDataList[position].name
        holder.itemView.txtName.text = name
        holder.itemView.txtAddContacts.visibility = View.GONE

        val personMsgDao = MyApplication.getInstance().daoSession.personMessageDao.queryBuilder()
            .where(PersonMessageDao.Properties.Name.eq(name)).list()
//        holder.itemView.txtName.setOnClickListener {
//            changePersonName(holder.itemView.context, name)
//        }
//        holder.itemView.txtAddContacts.setOnClickListener {
//            addLoveDay(holder.itemView.context, personMsgDao[0].id)
//        }
//        holder.itemView.txtName.setOnLongClickListener {
//            deletePersonName(holder.itemView.context, personMsgDao[0].id)
//            true
//        }
        val loveDayDaoDao = MyApplication.getInstance().daoSession.personLoveDayDao
        val res = loveDayDaoDao.queryBuilder()
            .where(PersonLoveDayDao.Properties.PersonId.eq(personMsgDao[0].id))
            .list()
        holder.itemView.showDayLayout.removeAllViews()
        for (day in res) {
            var loveDayView = LayoutInflater.from(holder.itemView.context)
                .inflate(R.layout.item_love_day_layout, null, false)
            holder.itemView.showDayLayout.addView(loveDayView)
            loveDayView.txtDayName.text = "${day.dayName}"
            loveDayView.txtDay.text = "${day.day}日"
            //0农历 1公历
            loveDayView.checkBoxShow.isChecked = day.dayType == "0"
//            loveDayView.dayLayout.setOnLongClickListener {
//                showDeleteDialog(holder.itemView.context, day, loveDayDaoDao)
//                true
//            }

            //提示信息
            val split = day.day.split("月")
            var index = if (day.dayType == "0") {
                CalculationDaysUtil.getLunarCalculationDays(split[0].toInt(), split[1].toInt())
            } else {
                CalculationDaysUtil.getSolarCalculationDays(split[0].toInt(), split[1].toInt())
            }
            loveDayView.txtAlertView.text = "距离今天还有${index}天"
            if (index >= 50){
                loveDayView.txtAlertView.setBackgroundResource(android.R.color.white)
            } else if (index >= 40) {
                loveDayView.txtAlertView.setBackgroundResource(R.color.color2)
            } else if (index >= 10){
                loveDayView.txtAlertView.setBackgroundResource(R.color.color3)
            } else {
                loveDayView.txtAlertView.setBackgroundResource(R.color.color4)
            }
        }
    }

    private fun deletePersonName(context: Context, id: Long) {
        AlertDialog.Builder(context).setTitle("提示")
            .setMessage("是否要删除这条信息")
            .setNegativeButton("取消") { dialogInterface, i -> dialogInterface.dismiss() }
            .setPositiveButton("确定") { dialogInterface, i ->
                MyApplication.getInstance().daoSession.personMessageDao.deleteByKey(id)
                MyApplication.getInstance().daoSession.personLoveDayDao.queryBuilder()
                    .where(PersonLoveDayDao.Properties.PersonId.eq(id))
                    .buildDelete().executeDeleteWithoutDetachingEntities()
                addAllPerson(MyApplication.getInstance().daoSession.personMessageDao.loadAll())
            }
            .show()
    }

    private fun showDeleteDialog(context: Context, day: PersonLoveDay, dao: PersonLoveDayDao) {
        AlertDialog.Builder(context).setTitle("提示")
            .setMessage("是否要删除这条信息")
            .setNegativeButton("取消") { dialogInterface, i -> dialogInterface.dismiss() }
            .setPositiveButton("确定") { dialogInterface, i ->
                dao.delete(day)
                notifyDataSetChanged()
            }
            .show()
    }

    private fun changePersonName(context: Context, name: String) {
        var nameEditText = EditText(context)
        nameEditText.setText(name)
        AlertDialog.Builder(context).setTitle("添加")
            .setView(nameEditText)
            .setNegativeButton("取消") { dialogInterface, i -> dialogInterface.dismiss() }
            .setPositiveButton("确定") { dialogInterface, i ->
                var newName = nameEditText.text.toString()
                if (TextUtils.isEmpty(newName)) {
                    Toast.makeText(context, "请输入人员名称", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                var personMessageDao = MyApplication.getInstance().daoSession.personMessageDao
                val personName =
                    personMessageDao.queryBuilder().where(PersonMessageDao.Properties.Name.eq(name)).list()[0]
                personName.name = newName
                personMessageDao.update(personName)
                notifyDataSetChanged()
            }
            .show()
    }

    private fun addLoveDay(context: Context, personId: Long) {
        var loveView =
            LayoutInflater.from(context).inflate(R.layout.item_edit_love_day_layout, null, false)
        AlertDialog.Builder(context).setTitle("添加日期")
            .setView(loveView)
            .setNegativeButton("取消") { dialogInterface, i -> dialogInterface.dismiss() }
            .setPositiveButton("确定") { dialogInterface, i ->
                var month = loveView.edtMonthDay.text.toString()
                var day = loveView.edtMonthDayDay.text.toString()
                if (TextUtils.isEmpty(month) || TextUtils.isEmpty(day)) {
                    Toast.makeText(context, "请输入日期", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                var loveDay = PersonLoveDay()
                loveDay.personId = personId
                loveDay.dayName = loveView.edtDayName.text.toString()
                loveDay.day = "${month}月${day}"
                //0农历 1公历
                loveDay.dayType = if (loveView.checkbox.isChecked) "0" else "1"
                var dao = MyApplication.getInstance().daoSession.personLoveDayDao
                dao.insert(loveDay)
                notifyDataSetChanged()
            }
            .show()
    }

    fun addPerson(person: PersonMessage) {
        mDataList.add(person)
        notifyDataSetChanged()
    }

    fun addAllPerson(personList: List<PersonMessage>) {
        mDataList.clear()
        mDataList.addAll(personList)
        notifyDataSetChanged()
    }
}