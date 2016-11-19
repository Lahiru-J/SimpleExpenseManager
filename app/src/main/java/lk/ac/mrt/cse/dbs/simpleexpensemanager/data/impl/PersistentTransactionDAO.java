package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by LahiruJ
 */

public final class PersistentTransactionDAO implements TransactionDAO {

    private SQLiteOpenHelper dbHelper;

    public PersistentTransactionDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql_transaction = String.format("INSERT OR IGNORE INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                Schema.TableTransaction.TB_NAME,
                Schema.TableTransaction.TRANS_DATE,
                Schema.TableTransaction.ACC_NUMBER,
                Schema.TableTransaction.EXPENSE_TYPE,
                Schema.TableTransaction.AMOUNT);

        db.execSQL(sql_transaction, new Object[]{
                date.getTime(),
                accountNo,
                expenseType,
                amount});
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql_transaction = String.format("SELECT %s, %s, %s, %s FROM %s)",
                Schema.TableTransaction.TRANS_DATE,
                Schema.TableTransaction.ACC_NUMBER,
                Schema.TableTransaction.EXPENSE_TYPE,
                Schema.TableTransaction.AMOUNT,
                Schema.TableTransaction.TB_NAME);

        List<Transaction> results = new ArrayList<>();
        final Cursor c = db.rawQuery(sql_transaction, null);

        if (c.moveToFirst()) {
            do {
                results.add(new Transaction(
                        new Date(c.getLong(0)),
                        c.getString(1),
                        Enum.valueOf(ExpenseType.class, c.getString(2)),
                        c.getDouble(3)));
            } while (c.moveToNext());
        }
        c.close();

        return results;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String sql_transaction = String.format("SELECT %s, %s, %s, %s FROM %s LIMIT %s",
                Schema.TableTransaction.TRANS_DATE,
                Schema.TableTransaction.ACC_NUMBER,
                Schema.TableTransaction.EXPENSE_TYPE,
                Schema.TableTransaction.AMOUNT,
                Schema.TableTransaction.TB_NAME,
                limit);

        List<Transaction> results = new ArrayList<>();
        final Cursor c = db.rawQuery(sql_transaction, null);

        if (c.moveToFirst()) {
            do {
                results.add(new Transaction(
                        new Date(c.getLong(0)),
                        c.getString(1),
                        Enum.valueOf(ExpenseType.class, c.getString(2)),
                        c.getDouble(3)));
            } while (c.moveToNext());
        }
        c.close();

        return results;
    }
}
