package ru.spacestar.core_ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ru.spacestar.core_ui.utils.UiExtensions.isBackStackEmpty
import ru.spacestar.core_ui.viewmodel.BaseViewModel
import ru.spacestar.core_ui.viewmodel.ErrorSideEffect
import spotmap.core_ui.generated.resources.Res
import spotmap.core_ui.generated.resources.no_internet_connection
import spotmap.core_ui.generated.resources.retry

@Composable
fun BaseAppBarScreen(
    navController: NavController,
    title: String? = null,
    onBackPressed: (() -> Unit) = { navController.popBackStack() },
    isBackEnabled: Boolean = !navController.isBackStackEmpty(),
    bindViewModel: BaseViewModel<out Any, out Any>? = null,
    backIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    appBarMenu: @Composable RowScope.() -> Unit = {},
    fab: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit = {}
) {
    val screenScope = rememberCoroutineScope()
    var askForRetry by remember { mutableStateOf(false) }
    val errorsList = remember { mutableStateListOf<String>() }


    bindViewModel?.collectErrors { e ->
        when (e) {
            is ErrorSideEffect.NetworkErrorSF -> askForRetry = true
            is ErrorSideEffect.ServerErrorSF -> e.error.userMessage?.let { errorsList.add(it) }
        }
    }

    if (askForRetry) {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = stringResource(Res.string.no_internet_connection),
                        textAlign = TextAlign.Center,
                    )
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        askForRetry = false
                        screenScope.launch {
                            bindViewModel?.retry(true)
                        }
                    }
                ) {
                    Text(stringResource(Res.string.retry))
                }
            }
        }
        return
    }
    Scaffold(
        topBar = {
            CustomAppBar(
                title = title,
                backIcon = backIcon,
                onBackPressed = if (isBackEnabled) onBackPressed else null,
                actions = appBarMenu
            )
        },
        floatingActionButton = fab,
        content = content
    )
    errorsList.firstOrNull()?.let {
        ErrorDialog(
            message = it,
            onDismiss = { errorsList.removeFirstOrNull() }
        )
    }
}