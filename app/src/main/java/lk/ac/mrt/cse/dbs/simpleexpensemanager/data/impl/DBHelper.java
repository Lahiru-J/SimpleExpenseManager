package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

/**
 * Created by LahiruJ
 */

public class DBHelper extends SQLiteOpenHelper implements Serializable {

    public static final int database_version = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    /* TableAccount SQL queries */
    private static final String CREATE_ENTRIES_ACCOUNT =
            "CREATE TABLE " + Schema.TableAccount.TB_NAME + " (" +
                    Schema.TableAccount.ACC_NUMBER + TEXT_TYPE + " PRIMARY KEY," +
                    Schema.TableAccount.BANK_NAME + TEXT_TYPE + COMMA_SEP +
                    Schema.TableAccount.ACC_HOLDER_NAME + TEXT_TYPE + COMMA_SEP +
                    Schema.TableAccount.BALANCE + " REAL" + " )";

    private static final String DELETE_ENTRIES_ACCOUNT =
            "DROP TABLE IF EXISTS " + Schema.TableAccount.TB_NAME;

    /* TableTransaction SQL queries */
    private static final String CREATE_ENTRIES_TRANSACTIONS =
            "CREATE TABLE " + Schema.TableTransaction.TB_NAME + " (" +
                    Schema.TableTransaction._ID + " INTEGER PRIMARY KEY"+COMMA_SEP +
                    Schema.TableTransaction.TRANS_DATE + " INT"+COMMA_SEP +
                    Schema.TableTransaction.AMOUNT + " REAL" + COMMA_SEP +
                    Schema.TableTransaction.EXPENSE_TYPE + TEXT_TYPE + COMMA_SEP +
                    Schema.TableTransaction.ACC_NUMBER + TEXT_TYPE + COMMA_SEP +
                    "FOREIGN KEY (" + Schema.TableAccount.ACC_NUMBER + ") REFERENCES " +
                    Schema.TableAccount.TB_NAME + "(" + Schema.TableAccount.ACC_NUMBER + "))";

    private static final String DELETE_ENTRIES_TRANSACTION =
            "DROP TABLE IF EXISTS " + Schema.TableTransaction.TB_NAME;

    public DBHelper(Context context) {
        super(context, Schema.DATABASE_NAME, null, database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(CREATE_ENTRIES_ACCOUNT);
        sdb.execSQL(CREATE_ENTRIES_TRANSACTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_ENTRIES_ACCOUNT);
        db.execSQL(DELETE_ENTRIES_TRANSACTION);
        onCreate(db);
    }
}
