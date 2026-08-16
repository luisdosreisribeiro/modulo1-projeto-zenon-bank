package br.com.zenon;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class TransactionIngestor {

    public List<Transaction> read(String fileName) {
        Path path = Path.of(fileName);

        try {
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
                    .limit(1000)
                    .map(this::parseTransaction)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler o arquivo:" + fileName, ex);
        }

    }

    private Optional<Transaction> parseTransaction(String line) {
        try {
            String[] trechos = line.split(",");

            int step = Integer.parseInt(trechos[0]);
            TransactionType type = TransactionType.valueOf(trechos[1]);

            if(trechos[2] == null || trechos[2].trim().isEmpty()) throw new IllegalArgumentException("O valor de amount " +
                    "não pode ser nulo me vazio.");
            BigDecimal amount = new BigDecimal(trechos[2]);

            var origin = new TransactionCustomer(trechos[3], new BigDecimal(trechos[4]), new BigDecimal(trechos[5]));
            var recipient = new TransactionCustomer(trechos[6], new BigDecimal(trechos[7]), new BigDecimal(trechos[8]));

            boolean isFraud = "1".equals(trechos[9]);
            boolean isFlaggedFraud = "1".equals(trechos[10]);

            return Optional.of(new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud));
        } catch (Exception e) {
           System.err.println("Erro ao fazer parse: " + line + " | " + e);
           return Optional.empty();
        }
    }
}
