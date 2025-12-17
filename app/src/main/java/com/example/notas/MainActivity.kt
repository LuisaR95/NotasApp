package com.example.notas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notas.data.AppDatabase
import com.example.notas.data.Nota
import com.example.notas.viewmodel.NotasViewModel
import com.example.notas.viewmodel.NotasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotasApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotasApp() {
    val context = LocalContext.current
    val db = AppDatabase.getInstance(context)
    val factory = NotasViewModelFactory(db.notaDao())
    val viewModel: NotasViewModel = viewModel(factory = factory)

    val notas by viewModel.notas.collectAsState()
    var mostrarFormulario by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Mis Notas") })

        if (mostrarFormulario) {
            FormularioNota(
                onGuardar = { titulo, contenido ->
                    viewModel.agregarNota(titulo, contenido)
                    mostrarFormulario = false
                },
                onCancelar = { mostrarFormulario = false }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(notas) { nota ->
                    NotaItem(nota = nota, onEliminar = { viewModel.eliminarNota(nota) })
                }

                item {
                    Button(
                        onClick = { mostrarFormulario = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("+ Nueva Nota")
                    }
                }
            }
        }
    }
}

@Composable
fun NotaItem(nota: Nota, onEliminar: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = nota.titulo, fontSize = 18.sp)
                Text(
                    text = nota.contenido,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@Composable
fun FormularioNota(onGuardar: (String, String) -> Unit, onCancelar: () -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var contenido by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Contenido") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = onCancelar) { Text("Cancelar") }
            Button(
                onClick = { onGuardar(titulo, contenido) },
                enabled = titulo.isNotBlank() && contenido.isNotBlank()
            ) {
                Text("Guardar")
            }
        }
    }
}
