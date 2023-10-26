package ru.fabit.rustore

import android.app.Activity
import android.content.Intent
import android.net.Uri
import ru.fabit.appupdate.core.ApplicationUpdateManager
import ru.fabit.appupdate.core.CheckForUpdateCallBack
import ru.fabit.appupdate.core.UpdateStatus

abstract class RuStoreApplicationUpdateManager : ApplicationUpdateManager {

    abstract fun checkUpdate(): UpdateStatus

    open fun packageName(activity: Activity): String = activity.packageName

    open val schema: String = "https"
    open val host: String = "apps.rustore.ru"

    override fun checkForUpdate(checkForUpdateCallBack: CheckForUpdateCallBack) {
        checkForUpdateCallBack.onUpdate(checkUpdate())
    }

    override fun startUpdate(activity: Any) {
        activity as Activity
        val deepLink = "$schema://$host/${packageName(activity)}"
        activity.startActivity(Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(deepLink)
        })
    }

    override fun installUpdate() {}
}