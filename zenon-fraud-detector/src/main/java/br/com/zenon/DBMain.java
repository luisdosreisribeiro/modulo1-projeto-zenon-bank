package br.com.zenon;

import java.util.List;

public class DBMain {

    void main() {
        ConnectoinFactory.getConnection();
        IO.println("Conexão com DB criada com  sucesso! :)");

        var repository = new TransactionSQLRepository();

        TransactionIngestor transactionIngestor = new TransactionIngestor();
        long startTimeSQL = System.nanoTime();

        List<Transaction> transactions = transactionIngestor.read("data/PS_20174392719_1491204439457_log.csv");
        IO.println(transactions.size());
        IO.println("iniciando adição das transações no BD..");

        transactions.forEach(repository::save);

        long endTimeSQL = System.nanoTime();
        IO.println("Tempo de Inserção (ms): " + (endTimeSQL - startTimeSQL) / 1_000_000.0);


        repository.buscarPorNomeOrigem("C1231006815")
                .ifPresentOrElse(IO::println, () -> IO.println("Transacao não encontrada para: C1000001"));

    }
}
