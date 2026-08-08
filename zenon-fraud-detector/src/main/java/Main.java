import br.com.zenon.Transaction;
import br.com.zenon.TransactionCustomer;
import br.com.zenon.TransactionType;

import java.math.BigDecimal;

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
    }
}




