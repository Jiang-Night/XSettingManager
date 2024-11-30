package me.jiangnight.xsettingmanager.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import me.jiangnight.xsettingmanager.R
import androidx.compose.material3.FilledTonalButton

@Destination
@Composable
fun AboutScreen(navigator: DestinationsNavigator) {
    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopBar(onBack = { navigator.popBackStack() })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // 显示头像
            Surface(
                modifier = Modifier.size(95.dp),
                color = colorResource(id = R.color.ic_launcher_background),
                shape = CircleShape
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.github_head),
                    contentDescription = "头像",
                    modifier = Modifier.scale(1f).offset(x = (-6).dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 昵称
            Text(
                text = "开发：JiangNight",
                style = MaterialTheme.typography.titleLarge
            )



            Spacer(modifier = Modifier.height(20.dp))

            // GitHub 按钮
            FilledTonalButton(
                onClick = { uriHandler.openUri("https://github.com/Jiang-Night/XSettingManager") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.github),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))
                Text(text = "GitHub")
            }

            Spacer(modifier = Modifier.height(20.dp))


            // 项目参考部分说明
            Text(
                text = "项目UI参考：",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),  // 确保 Row 占满整个宽度
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center  // 保证按钮居中
            ) {
                ProjectLinkButton(
                    url = "https://github.com/tiann/KernelSU",
                    label = "KernelSU"
                )
                Spacer(modifier = Modifier.width(8.dp))  // 添加间距
                ProjectLinkButton(
                    url = "https://github.com/bmax121/APatch",
                    label = "APatch"
                )
            }

            // 项目描述部分
            OutlinedCard(
                modifier = Modifier.padding(vertical = 30.dp, horizontal = 20.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 12.dp)
                ) {
                    Text(
                        text = "AOSP定制系统管理工具,主要有脱壳、frida持久化、frida监听、强制解释模式执行、函数调用流程等..",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun ProjectLinkButton(url: String, label: String) {
    val uriHandler = LocalUriHandler.current
    FilledTonalButton(
        onClick = { uriHandler.openUri(url) },
        modifier = Modifier.padding(horizontal = 8.dp)  // 添加一些水平内边距
    ) {
        Text(text = label)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onBack: () -> Unit = {}) {
    TopAppBar(
        title = { Text(stringResource(R.string.about)) },
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null) }
        },
    )
}
