import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import java.awt.Toolkit
import java.io.File

class MyViewModel {
    var searchText by mutableStateOf("")
    var items by mutableStateOf(List(100) { "Item $it" })

    fun filterItems() {
        val filteredItems = if (searchText.isNotBlank()) {
            items.filter { it.contains(searchText, ignoreCase = true) }
        } else {
            items
        }
        items = filteredItems
        searchText = ""
    }

    fun exportItems() {
        val file = File("lazyData.txt")
        file.writeText(items.joinToString("\n"))
    }
}

@Composable
fun GetWindowState(
    windowWidth: Dp,
    windowHeight: Dp,
): WindowState {
    // Obtener las dimensiones de la pantalla
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val screenWidth = screenSize.width
    val screenHeight = screenSize.height

    // Calcular la posición para centrar la ventana
    val positionX = (screenWidth / 2 - windowWidth.value.toInt() / 2)
    val positionY = (screenHeight / 2 - windowHeight.value.toInt() / 2)

    return rememberWindowState(
        size = DpSize(windowWidth, windowHeight),
        position = WindowPosition(positionX.dp, positionY.dp)
    )
}

fun main() = application {

    val windowState = GetWindowState(
        windowWidth = 800.dp,
        windowHeight = 800.dp
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose App",
        state = windowState
    ) {
        MaterialTheme {
            Surface(color = Color.White) {
                MyScreen()
            }
        }
    }
}

@Composable
fun MyScreen(viewModel: MyViewModel = remember { MyViewModel() }) {
    Row {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = viewModel.searchText,
                onValueChange = { viewModel.searchText = it },
                label = { Text("Search") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { viewModel.filterItems() }),
                modifier = Modifier.padding(16.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .size(width = 100.dp, height = 50.dp)
                        .padding(horizontal = 5.dp),
                    onClick = { viewModel.filterItems() }
                ) {
                    Text("Show")
                }
                Button(
                    modifier = Modifier
                        .size(width = 100.dp, height = 50.dp)
                        .padding(horizontal = 5.dp),
                    onClick = { viewModel.exportItems() }
                ) {
                    Text("Export")
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .border(BorderStroke(5.dp, Color.Black))
                    .width(300.dp)
                    .height(500.dp)
            ) {
                items(viewModel.items.size) { index ->
                    Text(
                        text = viewModel.items[index],
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}