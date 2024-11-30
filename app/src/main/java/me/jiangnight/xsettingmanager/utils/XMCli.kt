package me.jiangnight.xsettingmanager.utils

import android.util.Log
import com.topjohnwu.superuser.Shell
import rikka.parcelablelist.BuildConfig


object XMCli{
    val SHELL:Shell  =createRootShell()
}

fun createRootShell(globalMnt: Boolean = false): Shell {
    Shell.enableVerboseLogging = BuildConfig.DEBUG
    val builder = Shell.Builder.create()
    return try {
        // 尝试使用普通的 su
        if (globalMnt) {
            builder.build("su", "-g") // 如果需要全局挂载，使用 -g 选项
        } else {
            builder.build("su") // 仅使用普通的 su
        }
    } catch (e: Throwable) {
        Log.w("Jiang", "Failed to create root shell: ", e)
        // 如果第一次尝试失败，回退到默认的 sh shell
        builder.build("sh")
    }
}
