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
        
        String sql = "SELECT c.id_c, " 
               + "COALESCE(pf.nombre, pm.razon_social) AS nombre_cliente "
               + "FROM fruteria.cliente c "
               + "LEFT JOIN fruteria.p_fisica pf ON c.id_c = pf.id_c "
               + "LEFT JOIN fruteria.p_moral pm ON c.id_c = pm.id_c "
               + "ORDER BY nombre_cliente;";
        
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
