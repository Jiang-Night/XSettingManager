package me.jiangnight.xsettingmanager.service

import android.content.Intent
import android.content.pm.PackageInfo
import android.os.IBinder
import android.os.UserManager
import android.util.Log
import com.topjohnwu.superuser.ipc.RootService
import me.jiangnight.xsettingmanager.IXMInterface
import rikka.parcelablelist.ParcelableListSlice

class XMService : RootService() {

    override fun onBind(intent: Intent): IBinder {
        return Stub(this)
    }

    fun getUserIds(): List<Int> {
        val result: MutableList<Int> = ArrayList()
        val um = getSystemService(USER_SERVICE) as UserManager
        val userProfiles = um.userProfiles
        for (userProfile in userProfiles) {
            val userId = userProfile.hashCode()
            result.add(userProfile.hashCode())
        }
        return result
    }

    fun getInstalledPackagesAll(flags: Int): java.util.ArrayList<PackageInfo> {
        val packages = java.util.ArrayList<PackageInfo>()
        for (userId in getUserIds()) {
            Log.i("Jiang", "getInstalledPackagesAll: $userId")
            packages.addAll(getInstalledPackagesAsUser(flags, userId))
        }
        return packages
    }


    fun getInstalledPackagesAsUser(flags: Int, userId: Int): List<PackageInfo> {
        try {
            val pm = packageManager
            val getInstalledPackagesAsUser = pm.javaClass.getDeclaredMethod(
                "getInstalledPackagesAsUser",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            return getInstalledPackagesAsUser.invoke(pm, flags, userId) as List<PackageInfo>
        } catch (e: Throwable) {
            Log.e("Jiang", "err", e)
        }

        return java.util.ArrayList()
    }

    class Stub(private val service: XMService) : IXMInterface.Stub(){
        override fun getPackages(flags: Int): ParcelableListSlice<PackageInfo> {
            val list: List<PackageInfo> =service.getInstalledPackagesAll(flags)
            Log.i("Jiang", "getPackages: " + list.size)
            return ParcelableListSlice(list)
        }
    }
}