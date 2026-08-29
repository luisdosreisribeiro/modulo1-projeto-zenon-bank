package br.com.zenon;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FraudAnalyser {

    private final List<Transaction> transactions;

    public FraudAnalyser(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public long countFrauds() {
        return fraudStram()
                .count();

    }

    public List<BigDecimal> listarTop3Amount() {
        return fraudStram()
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(Transaction::amount)
                .limit(3)
                .toList();
    }


    public List<String> listarNomesSusteitos() {
        return transactions.stream()
                .filter(Transaction::isFraud)
                .sorted(Comparator.comparing(Transaction::amount).reversed())
                .map(transaction -> transaction.origin().name())
                .distinct()
                .limit(5).toList();
    }

    public BigDecimal obterPrejuizoTotalDeFraudes() {
        return fraudStram()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    public Map<TransactionType, Long> totalDeFraudesPorTipo() {
        return fraudStram()
                .collect(Collectors.groupingBy(Transaction::type, Collectors.counting()));
    }

    private Stream<Transaction> fraudStram() {
        return transactions.stream()
                .filter(Transaction::isFraud);
    }
}
