package com.example.mycalendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.room.*


@Entity
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "day") val day: Int,
    @ColumnInfo(name = "month") val month: Int,
    @ColumnInfo(name = "year") val year: Int){
    constructor(title: String, description: String, day: Int, month: Int, year: Int) : this(0, title, description, day, month, year)
}

@Dao
interface EventDao {
    @Query("SELECT * FROM event")
    fun getAll(): LiveData<List<Event>>

    @Query("SELECT * FROM event WHERE day = :day AND month = :month AND year = :year")
    fun getAllByDate(day: Int, month: Int, year: Int): LiveData<List<Event>>

    @Insert
    fun insertAll(vararg events: Event)

    @Delete
    fun delete(event: Event)
}

@Database(entities = [Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao
    private var INSTANCE: AppDatabase? = null

    companion object{
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java
                    , "calendar.db"
                ).allowMainThreadQueries().build()
            }
            return INSTANCE as AppDatabase
        }
    }
}

class EventAdapter(private val dataSet: List<Event>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val title: TextView
        val description: TextView
        val deleteButton: Button

        init {
            title = view.findViewById(R.id.event_view_title)
            description = view.findViewById(R.id.event_view_description)
            deleteButton = view.findViewById(R.id.deleteButton)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_recycler_view_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = dataSet[position].title
        holder.description.text = dataSet[position].description
        holder.deleteButton.setOnClickListener {
            val db = AppDatabase.getDatabase(holder.view.context)
            db.eventDao().delete(dataSet[position])
        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}