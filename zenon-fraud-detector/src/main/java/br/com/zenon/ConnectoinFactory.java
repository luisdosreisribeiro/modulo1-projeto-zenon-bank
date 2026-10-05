package br.com.zenon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectoinFactory {

     public static Connection  getConnection(){

         try {
             return DriverManager.getConnection("jdbc:mysql://localhost:3306/zenon_frauds", "root", "root123");
         } catch (SQLException e) {
             throw new RuntimeException("Erro ao conectar com o BD", e);
         }

     }

}
