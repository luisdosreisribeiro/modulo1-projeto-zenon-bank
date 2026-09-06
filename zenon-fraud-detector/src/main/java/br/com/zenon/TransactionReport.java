package br.com.zenon;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class TransactionReport {

    public record ReportTransacastion(BigDecimal amount, boolean isFraud){

}

    public record Statistics(long totalTransactions, long totalFrauds, BigDecimal totalAmount) {

        private static final Statistics ZERO = new Statistics(0,0, BigDecimal.ZERO);

        private Statistics addReportTransaction(ReportTransacastion rt) {
             return new Statistics(totalTransactions + 1,
                    totalFrauds + (rt.isFraud ? 1 : 0)
                    ,totalAmount.add(rt.amount));
        }

        private Statistics add(Statistics other) {
            return new Statistics(totalTransactions + other.totalTransactions,
                    totalFrauds + other.totalFrauds,
                    totalAmount.add(other.totalAmount));
        }
    }

    public Statistics generateReport(String fileName) {
        Path path = Path.of(fileName);

        try (Stream<String> lines = Files.lines(path)) {
             return lines
                    .skip(1)
                     .map(this::parseReportTransaction)
                     .filter(Optional::isPresent)
                     .map(Optional::get)
                     .reduce(
                             Statistics.ZERO, Statistics::addReportTransaction, Statistics::add);

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler o arquivo:" + fileName, ex);
        }

    }

    private Optional<ReportTransacastion> parseReportTransaction(String line) {
        try {
            String[] trechos = line.split(",");

            if(trechos[2] == null || trechos[2].trim().isEmpty()) throw new IllegalArgumentException("O valor de amount " +
                    "não pode ser nulo me vazio.");
            BigDecimal amount = new BigDecimal(trechos[2]);

            boolean isFraud = "1".equals(trechos[9]);

            return Optional.of(new ReportTransacastion(amount, isFraud));
        } catch (Exception e) {
            System.err.println("Erro ao fazer parse: " + line + " | " + e);
            return Optional.empty();
        }
    }

}
