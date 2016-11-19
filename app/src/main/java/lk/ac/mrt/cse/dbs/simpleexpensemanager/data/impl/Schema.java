/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.provider.BaseColumns;

class Schema {

    private Schema() {
    }

    static final String DATABASE_NAME = "simple_expense_managerdb";

    static class TableAccount implements BaseColumns {

        static final String ACC_NUMBER = "account_num";
        static final String BANK_NAME = "bank_name";
        static final String ACC_HOLDER_NAME = "account_holder_name";
        static final String BALANCE = "balance";
        static final String TB_NAME = "tb_account";
    }

    static class TableTransaction implements BaseColumns {

        static final String TRANS_DATE = "transaction_date";
        static final String AMOUNT = "amt";
        static final String EXPENSE_TYPE = "expense_type";
        static final String ACC_NUMBER = "account_num";
        static final String TB_NAME = "tb_transaction";
    }
}
