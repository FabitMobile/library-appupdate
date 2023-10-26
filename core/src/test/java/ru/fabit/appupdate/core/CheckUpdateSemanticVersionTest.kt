package ru.fabit.appupdate.core

import org.junit.Assert
import org.junit.Test

class CheckUpdateSemanticVersionTest {

    @Test
    fun `current version is less than blocking, return major`() {
        val current = SemanticVersion("1.0.0")
        val targetBlocking = SemanticVersion("2.0.0")
        val target = SemanticVersion("3.0.0")

        val result = checkUpdate(
            current = current,
            major = targetBlocking,
            regular = target
        )

        Assert.assertEquals(UpdateStatus.major, result)
    }

    @Test
    fun `blocking version is equal to target, current version is less than blocking, return major`() {
        val current = SemanticVersion("2.99.99")
        val targetBlocking = SemanticVersion("3.0.0")
        val target = SemanticVersion("3.0.0")

        val result = checkUpdate(
            current = current,
            major = targetBlocking,
            regular = target
        )

        Assert.assertEquals(UpdateStatus.major, result)
    }

    @Test
    fun `current version is less than blocking 2, return major`() {
        val current = SemanticVersion("1.10.10")
        val targetBlocking = SemanticVersion("2.0.0")
        val target = SemanticVersion("3.0.0")


        val result = checkUpdate(
            current = current,
            major = targetBlocking,
            regular = target
        )

        Assert.assertEquals(UpdateStatus.major, result)
    }

    @Test
    fun `current version is larger than the blocking and lesser than target, return regular`() {
        val current = SemanticVersion("2.0.0")
        val targetBlocking = SemanticVersion("2.0.0")
        val target = SemanticVersion("3.0.0")

        val result = checkUpdate(
            current = current,
            major = targetBlocking,
            regular = target
        )

        Assert.assertEquals(UpdateStatus.regular, result)
    }

    @Test
    fun `current version is larger than the blocking and lesser than target 2, return regular`() {
        val current = SemanticVersion("2.100.100")
        val targetBlocking = SemanticVersion("2.0.0")
        val target = SemanticVersion("3.0.0")

        val result = checkUpdate(
            current = current,
            major = targetBlocking,
            regular = target
        )

        Assert.assertEquals(UpdateStatus.regular, result)
    }

    @Test
    fun `current version is equal to target, return actual`() {
        val current = SemanticVersion("3.0.0")
        val targetBlocking = SemanticVersion("2.0.0")
        val target = SemanticVersion("3.0.0")

        val result = checkUpdate(
            current = current,
            major = targetBlocking,
            regular = target
        )

        Assert.assertEquals(UpdateStatus.actual, result)
    }

    @Test
    fun `current version is greater than target, return actual`() {
        val current = SemanticVersion("13.0.10")
        val targetBlocking = SemanticVersion("2.0.0")
        val target = SemanticVersion("3.0.0")

        val result = checkUpdate(
            current = current,
            major = targetBlocking,
            regular = target
        )

        Assert.assertEquals(UpdateStatus.actual, result)
    }

    @Test
    fun `current version is equal to target and blocking, return actual`() {
        val current = SemanticVersion("1.0.0")
        val targetBlocking = SemanticVersion("1.0.0")
        val target = SemanticVersion("1.0.0")

        val result = checkUpdate(
            current = current,
            major = targetBlocking,
            regular = target
        )

        Assert.assertEquals(UpdateStatus.actual, result)
    }
}