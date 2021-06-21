package com.hera.noteapp2.data.inner

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Parcelize
@Entity(tableName = "note_table")
data class Note(
        var title: String,

        var content: String,

        @ColumnInfo(name = "priority_level")
        var priorityLevel: Int,

        val date: Long = System.currentTimeMillis(),

        @PrimaryKey(autoGenerate = true)
        val id: Long = 0
) : Parcelable {

        val dateFormatted: String get() = DateFormat.getDateTimeInstance().format(date)
}
