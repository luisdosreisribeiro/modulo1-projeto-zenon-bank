package br.com.zenon;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TransactionListRepository implements TransactionRepository {

    private final List<Transaction> transactions;

    public TransactionListRepository(List<Transaction> transactions) {
        Objects.requireNonNull(transactions);
        this.transactions = transactions;
    }

    @Override
    public Optional<Transaction> buscarPorNome(String nome) {
        return transactions
                .stream()
                .filter(transaction -> transaction.origin().name().equalsIgnoreCase(nome))
                .findFirst();

    }
}
