package br.com.zenon;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionMapRepository implements TransactionRepository{

    private final Map<String, Transaction> transactionByOriginName;

    public TransactionMapRepository(List<Transaction> transactions) {

        this.transactionByOriginName =
                transactions
                        .stream()
                        .collect(Collectors.toMap(transaction -> transaction.origin().name(),
                                transaction -> transaction));
    }




    @Override
    public Optional<Transaction> buscarPorNome(String nome) {
        return Optional.ofNullable(transactionByOriginName.get(nome));
    }
}
