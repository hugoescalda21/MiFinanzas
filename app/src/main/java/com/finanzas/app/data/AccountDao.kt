package com.finanzas.app.data

import androidx.room.*
import com.finanzas.app.data.model.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    
    @Query("SELECT * FROM accounts ORDER BY isDefault DESC, name ASC")
    fun getAllAccounts(): Flow<List<Account>>
    
    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Long): Account?
    
    @Query("SELECT * FROM accounts WHERE isDefault = 1 LIMIT 1")
    suspend fun getDefaultAccount(): Account?
    
    @Query("SELECT * FROM accounts WHERE isDefault = 1 LIMIT 1")
    fun getDefaultAccountFlow(): Flow<Account?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account): Long
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accounts: List<Account>)
    
    @Update
    suspend fun updateAccount(account: Account)
    
    @Delete
    suspend fun deleteAccount(account: Account)
    
    @Query("UPDATE accounts SET isDefault = 0")
    suspend fun clearDefaultAccounts()
    
    @Query("UPDATE accounts SET isDefault = 1 WHERE id = :accountId")
    suspend fun setDefaultAccount(accountId: Long)
    
    @Query("SELECT COUNT(*) FROM accounts")
    suspend fun getAccountCount(): Int
}
