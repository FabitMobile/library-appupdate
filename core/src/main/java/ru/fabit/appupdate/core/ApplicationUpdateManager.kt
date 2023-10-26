package ru.fabit.appupdate.core

interface ApplicationUpdateManager {
    fun checkForUpdate(checkForUpdateCallBack: CheckForUpdateCallBack)

    fun startUpdate(activity: Any)

    fun installUpdate()
}

fun interface CheckForUpdateCallBack {
    fun onUpdate(status: UpdateStatus)
}