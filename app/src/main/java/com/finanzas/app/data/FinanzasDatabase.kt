package com.finanzas.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.finanzas.app.data.model.Budget
import com.finanzas.app.data.model.Transaction

@Database(
    entities = [Transaction::class, Budget::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FinanzasDatabase : RoomDatabase() {
    
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    
    companion object {
        @Volatile
        private var INSTANCE: FinanzasDatabase? = null
        
        fun getDatabase(context: Context): FinanzasDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FinanzasDatabase::class.java,
                    "finanzas_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
