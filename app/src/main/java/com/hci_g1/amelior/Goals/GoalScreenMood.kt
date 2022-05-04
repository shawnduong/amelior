package com.hci_g1.amelior

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.*
import com.github.mikephil.charting.charts.*
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.*
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.components.Legend.*
import com.github.mikephil.charting.components.XAxis.*

import com.hci_g1.amelior.entities.Goal
import com.hci_g1.amelior.entities.Mood

class GoalScreenMood: AppCompatActivity()
{
	private var today: Long = 0

	private lateinit var goalDao: GoalDao
	private lateinit var moodDao: MoodDao

	private lateinit var buttonClose: Button
	private lateinit var textViewSuggestion: TextView

	override fun onCreate(savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.goal_screen_mood)

		/* Initialize the mood database DAO. */
		goalDao = UserDatabase.getInstance(this).goalDao
		moodDao = UserDatabase.getInstance(this).moodDao

		/* Initialize variables. */
		today = get_epoch_day()

		/* Initialize widgets. */
		buttonClose         = findViewById(R.id.close)
		textViewSuggestion  = findViewById(R.id.suggestion)

		/* Change the suggestion text based on any bad goals the user might have. */
		for (i in 0..(goalDao.size()-1))
		{
			val g: Goal = goalDao.get_goal_now(i)

			/* Suggest a goal if it's less than or equal to level 3. */
			if (g.level <= 3)
			{
				if (g.quantity == -1)
				{
					textViewSuggestion.text = "Goal: ${g.action.capitalize()} every ${g.frequency}"
				}
				else
				{
					textViewSuggestion.text = "Goal: ${g.action.capitalize()} ${g.quantity} ${g.units}/${g.frequency}"
				}

				break
			}
		}

		/* UI logic. */

		/* Upon clicking the "GO BACK" button, go back to the main page. */
		buttonClose.setOnClickListener {
			Log.d(TAG, "Moving to Dashboard.")
			startActivity(Intent(this, Dashboard::class.java))
			finish()
		}

		/* Find the chart in the XML and get the axes. */
		val graph: LineChart = findViewById(R.id.lineChart)
		val axisY = graph.getAxisLeft()
		val axisX = graph.getXAxis()

		val days = arrayOf(
			"S", "M", "T", "W", "R", "F", "S",
			"S", "M", "T", "W", "R", "F", "S",
		)

		/* Set the X axis labels to the days of the week. */
		axisX.setValueFormatter(

			object: ValueFormatter()
			{
				override fun getAxisLabel(value: Float, axis: AxisBase): String
				{
					/* I'm not proud of this but we need to get the MVP. Epoch
					 * day 19106 was a Sunday and is used as a reference. This
					 * technique should never be used, ever.
					 */
					return days[((get_epoch_day().toInt()-19106) % 7) + value.toInt()+7]
				}
			}
		)

		/* Set the Y axis labels to the moods. */
		axisY.setValueFormatter(

			object: ValueFormatter()
			{
				override fun getAxisLabel(value: Float, axis: AxisBase): String
				{
					if      (value <=  0)  return ""
					else if (value <= 20)  return "Not too great!"
					else if (value <= 40)  return "A little down"
					else if (value <= 60)  return "Alright"
					else if (value <= 80)  return "Good"
					else                   return "Great!"
				}
			}
		)

		/* Entries populated by iterating through the past week of data. */
		val entries: MutableList<Entry> = ArrayList()

		/* Both of these are needed to calculate averages. */
		val totals = arrayOf<Float>(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)
		val samples = arrayOf<Float>(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f)

		for (i in moodDao.size()-1 downTo 0)
		{
			val mood: Mood = moodDao.get_mood_now(i)
			val day: Long = get_epoch_day(mood.timestamp)

			/* Don't bother going back further in the database if we're already
			   out of the bounds of a week. */
			if (today - day > 7)  break

			val i = (today.toInt() - day.toInt()+6) % 6
			totals.set(i, totals.get(i) + mood.value.toFloat())
			samples.set(i, samples.get(i)+1)
		}

		for (day in (today-6)..(today))
		{
			val i = (today-day).toInt()

			if (samples[i] != 0.0f)
				entries.add(Entry(i.toFloat() * -1.0f, totals[i]/samples[i]))
			else
				entries.add(Entry(i.toFloat() * -1.0f, 0.0f))
		}

		/* Disable the right Y axis. */
		graph.getAxisRight().setEnabled(false)

		/* The Y axis must start at 0 and end at 100. */
		axisY.setAxisMinValue(0.0f)
		axisY.setAxisMaxValue(100.0f)

		/* There is a day of extra padding in both directions for aesthetics. */
		axisX.setAxisMinValue(-6.0f)
		axisX.setAxisMaxValue( 1.0f)

		/* Set the description text. */
		graph.getDescription().setText("")

		/* Position the X axis on the bottom. */
		axisX.setPosition(XAxisPosition.BOTTOM)

		/* Disable the legend */
		graph.getLegend().setEnabled(false)

		/* Create the LineDataSet object used by the chart. */
		val dataset = LineDataSet(entries, "mood")

		/* Make the line black with size 2.0f circles. */
		dataset.setColor(Color.BLACK)
		dataset.setCircleColor(Color.BLACK)
		dataset.setCircleRadius(2.0f)
		dataset.setDrawCircleHole(false)

		/* Make the graph filled. */
		dataset.setFillDrawable(getResources().getDrawable(R.drawable.teal_blue_lighter_bg))
		dataset.setDrawFilled(true)

		/* Plot the data on the graph. */
		graph.setData(LineData(dataset))
	}

	/* Get the current epoch day. */
	private fun get_epoch_day(): Long
	{
		return System.currentTimeMillis()/(1000*60*60*24)
	}

	/* Get the current epoch day given an epoch time (ms). */
	private fun get_epoch_day(time: Long): Long
	{
		return time/(1000*60*60*24)
	}

	companion object
	{
		private val TAG = "GoalScreenMood"
	}
}
