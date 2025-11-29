package com.DAO;

import com.conexion.Conexion;
import com.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    
    // Método para autenticar un usuario
    public Usuario autenticarUsuario(String usuario, String password) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario user = null;
        
        // Consulta SQL para verificar credenciales y obtener datos del empleado y rol
        String sql = "SELECT e.id_e, e.nombre, r.id_rol, r.nombre_rol " +
                     "FROM fruteria.usuario u " +
                     "JOIN fruteria.empleado e ON u.id_e = e.id_e " +
                     "JOIN fruteria.rol r ON u.id_rol = r.id_rol " +
                     "WHERE u.nombre_usuario = ? AND u.password_hash = ?"; 
        
        try {
            // 1. Obtener la conexión (IMPORTANTE: Se obtiene una nueva conexión por llamada)
            conn = Conexion.getInstance().getConnection();
            
            ps = conn.prepareStatement(sql);
            
            // Asignar parámetros a la consulta
            ps.setString(1, usuario);
            ps.setString(2, password); 

            rs = ps.executeQuery();
            
            if (rs.next()) {
                // Si encontramos una fila, el usuario es válido
                int idEmpleado = rs.getInt("id_e");
                String nombreEmpleado = rs.getString("nombre");
                int idRol = rs.getInt("id_rol");
                String nombreRol = rs.getString("nombre_rol");
                
                // Crear y devolver el objeto Usuario
                user = new Usuario(idEmpleado, nombreEmpleado, idRol, nombreRol);
            }
            
        } catch (SQLException e) {
            System.err.println("Error en la autenticación del usuario: " + e.getMessage());
        } finally {
            // 2. CERRAR RECURSOS OBLIGATORIO: Ya que getConnection() devuelve una nueva conexión
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close(); // <--- ¡CERRAR LA CONEXIÓN!
            } catch (SQLException ex) {
                System.err.println("Error al cerrar recursos: " + ex.getMessage());
            }
        }
        return user;
    }
}