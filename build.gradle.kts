// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.9.0" apply false
    id("com.android.library") version "8.1.1" apply false
    id("maven-publish")
}

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.huawei.agconnect:agcp:1.5.2.300")
    }
}

allprojects {
    version = "0.1.0"
    group = "ru.fabit.appupdate"
}

subprojects {
    apply(plugin = "maven-publish")
}