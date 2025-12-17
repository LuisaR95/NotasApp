package com.example.notas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.notas.data.Nota
import com.example.notas.data.NotaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotasViewModel(private val notaDao: NotaDao) : ViewModel() {

    val notas: StateFlow<List<Nota>> = notaDao.obtenerTodas()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun agregarNota(titulo: String, contenido: String) {
        viewModelScope.launch(Dispatchers.IO) {
            notaDao.insertar(Nota(titulo = titulo, contenido = contenido))
        }
    }

    fun eliminarNota(nota: Nota) {
        viewModelScope.launch(Dispatchers.IO) {
            notaDao.eliminar(nota)
        }
    }
}

class NotasViewModelFactory(private val notaDao: NotaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return NotasViewModel(notaDao) as T
    }
}
