package br.com.zenon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionIngestor {

    public List<Transaction> read(String fileName) {
        Path path = Path.of(fileName);

        try {
            List<String> lines = Files.readAllLines(path);
            return lines.stream()
                    .skip(1)
                    .limit(1000)
                    .map(this::parseTransacrion)
                    .toList();

        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler o arquivo:" + fileName, ex);
        }


    }
    public List<Transaction> readOldSchool(String fileName) {

        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(fileName);
            Scanner scanner = new Scanner(fis);
            int lineCount = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineCount++;

                if(lineCount ==1) {
                    continue;
                }
                if(lineCount > 1001) {
                    break;
                }

                var transaction = parseTransacrion(line);
                transactions.add(transaction);

            }
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao ler o arquivo:" + fileName, ex);
        }

        return transactions;
    }

    private Transaction parseTransacrion(String line) {
        String[] trechos = line.split(",");

        int step = Integer.parseInt(trechos[0]);
        TransactionType type = TransactionType.valueOf(trechos[1]);
        BigDecimal amount = new BigDecimal(trechos[2]);

        var origin =  new TransactionCustomer(trechos[3], new BigDecimal(trechos[4]), new BigDecimal(trechos[5]));
        var recipient = new TransactionCustomer(trechos[6], new BigDecimal(trechos[7]), new BigDecimal(trechos[8]));

        boolean isFraud = "1".equals(trechos[9]);
        boolean isFlaggedFraud ="1".equals(trechos[10]);

        return new Transaction(step, type, amount, origin, recipient, isFraud, isFlaggedFraud);
    }
}
