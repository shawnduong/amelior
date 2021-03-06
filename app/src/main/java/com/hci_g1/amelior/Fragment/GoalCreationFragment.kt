package com.hci_g1.amelior

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

import com.hci_g1.amelior.entities.Goal
import com.hci_g1.amelior.entities.User

class GoalCreationFragment: Fragment()
{
	private var scale: Float = 0f
	private var screenHeight: Float = 0f
	private var userActionChoice: Int = -1

	private lateinit var goalDao: GoalDao
	private lateinit var userDao: UserDao

	private lateinit var buttonContinueButton: Button
	private lateinit var editTextAmountInputQuantity: EditText
	private lateinit var linearLayoutPromptActionContainer: LinearLayout
	private lateinit var linearLayoutPromptAmountContainer: LinearLayout
	private lateinit var numberPickerPickAction: NumberPicker
	private lateinit var spinnerAmountInputFrequency: Spinner
	private lateinit var spinnerAmountInputUnits: Spinner
	private lateinit var textViewAmountFrequency: TextView
	private lateinit var textViewGreeting: TextView
	private lateinit var textViewPromptAction: TextView
	private lateinit var textViewPromptGeneral: TextView

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View?
	{
		return inflater.inflate(R.layout.fragment_create, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?)
	{
		super.onViewCreated(view, savedInstanceState)

		val context = getContext()

		/* Initializing variables. */
		if (context != null)
		{
			goalDao = UserDatabase.getInstance(context).goalDao
			Log.d(TAG, "Goal database successfully loaded.")

			userDao = UserDatabase.getInstance(context).userDao
			Log.d(TAG, "User database successfully loaded.")
		}

		scale = resources.displayMetrics.density 

		/* Initialize widgets. */
		buttonContinueButton                = view.findViewById(R.id.continueButton)
		editTextAmountInputQuantity         = view.findViewById(R.id.amountInputQuantity)
		linearLayoutPromptActionContainer   = view.findViewById(R.id.promptActionContainer)
		linearLayoutPromptAmountContainer   = view.findViewById(R.id.promptAmountContainer)
		numberPickerPickAction              = view.findViewById(R.id.pickAction)
		spinnerAmountInputFrequency         = view.findViewById(R.id.amountInputFrequency)
		spinnerAmountInputUnits             = view.findViewById(R.id.amountInputUnits)
		textViewAmountFrequency             = view.findViewById(R.id.amountFrequency)
		textViewGreeting                    = view.findViewById(R.id.greeting)
		textViewPromptAction                = view.findViewById(R.id.promptAction)
		textViewPromptGeneral               = view.findViewById(R.id.promptGeneral)

		/* Set the action picker bounds. */
		// Index              0       1      2                       3
		val actions = arrayOf("walk", "run", "do something else...", "bike")
		numberPickerPickAction.minValue = 0
		numberPickerPickAction.maxValue = actions.size - 1
		numberPickerPickAction.displayedValues = actions

		/* UI logic. */

		/* Set the frequency. */
		if (context != null)
		{
			ArrayAdapter.createFromResource(
				context, R.array.frequencies, android.R.layout.simple_spinner_item
			)
			.also { adapter ->
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
				spinnerAmountInputFrequency.adapter = adapter
			}
		}

		/* Get the username from the database and set the greeting text. */
		val user = userDao.get_user_now("user")
		textViewGreeting.text = "Hello, ${user.name}!"

		/* Fade in the first text view and move it up. */
		ObjectAnimator.ofFloat(textViewGreeting, "alpha", 1.00f).apply {
			duration = 500  // milliseconds
			start()
		}
		ObjectAnimator.ofFloat(textViewGreeting, "translationY", 0.0f).apply {
			duration = 1000  // milliseconds
			start()
		}

		/* Fade in the general question prompt and move it up. */
		Handler().postDelayed(
			{
				ObjectAnimator.ofFloat(textViewPromptGeneral, "alpha", 1.00f).apply {
					duration = 500  // milliseconds
					start()
				}
				ObjectAnimator.ofFloat(textViewPromptGeneral, "translationY", 0.0f).apply {
					duration = 1000  // milliseconds
					start()
				}
			},
			1500
		)

		/* Fade in the action prompt. */
		Handler().postDelayed(
			{
				ObjectAnimator.ofFloat(linearLayoutPromptActionContainer, "alpha", 1.00f).apply {
					duration = 500  // milliseconds
					start()
				}
			},
			3000
		)

		/* Lambda for the action picker updates the selected value upon change. */
		numberPickerPickAction.setOnValueChangedListener { _, _, selection ->

			/* Make the next input UI elements appear, but only do it once or upon change. */
			if ((userActionChoice == -1) || (userActionChoice == 2))
			{
				ObjectAnimator.ofFloat(buttonContinueButton, "alpha", 0.00f).apply {
					duration = 500  // milliseconds
					start()
				}
				ObjectAnimator.ofFloat(editTextAmountInputQuantity, "alpha", 1.00f).apply {
					duration = 500  // milliseconds
					start()
				}
				ObjectAnimator.ofFloat(linearLayoutPromptAmountContainer, "alpha", 1.00f).apply {
					duration = 500  // milliseconds
					start()
				}

				Handler().postDelayed(
					{
						spinnerAmountInputUnits.visibility = View.VISIBLE
						ObjectAnimator.ofFloat(spinnerAmountInputUnits, "alpha", 1.00f).apply {
							duration = 500  // milliseconds
							start()
						}
					},
					250  // milliseconds
				)
				Handler().postDelayed(
					{
						ObjectAnimator.ofFloat(textViewAmountFrequency, "alpha", 1.00f).apply {
							duration = 500  // milliseconds
							start()
						}
					},
					500  // milliseconds
				)
				Handler().postDelayed(
					{
						spinnerAmountInputFrequency.visibility = View.VISIBLE
						ObjectAnimator.ofFloat(spinnerAmountInputFrequency, "alpha", 1.00f).apply {
							duration = 500  // milliseconds
							start()
						}
					},
					750  // milliseconds
				)
			}

			/* Save the new selection. */
			userActionChoice = selection
			Log.d(TAG, "User chose ${actions[userActionChoice]}.")

			/* If "do something else..." fade out the elements and show the "Create" button. */
			if (userActionChoice == 2)
			{
				ObjectAnimator.ofFloat(spinnerAmountInputUnits, "alpha", 0.00f).apply {
					duration = 500  // milliseconds
					start()
				}
				ObjectAnimator.ofFloat(spinnerAmountInputFrequency, "alpha", 0.00f).apply {
					duration = 500  // milliseconds
					start()
				}
				ObjectAnimator.ofFloat(editTextAmountInputQuantity, "alpha", 0.00f).apply {
					duration = 500  // milliseconds
					start()
				}
				ObjectAnimator.ofFloat(textViewAmountFrequency, "alpha", 0.00f).apply {
					duration = 500  // milliseconds
					start()
				}

				/* This prevents the bottom right UI triangle from showing. */
				Handler().postDelayed(
					{
						spinnerAmountInputUnits.visibility = View.INVISIBLE
						spinnerAmountInputFrequency.visibility = View.INVISIBLE
					},
					500
				)

				/* Make the continue button appear. */
				buttonContinueButton.text = "Continue"
				ObjectAnimator.ofFloat(buttonContinueButton, "alpha", 1.00f).apply {
					duration = 500  // milliseconds
					start()
				}
			}

			if (context != null)
			{
				var units: Int = 0

				/* "run" */
				if      (userActionChoice == 1)  units = R.array.units_array_run

				/* "bike" */
				else if (userActionChoice == 3)  units = R.array.units_array_bike

				/* Default is walk. */
				else                             units = R.array.units_array_walk

				/* Set the spinner units based on the selection. */
				ArrayAdapter.createFromResource(
					context, units, android.R.layout.simple_spinner_item
				)
				.also { adapter ->
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
					spinnerAmountInputUnits.adapter = adapter
				}
			}
		}

		/* Upon finishing typing in the quantity, clear the focus and show the continue button. */
		editTextAmountInputQuantity.setOnKeyListener(

			View.OnKeyListener { _, key, event ->

				if (key == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP)
				{
					/* Clear the focus on the form item. */
					editTextAmountInputQuantity.clearFocus()

					/* Make the continue button appear. */
					buttonContinueButton.text = "Create"
					ObjectAnimator.ofFloat(buttonContinueButton, "alpha", 1.00f).apply {
						duration = 500  // milliseconds
						start()
					}
				}

				false
			}
		)

		/* Upon pressing the continue button, save the data and go home. */
		buttonContinueButton.setOnClickListener {

			/* The "do something else..." option goes to a custom creation page. */
			if (userActionChoice == 2)
			{
				Log.d(TAG, "Moving to custom goal creation page.")
				view.context.startActivity(Intent(view.context, GoalScreenCustomCreation::class.java))
			}
			/* Save the data and go home. */
			else
			{
				val action: String = actions[userActionChoice]
				val quantity: Int = editTextAmountInputQuantity.text.toString().toInt()
				val units: String = spinnerAmountInputUnits.getItemAtPosition(
					spinnerAmountInputUnits.getSelectedItemPosition()
				).toString()
				val frequency: String = spinnerAmountInputFrequency.getItemAtPosition(
					spinnerAmountInputFrequency.getSelectedItemPosition()
				).toString()

				goalDao.insert_goal_now(
					Goal(
						goalDao.size(),  // key
						false,           // custom
						action,          // action
						quantity,        // quantity
						units,           // units
						frequency,       // frequency
						3,               // level
						0,               // local progress
						-1,              // last completed
						0,0,0,0,0,0,0    // last 7 days history
					)
				)

				/* Test that it was inserted correctly. */
				val goal: Goal = goalDao.get_goal_now(goalDao.size()-1)
				Log.d(TAG, "User created goal ${goal.key} ${goal.action} ${goal.quantity} ${goal.units} ${goal.frequency}")

				/* Go home. */
				view.findNavController().navigate(R.id.goHomeAction)
			}
		}
	}

	companion object
	{
		private val TAG = "GoalCreationFragment"
	}
}
