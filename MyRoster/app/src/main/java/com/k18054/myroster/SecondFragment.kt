package com.k18054.myroster

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.k18054.myroster.databinding.FragmentSecondBinding
import io.realm.Realm
import io.realm.kotlin.where
import java.util.*
import java.util.Calendar.*
import kotlin.collections.ArrayList

data class ListModel(val id:Long, val birthday: Calendar)

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        (activity as? MainActivity)?.setFabVisible(View.VISIBLE)
        binding.list.layoutManager = LinearLayoutManager(context)
        var dateTime = Calendar.getInstance().apply {
            timeInMillis = binding.calendarView.date
        }
        findBirthday(
                dateTime.get(Calendar.YEAR),
                dateTime.get(Calendar.MONTH),
                dateTime.get(Calendar.DAY_OF_MONTH),
        )
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            Log.d("calenderListener",year.toString().plus(",").plus(month).plus(",").plus(dayOfMonth))
            findBirthday(year, month, dayOfMonth)
        }
    }


    private fun findBirthday( year:Int, month: Int, dayOfMonth:Int ) {
        var selectDate = Calendar.getInstance().apply {
            clear()
            set(year,month,dayOfMonth)
        }
        val roster = realm.where<Roster>().findAll().sort("birthday")

        //????????????????????????
        val sortList: MutableList<ListModel> = ArrayList()
        //?????????????????????????????????
        val orderBirthday = roster.map { roster ->
            //Date???Calendar???????????????
            val calendar: Calendar = Calendar.getInstance().apply {
                time = roster.birthday
            }
            calendar.set(year,calendar.get(MONTH),calendar.get(DAY_OF_MONTH))
            if (calendar.before(selectDate)){
                calendar.set(year+1,calendar.get(MONTH),calendar.get(DAY_OF_MONTH))
            }
            sortList.add(ListModel(roster.id, calendar))
        }
        sortList.sortBy { it.birthday }
        sortList.map { sortList ->
            Log.d("map",sortList.id.toString().plus(sortList.birthday))
        }
        // ????????????id???????????????????????????sort???????????????????????????????????????????????????
        // Realm???????????????????????????????????????????????????List???add??????????????????????????????
        // ?????????????????????????????????2021????????????????????????????????????????????????????????????2021+1???????????????sort??????????????????????????????

        val adapter = RosterAdapter(roster)
        binding.list.adapter = adapter

        adapter.setOnItemClickListener { id ->
            id?.let {
                val action = SecondFragmentDirections.actionToRosterEditFragment(it)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}