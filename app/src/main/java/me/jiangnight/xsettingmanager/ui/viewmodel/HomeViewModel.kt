package me.jiangnight.xsettingmanager.ui.viewmodel

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.IBinder
import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import me.jiangnight.xsettingmanager.utils.HanziToPinyin
import me.jiangnight.xsettingmanager.xmApp
import java.text.Collator
import java.util.Locale

class HomeViewModel : ViewModel() {

    @Parcelize
    data class AppInfo(
        val label: String,
        val packageInfo: PackageInfo,
        val appIcon :@RawValue Drawable // 直接保存图标
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
            showSystemApps || (it.packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 0
        }
    }


    suspend fun fetchAppList() {
        isRefreshing = true
        withContext(Dispatchers.IO) {
            val packageManager = xmApp.packageManager
            val allPackages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
            val basicAppList = allPackages.mapNotNull { appInfo ->
                val packageInfo =
                    packageManager.getPackageInfo(appInfo.packageName, PackageManager.GET_META_DATA)
                AppInfo(
                    label = appInfo.loadLabel(packageManager).toString(),
                    packageInfo = packageInfo,
                    appIcon = appInfo.loadIcon(packageManager) // 获取图标
                )
            }.filter { it.packageName != xmApp.packageName }
            withContext(Dispatchers.Main) {
                apps = basicAppList
                isRefreshing = false
            }
        }
    }
}


