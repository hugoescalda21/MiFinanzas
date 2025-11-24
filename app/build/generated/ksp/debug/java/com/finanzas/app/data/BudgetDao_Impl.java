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
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.finanzas.app.data.model.Budget;
import com.finanzas.app.data.model.Category;
import java.lang.Class;
import java.lang.Exception;
import java.lang.IllegalStateException;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.time.YearMonth;
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
public final class BudgetDao_Impl implements BudgetDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Budget> __insertionAdapterOfBudget;

  private final Converters __converters = new Converters();

  private final EntityDeletionOrUpdateAdapter<Budget> __deletionAdapterOfBudget;

  private final EntityDeletionOrUpdateAdapter<Budget> __updateAdapterOfBudget;

  public BudgetDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBudget = new EntityInsertionAdapter<Budget>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `budgets` (`id`,`category`,`amount`,`yearMonth`,`alertThreshold`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Budget entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromCategory(entity.getCategory());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        statement.bindDouble(3, entity.getAmount());
        final String _tmp_1 = __converters.fromYearMonth(entity.getYearMonth());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        statement.bindLong(5, entity.getAlertThreshold());
      }
    };
    this.__deletionAdapterOfBudget = new EntityDeletionOrUpdateAdapter<Budget>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `budgets` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Budget entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfBudget = new EntityDeletionOrUpdateAdapter<Budget>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `budgets` SET `id` = ?,`category` = ?,`amount` = ?,`yearMonth` = ?,`alertThreshold` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Budget entity) {
        statement.bindLong(1, entity.getId());
        final String _tmp = __converters.fromCategory(entity.getCategory());
        if (_tmp == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, _tmp);
        }
        statement.bindDouble(3, entity.getAmount());
        final String _tmp_1 = __converters.fromYearMonth(entity.getYearMonth());
        if (_tmp_1 == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, _tmp_1);
        }
        statement.bindLong(5, entity.getAlertThreshold());
        statement.bindLong(6, entity.getId());
      }
    };
  }

  @Override
  public Object insertBudget(final Budget budget, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBudget.insertAndReturnId(budget);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBudget(final Budget budget, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBudget.handle(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateBudget(final Budget budget, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBudget.handle(budget);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Budget>> getAllBudgets() {
    final String _sql = "SELECT * FROM budgets ORDER BY yearMonth DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"budgets"}, new Callable<List<Budget>>() {
      @Override
      @NonNull
      public List<Budget> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfYearMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "yearMonth");
          final int _cursorIndexOfAlertThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "alertThreshold");
          final List<Budget> _result = new ArrayList<Budget>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Budget _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Category _tmpCategory;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCategory);
            }
            _tmpCategory = __converters.toCategory(_tmp);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final YearMonth _tmpYearMonth;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfYearMonth)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfYearMonth);
            }
            final YearMonth _tmp_2 = __converters.toYearMonth(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.YearMonth', but it was NULL.");
            } else {
              _tmpYearMonth = _tmp_2;
            }
            final int _tmpAlertThreshold;
            _tmpAlertThreshold = _cursor.getInt(_cursorIndexOfAlertThreshold);
            _item = new Budget(_tmpId,_tmpCategory,_tmpAmount,_tmpYearMonth,_tmpAlertThreshold);
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
  public Object getBudgetById(final long id, final Continuation<? super Budget> $completion) {
    final String _sql = "SELECT * FROM budgets WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Budget>() {
      @Override
      @Nullable
      public Budget call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfYearMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "yearMonth");
          final int _cursorIndexOfAlertThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "alertThreshold");
          final Budget _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Category _tmpCategory;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCategory);
            }
            _tmpCategory = __converters.toCategory(_tmp);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final YearMonth _tmpYearMonth;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfYearMonth)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfYearMonth);
            }
            final YearMonth _tmp_2 = __converters.toYearMonth(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.YearMonth', but it was NULL.");
            } else {
              _tmpYearMonth = _tmp_2;
            }
            final int _tmpAlertThreshold;
            _tmpAlertThreshold = _cursor.getInt(_cursorIndexOfAlertThreshold);
            _result = new Budget(_tmpId,_tmpCategory,_tmpAmount,_tmpYearMonth,_tmpAlertThreshold);
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
  public Flow<Budget> getTotalBudgetForMonth(final String yearMonth) {
    final String _sql = "\n"
            + "        SELECT * FROM budgets \n"
            + "        WHERE yearMonth = ? \n"
            + "        AND category IS NULL\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"budgets"}, new Callable<Budget>() {
      @Override
      @Nullable
      public Budget call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfYearMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "yearMonth");
          final int _cursorIndexOfAlertThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "alertThreshold");
          final Budget _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Category _tmpCategory;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCategory);
            }
            _tmpCategory = __converters.toCategory(_tmp);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final YearMonth _tmpYearMonth;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfYearMonth)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfYearMonth);
            }
            final YearMonth _tmp_2 = __converters.toYearMonth(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.YearMonth', but it was NULL.");
            } else {
              _tmpYearMonth = _tmp_2;
            }
            final int _tmpAlertThreshold;
            _tmpAlertThreshold = _cursor.getInt(_cursorIndexOfAlertThreshold);
            _result = new Budget(_tmpId,_tmpCategory,_tmpAmount,_tmpYearMonth,_tmpAlertThreshold);
          } else {
            _result = null;
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
  public Flow<Budget> getCategoryBudgetForMonth(final String yearMonth, final Category category) {
    final String _sql = "\n"
            + "        SELECT * FROM budgets \n"
            + "        WHERE yearMonth = ? \n"
            + "        AND category = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    _argIndex = 2;
    final String _tmp = __converters.fromCategory(category);
    if (_tmp == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, _tmp);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[] {"budgets"}, new Callable<Budget>() {
      @Override
      @Nullable
      public Budget call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfYearMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "yearMonth");
          final int _cursorIndexOfAlertThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "alertThreshold");
          final Budget _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Category _tmpCategory;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfCategory);
            }
            _tmpCategory = __converters.toCategory(_tmp_1);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final YearMonth _tmpYearMonth;
            final String _tmp_2;
            if (_cursor.isNull(_cursorIndexOfYearMonth)) {
              _tmp_2 = null;
            } else {
              _tmp_2 = _cursor.getString(_cursorIndexOfYearMonth);
            }
            final YearMonth _tmp_3 = __converters.toYearMonth(_tmp_2);
            if (_tmp_3 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.YearMonth', but it was NULL.");
            } else {
              _tmpYearMonth = _tmp_3;
            }
            final int _tmpAlertThreshold;
            _tmpAlertThreshold = _cursor.getInt(_cursorIndexOfAlertThreshold);
            _result = new Budget(_tmpId,_tmpCategory,_tmpAmount,_tmpYearMonth,_tmpAlertThreshold);
          } else {
            _result = null;
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
  public Flow<List<Budget>> getBudgetsForMonth(final String yearMonth) {
    final String _sql = "\n"
            + "        SELECT * FROM budgets \n"
            + "        WHERE yearMonth = ?\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, yearMonth);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"budgets"}, new Callable<List<Budget>>() {
      @Override
      @NonNull
      public List<Budget> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "amount");
          final int _cursorIndexOfYearMonth = CursorUtil.getColumnIndexOrThrow(_cursor, "yearMonth");
          final int _cursorIndexOfAlertThreshold = CursorUtil.getColumnIndexOrThrow(_cursor, "alertThreshold");
          final List<Budget> _result = new ArrayList<Budget>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Budget _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final Category _tmpCategory;
            final String _tmp;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getString(_cursorIndexOfCategory);
            }
            _tmpCategory = __converters.toCategory(_tmp);
            final double _tmpAmount;
            _tmpAmount = _cursor.getDouble(_cursorIndexOfAmount);
            final YearMonth _tmpYearMonth;
            final String _tmp_1;
            if (_cursor.isNull(_cursorIndexOfYearMonth)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getString(_cursorIndexOfYearMonth);
            }
            final YearMonth _tmp_2 = __converters.toYearMonth(_tmp_1);
            if (_tmp_2 == null) {
              throw new IllegalStateException("Expected NON-NULL 'java.time.YearMonth', but it was NULL.");
            } else {
              _tmpYearMonth = _tmp_2;
            }
            final int _tmpAlertThreshold;
            _tmpAlertThreshold = _cursor.getInt(_cursorIndexOfAlertThreshold);
            _item = new Budget(_tmpId,_tmpCategory,_tmpAmount,_tmpYearMonth,_tmpAlertThreshold);
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
