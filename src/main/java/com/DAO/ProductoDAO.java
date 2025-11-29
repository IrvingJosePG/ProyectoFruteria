package com.DAO; // Ajusta el paquete según tu proyecto

import com.conexion.Conexion;
import com.model.Producto; // Importa el modelo creado arriba
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// import com.util.Conexion; // Suponiendo que tienes una clase de conexión

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
    
    // (Aquí irán otros métodos como insertar, actualizar, listar, etc.)
}