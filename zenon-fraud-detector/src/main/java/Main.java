import br.com.zenon.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;

public class Main {
    static void main() {

    var transacao1 =    new Transaction(1,
                TransactionType.PAYMENT,
                new BigDecimal("9839.64"),
                new TransactionCustomer("C1231006815", new BigDecimal("170136.0"), new BigDecimal("160296.36")),
                new TransactionCustomer("M1979787155", new BigDecimal("0.0"), new BigDecimal("0.0")),
                false,
                false
        );

        var transacao2 =new Transaction(743,
                TransactionType.CASH_OUT,
                new BigDecimal("850002.52"),
                new TransactionCustomer("C1280323807", new BigDecimal("850002.52"), new BigDecimal("0.0")),
                new TransactionCustomer("C873221189", new BigDecimal("6510099.11"), new BigDecimal("7360101.63")),
                true,
                false
        );

        IO.println(transacao1);
        IO.println(transacao2);

        IO.println("-------------------------------------------");

        var transactionIngestor = new TransactionIngestor();
        List<Transaction> transactions = transactionIngestor.read("data/PS_20174392719_1491204439457_log.csv");

        var fraudAnalyser = new FraudAnalyser(transactions);

        long countFrauds = fraudAnalyser.countFrauds();
        IO.println("1. Total de Fraudes: " + countFrauds);

        List<BigDecimal> top3AmountFraud = fraudAnalyser.listarTop3Amount();
        IO.println("2. Top 3 Fraudes de Maior Valor: ");
        top3AmountFraud.forEach(amount ->IO.println("-%.2f".formatted(amount)));


        List<String> listarNomesSusteitos = fraudAnalyser.listarNomesSusteitos();
        IO.println("3. Clientes Suspeitos: ");
        listarNomesSusteitos.forEach(IO::println);

        BigDecimal bigDecimal = fraudAnalyser.obterPrejuizoTotalDeFraudes();
        IO.println("4. Prejuízo total: " + bigDecimal);

        Map<TransactionType, Long> transactionTypeLongMap = fraudAnalyser.totalDeFraudesPorTipo();
        IO.println("Fraudes por tipo:");
        transactionTypeLongMap.forEach((type, count) -> IO.println("- %s: %d".formatted(type, count)));


    }
}




