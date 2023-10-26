package ru.fabit.appupdate.core

@JvmInline
value class UpdateStatus private constructor(private val status: Int) {
    companion object {
        val actual = UpdateStatus(0)
        val regular = UpdateStatus(1)
        val regularDownloaded = UpdateStatus(2)
        val major = UpdateStatus(3)
    }
}

fun <Version> checkUpdate(
    current: AppVersion<Version>,
    major: AppVersion<Version>,
    regular: AppVersion<Version>
): UpdateStatus {
    return if (current < major)
        UpdateStatus.major
    else if (current < regular)
        UpdateStatus.regular
    else
        UpdateStatus.actual
}