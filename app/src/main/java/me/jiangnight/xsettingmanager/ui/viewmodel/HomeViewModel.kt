package me.jiangnight.xsettingmanager.ui.viewmodel

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Parcelable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.ipc.RootService.bindOrTask
import com.topjohnwu.superuser.ipc.RootService.stop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import me.jiangnight.xsettingmanager.service.XMService
import me.jiangnight.xsettingmanager.utils.HanziToPinyin
import me.jiangnight.xsettingmanager.utils.XMCli
import me.jiangnight.xsettingmanager.xmApp
import java.text.Collator
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class HomeViewModel : ViewModel() {

    @Parcelize
    data class AppInfo(
        val label: String,
        val packageInfo: PackageInfo
    ) : Parcelable {
        val packageName: String get() = packageInfo.packageName
    }

    var search by mutableStateOf("")
    var showSystemApps by mutableStateOf(false)
    var isRefreshing by mutableStateOf(false)
        private set
    private var apps by mutableStateOf<List<AppInfo>>(emptyList())

    private val sortedList by derivedStateOf {
        apps.sortedWith(
            compareBy(Collator.getInstance(Locale.getDefault()), AppInfo::label)
        ).also {
            isRefreshing = false
        }
    }

    val appList by derivedStateOf {
        sortedList.filter {
            it.label.lowercase().contains(search.lowercase()) ||
                    it.packageName.lowercase().contains(search.lowercase()) ||
                    HanziToPinyin.getInstance().toPinyinString(it.label)
                        .contains(search.lowercase())
        }.filter {
            showSystemApps || !isSystemApp(it.packageName)
        }
    }

    private fun isSystemApp(packageName: String): Boolean {
        return try {
            val appInfo = xmApp.packageManager.getApplicationInfo(packageName, 0)
            appInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    //not root
    private suspend inline fun connectXSmService(
        crossinline onDisconnect: () -> Unit = {}
    ): Pair<IBinder, ServiceConnection> = suspendCoroutine {
        val connection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                onDisconnect()
            }
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                it.resume(binder as IBinder to this)
            }
        }
        val intent = Intent(xmApp, XMService::class.java)
        val task = bindOrTask(intent, Shell.EXECUTOR, connection)

        val shell = XMCli.SHELL
        task?.let { it1 -> shell.execTask(it1) }
    }

    private fun stopXSmService() {
        val intent = Intent(xmApp, XMService::class.java)
        stop(intent)
    }

    suspend fun fetchAppList() {
        isRefreshing = true
        withContext(Dispatchers.IO) {
            val packageManager = xmApp.packageManager
            // 获取所有安装的应用
            val allPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

            // 使用 PackageManager 获取每个应用的 PackageInfo
            apps = allPackages.mapNotNull {
                val packageInfo = it
                AppInfo(
                    label = packageManager.getApplicationLabel(packageInfo.applicationInfo).toString(),
                    packageInfo = packageInfo,
                )
            }
        }
    }

}
