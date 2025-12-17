package com.example.notas.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotaDao {

    @Insert
    suspend fun insertar(nota: Nota)

    @Update
    suspend fun actualizar(nota: Nota)

    @Delete
    suspend fun eliminar(nota: Nota)

    @Query("SELECT * FROM notas WHERE id = :id")
    suspend fun obtenerPorId(id: Int): Nota?

    @Query("SELECT * FROM notas ORDER BY fechaCreacion DESC")
    fun obtenerTodas(): Flow<List<Nota>>
}
