package me.jiangnight.xsettingmanager.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import me.jiangnight.xsettingmanager.ui.component.SwitchItem
import me.jiangnight.xsettingmanager.ui.viewmodel.HomeViewModel
import me.jiangnight.xsettingmanager.utils.LocalSnackbarHost

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AppSettingScreen(
    navigator: DestinationsNavigator,
    appInfo: HomeViewModel.AppInfo
) {
    val context = LocalContext.current
    val snackBarHost = LocalSnackbarHost.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    // 状态管理：用于各个设置项
    var jsonFilePath by remember { mutableStateOf("") }
    var forceInterpretMode by remember { mutableStateOf(false) }
    var fridaPersistenceMode by remember { mutableStateOf(false) }
    var fridaListeningMode by remember { mutableStateOf(false) }
    var deShellMode by remember { mutableStateOf(false) }
    var functionFlow by remember { mutableStateOf(false) }
    var smailFlow by remember { mutableStateOf(false) }
    var idaListeningMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopBar(onBack = { navigator.popBackStack() }, scrollBehavior = scrollBehavior)
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHost) },
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
    ) { paddingValues ->
        AppProfileInner(
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            appInfo = appInfo,
            jsonFilePath = jsonFilePath,
            forceInterpretMode = forceInterpretMode,
            fridaPersistenceMode = fridaPersistenceMode,
            fridaListeningMode = fridaListeningMode,
            deShellMode = deShellMode,
            functionFlow = functionFlow,
            smailFlow = smailFlow,
            idaListeningMode = idaListeningMode,
            onJsonFileSelect = {
                // 打开文件选择器
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                context.startActivity(intent)
            },
            onForceInterpretModeChange = { forceInterpretMode = it },
            onFridaPersistenceModeChange = { fridaPersistenceMode = it },
            onFridaListeningModeChange = { fridaListeningMode = it },
            onDeShellModeChange = { deShellMode = it },
            onFunctionFlowChange = { functionFlow = it },
            onSmailFlowChange = { smailFlow = it },
            onIdaListeningModeChange = { idaListeningMode = it }
        )
    }
}

@Composable
fun AppProfileInner(
    modifier: Modifier = Modifier,
    appInfo: HomeViewModel.AppInfo,
    jsonFilePath: String,
    forceInterpretMode: Boolean,
    fridaPersistenceMode: Boolean,
    fridaListeningMode: Boolean,
    deShellMode: Boolean,
    functionFlow: Boolean,
    smailFlow: Boolean,
    idaListeningMode: Boolean,
    onJsonFileSelect: () -> Unit,
    onForceInterpretModeChange: (Boolean) -> Unit,
    onFridaPersistenceModeChange: (Boolean) -> Unit,
    onFridaListeningModeChange: (Boolean) -> Unit,
    onDeShellModeChange: (Boolean) -> Unit,
    onFunctionFlowChange: (Boolean) -> Unit,
    onSmailFlowChange: (Boolean) -> Unit,
    onIdaListeningModeChange: (Boolean) -> Unit
) {
    Column(modifier = modifier.padding(16.dp)) {
        // 显示应用的图标
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(LocalContext.current.packageManager.getApplicationIcon(appInfo.packageName))
                .crossfade(true)
                .build(),
            contentDescription = appInfo.label,
            modifier = Modifier
                .padding(4.dp)
                .width(48.dp)
                .height(48.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 显示应用标签
        Text(
            text = appInfo.label,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 显示包名
        Text(
            text = "包名: ${appInfo.packageName}",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 使用 LazyColumn 来创建按钮和开关
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                // 选择 JSON 文件按钮
                Button(
                    onClick = onJsonFileSelect,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Text("选择JS文件")
                }
            }
            item {
                // 强制解释执行模式开关
                SwitchItem(
                    title = "强制解释执行模式",
                    checked = forceInterpretMode,
                    onCheckedChange = onForceInterpretModeChange
                )
            }
            item {
                // Frida 持久化模式开关
                SwitchItem(
                    title = "Frida 持久化模式",
                    checked = fridaPersistenceMode,
                    onCheckedChange = onFridaPersistenceModeChange
                )
            }
            item {
                // Frida 监听模式开关
                SwitchItem(
                    title = "Frida 监听模式",
                    checked = fridaListeningMode,
                    onCheckedChange = onFridaListeningModeChange
                )
            }
            item {
                // 脱壳开关
                SwitchItem(
                    title = "脱壳",
                    checked = deShellMode,
                    onCheckedChange = onDeShellModeChange
                )
            }
            item {
                // 函数调用流程开关
                SwitchItem(
                    title = "函数调用流程",
                    checked = functionFlow,
                    onCheckedChange = onFunctionFlowChange
                )
            }
            item {
                // Smali 调用流程开关
                SwitchItem(
                    title = "Smali 调用流程",
                    checked = smailFlow,
                    onCheckedChange = onSmailFlowChange
                )
            }
            item {
                // IDA 监听模式开关
                SwitchItem(
                    title = "IDA 监听模式",
                    checked = idaListeningMode,
                    onCheckedChange = onIdaListeningModeChange
                )
            }
            // 添加底部空白区域，避免被 NavHost 遮挡
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onBack: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    TopAppBar(title = {
        Text(text = "APP Setting")
    }, navigationIcon = {
        IconButton(onClick = onBack) {
            Icon(Icons.Filled.ArrowBack, contentDescription = null)
        }
    }, windowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
        scrollBehavior = scrollBehavior
    )
}
