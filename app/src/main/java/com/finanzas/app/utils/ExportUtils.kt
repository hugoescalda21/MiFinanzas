package com.finanzas.app.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.finanzas.app.data.model.Transaction
import com.finanzas.app.data.model.TransactionType
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ExportUtils {
    
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val fileNameFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
    
    fun exportToCsv(
        context: Context,
        transactions: List<Transaction>,
        onSuccess: (Uri) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val fileName = "MiFinanzas_${LocalDateTime.now().format(fileNameFormatter)}.csv"
            val file = File(context.cacheDir, fileName)
            
            FileWriter(file).use { writer ->
                // Header
                writer.append("Fecha,Tipo,Categoría,Descripción,Monto,Nota\n")
                
                // Data rows
                transactions.sortedByDescending { it.date }.forEach { transaction ->
                    val tipo = if (transaction.type == TransactionType.INCOME) "Ingreso" else "Gasto"
                    val fecha = transaction.date.format(dateFormatter)
                    val categoria = transaction.category.displayName
                    val descripcion = escapeCsv(transaction.description)
                    val monto = if (transaction.type == TransactionType.INCOME) 
                        transaction.amount.toString() 
                    else 
                        "-${transaction.amount}"
                    val nota = escapeCsv(transaction.note)
                    
                    writer.append("$fecha,$tipo,$categoria,$descripcion,$monto,$nota\n")
                }
            }
            
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            
            onSuccess(uri)
            
        } catch (e: Exception) {
            onError("Error al exportar: ${e.message}")
        }
    }
    
    fun shareFile(context: Context, uri: Uri, fileName: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "Exportación MiFinanzas")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Compartir archivo"))
    }
    
    private fun escapeCsv(text: String): String {
        return if (text.contains(",") || text.contains("\"") || text.contains("\n")) {
            "\"${text.replace("\"", "\"\"")}\""
        } else {
            text
        }
    }
    
    fun generateSummary(transactions: List<Transaction>): ExportSummary {
        val totalIncome = transactions
            .filter { it.type == TransactionType.INCOME }
            .sumOf { it.amount }
        
        val totalExpense = transactions
            .filter { it.type == TransactionType.EXPENSE }
            .sumOf { it.amount }
        
        return ExportSummary(
            totalTransactions = transactions.size,
            totalIncome = totalIncome,
            totalExpense = totalExpense,
            balance = totalIncome - totalExpense
        )
    }
}

data class ExportSummary(
    val totalTransactions: Int,
    val totalIncome: Double,
    val totalExpense: Double,
    val balance: Double
)
