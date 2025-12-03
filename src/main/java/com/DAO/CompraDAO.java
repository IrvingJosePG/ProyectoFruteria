package com.DAO;

import com.conexion.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;


public class CompraDAO {

    public void registrarCompraCompleta(int folioC, int noLote, Date fecha, int idP, int idEn, int codigoProd, int cantidad, double costoUnitario) throws SQLException {

        // NOTA: La función SQL toma 8 parámetros.
        String sql = "{CALL fruteria.registrar_compra_transaccion(?, ?, ?, ?, ?, ?, ?, ?)}";

        // Usamos CallableStatement porque estamos llamando a una función PL/pgSQL
        try (Connection conn = Conexion.getInstance().getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            // Asignar los 8 parámetros a la función
            cs.setInt(1, folioC);
            cs.setInt(2, noLote);
            cs.setDate(3, new java.sql.Date(fecha.getTime())); // Convertir java.util.Date a java.sql.Date si es necesario
            cs.setInt(4, idP);
            cs.setInt(5, idEn);
            cs.setInt(6, codigoProd);
            cs.setInt(7, cantidad);
            cs.setDouble(8, costoUnitario);

            cs.execute();

        } catch (SQLException e) {
            // La excepción indicará si la transacción falló
            throw e;
        }
    }
}
