package com.DAO;

import com.conexion.Conexion;
import com.model.Auditoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaDAO {
    public List<Auditoria> obtenerAuditoria() throws SQLException {
        List<Auditoria> lista = new ArrayList<>();
        Connection conn = null;

        String sql =  "SELECT " 
                + " id as folio, "
                + "nombre_tabla," 
                + "operacion," 
                + "usuario_bd," 
                + "TO_CHAR(fecha_auditoria, 'DD/MON/YY HH24:MI:SS') AS fecha " 
                + " FROM fruteria.auditoria;";
        
        try {
            conn = Conexion.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Auditoria c = new Auditoria();
                    c.setId_a(rs.getInt("folio"));
                    c.setNombretabla(rs.getString("nombre_tabla"));
                    c.setOperacion(rs.getString("operacion"));
                    c.setUsuariobd(rs.getString("usuario_bd"));
                    c.setFechaDisplay(rs.getString("fecha"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener reporte de ventas: " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return lista;
    }
}
