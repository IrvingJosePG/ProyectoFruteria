package com.DAO;

import com.conexion.Conexion;
import com.model.DetalleVentaTemporal;
import com.model.Venta;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class VentaDAO {
    
    public boolean registrarVentaTransaccion(Venta venta, List<DetalleVentaTemporal> detalles) {
        Connection conn = null;
        boolean exito = false;
        
        try {
            conn = Conexion.getInstance().getConnection();
            conn.setAutoCommit(false); // 1. INICIA LA TRANSACCIÓN
            int folioGenerado = -1;

            // A. INSERTAR ENCABEZADO DE VENTA Y CAPTURAR FOLIO
            String sqlVenta = "INSERT INTO fruteria.venta (fecha, id_c, id_e) VALUES (?, ?, ?)";
            try (PreparedStatement psVenta = conn.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS)) {
                
                psVenta.setDate(1, Date.valueOf(venta.getFecha())); 
                psVenta.setInt(2, venta.getIdCliente());
                psVenta.setInt(3, venta.getIdEmpleado());
                psVenta.executeUpdate();
                
                try (ResultSet rs = psVenta.getGeneratedKeys()) {
                    if (rs.next()) {
                        folioGenerado = rs.getInt(1); // ¡Capturamos el folio!
                        venta.setFolioV(folioGenerado);
                    }
                }
                if (folioGenerado == -1) {
                    throw new SQLException("Error: No se pudo obtener el folio_v de la venta.");
                }
            }

            // B. BUCLE: LLAMAR FUNCIÓN SQL PARA CADA DETALLE
            String sqlFuncion = "{CALL fruteria.registrar_detalle_y_stock(?, ?, ?, ?)}"; 
            
            for (DetalleVentaTemporal detalle : detalles) {
                try (CallableStatement cs = conn.prepareCall(sqlFuncion)) {
                    cs.setInt(1, folioGenerado);           // p_folio_v
                    cs.setInt(2, detalle.getIdProducto());  // p_codigo
                    cs.setInt(3, detalle.getCantidad());    // p_cantidad
                    cs.setString(4, "");                    // p_observaciones
                    cs.execute();
                }
            }
            
            conn.commit(); // 2. COMMIT (Guarda todos los cambios)
            exito = true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // 3. ROLLBACK (Deshace todos los cambios si hay error)
                }
            } catch (SQLException ex) {
                // Manejo de error al hacer rollback
            }
            System.err.println("Error en transacción de venta: " + e.getMessage());
        } finally {
            // Cierre de conexión y restauración de autocommit
        }
        return exito;
    }
}