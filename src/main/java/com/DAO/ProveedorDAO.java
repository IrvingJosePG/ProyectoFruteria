package com.DAO;

import com.conexion.Conexion;
import com.model.Proveedor; // Asumimos que tienes una clase Proveedor con idP y nombre
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO para la gestión de datos de Proveedores.
 * Se utiliza en la interfaz de Compras para seleccionar quién suministra el producto.
 */
public class ProveedorDAO {
    
    /**
     * Lista todos los proveedores de la base de datos.
     * @return Una lista de objetos Proveedor.
     */
    public List<Proveedor> listarProveedores() {
        List<Proveedor> proveedores = new ArrayList<>();
        
        // Consulta simple para obtener la lista de proveedores
        String sql = "SELECT id_p, nombre FROM fruteria.proveedor ORDER BY nombre;";
        
        try (Connection conn = Conexion.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Proveedor proveedor = new Proveedor();
                proveedor.setId_p(rs.getInt("id_p"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedores.add(proveedor);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar proveedores: " + e.getMessage());
        }
        return proveedores;
    }
}