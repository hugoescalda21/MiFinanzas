package com.finanzas.app.data.repository

import com.finanzas.app.data.AccountDao
import com.finanzas.app.data.model.Account
import kotlinx.coroutines.flow.Flow

class AccountRepository(private val accountDao: AccountDao) {
    
    val allAccounts: Flow<List<Account>> = accountDao.getAllAccounts()
    
    val defaultAccount: Flow<Account?> = accountDao.getDefaultAccountFlow()
    
    suspend fun getAccountById(id: Long): Account? {
        return accountDao.getAccountById(id)
    }
    
    suspend fun getDefaultAccount(): Account? {
        return accountDao.getDefaultAccount()
    }
    
    suspend fun insertAccount(account: Account): Long {
        return accountDao.insertAccount(account)
    }
    
    suspend fun updateAccount(account: Account) {
        accountDao.updateAccount(account)
    }
    
    suspend fun deleteAccount(account: Account) {
        accountDao.deleteAccount(account)
    }
    
    suspend fun setDefaultAccount(accountId: Long) {
        accountDao.clearDefaultAccounts()
        accountDao.setDefaultAccount(accountId)
    }
    
    suspend fun getAccountCount(): Int {
        return accountDao.getAccountCount()
    }
}
