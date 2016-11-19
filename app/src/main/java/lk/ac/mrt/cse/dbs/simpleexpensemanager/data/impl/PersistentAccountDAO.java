package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by LahiruJ
 */

public class PersistentAccountDAO implements AccountDAO {

    private SQLiteOpenHelper helper;

    public PersistentAccountDAO(Context context) {
        helper = new DBHelper(context);
    }

    @Override
    public List<String> getAccountNumbersList() {

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = String.format(
                "SELECT %s FROM %s",
                Schema.TableAccount.ACC_NUMBER,
                Schema.TableAccount.TB_NAME);

        List<String> results = new ArrayList<>();
        final Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                results.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();

        return results;
    }

    @Override
    public List<Account> getAccountsList() {

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = String.format("SELECT %s, %s, %s, %s FROM %s",
                Schema.TableAccount.ACC_NUMBER,
                Schema.TableAccount.BANK_NAME,
                Schema.TableAccount.ACC_HOLDER_NAME,
                Schema.TableAccount.BALANCE,
                Schema.TableAccount.TB_NAME);

        List<Account> results = new ArrayList<>();
        final Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                results.add(new Account(c.getString(0), c.getString(1), c.getString(2), c.getDouble(3)));
            } while (c.moveToNext());
        }
        c.close();

        return results;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = String.format("SELECT %s, %s, %s, %s FROM %s WHERE %s = ?",
                Schema.TableAccount.ACC_NUMBER,
                Schema.TableAccount.BANK_NAME,
                Schema.TableAccount.ACC_HOLDER_NAME,
                Schema.TableAccount.BALANCE,
                Schema.TableAccount.TB_NAME,
                Schema.TableAccount.ACC_NUMBER);

        final Cursor c = db.rawQuery(sql, new String[]{accountNo});
        c.moveToFirst();

        if (!c.getString(0).equals(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }

        Account account = new Account(c.getString(0), c.getString(1), c.getString(2), c.getDouble(3));
        c.close();
        return account;
    }

    @Override
    public void addAccount(Account account) {

        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = String.format("INSERT OR IGNORE INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
                Schema.TableAccount.TB_NAME,
                Schema.TableAccount.ACC_NUMBER,
                Schema.TableAccount.BANK_NAME,
                Schema.TableAccount.ACC_HOLDER_NAME,
                Schema.TableAccount.BALANCE);

        db.execSQL(sql, new Object[]{
                account.getAccountNo(),
                account.getBankName(),
                account.getAccountHolderName(),
                account.getBalance()});
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        // Try to get account from db. Method throws an InvalidAccountException if not found
        getAccount(accountNo);

        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = String.format("DELETE FROM %s WHERE %s = ?",
                Schema.TableAccount.TB_NAME,
                Schema.TableAccount.ACC_NUMBER);

        db.execSQL(sql, new Object[]{accountNo});
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        // Try to get account from db. Method throws an InvalidAccountException if not found
        getAccount(accountNo);

        SQLiteDatabase db = helper.getWritableDatabase();

        String sql_account = null;
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                sql_account = "UPDATE %s SET %s = %s - ? WHERE %s = ?";
                break;
            case INCOME:
                sql_account = "UPDATE %s SET %s = %s + ? WHERE %s = ?";
                break;
        }
        sql_account = String.format(sql_account,
                Schema.TableAccount.TB_NAME,
                Schema.TableAccount.BALANCE,
                Schema.TableAccount.BALANCE,
                Schema.TableAccount.ACC_NUMBER);

        db.execSQL(sql_account, new Object[]{amount, accountNo});
    }
}
