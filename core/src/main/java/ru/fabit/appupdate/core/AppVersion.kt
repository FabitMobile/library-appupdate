package ru.fabit.appupdate.core

abstract class AppVersion<Version>(val version: Version) : Comparable<AppVersion<Version>>

class SemanticVersion(version: String) : AppVersion<String>(version) {
    override fun compareTo(other: AppVersion<String>): Int {
        return this.version.mapVersion().compareTo(other.version.mapVersion())
    }

    /**
     * Переводим каждое число в строку с 3 знаками и затем объединяем в одно число
     * "1.234.5" -> "001234005" -> 1234005
     */
    private fun String.mapVersion() = split(".").joinToString("") {
        String.format("%03d", it.toInt())
    }.toInt()
}