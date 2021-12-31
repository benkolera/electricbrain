package me.ben.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.apollographql.apollo3.ApolloClient
import com.benkolera.electricbrain.common.apollo.GetTagsQuery
import kotlinx.coroutines.launch

sealed class RemoteState<T> {
    class Loading<T>(): RemoteState<T>()
    class Error<T>(val errorMessage: String): RemoteState<T>()
    class Success<T>(val value: T): RemoteState<T>()

    companion object {
        fun <T> loading(): RemoteState<T> = Loading()
        fun <T> error(errorMessage: String): RemoteState<T> = Error(errorMessage)
        fun <T> success(value: T): RemoteState<T> = Success(value)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun App() {
    val coroutineScope = rememberCoroutineScope()
    val (tagsResult,setTagsResult) = remember { mutableStateOf(RemoteState.loading<List<GetTagsQuery.Tag>>()) }
    val modalState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)

    LaunchedEffect(true) { loadTags(setTagsResult) }
    var offset by remember { mutableStateOf(0f) }

    Scaffold(
        topBar = {
            TopAppBar(title = {Text("Electric Brain", color = Color.White)}, backgroundColor = MaterialTheme.colors.primary)
        },
        floatingActionButton = {
            if (!modalState.isVisible) {
                FloatingActionButton(
                    backgroundColor = MaterialTheme.colors.primary,
                    onClick = {
                        coroutineScope.launch {
                            modalState.show()
                        }
                    }
                ) {
                    Text("+")
                }
            }
        }
    ){
        when (tagsResult) {
            is RemoteState.Loading -> Text("Loading...")
            is RemoteState.Error -> Text("Error: ${tagsResult.errorMessage}")
            is RemoteState.Success -> {
                ModalBottomSheetLayout(
                    sheetState = modalState,
                    sheetContent = {
                        Column {
                            Text("New Tag")
                            Spacer(Modifier.height(20.dp))
                            Row {
                                Button(
                                    colors = ButtonDefaults.buttonColors(
                                        MaterialTheme.colors.secondary
                                    ),
                                    onClick = {
                                        coroutineScope.launch {
                                            modalState.hide()
                                        }
                                    }
                                ) {
                                    Text("Cancel")
                                }
                                Spacer(Modifier.width(10.dp))
                                Button(onClick = {
                                    coroutineScope.launch {
                                        modalState.hide()
                                    }
                                }) {
                                    Text("Create")
                                }
                            }
                        }
                    },
                ) {
                    Column(
                        modifier = Modifier.scrollable(
                            orientation = Orientation.Vertical,
                            state = rememberScrollableState { delta ->
                                offset += delta
                                delta
                            }
                        ).fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp )
                    ){
                        tagsResult.value.forEach {
                            Card(
                                elevation = 2.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(all = 15.dp)
                                ) {
                                    Text(it.name.orEmpty())
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

suspend fun loadTags(setValue: (s: RemoteState<List<GetTagsQuery.Tag>>) -> Unit) {
    try {
        val res = ApolloClient.Builder().serverUrl("http://localhost:8080/graphql").build().query(GetTagsQuery())
            .execute()
        if (res.hasErrors()) {
            setValue(RemoteState.error(res.errors.toString()))
        }

        setValue(RemoteState.success(res.data?.tags?.filterNotNull().orEmpty()))
    } catch (e: Exception) {
        setValue(RemoteState.error("An exception occurred: ${e.message}"))
    }
}