package com.hci_g1.amelior

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.hci_g1.amelior.entities.Distance
import com.hci_g1.amelior.entities.Mood
import com.hci_g1.amelior.entities.StepCount
import com.hci_g1.amelior.entities.User
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*

class SettingFragment: Fragment() {

    private val displayMetrics = DisplayMetrics()
    private var screenHeight: Float = 0f

    private lateinit var userDao: UserDao
    private lateinit var moodDao: MoodDao
    private lateinit var stepCountDao: StepCountDao
    private lateinit var distanceDao: DistanceDao

    private lateinit var buttonSplashSubmit: Button
    private lateinit var buttonSplashShowInfo: Button

	/* Debugging buttons. */
	private lateinit var buttonSampleA: Button
	private lateinit var buttonSampleB: Button
	private lateinit var buttonSampleC: Button
	private lateinit var buttonSampleD: Button

	/* Debugging other DAOs. */
//	private lateinit var userDao: UserDao
//	private lateinit var moodDao: MoodDao

    private lateinit var textViewInfo: TextView
    private lateinit var editTextNameInputField: TextView
    private lateinit var textViewName: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val context = getContext()

        /* Initializing variables. */
        if (context != null)
        {
            userDao = UserDatabase.getInstance(context).userDao
            // moodDao = UserDatabase.getInstance(context).moodDao
            Log.d(SettingFragment.TAG, "Database successfully loaded.")
        }

        /* Get the screen metrics. */
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels.toFloat()

        /* Initializing widgets. */
        textViewName                    = view.findViewById(R.id.textViewName)
        editTextNameInputField          = view.findViewById(R.id.editTextNameInputField)
        buttonSplashSubmit              = view.findViewById(R.id.splashSubmit)
        buttonSplashShowInfo            = view.findViewById(R.id.splashInfo)
        textViewInfo                    = view.findViewById(R.id.textViewInfo)

		/* Debugging widgets. */
        buttonSampleA = view.findViewById(R.id.sampleA)
        buttonSampleB = view.findViewById(R.id.sampleB)
        buttonSampleC = view.findViewById(R.id.sampleC)
        buttonSampleD = view.findViewById(R.id.sampleD)

        val user = userDao.get_user_now("user")
        textViewName.text = "${user.name}"

        buttonSplashSubmit.setOnClickListener {
            val name: String = editTextNameInputField.text.toString()
            userDao.insert_user_now(User("user", name))
            textViewName.text = "${name}"
            Log.d(TAG, "Change Name")
        }


        buttonSplashShowInfo.setOnClickListener {
            if (textViewInfo.visibility == View.VISIBLE)
            {
                textViewInfo.visibility = View.INVISIBLE
            }
            else
            {
                textViewInfo.visibility = View.VISIBLE
                Log.d(TAG, "Show Info")
            }
        }

		/* Debugging buttons. */
        /* Mood */
		buttonSampleA.setOnClickListener {

			val context = getContext()

			/* Initializing variables. */
			if (context != null)
			{
				userDao = UserDatabase.getInstance(context).userDao
				moodDao = UserDatabase.getInstance(context).moodDao
			}

			Log.d(TAG, "Button A")

            /* 4/22/2022 */
            moodDao.insert_mood_now(Mood(0, 1650655800000, 10))
            /* 4/23/2022 */
            moodDao.insert_mood_now(Mood(1, 1650742200000, 10))
            /* 4/24/2022 */
            moodDao.insert_mood_now(Mood(2, 1650828600000, 20))
            /* 4/25/2022 */
            moodDao.insert_mood_now(Mood(3, 1650915000000, 40))
            /* 4/26/2022 */
            moodDao.insert_mood_now(Mood(4, 1651001400000, 50))
            /* 4/27/2022 */
            moodDao.insert_mood_now(Mood(5, 1651087800000, 50))
            /* 4/28/2022 */
            moodDao.insert_mood_now(Mood(6, 1651174200000, 70))
            /* 4/29/2022 */
            moodDao.insert_mood_now(Mood(7, 1651260600000, 90))
        }

        /* StepCount */
        buttonSampleB.setOnClickListener {

            val context = getContext()

            /* Initializing variables. */
            if (context != null)
            {
                userDao = UserDatabase.getInstance(context).userDao
                moodDao = UserDatabase.getInstance(context).moodDao
                stepCountDao = UserDatabase.getInstance(context).stepCountDao
            }

            Log.d(TAG, "Button B")
            /* 4/21/2022 */
            stepCountDao.insert_step_count_now(StepCount(19103, 100f))
            /* 4/22/2022 */
            stepCountDao.insert_step_count_now(StepCount(19104, 100f))
            /* 4/23/2022 */
            stepCountDao.insert_step_count_now(StepCount(19105, 150f))
            /* 4/24/2022 */
            stepCountDao.insert_step_count_now(StepCount(19106, 200f))
            /* 4/25/2022 */
            stepCountDao.insert_step_count_now(StepCount(19107, 250f))
            /* 4/26/2022 */
            stepCountDao.insert_step_count_now(StepCount(19108, 300f))
            /* 4/27/2022 */
            stepCountDao.insert_step_count_now(StepCount(19109, 350f))
            /* 4/28/2022 */
            stepCountDao.insert_step_count_now(StepCount(19110, 400f))
            /* 4/29/2022 */
            stepCountDao.insert_step_count_now(StepCount(19111, 450f))

            /* 4/22/2022 */
            moodDao.insert_mood_now(Mood(0, 1650655800000, 30))
            /* 4/23/2022 */
            moodDao.insert_mood_now(Mood(1, 1650742200000, 20))
            /* 4/24/2022 */
            moodDao.insert_mood_now(Mood(2, 1650828600000, 30))
            /* 4/25/2022 */
            moodDao.insert_mood_now(Mood(3, 1650915000000, 50))
            /* 4/26/2022 */
            moodDao.insert_mood_now(Mood(4, 1651001400000, 60))
            /* 4/27/2022 */
            moodDao.insert_mood_now(Mood(5, 1651087800000, 80))
            /* 4/28/2022 */
            moodDao.insert_mood_now(Mood(6, 1651174200000, 70))
            /* 4/29/2022 */
            moodDao.insert_mood_now(Mood(7, 1651260600000, 90))

        }


