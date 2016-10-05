package com.odde.bbuddy.transaction.view;

import com.nitorcreations.junit.runners.NestedRunner;
import com.odde.bbuddy.transaction.domain.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.odde.bbuddy.common.Formats.parseDay;
import static com.odde.bbuddy.common.controller.Urls.TRANSACTION_INDEX;
import static com.odde.bbuddy.transaction.domain.Transaction.Type;
import static com.odde.bbuddy.transaction.domain.Transaction.Type.Income;
import static com.odde.bbuddy.transaction.domain.Transaction.Type.Outcome;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(NestedRunner.class)
public class PresentableTransactionsTest {

    PresentableTransactions presentableTransactions = new PresentableTransactions("whatever message");

    @Test
    public void should_go_to_index_page() {
        assertThat(presentableTransactions.getViewName()).isEqualTo(TRANSACTION_INDEX);
    }

    public class NoTransaction {

        @Test
        public void should_hide_list_view() {
            assertThat(modelOf(presentableTransactions).get("hidden")).isEqualTo("hidden");
        }

        @Test
        public void should_show_message() {
            presentableTransactions = new PresentableTransactions("no transaction message");

            assertThat(modelOf(presentableTransactions).get("message")).isEqualTo("no transaction message");
        }

    }

    public class HasSomeTransactions {

        private Date date = parseDay("2016-07-01");
        private int amount = 100;

        @Test
        public void should_not_hide_list_view() {
            displayTransaction();

            assertThat(modelOf(presentableTransactions)).doesNotContainKey("hidden");
        }

        @Test
        public void should_not_show_message() {
            displayTransaction();

            assertThat(modelOf(presentableTransactions)).doesNotContainKey("message");
        }

        @Test
        public void should_pass_transaction_to_page() {
            presentableTransactions.display(transaction(Income, "Income description", date, amount));
            presentableTransactions.display(transaction(Outcome, "Outcome description", date, amount));

            assertThat((List<PresentableTransaction>) modelOf(presentableTransactions).get("transactions"))
                    .usingFieldByFieldElementComparator()
                    .containsExactly(
                            presentableTransaction(Income, "Income description", date, amount),
                            presentableTransaction(Outcome, "Outcome description", date, amount));
        }

        private void displayTransaction() {
            presentableTransactions.display(transaction(Income, "description", date, amount));
        }

        private PresentableTransaction presentableTransaction(Type type, String description, Date date, int amount) {
            PresentableTransaction presentableTransaction = new PresentableTransaction();
            presentableTransaction.setType(type);
            presentableTransaction.setDescription(description);
            presentableTransaction.setDate(date);
            presentableTransaction.setAmount(amount);
            return presentableTransaction;
        }

        private Transaction transaction(Type type, String description, Date date, int amount) {
            Transaction transaction = new Transaction();
            transaction.setType(type);
            transaction.setDescription(description);
            transaction.setDate(date);
            transaction.setAmount(amount);
            return transaction;
        }

    }

    private Map<String, Object> modelOf(PresentableTransactions presentableTransactions) {
        return presentableTransactions.getModel();
    }

}