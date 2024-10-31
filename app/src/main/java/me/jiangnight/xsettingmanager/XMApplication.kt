package me.jiangnight.xsettingmanager

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

lateinit var xmApp:XMApplication

class XMApplication : Application() {

    // 定义应用状态的枚举类型
    enum class State{
    UNKNOWN_STATE,YES_STATE
    }

    // 定义一个伴生对象，用于持有全局的 LiveData 状态
    companion object{
        private val _xmStateLiveData = MutableLiveData(State.UNKNOWN_STATE)
        // 对外暴露的不可变 LiveData，用于观察状态变化
        val xmStateLiveData:LiveData<State> = _xmStateLiveData
    }

    override fun onCreate() {
        super.onCreate()
        xmApp = this
    }
}