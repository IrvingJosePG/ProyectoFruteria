package com.DAO;

import com.conexion.Conexion;
import com.model.ReporteVenta;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para generar Reportes de Ventas y consultar la tabla de Auditoría.
 */
public class ReporteDAO {

    /**
     * Requisito 1: ReporteVenta Detallado de Ventas (5 Tablas Reunidas).
     * @return Vector de Vectores que representa las filas y columnas de la tabla.
     * @throws SQLException
     */
    public List<ReporteVenta> obtenerReporteVentasDetallado() throws SQLException {
        List<ReporteVenta> lista = new ArrayList<>();
        Connection conn = null;

        // Consulta que une VENTA, DETALLE_VENTA, EMPLEADO, CLIENTE, PRODUCTO
        String sql = 
                    "SELECT "
                  + "TO_CHAR(v.fecha, 'DD/MON/YY') AS fecha, "
                  + "v.folio_v, "
                  + "e.nombre AS empleado, "
                  + "COALESCE(pf.nombre, pm.razon_social) AS cliente, "
                  + "p.descripcion AS producto, "
                  + "dv.cantidad, "
                  + "p.precio_v AS precio_venta_unitario, "
                  + "fruteria.calcular_total_venta(v.folio_v) AS total_venta "
                  + "FROM fruteria.venta v "
                  + "JOIN fruteria.detalle_venta dv ON v.folio_v = dv.folio_v "
                  + "JOIN fruteria.empleado e ON v.id_e = e.id_e "
                  + "JOIN fruteria.cliente c ON v.id_c = c.id_c "
                  + "JOIN fruteria.producto p ON dv.codigo = p.codigo "
                  + "LEFT JOIN fruteria.p_fisica pf ON c.id_c = pf.id_c "
                  + "LEFT JOIN fruteria.p_moral pm ON c.id_c = pm.id_c "
                  + "ORDER BY v.fecha DESC, v.folio_v";
        
        try {
            conn = Conexion.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    ReporteVenta c = new ReporteVenta();
                    c.setFechaDisplay(rs.getString("fecha"));
                    c.setFolio_v(rs.getInt("folio_v"));
                    c.setEmpleado(rs.getString("empleado"));
                    c.setCliente(rs.getString("cliente"));
                    c.setProducto(rs.getString("producto"));
                    c.setCantidad(rs.getInt("cantidad"));
                    c.setPrecio_u(rs.getBigDecimal("precio_venta_unitario").doubleValue());
                    c.setTotal_venta(rs.getBigDecimal("total_venta").doubleValue());

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
    
    /**
     * Requisito 6: Obtener los datos de la tabla de auditoría (Disparadores).
     * @return Vector de Vectores con los datos de auditoría.
     * @throws SQLException
     
    public Vector<Vector<Object>> obtenerAuditoria() throws SQLException {
        Vector<Vector<Object>> data = new Vector<>();
        Connection conn = null;

        // Consulta de la tabla de auditoría (asumiendo que existe)
        String sql = "SELECT id_auditoria, tabla_afectada, operacion, usuario, fecha_registro, datos_viejos "
                   + "FROM fruteria.auditoria "
                   + "ORDER BY fecha_registro DESC";

        try {
            conn = Conexion.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("id_auditoria"));
                    row.add(rs.getString("tabla_afectada"));
                    row.add(rs.getString("operacion"));
                    row.add(rs.getString("usuario"));
                    row.add(rs.getTimestamp("fecha_registro"));
                    row.add(rs.getString("datos_viejos"));
                    data.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener datos de auditoría: " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return data;
    }

    /**
     * Llama a la Función 4 SQL para calcular el total de una venta.
     * @param folio_v Folio de la venta.
     * @return El monto total de la venta.
     * @throws SQLException
     */
    public double calcularTotalVenta(int folio_v) throws SQLException {
        Connection conn = null;
        double total = 0.0;
        String sqlCall = "{? = CALL fruteria.calcular_total_venta(?)}";
        
        try {
            conn = Conexion.getInstance().getConnection();
            try (CallableStatement cs = conn.prepareCall(sqlCall)) {
                
                cs.registerOutParameter(1, java.sql.Types.NUMERIC);
                cs.setInt(2, folio_v);
                
                cs.execute();
                
                total = cs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al calcular total de venta: " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return total;
    }
}