package com.k18054.myroster

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.k18054.myroster.databinding.FragmentFirstBinding
import io.realm.Realm
import io.realm.kotlin.where

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
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
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController()
                    .navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.list.layoutManager = LinearLayoutManager(context)
        val rosters = realm.where<Roster>().findAll()
        val adapter = RosterAdapter(rosters)
        binding.list.adapter = adapter

        adapter.setOnItemClickListener { id ->
            id?.let {
                val action = FirstFragmentDirections.actionToRosterEditFragment(it)
                findNavController().navigate(action)
            }
        }
        //'androidx.appcompat:appcompat:1.3.0'ではエラーが出る。
        (activity as? MainActivity)?.setFabVisible(View.VISIBLE)
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