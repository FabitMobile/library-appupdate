package ru.fabit.appupdate.core.impl

import ru.fabit.appupdate.core.ApplicationUpdateManager
import ru.fabit.appupdate.core.CheckForUpdateCallBack
import ru.fabit.appupdate.core.UpdateStatus

class NoOpApplicationUpdateManager(
    private val updateStatus: UpdateStatus
) : ApplicationUpdateManager {
    override fun checkForUpdate(checkForUpdateCallBack: CheckForUpdateCallBack) {
        checkForUpdateCallBack.onUpdate(updateStatus)
        println("NoOpApplicationUpdateManager: checkForUpdate return $updateStatus")
    }

    override fun startUpdate(activity: Any) {
        println("NoOpApplicationUpdateManager: startUpdate in $activity")
    }

    override fun installUpdate() {
        println("NoOpApplicationUpdateManager: installUpdate")
    }
}