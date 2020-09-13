package com.sem.lvl2_example

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sem.lvl2_example.databinding.ActivityMainBinding
import com.sem.lvl2_example.databinding.ItemReminderBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_reminder.view.*

class MainActivity : AppCompatActivity() {

    private val reminders = arrayListOf<Reminder>()
    private val reminderAdapter = ReminderAdapter(reminders)
    // Don't forget to create a binding object as you did in previous assignments.
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {

        binding.btnAddReminder.setOnClickListener {
            val reminder = binding.etReminder.text.toString()
            addReminder(reminder)
        }

        binding.rvReminders.layoutManager =
            LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL,
            false)
        binding.rvReminders.adapter = reminderAdapter

        binding.rvReminders.addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))

        createItemTouchHelper().attachToRecyclerView(rvReminders)

    }

    private fun addReminder(reminder: String) {
        if(reminder.isNotBlank()) {
            reminders.add(Reminder(reminder))
            reminderAdapter.notifyDataSetChanged()
            binding.etReminder.text?.clear()
        } else {
            Snackbar.make(etReminder, "You must fill in the input field!", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        // Callback which is used to create the ItemTouch helper. Only enables left swipe.
        // Use ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) to also enable right swipe.
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // Enables or Disables the ability to move items up and down.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            // Callback triggered when a user swiped an item.
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                reminders.removeAt(position)
                reminderAdapter.notifyDataSetChanged()
            }
        }
        return ItemTouchHelper(callback)
    }


}



data class Reminder (
    var reminderText: String
)

class ReminderAdapter(private val reminders: List<Reminder>) : RecyclerView.Adapter<ReminderAdapter.ViewHolder>(){


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ItemReminderBinding.bind(itemView)

        fun databind(reminder: Reminder) {
            binding.tvReminder.text = reminder.reminderText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_reminder, parent, false)
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return reminders.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.databind(reminders[position])
    }


}
