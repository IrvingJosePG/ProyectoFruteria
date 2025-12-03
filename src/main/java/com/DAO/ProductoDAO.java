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
                p.setPrecio_v(rs.getDouble("precio_v"));
                productos.add(p);
            }
        }
        return productos;
    }
    
    // Archivo: ProductoDAO.java

    public int guardarProducto(Producto p) throws SQLException {
        String sql = "INSERT INTO fruteria.producto (descripcion, categoria, unidad_medida, existencia, precio_c, precio_v) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getDescripcion());
            ps.setString(2, p.getCategoria());
            ps.setString(3, p.getUnidad_medida());
            ps.setInt(4, p.getExitencia());
            ps.setDouble(5, p.getPrecio_c());
            ps.setDouble(6, p.getPrecio_v());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Devuelve el código generado
                    }
                }
            }
            return -1; // Devuelve -1 si falla

        } catch (SQLException e) {
            throw e;
        }
    }
    
    public boolean actualizarAtributosProducto(Producto p) throws SQLException {
        String sql = "UPDATE fruteria.producto SET descripcion=?, categoria=?, unidad_medida=?, precio_c=?, precio_v=? WHERE codigo=?";

        try (Connection conn = Conexion.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getDescripcion());
            ps.setString(2, p.getCategoria());
            ps.setString(3, p.getUnidad_medida());
            // NO incluimos 'existencia' (stock)
            ps.setDouble(4, p.getPrecio_c());
            ps.setDouble(5, p.getPrecio_v());

            // La condición WHERE
            ps.setInt(6, p.getCodigo()); 

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw e;
        }
    }
    
    
    public boolean eliminarProducto(int codigo) throws SQLException {
        // La eliminación aquí dispara el TRIGGER de Auditoría
        String sql = "DELETE FROM fruteria.producto WHERE codigo = ?";

        try (Connection conn = Conexion.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw e;
        }
    }
    // (Aquí irán otros métodos como insertar, actualizar, listar, etc.)
    
    
}