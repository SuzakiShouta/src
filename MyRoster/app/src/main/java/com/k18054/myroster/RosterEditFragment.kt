package com.k18054.myroster

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.k18054.myroster.databinding.FragmentRosterEditBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RosterEditFragment : Fragment() {

    private var _binding: FragmentRosterEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var realm: Realm

    private val args: RosterEditFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRosterEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (args.rosterId == -1L) {
            binding.delete.visibility = View.INVISIBLE
        } else {
            binding.delete.visibility = View.VISIBLE

            val roster = realm.where<Roster>().equalTo("id", args.rosterId).findFirst()
            // DateFormat.format() だと　インポートに反応してくれなかった。
            binding.birthdayEdit.setText(android.text.format.DateFormat.format("yyyy/MM/dd",roster?.birthday))
            binding.firstNameEdit.setText(roster?.firstName)
            binding.lastNameEdit.setText(roster?.lastName)
            binding.postalCodeEdit.setText(roster?.postalCode.toString())
            binding.addressEdit.setText(roster?.address)
        }
        //val aaa: MainActivity = activity as MainActivityと同じ
        (activity as? MainActivity)?.setFabVisible(View.INVISIBLE)
        println((activity as? MainActivity)?.setFabVisible(View.INVISIBLE))
        binding.save.setOnClickListener { saveRoster(it) }
        binding.delete.setOnClickListener { deleteRoster(it) }
        binding.postalCode.setOnClickListener { setAddress(it) }
    }

    private fun saveRoster (view: View) {
        when (args.rosterId) {
            // 新規登録
            -1L -> {
                realm.executeTransaction { db: Realm ->
                    val maxId = db.where<Roster>().max("id")
                    val nextId = (maxId?.toLong() ?: 0L) + 1L
                    val roster = db.createObject<Roster>(nextId)
                    val birthday = "${binding.birthdayEdit.text}".toDate()
                    if (birthday != null) roster.birthday = birthday
                    roster.firstName = binding.firstNameEdit.text.toString()
                    roster.lastName = binding.lastNameEdit.text.toString()
                    roster.postalCode = "${binding.postalCodeEdit.text}".toInt()
                    roster.address = binding.addressEdit.text.toString()
                }
                makeSnackbar(view, "登録しました")
            }
            // 更新
            else -> {
                realm.executeTransaction { db: Realm ->
                    val roster = db.where<Roster>()
                        .equalTo("id", args.rosterId).findFirst()
                    val birthday = ("${binding.birthdayEdit.text}").toDate()
                    if (birthday != null) roster?.birthday = birthday
                    roster?.firstName = binding.firstNameEdit.text.toString()
                    roster?.lastName = binding.lastNameEdit.text.toString()
                    roster?.postalCode = "${binding.postalCodeEdit.text}".toInt()
                    roster?.address = binding.addressEdit.text.toString()
                }
                makeSnackbar(view,"更新しました")
            }
        }
    }

    private fun deleteRoster (view: View) {
        realm.executeTransaction { db: Realm ->
            db.where<Roster>().equalTo("id", args.rosterId)
                ?.findFirst()
                ?.deleteFromRealm()
        }
        makeSnackbar(view, "削除しました")
    }

    private fun setAddress (view: View) {
        val Api = ZipcloudApi()
        Api.getAddress("${binding.postalCodeEdit.text}".toInt())
        Api.setTextApi { address ->
            address?.let {
                Log.d("MainActivity",address.toString())
                binding.addressEdit.setText(address)
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

    private fun makeSnackbar (view: View, text: String) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
            .setAction("戻る") { findNavController().popBackStack() }
            .setActionTextColor(Color.YELLOW)
            .show()
    }

    private fun String.toDate(pattern: String = "yyyy/MM/dd"): Date? {
        return try {
            SimpleDateFormat(pattern).parse(this)
        } catch (e: IllegalArgumentException) {
            return null
        } catch (e: ParseException) {
            return null
        }
    }
}