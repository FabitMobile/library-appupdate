package ru.fabit.appupdate.core

@JvmInline
value class InstallerVendor private constructor(val installerPackage: String) {
    companion object {
        val googlePlay = InstallerVendor("com.android.vending")
        val appGallery = InstallerVendor("com.huawei.appmarket")
        val ruStore = InstallerVendor("ru.vk.store")
        val other = InstallerVendor("")
    }
}

fun getInstallerVendor(
    installerPackage: String
) = when (installerPackage) {
    InstallerVendor.googlePlay.installerPackage -> InstallerVendor.googlePlay
    InstallerVendor.appGallery.installerPackage -> InstallerVendor.appGallery
    InstallerVendor.ruStore.installerPackage -> InstallerVendor.ruStore

    else -> InstallerVendor.other
}