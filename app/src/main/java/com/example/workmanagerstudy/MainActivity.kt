package com.example.workmanagerstudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.*
import com.example.workmanagerstudy.Notification.NotificationWork
import com.example.workmanagerstudy.Notification.NotificationWork.Companion.NOTIFICATION_ID
import com.example.workmanagerstudy.Notification.NotificationWork.Companion.NOTIFICATION_WORK
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userInfo()
    }

    private fun userInfo() {

        //setSupportActionBar(toolbar)
        done_fab?.setOnClickListener {
            val customCalendar = Calendar.getInstance()
            customCalendar.set(
                date_p.year, date_p.month, date_p.dayOfMonth, time_p.hour, time_p.minute, 0
            )
            val customTime = customCalendar.timeInMillis
            val currentTime = System.currentTimeMillis()
            if (customTime > currentTime) {
                val data = Data.Builder().putInt(NOTIFICATION_ID, 0).build()
                val delay = customTime - currentTime
                scheduleNotification(delay, data)

                val titleNotificationSchedule = getString(R.string.notification_schedule_title)
                val patternNotificationSchedule = getString(R.string.notification_schedule_pattern)
                Snackbar.make(
                    coordinator_l,
                    titleNotificationSchedule + SimpleDateFormat(
                        patternNotificationSchedule, Locale.getDefault()
                    ).format(customCalendar.time).toString(),
                    Snackbar.LENGTH_LONG
                ).show()
            } else {
                val errorNotificationSchedule = getString(R.string.notification_schedule_error)
                Snackbar.make(coordinator_l, errorNotificationSchedule, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun scheduleNotification(delay: Long, data: Data) {
        val constraints = Constraints.Builder().setRequiresCharging(false).build()

//        val notificationWork = OneTimeWorkRequest.Builder(NotificationWork::class.java)
//            .setInitialDelay(delay,TimeUnit.MILLISECONDS).setConstraints(constraints).setInputData(data).build()
        //        val workManager = WorkManager.getInstance(this)
//        workManager.beginUniqueWork(NOTIFICATION_WORK,ExistingWorkPolicy.REPLACE,notificationWork).enqueue()

            val notificationWork = PeriodicWorkRequest.Builder(NotificationWork::class.java,15,TimeUnit.MINUTES)
            .setInitialDelay(delay,TimeUnit.MILLISECONDS).setConstraints(constraints).build()
        val workManager = WorkManager.getInstance(this)
        workManager.enqueue(notificationWork)

    }


}