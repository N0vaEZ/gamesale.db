package com.example.gamesaledb.ui.reminders

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.text.format.DateFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.gamesaledb.data.local.WishlistEntity
import com.example.gamesaledb.reminders.GameReminder
import com.example.gamesaledb.reminders.ReminderScheduler
import com.example.gamesaledb.reminders.ReminderStatus
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersScreen(
    game: WishlistEntity?, state: ReminderUiState, viewModel: ReminderViewModel,
    onBack: () -> Unit, onWishlist: () -> Unit
) {
    val context = LocalContext.current
    val scheduler = remember(context) { ReminderScheduler(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    var title by rememberSaveable(game?.gameId) {
        mutableStateOf(game?.let { "Conferir ofertas: ${it.title}" } ?: "")
    }
    var message by rememberSaveable(game?.gameId) {
        mutableStateOf(game?.let { "Veja os preços de ${it.title} no GameSaleDB." } ?: "")
    }
    var whenMillis by rememberSaveable(game?.gameId) {
        mutableStateOf(Calendar.getInstance().apply {
            add(Calendar.MINUTE, 5); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        }.timeInMillis)
    }
    var permissionRequested by rememberSaveable { mutableStateOf(false) }
    var cancelling by remember { mutableStateOf<GameReminder?>(null) }
    val permission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        viewModel.inform(if (granted) "Permissão concedida. Confira os dados e toque em Agendar."
            else "Permissão negada. Nenhum lembrete foi agendado por esta ação. Você pode abrir as configurações.")
        viewModel.refresh()
    }
    DisposableEffect(lifecycle, viewModel) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) viewModel.refresh()
        }
        lifecycle.addObserver(observer)
        viewModel.refresh()
        onDispose { lifecycle.removeObserver(observer) }
    }
    fun pickDateTime() {
        val selected = Calendar.getInstance().apply { timeInMillis = whenMillis }
        DatePickerDialog(context, { _, year, month, day ->
            TimePickerDialog(context, { _, hour, minute ->
                whenMillis = Calendar.getInstance().apply {
                    set(year, month, day, hour, minute, 0); set(Calendar.MILLISECOND, 0)
                }.timeInMillis
            }, selected.get(Calendar.HOUR_OF_DAY), selected.get(Calendar.MINUTE),
                DateFormat.is24HourFormat(context)).show()
        }, selected.get(Calendar.YEAR), selected.get(Calendar.MONTH), selected.get(Calendar.DAY_OF_MONTH)).show()
    }
    fun formatted(time: Long): String = java.text.DateFormat.getDateTimeInstance(
        java.text.DateFormat.SHORT, java.text.DateFormat.SHORT
    ).format(Date(time))

    Scaffold(topBar = {
        TopAppBar(title = { Text("Lembretes de jogos") }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
            }
        })
    }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding), contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                Text("Lembre-se de conferir ofertas de um jogo salvo.", style = MaterialTheme.typography.titleMedium)
                Text("O horário é aproximado. Lembretes não são restaurados após reiniciar o celular ou forçar a parada: cancele e crie novamente. Remover um jogo da wishlist não cancela seus lembretes.",
                    style = MaterialTheme.typography.bodySmall)
            }
            item {
                Text(if (state.notificationsAllowed) "Notificações habilitadas" else "Notificações bloqueadas")
                if (!state.notificationsAllowed && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED &&
                    !permissionRequested) {
                    OutlinedButton(onClick = {
                        permissionRequested = true
                        permission.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }) { Text("Permitir notificações") }
                }
                TextButton(onClick = {
                    try { context.startActivity(scheduler.settingsIntent()) }
                    catch (_: Exception) { viewModel.inform("Abra as configurações do Android e ative as notificações do GameSaleDB.") }
                }) { Text("Configurações de notificações") }
                OutlinedButton(onClick = viewModel::testNow, enabled = !state.busy) {
                    Text("Testar notificação agora")
                }
            }
            item {
                if (game == null) {
                    Text("Para criar um lembrete, escolha um jogo na lista de desejos e toque em Criar lembrete.")
                    Button(onClick = onWishlist) { Text("Abrir lista de desejos") }
                } else {
                    Card {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(game.title, style = MaterialTheme.typography.titleMedium)
                            OutlinedTextField(title, { title = it }, label = { Text("Título") },
                                modifier = Modifier.fillMaxWidth(), singleLine = true)
                            OutlinedTextField(message, { message = it }, label = { Text("Mensagem") },
                                modifier = Modifier.fillMaxWidth(), minLines = 2)
                            Text("Data e hora: ${formatted(whenMillis)}")
                            OutlinedButton(onClick = ::pickDateTime, enabled = !state.busy) { Text("Escolher data e hora") }
                            Button(onClick = { viewModel.schedule(game, title, message, whenMillis) },
                                enabled = !state.busy) { Text(if (state.busy) "Processando..." else "Agendar") }
                        }
                    }
                }
            }
            state.message?.let { feedback -> item { Text(feedback, style = MaterialTheme.typography.bodyMedium) } }
            item {
                Text("Histórico de lembretes", style = MaterialTheme.typography.titleLarge)
                if (state.items.isEmpty()) Text("Nenhum lembrete cadastrado.")
                TextButton(onClick = viewModel::refresh) { Text("Atualizar lista") }
            }
            items(state.items, key = { it.id }) { item ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(item.title, style = MaterialTheme.typography.titleMedium)
                        Text(item.gameTitle)
                        Text(item.message)
                        Text("Solicitado: ${formatted(item.whenMillis)}")
                        Text("#${item.id} · ${item.status.label}")
                        item.publishedAt?.let { Text("Enviado ao sistema: ${formatted(it)}") }
                        if (item.status == ReminderStatus.SCHEDULED) {
                            OutlinedButton(onClick = { cancelling = item }, enabled = !state.busy) { Text("Cancelar lembrete") }
                        }
                    }
                }
            }
        }
    }
    cancelling?.let { item ->
        AlertDialog(onDismissRequest = { cancelling = null }, title = { Text("Cancelar lembrete?") },
            text = { Text("${item.title}\nO registro permanecerá no histórico.") },
            confirmButton = { TextButton(onClick = { viewModel.cancel(item.id); cancelling = null }) { Text("Cancelar lembrete") } },
            dismissButton = { TextButton(onClick = { cancelling = null }) { Text("Manter agendado") } })
    }
}
