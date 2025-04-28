package com.basaran.casestudy.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "suppliers")
data class Supplier(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String = "",
    val name: String = "",
    val contactPerson: String = "",
    val phone: String = "",
    val email: String = "",
    val address: String = ""
) : Parcelable