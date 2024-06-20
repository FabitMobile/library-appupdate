package ru.fabit.appupdate.googleplay

import android.app.Activity
import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import ru.fabit.appupdate.core.ApplicationUpdateManager
import ru.fabit.appupdate.core.CheckForUpdateCallBack
import ru.fabit.appupdate.core.UpdateStatus

abstract class GoogleApplicationUpdateManager(context: Context) : ApplicationUpdateManager {

    abstract fun isAppInBackground(): Boolean

    abstract fun checkUpdate(): UpdateStatus

    private val appUpdateManager = AppUpdateManagerFactory.create(context)
    private var updateStatusExternal = UpdateStatus.actual
    private var checkForUpdateCallBack: CheckForUpdateCallBack? = null

    private val installListener = InstallStateUpdatedListener { state: InstallState ->
        if (state.installStatus() == InstallStatus.DOWNLOADED)
            onUpdateDownloaded()
    }

    override fun checkForUpdate(checkForUpdateCallBack: CheckForUpdateCallBack) {
        this.checkForUpdateCallBack = checkForUpdateCallBack
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        updateStatusExternal = checkUpdate()
        if (updateStatusExternal == UpdateStatus.actual) {
            checkForUpdateCallBack.onUpdate(UpdateStatus.actual)
            return
        }

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            when {
                appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED ->
                    if (updateStatusExternal == UpdateStatus.regular)
                        checkForUpdateCallBack.onUpdate(UpdateStatus.regularDownloaded)
                    else
                        checkForUpdateCallBack.onUpdate(updateStatusExternal)

                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE ->
                    checkForUpdateCallBack.onUpdate(updateStatusExternal)

                else -> checkForUpdateCallBack.onUpdate(UpdateStatus.actual)
            }
        }

        appUpdateInfoTask.addOnCanceledListener {
            checkForUpdateCallBack.onUpdate(UpdateStatus.actual)
        }

        appUpdateInfoTask.addOnFailureListener {
            checkForUpdateCallBack.onUpdate(UpdateStatus.actual)
        }
    }

    override fun startUpdate(activity: Any) {
        val appUpdateType: Int = when (updateStatusExternal) {
            UpdateStatus.regular -> AppUpdateType.FLEXIBLE
            UpdateStatus.major -> AppUpdateType.IMMEDIATE
            else -> return
        }

        appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                activity as Activity,
                AppUpdateOptions.defaultOptions(appUpdateType),
                UPDATE_REQUEST_CODE
            )
            appUpdateManager.registerListener(installListener)
        }
    }

    override fun installUpdate() {
        appUpdateManager.completeUpdate()
    }

    private fun onUpdateDownloaded() {
        appUpdateManager.unregisterListener(installListener)
        if (isAppInBackground())
            installUpdate()
        else
            checkForUpdateCallBack?.onUpdate(UpdateStatus.regularDownloaded)
    }

    companion object {
        private const val UPDATE_REQUEST_CODE = 8
    }
}