package ru.spacestar.spotmap

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.spacestar.core.utils.preferences.ApplicationPreferences

// TODO: implement and test on IOS side, then delete file
class TestPreferences : KoinComponent {
    @Composable
    operator fun invoke() {
        var text by remember { mutableStateOf("") }
        val repo: ApplicationPreferences by inject()

        LaunchedEffect(true) {
//            val name2 = repo.name2.get()
//            text += "name2 = $name2\n"
//            var name = repo.name.get()
//            text += "name = $name\n"
//            repo.name.put("vasya")
//            name = repo.name.get()
//            text += "name = $name after put\n"
//            repo.name.remove()
//            name = repo.name.get()
//            text += "name = $name after clean\n"
        }

        Text(text)
    }
}