        /* Distance */
        buttonSampleC.setOnClickListener {

            val context = getContext()

            /* Initializing variables. */
            if (context != null)
            {
                userDao = UserDatabase.getInstance(context).userDao
                moodDao = UserDatabase.getInstance(context).moodDao
                distanceDao = UserDatabase.getInstance(context).distanceDao
            }

            Log.d(TAG, "Button C")
            /*
            distanceDao.insert_distance_now(Distance(1650655800000, 100f))
            /* 4/23/2022 */
            distanceDao.insert_distance_now(Distance(1650742200000, 150f))
            /* 4/24/2022 */
            distanceDao.insert_distance_now(Distance(1650828600000, 200f))
            /* 4/25/2022 */
            distanceDao.insert_distance_now(Distance(1650915000000, 250f))
            /* 4/26/2022 */
            distanceDao.insert_distance_now(Distance(1651001400000, 300f))
            /* 4/27/2022 */
            distanceDao.insert_distance_now(Distance(1651087800000, 350f))
            /* 4/28/2022 */
            distanceDao.insert_distance_now(Distance(1651174200000, 400f))
            /* 4/29/2022 */
            distanceDao.insert_distance_now(Distance(1651260600000, 450f))
            */

            /* 4/22/2022 */
            moodDao.insert_mood_now(Mood(0, 1650655800000, 20))
            /* 4/23/2022 */
            moodDao.insert_mood_now(Mood(1, 1650742200000, 10))
            /* 4/24/2022 */
            moodDao.insert_mood_now(Mood(2, 1650828600000, 30))
            /* 4/25/2022 */
            moodDao.insert_mood_now(Mood(3, 1650915000000, 50))
            /* 4/26/2022 */
            moodDao.insert_mood_now(Mood(4, 1651001400000, 60))
            /* 4/27/2022 */
            moodDao.insert_mood_now(Mood(5, 1651087800000, 80))
            /* 4/28/2022 */
            moodDao.insert_mood_now(Mood(6, 1651174200000, 70))
            /* 4/29/2022 */
            moodDao.insert_mood_now(Mood(7, 1651260600000, 90))

        }

        /* ? */
        buttonSampleD.setOnClickListener {

            val context = getContext()

            /* Initializing variables. */
            if (context != null)
            {
                userDao = UserDatabase.getInstance(context).userDao
                moodDao = UserDatabase.getInstance(context).moodDao
            }

            Log.d(TAG, "Button D")
            /*
            distanceDao.insert_distance_now(Distance(1650655800000, 100f))
            /* 4/23/2022 */
            distanceDao.insert_distance_now(Distance(1650742200000, 150f))
            /* 4/24/2022 */
            distanceDao.insert_distance_now(Distance(1650828600000, 200f))
            /* 4/25/2022 */
            distanceDao.insert_distance_now(Distance(1650915000000, 250f))
            /* 4/26/2022 */
            distanceDao.insert_distance_now(Distance(1651001400000, 300f))
            /* 4/27/2022 */
            distanceDao.insert_distance_now(Distance(1651087800000, 350f))
            /* 4/28/2022 */
            distanceDao.insert_distance_now(Distance(1651174200000, 400f))
            /* 4/29/2022 */
            distanceDao.insert_distance_now(Distance(1651260600000, 450f))
            */

            /* 4/22/2022 */
            moodDao.insert_mood_now(Mood(0, 1650655800000, 10))
            /* 4/23/2022 */
            moodDao.insert_mood_now(Mood(1, 1650742200000, 10))
            /* 4/24/2022 */
            moodDao.insert_mood_now(Mood(2, 1650828600000, 40))
            /* 4/25/2022 */
            moodDao.insert_mood_now(Mood(3, 1650915000000, 50))
            /* 4/26/2022 */
            moodDao.insert_mood_now(Mood(4, 1651001400000, 70))
            /* 4/27/2022 */
            moodDao.insert_mood_now(Mood(5, 1651087800000, 80))
            /* 4/28/2022 */
            moodDao.insert_mood_now(Mood(6, 1651174200000, 90))
            /* 4/29/2022 */
            moodDao.insert_mood_now(Mood(7, 1651260600000, 90))

        }

    }

    companion object
    {
        private val TAG = "SettingFragment"
    }
}

/*
class SettingFragment : Fragment(R.layout.fragment_setting) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val settingView = inflater.inflate(R.layout.fragment_setting, container, false)
        return settingView
    }

}
 */


/* STORY: This person is on top of their goals that they've set and
has been really motivated. */

/* STORY: This person is very inconsistent with their goals
that they've set. They have been slacking off recently and
haven't been reaching their goals lately. */

/* STORY: This person is very good at satisfying their goals set for
walking certain amount of steps, but it seems like they've been slacking off
on other goals. */

/* STORY: This person sets their goals, but never bothered to even try
completing their goals.
*/
