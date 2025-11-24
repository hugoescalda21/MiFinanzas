package com.finanzas.app.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.finanzas.app.data.model.Category;
import com.finanzas.app.data.model.RecurringPeriod;
import com.finanzas.app.data.model.Transaction;
import com.finanzas.app.data.model.TransactionType;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TransactionDao_Impl implements TransactionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Transaction> __insertionAdapterOfTransaction;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Transaction> __deletionAdapterOfTransaction;

  private final EntityDeletionOrUpdateAdapter<Transaction> __updateAdapterOfTransaction;

  private final SharedSQLiteStatement __preparedStmtOfDeleteTransactionById;

  public TransactionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTransaction = new EntityInsertionAdapter<Transaction>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `transactions` (`id`,`amount`,`description`,`category`,`type`,`date`,`note`,`isRecurring`,`recurringPeriod`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Transaction entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getAmount());
        statement.bindString(3, entity.getDescription());
        final String _tmp = __converters.fromCategory(entity.getCategory());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        final String _tmp_1 = __converters.fromTransactionType(entity.getType());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        final String _tmp_2 = __converters.fromLocalDateTime(entity.getDate());
        if (_tmp_2 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_2);
        }
        statement.bindString(7, entity.getNote());
        final int _tmp_3 = entity.isRecurring() ? 1 : 0;
        statement.bindLong(8, _tmp_3);
        final String _tmp_4 = __converters.fromRecurringPeriod(entity.getRecurringPeriod());
        if (_tmp_4 == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp_4);
        }
      }
    };
    this.__deletionAdapterOfTransaction = new EntityDeletionOrUpdateAdapter<Transaction>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `transactions` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Transaction entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfTransaction = new EntityDeletionOrUpdateAdapter<Transaction>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `transactions` SET `id` = ?,`amount` = ?,`description` = ?,`category` = ?,`type` = ?,`date` = ?,`note` = ?,`isRecurring` = ?,`recurringPeriod` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Transaction entity) {
        statement.bindLong(1, entity.getId());
        statement.bindDouble(2, entity.getAmount());
        statement.bindString(3, entity.getDescription());
        final String _tmp = __converters.fromCategory(entity.getCategory());
        if (_tmp == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp);
        }
        final String _tmp_1 = __converters.fromTransactionType(entity.getType());
        if (_tmp_1 == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, _tmp_1);
        }
        final String _tmp_2 = __converters.fromLocalDateTime(entity.getDate());
        if (_tmp_2 == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, _tmp_2);
        }
        statement.bindString(7, entity.getNote());
        final int _tmp_3 = entity.isRecurring() ? 1 : 0;
        statement.bindLong(8, _tmp_3);
        final String _tmp_4 = __converters.fromRecurringPeriod(entity.getRecurringPeriod());
        if (_tmp_4 == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, _tmp_4);
        }
        statement.bindLong(10, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteTransactionById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM transactions WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertTransaction(final Transaction transaction,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfTransaction.insertAndReturnId(transaction);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTransaction(final Transaction transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfTransaction.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTransaction(final Transaction transaction,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfTransaction.handle(transaction);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteTransactionById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteTransactionById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteTransactionById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Transaction>> getAllTransactions() {
    final String _sql = "SELECT * FROM transactions ORDER BY date DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<Transaction>>() {
      @Override
      @NonNull
      public List<Transaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfIsRecurring = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurring");
          final int _cursorIndexOfRecurringPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPeriod");
          final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transaction _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final Category _tmpCategory;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCategory);
            }
            final Category _tmp_1 = __converters.toCategory(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.Category', but it was NULL.");
            } else {
              _tmpCategory = _tmp_1;
            }
            final TransactionType _tmpType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_3 = __converters.toTransactionType(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_3;
            }
            final LocalDateTime _tmpDate;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfDate);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpDate = _tmp_5;
            }
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final boolean _tmpIsRecurring;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsRecurring);
            _tmpIsRecurring = _tmp_6 != 0;
            final RecurringPeriod _tmpRecurringPeriod;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfRecurringPeriod)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfRecurringPeriod);
            }
            _tmpRecurringPeriod = __converters.toRecurringPeriod(_tmp_7);
            _item = new Transaction(_tmpId,_tmpAmount,_tmpDescription,_tmpCategory,_tmpType,_tmpDate,_tmpNote,_tmpIsRecurring,_tmpRecurringPeriod);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getTransactionById(final long id,
      final Continuation<? super Transaction> $completion) {
    final String _sql = "SELECT * FROM transactions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Transaction>() {
      @Override
      @Nullable
      public Transaction call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfIsRecurring = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurring");
          final int _cursorIndexOfRecurringPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPeriod");
          final Transaction _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final Category _tmpCategory;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCategory);
            }
            final Category _tmp_1 = __converters.toCategory(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.Category', but it was NULL.");
            } else {
              _tmpCategory = _tmp_1;
            }
            final TransactionType _tmpType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_3 = __converters.toTransactionType(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_3;
            }
            final LocalDateTime _tmpDate;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfDate);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpDate = _tmp_5;
            }
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final boolean _tmpIsRecurring;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsRecurring);
            _tmpIsRecurring = _tmp_6 != 0;
            final RecurringPeriod _tmpRecurringPeriod;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfRecurringPeriod)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfRecurringPeriod);
            }
            _tmpRecurringPeriod = __converters.toRecurringPeriod(_tmp_7);
            _result = new Transaction(_tmpId,_tmpAmount,_tmpDescription,_tmpCategory,_tmpType,_tmpDate,_tmpNote,_tmpIsRecurring,_tmpRecurringPeriod);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Transaction>> getCurrentMonthTransactions() {
    final String _sql = "\n"
            + "        SELECT * FROM transactions \n"
            + "        WHERE strftime('%Y-%m', date) = strftime('%Y-%m', 'now', 'localtime')\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<Transaction>>() {
      @Override
      @NonNull
      public List<Transaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfIsRecurring = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurring");
          final int _cursorIndexOfRecurringPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPeriod");
          final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transaction _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final Category _tmpCategory;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCategory);
            }
            final Category _tmp_1 = __converters.toCategory(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.Category', but it was NULL.");
            } else {
              _tmpCategory = _tmp_1;
            }
            final TransactionType _tmpType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_3 = __converters.toTransactionType(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_3;
            }
            final LocalDateTime _tmpDate;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfDate);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpDate = _tmp_5;
            }
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final boolean _tmpIsRecurring;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsRecurring);
            _tmpIsRecurring = _tmp_6 != 0;
            final RecurringPeriod _tmpRecurringPeriod;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfRecurringPeriod)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfRecurringPeriod);
            }
            _tmpRecurringPeriod = __converters.toRecurringPeriod(_tmp_7);
            _item = new Transaction(_tmpId,_tmpAmount,_tmpDescription,_tmpCategory,_tmpType,_tmpDate,_tmpNote,_tmpIsRecurring,_tmpRecurringPeriod);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Transaction>> getTransactionsByMonth(final String yearMonth) {
    final String _sql = "\n"
            + "        SELECT * FROM transactions \n"
            + "        WHERE strftime('%Y-%m', date) = ?\n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<Transaction>>() {
      @Override
      @NonNull
      public List<Transaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfIsRecurring = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurring");
          final int _cursorIndexOfRecurringPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPeriod");
          final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transaction _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final Category _tmpCategory;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCategory);
            }
            final Category _tmp_1 = __converters.toCategory(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.Category', but it was NULL.");
            } else {
              _tmpCategory = _tmp_1;
            }
            final TransactionType _tmpType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_3 = __converters.toTransactionType(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_3;
            }
            final LocalDateTime _tmpDate;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfDate);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpDate = _tmp_5;
            }
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final boolean _tmpIsRecurring;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsRecurring);
            _tmpIsRecurring = _tmp_6 != 0;
            final RecurringPeriod _tmpRecurringPeriod;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfRecurringPeriod)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfRecurringPeriod);
            }
            _tmpRecurringPeriod = __converters.toRecurringPeriod(_tmp_7);
            _item = new Transaction(_tmpId,_tmpAmount,_tmpDescription,_tmpCategory,_tmpType,_tmpDate,_tmpNote,_tmpIsRecurring,_tmpRecurringPeriod);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Transaction>> getTransactionsByType(final TransactionType type) {
    final String _sql = "\n"
            + "        SELECT * FROM transactions \n"
            + "        WHERE type = ? \n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromTransactionType(type);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<Transaction>>() {
      @Override
      @NonNull
      public List<Transaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfIsRecurring = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurring");
          final int _cursorIndexOfRecurringPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPeriod");
          final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transaction _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final Category _tmpCategory;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfCategory);
            }
            final Category _tmp_2 = __converters.toCategory(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.Category', but it was NULL.");
            } else {
              _tmpCategory = _tmp_2;
            }
            final TransactionType _tmpType;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_4 = __converters.toTransactionType(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_4;
            }
            final LocalDateTime _tmpDate;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfDate);
            }
            final LocalDateTime _tmp_6 = __converters.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpDate = _tmp_6;
            }
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final boolean _tmpIsRecurring;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsRecurring);
            _tmpIsRecurring = _tmp_7 != 0;
            final RecurringPeriod _tmpRecurringPeriod;
            final String _tmp_8;
            if (_cursor.isNull(_cursorIndexOfRecurringPeriod)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getString(_cursorIndexOfRecurringPeriod);
            }
            _tmpRecurringPeriod = __converters.toRecurringPeriod(_tmp_8);
            _item = new Transaction(_tmpId,_tmpAmount,_tmpDescription,_tmpCategory,_tmpType,_tmpDate,_tmpNote,_tmpIsRecurring,_tmpRecurringPeriod);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Transaction>> getTransactionsByCategory(final Category category) {
    final String _sql = "\n"
            + "        SELECT * FROM transactions \n"
            + "        WHERE category = ? \n"
            + "        ORDER BY date DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromCategory(category);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<Transaction>>() {
      @Override
      @NonNull
      public List<Transaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfIsRecurring = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurring");
          final int _cursorIndexOfRecurringPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPeriod");
          final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transaction _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final Category _tmpCategory;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfCategory);
            }
            final Category _tmp_2 = __converters.toCategory(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.Category', but it was NULL.");
            } else {
              _tmpCategory = _tmp_2;
            }
            final TransactionType _tmpType;
            final String _tmp_3;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp_3 = null;
            } else {
              _tmp_3 = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_4 = __converters.toTransactionType(_tmp_3);
            if (_tmp_4 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_4;
            }
            final LocalDateTime _tmpDate;
            final String _tmp_5;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_5 = null;
            } else {
              _tmp_5 = _cursor.getString(_cursorIndexOfDate);
            }
            final LocalDateTime _tmp_6 = __converters.toLocalDateTime(_tmp_5);
            if (_tmp_6 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpDate = _tmp_6;
            }
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final boolean _tmpIsRecurring;
            final int _tmp_7;
            _tmp_7 = _cursor.getInt(_cursorIndexOfIsRecurring);
            _tmpIsRecurring = _tmp_7 != 0;
            final RecurringPeriod _tmpRecurringPeriod;
            final String _tmp_8;
            if (_cursor.isNull(_cursorIndexOfRecurringPeriod)) {
              _tmp_8 = null;
            } else {
              _tmp_8 = _cursor.getString(_cursorIndexOfRecurringPeriod);
            }
            _tmpRecurringPeriod = __converters.toRecurringPeriod(_tmp_8);
            _item = new Transaction(_tmpId,_tmpAmount,_tmpDescription,_tmpCategory,_tmpType,_tmpDate,_tmpNote,_tmpIsRecurring,_tmpRecurringPeriod);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Double> getCurrentMonthTotal(final TransactionType type) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(amount), 0) FROM transactions \n"
            + "        WHERE type = ? \n"
            + "        AND strftime('%Y-%m', date) = strftime('%Y-%m', 'now', 'localtime')\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    final String _tmp = __converters.fromTransactionType(type);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp_1;
            _tmp_1 = _cursor.getDouble(0);
            _result = _tmp_1;
          } else {
            _result = 0.0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Double> getMonthTotal(final TransactionType type, final String yearMonth) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(amount), 0) FROM transactions \n"
            + "        WHERE type = ? \n"
            + "        AND strftime('%Y-%m', date) = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __converters.fromTransactionType(type);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    _statement.bindString(_argIndex, yearMonth);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp_1;
            _tmp_1 = _cursor.getDouble(0);
            _result = _tmp_1;
          } else {
            _result = 0.0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Double> getTotalBalance() {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(CASE WHEN type = 'INCOME' THEN amount ELSE -amount END), 0) \n"
            + "        FROM transactions\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp;
            _tmp = _cursor.getDouble(0);
            _result = _tmp;
          } else {
            _result = 0.0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<Double> getMonthExpenseByCategory(final Category category, final String yearMonth) {
    final String _sql = "\n"
            + "        SELECT COALESCE(SUM(amount), 0) FROM transactions \n"
            + "        WHERE type = 'EXPENSE' \n"
            + "        AND category = ?\n"
            + "        AND strftime('%Y-%m', date) = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    final String _tmp = __converters.fromCategory(category);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    _argIndex = 2;
    _statement.bindString(_argIndex, yearMonth);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<Double>() {
      @Override
      @NonNull
      public Double call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Double _result;
          if (_cursor.moveToFirst()) {
            final double _tmp_1;
            _tmp_1 = _cursor.getDouble(0);
            _result = _tmp_1;
          } else {
            _result = 0.0;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Transaction>> getRecentTransactions(final int limit) {
    final String _sql = "\n"
            + "        SELECT * FROM transactions \n"
            + "        ORDER BY date DESC \n"
            + "        LIMIT ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<Transaction>>() {
      @Override
      @NonNull
      public List<Transaction> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfNote = CursorUtil.getColumnIndexOrThrow(_cursor, "note");
          final int _cursorIndexOfIsRecurring = CursorUtil.getColumnIndexOrThrow(_cursor, "isRecurring");
          final int _cursorIndexOfRecurringPeriod = CursorUtil.getColumnIndexOrThrow(_cursor, "recurringPeriod");
          final List<Transaction> _result = new ArrayList<Transaction>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Transaction _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final Category _tmpCategory;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCategory);
            }
            final Category _tmp_1 = __converters.toCategory(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.Category', but it was NULL.");
            } else {
              _tmpCategory = _tmp_1;
            }
            final TransactionType _tmpType;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfType);
            }
            final TransactionType _tmp_3 = __converters.toTransactionType(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.TransactionType', but it was NULL.");
            } else {
              _tmpType = _tmp_3;
            }
            final LocalDateTime _tmpDate;
            final String _tmp_4;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp_4 = null;
            } else {
              _tmp_4 = _cursor.getString(_cursorIndexOfDate);
            }
            final LocalDateTime _tmp_5 = __converters.toLocalDateTime(_tmp_4);
            if (_tmp_5 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.LocalDateTime', but it was NULL.");
            } else {
              _tmpDate = _tmp_5;
            }
            final String _tmpNote;
            _tmpNote = _cursor.getString(_cursorIndexOfNote);
            final boolean _tmpIsRecurring;
            final int _tmp_6;
            _tmp_6 = _cursor.getInt(_cursorIndexOfIsRecurring);
            _tmpIsRecurring = _tmp_6 != 0;
            final RecurringPeriod _tmpRecurringPeriod;
            final String _tmp_7;
            if (_cursor.isNull(_cursorIndexOfRecurringPeriod)) {
              _tmp_7 = null;
            } else {
              _tmp_7 = _cursor.getString(_cursorIndexOfRecurringPeriod);
            }
            _tmpRecurringPeriod = __converters.toRecurringPeriod(_tmp_7);
            _item = new Transaction(_tmpId,_tmpAmount,_tmpDescription,_tmpCategory,_tmpType,_tmpDate,_tmpNote,_tmpIsRecurring,_tmpRecurringPeriod);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<CategorySum>> getExpensesByCategory(final String yearMonth) {
    final String _sql = "\n"
            + "        SELECT category, SUM(amount) as total \n"
            + "        FROM transactions \n"
            + "        WHERE type = 'EXPENSE' \n"
            + "        AND strftime('%Y-%m', date) = ?\n"
            + "        GROUP BY category\n"
            + "        ORDER BY total DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"transactions"}, new Callable<List<CategorySum>>() {
      @Override
      @NonNull
      public List<CategorySum> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCategory = 0;
          final int _cursorIndexOfTotal = 1;
          final List<CategorySum> _result = new ArrayList<CategorySum>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CategorySum _item;
            final Category _tmpCategory;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCategory);
            }
            final Category _tmp_1 = __converters.toCategory(_tmp);
            if (_tmp_1 == null) {
              throw new IllegalStateException("Expected NON-NULL 'com.finanzas.app.data.model.Category', but it was NULL.");
            } else {
              _tmpCategory = _tmp_1;
            }
            final double _tmpTotal;
            _tmpTotal = _cursor.getDouble(_cursorIndexOfTotal);
            _item = new CategorySum(_tmpCategory,_tmpTotal);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
