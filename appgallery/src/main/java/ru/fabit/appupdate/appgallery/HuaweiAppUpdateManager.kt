package ru.fabit.appupdate.appgallery

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.huawei.hms.jos.JosApps
import com.huawei.updatesdk.service.appmgr.bean.ApkUpgradeInfo
import com.huawei.updatesdk.service.otaupdate.CheckUpdateCallBack
import com.huawei.updatesdk.service.otaupdate.UpdateKey
import ru.fabit.appupdate.core.ApplicationUpdateManager
import ru.fabit.appupdate.core.CheckForUpdateCallBack
import ru.fabit.appupdate.core.UpdateStatus

abstract class HuaweiAppUpdateManager(
    private val context: Context
) : ApplicationUpdateManager {

    abstract fun checkUpdate(): UpdateStatus

    private val client = JosApps.getAppUpdateClient(context)
    private var updateStatusExternal = UpdateStatus.actual
    private var apkUpdateInfo: ApkUpgradeInfo? = null

    override fun checkForUpdate(checkForUpdateCallBack: CheckForUpdateCallBack) {
        updateStatusExternal = checkUpdate()
        if (updateStatusExternal == UpdateStatus.actual) return

        client.checkAppUpdate(context, object : CheckUpdateCallBack {

            override fun onUpdateInfo(intent: Intent?) {
                val info = intent?.getSerializableExtra(UpdateKey.INFO)
                if (info is ApkUpgradeInfo) {
                    apkUpdateInfo = info
                    checkForUpdateCallBack.onUpdate(
                        if (updateStatusExternal == UpdateStatus.major)
                            UpdateStatus.major
                        else
                            UpdateStatus.regular
                    )
                } else {
                    apkUpdateInfo = null
                    checkForUpdateCallBack.onUpdate(UpdateStatus.actual)
                }
            }

            override fun onMarketInstallInfo(p0: Intent?) {}
            override fun onMarketStoreError(p0: Int) {}
            override fun onUpdateStoreError(p0: Int) {}
        })
    }

    override fun startUpdate(activity: Any) {
        if (updateStatusExternal != UpdateStatus.major
            && updateStatusExternal != UpdateStatus.regular
        ) return

        val isMajorUpdate = updateStatusExternal == UpdateStatus.major
        apkUpdateInfo?.let { info ->
            client.showUpdateDialog(
                activity as Activity,
                info,
                isMajorUpdate
            )
        }
    }

    override fun installUpdate() {}
}