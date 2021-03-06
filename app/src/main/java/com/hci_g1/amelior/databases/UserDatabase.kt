package com.hci_g1.amelior

import android.content.Context
import androidx.room.*

import com.hci_g1.amelior.entities.Goal
import com.hci_g1.amelior.entities.Mood
import com.hci_g1.amelior.entities.Distance
import com.hci_g1.amelior.entities.StepCount
import com.hci_g1.amelior.entities.User

@Database
(
	entities = [
		Goal::class,
		Mood::class,
		Distance::class,
        StepCount::class,
		User::class,
	],
	version = 1
)

abstract class UserDatabase: RoomDatabase()
{
	abstract val goalDao: GoalDao
	abstract val moodDao: MoodDao
	abstract val distanceDao: DistanceDao
	abstract val stepCountDao: StepCountDao
	abstract val userDao: UserDao

	companion object
	{
		/* Writes to INSTANCE; whenever we change it, it's visible. */
		@Volatile
		private var INSTANCE: UserDatabase? = null

		fun getInstance(context: Context): UserDatabase
		{
			/* Make sure that whenever we executed this block of code, it only executes by a single thread. */
			synchronized(this)
			{
				/* When it's null, we will construct our database with room.databaseBuilder. */
				return INSTANCE?: Room.databaseBuilder(
					context.applicationContext,
					UserDatabase::class.java,
					"user_db"
				)
				/* Allow synchronous SQL queries. */
				.allowMainThreadQueries()
				/* Uses .also because we want to update our INSTANCE. */
				.build().also {
					INSTANCE = it
				}
			}
		}
	}
}
