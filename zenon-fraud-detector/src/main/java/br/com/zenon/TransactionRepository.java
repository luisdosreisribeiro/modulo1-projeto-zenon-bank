package br.com.zenon;

import java.util.Optional;

public interface TransactionRepository {
    Optional<Transaction> buscarPorNome(String nome);
}
