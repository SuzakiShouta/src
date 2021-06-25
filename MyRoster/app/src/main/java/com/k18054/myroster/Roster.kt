package com.k18054.myroster

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Roster : RealmObject() {
    @PrimaryKey
    var id : Long = 0
    var firstName : String = ""
    var lastName : String = ""
    var birthday : Date = Date()
    var postalCode : Int = 0
    var address : String = ""
}