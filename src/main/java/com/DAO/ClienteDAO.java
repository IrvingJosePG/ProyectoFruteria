package com.DAO;

import com.conexion.Conexion;
import com.model.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        
        String sql = "SELECT id_c, COALESCE(pf.nombre, pm.razon_social) AS nombre_cliente"
                + "FROM fruteria.cliente NATURAL JOIN p_fisica pf NATURAL JOIN p_moral pm "
                + "ORDER BY nombre";
        
        try (Connection conn = Conexion.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId_c(rs.getInt("id_c"));
                cliente.setNombre(rs.getString("nombre_cliente"));
                clientes.add(cliente);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return clientes;
    }
}
