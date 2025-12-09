package com.DAO;

import com.conexion.Conexion;
import com.model.Cliente;
import java.sql.CallableStatement;
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
    
    /**
     * Obtiene una lista simplificada de todos los clientes activos 
     * para la carga inicial de la JTable.
     * @return Lista de objetos Cliente con datos básicos.
     * @throws SQLException 
     */
    public List<Cliente> listarTodosActivos() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        Connection conn = null;
        // Consulta unificada: JOIN con ambas tablas de subtipo para obtener el nombre/razón social
        String sql = "SELECT c.id_c, c.rfc, c.domicilio, c.telefono, " +
                     "   COALESCE(pf.nombre, pm.razon_social) AS nombre, " +
                     "   CASE WHEN pf.id_c IS NOT NULL THEN 'Fisica' ELSE 'Moral' END AS tipo_cliente " +
                     "FROM fruteria.cliente c " +
                     "LEFT JOIN fruteria.p_fisica pf ON c.id_c = pf.id_c " +
                     "LEFT JOIN fruteria.p_moral pm ON c.id_c = pm.id_c " +
                     "ORDER BY c.id_c";

        try {
            conn = Conexion.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                
                while (rs.next()) {
                    Cliente c = new Cliente();
                    c.setId_c(rs.getInt("id_c"));
                    c.setRfc(rs.getString("rfc"));
                    c.setDomicilio(rs.getString("domicilio"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setTipo(rs.getString("tipo_cliente"));

                    // Asigna el nombre completo (nombre o razón social)
                    if (c.getTipo().equals("Fisica")) {
                         // El nombre completo se almacena en el campo 'nombre' del modelo para simplificar la JTable
                        String[] partesNombre = rs.getString("nombre").split(" ");
                        c.setNombre(partesNombre[0]); 
                        // Los apellidos quedan nulos en esta lista simple, se cargan en la búsqueda detallada
                    } else {
                        c.setRazonSocial(rs.getString("nombre"));
                    }

                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes activos: " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return lista;
    }
     
    /**
     * Llama a la función SQL para obtener el ID de cliente dado un identificador (RFC, Nombre o Razón Social).
     * @param identificador RFC, nombre o razón social.
     * @return El ID del cliente o -1 si no se encuentra.
     * @throws SQLException 
     */
    public int buscarIdPorIdentificador(String identificador) throws SQLException {
        Connection conn = null;
        int idC = -1;
        // {? = CALL fruteria.obtener_id_cliente_por_identificador(?)}
        String sqlCall = "{? = CALL fruteria.obtener_id_cliente_por_identificador(?)}";
        
        try {
            conn = Conexion.getInstance().getConnection();
            try (CallableStatement cs = conn.prepareCall(sqlCall)) {
                
                // 1. Registrar el parámetro de retorno (Índice 1)
                cs.registerOutParameter(1, java.sql.Types.INTEGER);
                
                // 2. Asignar el parámetro de entrada (Índice 2)
                cs.setString(2, identificador);
                
                cs.execute();
                
                // 3. Obtener el resultado
                idC = cs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar ID de cliente: " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return idC;
    }
    
    
    /**
     * Obtiene todos los datos de un cliente (incluyendo subtipos) para edición.
     * @param id_c El ID del cliente a buscar.
     * @return Objeto Cliente completo o null si no existe.
     * @throws SQLException 
     */
    public Cliente obtenerClienteCompletoPorId(int id_c) throws SQLException {
        Cliente cliente = null;
        Connection conn = null;
        String sql = "SELECT c.id_c, c.rfc, c.domicilio, c.telefono, c.estado, " +
                     "       pf.nombre, pm.razon_social, " +
                     "       CASE WHEN pf.id_c IS NOT NULL THEN 'Fisica' ELSE 'Moral' END AS tipo_cliente " +
                     "FROM fruteria.cliente c " +
                     "LEFT JOIN fruteria.p_fisica pf ON c.id_c = pf.id_c " +
                     "LEFT JOIN fruteria.p_moral pm ON c.id_c = pm.id_c " +
                     "WHERE c.id_c = ?";

        try {
            conn = Conexion.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id_c);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        cliente = new Cliente();
                        cliente.setId_c(rs.getInt("id_c"));
                        cliente.setRfc(rs.getString("rfc"));
                        cliente.setDomicilio(rs.getString("domicilio"));
                        cliente.setEstado(rs.getBoolean("estado"));
                        cliente.setTelefono(rs.getString("telefono"));
                        cliente.setTipo(rs.getString("tipo_cliente"));

                        if (cliente.getTipo().equals("Fisica")) {
                            cliente.setNombre(rs.getString("nombre"));
                        } else {
                            cliente.setRazonSocial(rs.getString("razon_social"));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente completo por ID: " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return cliente;
    }
    
    /**
     * Actualiza el domicilio y teléfono de un cliente (Requisito 3).
     * @param cliente El objeto Cliente con el ID y los nuevos datos.
     * @return true si la actualización fue exitosa, false en caso contrario.
     * @throws SQLException 
     */
    public boolean actualizarDatosCliente(Cliente cliente) throws SQLException {
        Connection conn = null;
        boolean exito = false;

        // SQL para tabla principal (cliente) - ASUMIMOS que RFC se puede actualizar
        String sql_cliente = "UPDATE fruteria.cliente SET rfc = ?, domicilio = ?, telefono = ? WHERE id_c = ?";

        // SQL para subtipo (p_fisica)
        String sql_pfisica = "UPDATE fruteria.p_fisica SET nombre = ? WHERE id_c = ?";

        // SQL para subtipo (p_moral)
        String sql_pmoral = "UPDATE fruteria.p_moral SET razon_social = ? WHERE id_c = ?";

        try {
            conn = Conexion.getInstance().getConnection();
            conn.setAutoCommit(false); // Iniciar transacción

            // 1. Actualizar tabla CLIENTE (RFC, Domicilio, Teléfono)
            try (PreparedStatement ps_c = conn.prepareStatement(sql_cliente)) {
                ps_c.setString(1, cliente.getRfc());
                ps_c.setString(2, cliente.getDomicilio());
                ps_c.setString(3, cliente.getTelefono());
                ps_c.setInt(4, cliente.getId_c());
                ps_c.executeUpdate();
            }

            // 2. Actualizar tabla de subtipo (P_FISICA o P_MORAL)
            if ("Fisica".equals(cliente.getTipo())) {
                try (PreparedStatement ps_pf = conn.prepareStatement(sql_pfisica)) {
                    ps_pf.setString(1, cliente.getNombre());
                    ps_pf.setInt(2, cliente.getId_c());
                    ps_pf.executeUpdate();
                }
            } else if ("Moral".equals(cliente.getTipo())) {
                try (PreparedStatement ps_pm = conn.prepareStatement(sql_pmoral)) {
                    ps_pm.setString(1, cliente.getRazonSocial());
                    ps_pm.setInt(2, cliente.getId_c());
                    ps_pm.executeUpdate();
                }
            }

            // Si todo va bien, confirmar la transacción
            conn.commit();
            exito = true; 

        } catch (SQLException e) {
            // Si hay un error, revertir todos los cambios
            if (conn != null) {
                conn.rollback();
            }
            System.err.println("Error al actualizar datos del cliente: " + e.getMessage());
            throw e;

        } finally {
            // Restaurar auto-commit y cerrar la conexión
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
        return exito;
    }
    
    /**
     * Realiza la baja lógica de un cliente (cambia el estado a 'Inactivo') (Requisito 4).
     * @param id_c El ID del cliente a eliminar.
     * @return true si la baja fue exitosa, false en caso contrario.
     * @throws SQLException 
     */
    public boolean eliminarCliente(int id_c) throws SQLException {
        Connection conn = null;
        // La consulta cambia de DELETE a UPDATE, que es la baja lógica
        String sql = "UPDATE fruteria.cliente SET estado = FALSE WHERE id_c = ?"; 
        boolean exito = false;

        try {
            conn = Conexion.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id_c);

                if (ps.executeUpdate() > 0) {
                    exito = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al realizar la baja lógica del cliente: " + e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return exito;
    }
}
