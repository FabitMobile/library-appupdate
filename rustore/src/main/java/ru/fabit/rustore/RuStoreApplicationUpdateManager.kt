package ru.fabit.rustore

import android.content.Context
import ru.fabit.appupdate.core.ApplicationUpdateManager
import ru.fabit.appupdate.core.CheckForUpdateCallBack
import ru.fabit.appupdate.core.UpdateStatus
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.AppUpdateType
import ru.rustore.sdk.appupdate.model.InstallState
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.appupdate.model.UpdateAvailability

abstract class RuStoreApplicationUpdateManager(context: Context) : ApplicationUpdateManager {

    abstract fun isAppInBackground(): Boolean

    abstract fun checkUpdate(): UpdateStatus

    private val appUpdateManager = RuStoreAppUpdateManagerFactory.create(context)
    private var updateStatusExternal = UpdateStatus.actual
    private var checkForUpdateCallBack: CheckForUpdateCallBack? = null

    private val installListener = InstallStateUpdateListener { state: InstallState ->
        if (state.installStatus == InstallStatus.DOWNLOADED)
            onUpdateDownloaded()
    }

    override fun checkForUpdate(checkForUpdateCallBack: CheckForUpdateCallBack) {
        this.checkForUpdateCallBack = checkForUpdateCallBack
        updateStatusExternal = checkUpdate()

        if (updateStatusExternal == UpdateStatus.actual) {
            checkForUpdateCallBack.onUpdate(UpdateStatus.actual)
            return
        }

        appUpdateManager.getAppUpdateInfo()
            .addOnSuccessListener { appUpdateInfo ->
                when {
                    appUpdateInfo.installStatus == InstallStatus.DOWNLOADED -> {
                        if (updateStatusExternal == UpdateStatus.regular)
                            checkForUpdateCallBack.onUpdate(UpdateStatus.regularDownloaded)
                        else
                            checkForUpdateCallBack.onUpdate(updateStatusExternal)
                    }

                    appUpdateInfo.updateAvailability == UpdateAvailability.UPDATE_AVAILABLE -> {
                        checkForUpdateCallBack.onUpdate(updateStatusExternal)
                    }

                    else -> checkForUpdateCallBack.onUpdate(UpdateStatus.actual)
                }
            }
            .addOnFailureListener {
                checkForUpdateCallBack.onUpdate(UpdateStatus.actual)
            }
    }

    override fun startUpdate(activity: Any) {
        val appUpdateType: Int = when (updateStatusExternal) {
            UpdateStatus.regular -> AppUpdateType.FLEXIBLE
            UpdateStatus.major -> AppUpdateType.IMMEDIATE
            else -> return
        }

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener { appUpdateInfo ->
            appUpdateManager.startUpdateFlow(
                appUpdateInfo,
                AppUpdateOptions.Builder().appUpdateType(appUpdateType).build(),
            )
            appUpdateManager.registerListener(installListener)
        }
    }

    private fun onUpdateDownloaded() {
        appUpdateManager.unregisterListener(installListener)
        if (isAppInBackground())
            installUpdate()
        else
            checkForUpdateCallBack?.onUpdate(UpdateStatus.regularDownloaded)
    }

    override fun installUpdate() {
        val appUpdateType: Int = when (updateStatusExternal) {
            UpdateStatus.regular -> AppUpdateType.FLEXIBLE
            UpdateStatus.major -> AppUpdateType.IMMEDIATE
            else -> return
        }
        appUpdateManager.completeUpdate(
            AppUpdateOptions.Builder().appUpdateType(appUpdateType).build()
        )
    }
}