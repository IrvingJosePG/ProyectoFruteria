package com.DAO;

import com.conexion.Conexion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import com.model.Compra;
import com.model.DetalleCompraTemporal;
import com.model.ReporteCompra;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;


public class CompraDAO {
     /**
     * Llama a la función PL/pgSQL para registrar la compra, el detalle y actualizar el stock.
     */
     public boolean registrarCompraTransaccion(Compra compra, List<DetalleCompraTemporal> detalles) throws SQLException {
        Connection conn = null;
        boolean exito = false;
        int folioGenerado = -1;

        try {
            conn = Conexion.getInstance().getConnection();
            conn.setAutoCommit(false); // Inicia transacción

            // 1. INSERTAR ENCABEZADO DE COMPRA Y OBTENER FOLIO
            String sqlInsertCompra = 
                "INSERT INTO fruteria.compra (no_lote, fecha, id_p, id_e) VALUES (?, ?, ?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(sqlInsertCompra, Statement.RETURN_GENERATED_KEYS)) {

                ps.setInt(1, compra.getNo_lote());
                ps.setDate(2, Date.valueOf(compra.getFecha()));
                ps.setInt(3, compra.getId_p());
                ps.setInt(4, compra.getId_e());

                ps.executeUpdate();

                // OBTENER FOLIO GENERADO POR LA BD
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        folioGenerado = rs.getInt(1);
                        compra.setFolio_c(folioGenerado); // lo guardamos en el objeto
                    }
                }
            }

            if (folioGenerado == -1) {
                throw new SQLException("No se pudo obtener el folio_c generado.");
            }

            // 2. INSERTAR DETALLES Y ACTUALIZAR STOCK MEDIANTE LA FUNCIÓN
            String sqlFuncion = "{CALL fruteria.registrar_detalle_compra_y_stock(?, ?, ?)}";

            for (DetalleCompraTemporal detalle : detalles) {
                try (CallableStatement cs = conn.prepareCall(sqlFuncion)) {

                    cs.setInt(1, folioGenerado);            // p_folio_c
                    cs.setInt(2, detalle.getIdProducto());  // p_codigo_prod
                    cs.setInt(3, detalle.getCantidad());    // p_cantidad

                    cs.execute();
                }
            }

            // 3. COMMIT FINAL
            conn.commit();
            exito = true;

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            System.err.println("Error en transacción de compra: " + e.getMessage());
        }

        return exito;
    }


    /**
     * Realiza una consulta JOIN de 3 o más tablas para mostrar los detalles de compra.
     * Cumple con el Requisito 1 (Muestra datos de una consulta de al menos 3 tablas reunidas).
     * Tablas: compra, detalle_compra, producto, proveedor, empleado (5 tablas)
     * * @return Lista de objetos ReporteCompra.\
     */
    public List<ReporteCompra> listarReporteDetalleCompras() {
        List<ReporteCompra> reportes = new ArrayList<>();
        
        String sql = "SELECT "
                + "c.folio_c, TO_CHAR(c.fecha, 'DD/MON/YY') AS fecha, "
                + "p.nombre AS proveedor_nombre, "
                + "e.nombre AS empleado_nombre, "
                + "prod.descripcion AS producto_descripcion, " 
                + "dc.cantidad, prod.precio_c AS precio_compra " 
                + "FROM fruteria.compra c "
                + "JOIN fruteria.detalle_compra dc ON c.folio_c = dc.folio_c " 
                + "JOIN fruteria.proveedor p ON c.id_p = p.id_p "
                +"JOIN fruteria.empleado e ON c.id_e = e.id_e "
                + "JOIN fruteria.producto prod ON dc.codigo = prod.codigo " 
                + "ORDER BY c.fecha DESC, c.folio_c DESC;";

        try (Connection conn = Conexion.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                ReporteCompra rc = new ReporteCompra();
                rc.setFolioC(rs.getInt("folio_c"));
                rc.setFechaDisplay(rs.getString("fecha"));
                rc.setNombreProveedor(rs.getString("proveedor_nombre"));
                rc.setNombreEmpleado(rs.getString("empleado_nombre"));
                rc.setDescripcionProducto(rs.getString("producto_descripcion"));
                rc.setCantidad(rs.getInt("cantidad"));
                rc.setCostoUnitario(rs.getDouble("precio_compra"));
                // El total podría ser calculado aquí o en la vista:
                rc.setTotal(rc.getCantidad() * rc.getCostoUnitario()); 
                
                reportes.add(rc);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar el reporte de compras: " + e.getMessage());
        }
        return reportes;
    }
}
