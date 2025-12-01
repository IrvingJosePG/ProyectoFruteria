package com.DAO;

import com.conexion.Conexion;
import com.model.Producto; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    
    public Producto buscarProductoPorCodigo(int codigo) {
        Producto producto = null;
        String sql = "SELECT p.codigo, p.descripcion, p.categoria, p.unidad_medida, "
                   + "p.existencia, p.precio_c, p.precio_v "
                   + "FROM fruteria.producto p "
                   + "WHERE p.codigo = ?";

        try (Connection conn = Conexion.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) { 
            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    producto = new Producto();
                    producto.setCodigo(rs.getInt("codigo"));
                    producto.setDescripcion(rs.getString("descripcion"));
                    producto.setCategoria(rs.getString("categoria")); 
                    producto.setUnidad_medida(rs.getString("unidad_medida"));
                    producto.setExitencia(rs.getInt("existencia"));
                    producto.setPrecio_c(rs.getDouble("precio_c"));
                    producto.setPrecio_v(rs.getDouble("precio_v"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto: " + e.getMessage());
            // Podrías relanzar la excepción para que el PuntoVenta la gestione mejor.
        }
        return producto;
    }
    
    public List<Producto> listarProductos() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT codigo, descripcion, categoria, unidad_medida, existencia, precio_c, precio_v FROM fruteria.producto ORDER BY codigo";
        
        // Usamos try-with-resources para manejar automáticamente la conexión y el PreparedStatement
        try (Connection conn = Conexion.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Producto p = new Producto();
                p.setCodigo(rs.getInt("codigo"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setCategoria(rs.getString("categoria"));
                p.setUnidad_medida(rs.getString("unidad_medida"));
                p.setExitencia(rs.getInt("existencia"));
                p.setPrecio_c(rs.getDouble("precio_c"));
                p.setPrecio_c(rs.getDouble("precio_v"));
                productos.add(p);
            }
        }
        return productos;
    }
    
    
    // (Aquí irán otros métodos como insertar, actualizar, listar, etc.)
    
    
}