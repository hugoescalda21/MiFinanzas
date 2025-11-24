package com.finanzas.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.finanzas.app.data.model.Account
import com.finanzas.app.data.model.AccountType
import com.finanzas.app.data.model.Budget
import com.finanzas.app.data.model.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Transaction::class, Budget::class, Account::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class FinanzasDatabase : RoomDatabase() {
    
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun accountDao(): AccountDao
    
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
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Insert default accounts on first creation
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.accountDao().insertAccounts(
                                        AccountType.getDefaultAccounts()
                                    )
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
