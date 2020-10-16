package com.flowz.introtooralanguage.workmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.flowz.introtooralanguage.MainActivity
import com.flowz.introtooralanguage.R
import com.flowz.introtooralanguage.extensions.vectorToBitmap

class ReminderWorker(context:Context, params:WorkerParameters): Worker(context, params) {

    companion object{

        const val KEY_WORKER = "key_worker"
        const val CHANNEL_ID = "oraNumChannel"
        const val Notification_ID = 10
        const val name = "oraChannel"
        const val descriptionText = "myOraChannel"
    }

    override fun doWork(): Result {

        triggerNotification();

        return Result.success()
    }

    fun triggerNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){


            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val myChannel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }


            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(myChannel)
        }


        val intent = Intent(applicationContext, MainActivity::class.java).apply{

            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }


        val pendingIntent : PendingIntent = PendingIntent.getActivity(applicationContext, 1, intent, 0)

        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_notifications_none_black_24dp)


        val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_none_black_24dp)
            .setLargeIcon(bitmap).setSmallIcon(R.drawable.ic_mic_black_24dp)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(applicationContext.getString(R.string.notification_text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        with(NotificationManagerCompat.from(applicationContext)){

            notify(Notification_ID, builder.build())
        }







    }


}