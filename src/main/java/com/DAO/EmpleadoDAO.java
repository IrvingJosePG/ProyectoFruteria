package com.DAO;

import com.conexion.Conexion;
import com.model.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    
    public List<Empleado> listarEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        
        String sql = "SELECT e.id_e, " 
               + "e.nombre AS nombre_empleado "
               + "FROM fruteria.empleado e "
               + "ORDER BY nombre_empleado;";
        
        try (Connection conn = Conexion.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Empleado empleado = new Empleado();
                empleado.setId_e(rs.getInt("id_e"));
                empleado.setNombre(rs.getString("nombre_empleado"));
                empleados.add(empleado);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return empleados;
    }
    
}
