package me.jiangnight.xsettingmanager.ui.screen

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec
import me.jiangnight.xsettingmanager.R
import me.jiangnight.xsettingmanager.ui.screen.destinations.AboutScreenDestination
import me.jiangnight.xsettingmanager.ui.screen.destinations.HomeScreenDestination
import me.jiangnight.xsettingmanager.ui.screen.destinations.SettingScreenDestination

enum class BottomBarDestination(
    val direction:DirectionDestinationSpec,
    @StringRes val label:Int,
    val iconSelected : ImageVector, //选中图标
    val iconNotSelected : ImageVector, //未选中图标
){
    Home(
        HomeScreenDestination,
        R.string.home,
        Icons.Filled.Home,
        Icons.Outlined.Home
    ),
    Settings(
        SettingScreenDestination,
        R.string.setting,
        Icons.Filled.Settings,
        Icons.Outlined.Settings
    ),
    About(
        AboutScreenDestination,
        R.string.about,
        Icons.Filled.Info,
        Icons.Outlined.Info
    )
}

