package br.com.zenon;

import com.mysql.cj.protocol.Resultset;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class TransactionSQLRepository implements TransactionRepository{
    @Override
    public void save(Transaction transaction) {
        String sql = """
                INSERT INTO transactions
                (step, 
                `type`, 
                amount, 
                name_origin, 
                old_balance_origin, 
                new_balance_origin, 
                name_recipient, 
                old_balance_recipient, 
                new_balance_recipient, 
                is_fraud, 
                is_flagged_fraud)
                VALUES(?, ?,?, ?,?, ?, ?, ?, ?, ?,?);
                """;

        try (Connection connection = ConnectoinFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, transaction.step());
            ps.setString(2, transaction.type().name());
            ps.setBigDecimal(3, transaction.amount());

            ps.setString(4, transaction.origin().name());
            ps.setBigDecimal(5, transaction.origin().oldBalance());
            ps.setBigDecimal(6, transaction.origin().newBalance());

            ps.setString(7, transaction.recipient().name());
            ps.setBigDecimal(8, transaction.recipient().oldBalance());
            ps.setBigDecimal(9, transaction.recipient().newBalance());

            ps.setBoolean(10, transaction.isFraud());
            ps.setBoolean(11, transaction.isFlaggeFraud());


            ps.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar nova transação");
        }
    }

    @Override
    public Optional<Transaction> buscarPorNomeOrigem(String nomeOrigem) {
        String sql = """
                SELECT id, 
                step, 
                `type`, 
                amount, 
                name_origin, 
                old_balance_origin, 
                new_balance_origin, 
                name_recipient, 
                old_balance_recipient, 
                new_balance_recipient, 
                is_fraud, 
                is_flagged_fraud
                FROM zenon_frauds.transactions
                where name_origin = ?   
                ORDER BY step
                LIMIT 1
                """;
        try (Connection connection = ConnectoinFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, nomeOrigem);

            try (ResultSet rs =  ps.executeQuery()){

                if(rs.next()) {

                    Transaction transaction = mapResultSetToTransaction(rs);
                    return Optional.of(transaction);
                } else {
                    IO.println("Transação não encontrada para nome origem: " + nomeOrigem);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) {

        try{
            int step = rs.getInt("step");
            TransactionType type = TransactionType.valueOf(rs.getString("Type"));

            BigDecimal amount = rs.getBigDecimal("amount");

            String originName = rs.getString("name_origin");
            BigDecimal originOldBalance = rs.getBigDecimal("old_balance_origin");
            BigDecimal originNewBalance = rs.getBigDecimal("new_balance_origin");
            TransactionCustomer origin = new TransactionCustomer(originName, originOldBalance, originNewBalance);

            String recipientName = rs.getString("name_recipient");
            BigDecimal recipienteOldBalance = rs.getBigDecimal("old_balance_recipient");
            BigDecimal recipenteNewBalance = rs.getBigDecimal("new_balance_recipient");
            TransactionCustomer recipiente = new TransactionCustomer(recipientName, recipienteOldBalance, recipenteNewBalance);

            boolean isFraud = rs.getBoolean("is_fraud");
            boolean isFlaggedFraud = rs.getBoolean("is_flagged_fraud");


            return new Transaction(step, type, amount, origin, recipiente, isFraud, isFlaggedFraud);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
