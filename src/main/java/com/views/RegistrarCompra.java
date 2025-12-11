package com.views;

import com.DAO.CompraDAO;
import com.DAO.EmpleadoDAO;
import com.DAO.ProductoDAO;
import com.DAO.ProveedorDAO;
import com.model.Cliente;
import com.model.Compra;
import com.model.DetalleCompra;
import com.model.DetalleCompraTemporal;
import com.model.Empleado;
import com.model.Producto;
import com.model.Proveedor;
import com.model.Venta;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class RegistrarCompra extends javax.swing.JPanel {
    
    private DefaultTableModel modeloCarrito;
    private double totalCompraAcumulado = 0.0;
    private java.util.List<DetalleCompraTemporal> detallesCompra;
    // DAO
    private final ProductoDAO productoDAO = new ProductoDAO();
    private final ProveedorDAO proveedorDAO = new ProveedorDAO();
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private final CompraDAO compraDAO = new CompraDAO();

    public RegistrarCompra() {
        initComponents();
        
        modeloCarrito = (DefaultTableModel) tablecompraproducto.getModel();
        detallesCompra = new ArrayList<>();
        fechacompra.setDate(new Date());
        cargarProveedores();
        cargarEmpleados();
        configurarTabla();
        limpiarCamposDetalle();
    }
    
    public void configurarTabla() {
        String[] columnNames = {"ID", "Producto", "Cantidad", "Costo_U", "Subtotal"};

        this.modeloCarrito = new DefaultTableModel(columnNames, 0); 
        
        tablecompraproducto.setModel(this.modeloCarrito);
        tablecompraproducto.getTableHeader().setResizingAllowed(true);

        tablecompraproducto.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablecompraproducto.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablecompraproducto.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablecompraproducto.getColumnModel().getColumn(3).setPreferredWidth(60);
        tablecompraproducto.getColumnModel().getColumn(4).setPreferredWidth(60); 
    }
    
    private void agregarAlCarrito() {
        String codigoStr = codigop.getText().trim();
        int codigo;
        int cantidad;
        double costoUnitario;

        if (codigoStr.isEmpty() || codigoStr.equals("Codigo de producto")) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el código del producto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            limpiarCamposDetalle();
            return;
        }

        try {
            codigo = Integer.parseInt(codigoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El código debe ser un número entero (ej: 5000).", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
            limpiarCamposDetalle();
            return;
        }
        
        Producto productoEncontrado = productoDAO.buscarProductoPorCodigo(codigo);
        
        if (productoEncontrado == null) {
            JOptionPane.showMessageDialog(this, "Producto con código " + codigo + " no encontrado.", "Producto No Válido", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 1. Leer Cantidad
        cantidad = (Integer) cantidadcomprada.getValue();
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
         // 2. Leer Costo Unitario ingresado (CRÍTICO: No usar el precio DB)
        try {
            costoUnitario = productoEncontrado.getPrecio_c();
            if (costoUnitario <= 0) {
                 JOptionPane.showMessageDialog(this, "El costo unitario debe ser mayor a cero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                 return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Costo Unitario debe ser un número válido.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        double subtotal = costoUnitario * cantidad;
        
        // 3. Añadir a la tabla
        Object[] fila = new Object[]{
        productoEncontrado.getCodigo(),
        productoEncontrado.getDescripcion(),
        cantidad,
        String.format("%.2f", costoUnitario),
        String.format("%.2f", subtotal)
        };
        
        modeloCarrito.addRow(fila);
        
        // 4. Añadir a la lista interna (usando el costo ingresado)
        detallesCompra.add(new DetalleCompraTemporal(
            productoEncontrado.getCodigo(), 
            productoEncontrado.getDescripcion(), 
            cantidad, 
            costoUnitario, 
            subtotal
        ));
      
        actualizarTotalCompra(subtotal);
        limpiarCamposDetalle(); // Limpiar solo campos de detalle
    }
    
    private void actualizarTotalCompra(double subtotalNuevo) {
        totalCompraAcumulado += subtotalNuevo;
        totalventa.setText(String.format("%.2f", totalCompraAcumulado)); // totalventa es ahora el total de la compra
    }
    
    private void cargarProveedores() {
        try {
            ProveedorDAO dao = new ProveedorDAO();
            java.util.List<Proveedor> proveedores = dao.listarProveedores();

            proveedoresregistrados.removeAllItems(); 
            
            Proveedor ProveedorDefault = new Proveedor(0, "Seleccionar Proveedor", " ", " " , "");
            proveedoresregistrados.addItem(ProveedorDefault);
            
            for (Proveedor proveedor : proveedores) {
                proveedoresregistrados.addItem(proveedor);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la lista de clientes: " + e.getMessage(), "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }  
    
    private void cargarEmpleados(){
        try {
            EmpleadoDAO dao = new EmpleadoDAO();
            java.util.List<Empleado> empleados = dao.listarEmpleados();

            empleadosregistrados.removeAllItems(); 
            
            Empleado EmpleadoDefault = new Empleado(0, "Cargar Empleado ", " " , 0);
            empleadosregistrados.addItem(EmpleadoDefault);
            
            for (Empleado empleado : empleados) {
                empleadosregistrados.addItem(empleado);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la lista de empleados: " + e.getMessage(), "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Solo limpia los campos de entrada para el nuevo producto
    private void limpiarCamposDetalle() {
        codigop.setText("");
        cantidadcomprada.setValue(1); // Reset a 1, no a 0
    }
    
    // Limpia todo el carrito y la interfaz
    private void limpiarTodo() {
        limpiarCamposDetalle();
        
        // Limpiar Carrito
        modeloCarrito.setRowCount(0);
        detallesCompra.clear();
        totalCompraAcumulado = 0.0;
        totalventa.setText("0.00");
        
        // Limpiar Encabezado
        proveedoresregistrados.setSelectedIndex(0);
        empleadosregistrados.setSelectedIndex(0);
        fechacompra.setDate(new Date());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textcompra = new javax.swing.JLabel();
        textfecha = new javax.swing.JLabel();
        fechacompra = new com.toedter.calendar.JDateChooser();
        textproveedor = new javax.swing.JLabel();
        proveedoresregistrados = new javax.swing.JComboBox<>();
        textempleado = new javax.swing.JLabel();
        empleadosregistrados = new javax.swing.JComboBox<>();
        textdetalleproducto = new javax.swing.JLabel();
        textcodigo = new javax.swing.JLabel();
        textcantidad = new javax.swing.JLabel();
        registrarcompra = new javax.swing.JPanel();
        registarcompras = new javax.swing.JLabel();
        buttonclean = new javax.swing.JPanel();
        limpiarcampos = new javax.swing.JLabel();
        carrito = new javax.swing.JPanel();
        textcarrito = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablecompraproducto = new javax.swing.JTable();
        texttotalventa = new javax.swing.JLabel();
        totalventa = new javax.swing.JLabel();
        cantidadcomprada = new javax.swing.JSpinner();
        codigop = new javax.swing.JTextField();

        setBackground(new java.awt.Color(252, 249, 235));
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Registrar Nueva Compra", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Helvetica Neue", 1, 13))); // NOI18N
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        textcompra.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        textcompra.setText("Datos de la compra:");
        add(textcompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        textfecha.setText("Fecha de Compra:");
        add(textfecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 120, 20));
        add(fechacompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 50, 130, 20));

        textproveedor.setText("Proveedor:");
        add(textproveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, 20));

        add(proveedoresregistrados, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 80, 250, 20));

        textempleado.setText("Empleado:");
        add(textempleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, 20));

        add(empleadosregistrados, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 250, 20));

        textdetalleproducto.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        textdetalleproducto.setText("Detalle de Productos:");
        add(textdetalleproducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, 20));

        textcodigo.setText("Código Producto:");
        add(textcodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, 25));

        textcantidad.setText("Cantidad Comprada:");
        add(textcantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, -1, 25));

        registrarcompra.setBackground(new java.awt.Color(124, 123, 174));

        registarcompras.setBackground(new java.awt.Color(124, 123, 174));
        registarcompras.setFont(new java.awt.Font("PT Sans", 1, 16)); // NOI18N
        registarcompras.setForeground(new java.awt.Color(255, 255, 255));
        registarcompras.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        registarcompras.setText("Registrar Compra");
        registarcompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                registarcomprasMouseClicked(evt);
            }
        });
        registarcompras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                registarcomprasKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout registrarcompraLayout = new javax.swing.GroupLayout(registrarcompra);
        registrarcompra.setLayout(registrarcompraLayout);
        registrarcompraLayout.setHorizontalGroup(
            registrarcompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(registarcompras, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        registrarcompraLayout.setVerticalGroup(
            registrarcompraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(registarcompras, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        add(registrarcompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(125, 350, 160, 30));

        buttonclean.setBackground(new java.awt.Color(124, 123, 174));

        limpiarcampos.setBackground(new java.awt.Color(124, 123, 174));
        limpiarcampos.setFont(new java.awt.Font("PT Sans", 1, 16)); // NOI18N
        limpiarcampos.setForeground(new java.awt.Color(255, 255, 255));
        limpiarcampos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        limpiarcampos.setText("Limpiar Campos");
        limpiarcampos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                limpiarcamposMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout buttoncleanLayout = new javax.swing.GroupLayout(buttonclean);
        buttonclean.setLayout(buttoncleanLayout);
        buttoncleanLayout.setHorizontalGroup(
            buttoncleanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(limpiarcampos, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        buttoncleanLayout.setVerticalGroup(
            buttoncleanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(limpiarcampos, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        add(buttonclean, new org.netbeans.lib.awtextra.AbsoluteConstraints(411, 350, 160, 30));

        carrito.setBackground(new java.awt.Color(124, 123, 174));

        textcarrito.setBackground(new java.awt.Color(124, 123, 174));
        textcarrito.setFont(new java.awt.Font("PT Sans", 1, 16)); // NOI18N
        textcarrito.setForeground(new java.awt.Color(255, 255, 255));
        textcarrito.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        textcarrito.setText("Agregar a compras");
        textcarrito.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                textcarritoMouseClicked(evt);
            }
        });
        textcarrito.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textcarritoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout carritoLayout = new javax.swing.GroupLayout(carrito);
        carrito.setLayout(carritoLayout);
        carritoLayout.setHorizontalGroup(
            carritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textcarrito, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        carritoLayout.setVerticalGroup(
            carritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(textcarrito, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        add(carrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 50, 160, 30));

        tablecompraproducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Producto", "Cantidad", "Precio Compra", "Subtotal"
            }
        ));
        jScrollPane1.setViewportView(tablecompraproducto);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(282, 140, 400, 150));

        texttotalventa.setText("Total Compra:");
        add(texttotalventa, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 300, -1, -1));

        totalventa.setText("0.0");
        add(totalventa, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, -1, -1));
        add(cantidadcomprada, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 220, 100, 25));
        add(codigop, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 170, 100, 25));
    }// </editor-fold>//GEN-END:initComponents

    private void registarcomprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registarcomprasMouseClicked
        // 1. Validaciones
        if (detallesCompra.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El carrito de compra está vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Proveedor ProveedorSeleccionado = (Proveedor) proveedoresregistrados.getSelectedItem();
        if (ProveedorSeleccionado == null || ProveedorSeleccionado.getId_p() == 0 || "Seleccionar Proveedor".equals(ProveedorSeleccionado.getNombre())) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un proveedor válido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Empleado EmpleadoSeleccionado = (Empleado) empleadosregistrados.getSelectedItem();
        if (EmpleadoSeleccionado == null || EmpleadoSeleccionado.getId_e() == 0 || "Seleccionar Empleado".equals(EmpleadoSeleccionado.getNombre())) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un empleado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Date fechaCompra = fechacompra.getDate();
        if (fechaCompra == null) {
             JOptionPane.showMessageDialog(this, "Debe seleccionar una fecha de compra válida.", "Advertencia", JOptionPane.WARNING_MESSAGE);
             return;
        }

        // 2. Preparar el encabezado de la Compra
        Compra compra = new Compra();
        java.util.Date fechaUtil = fechacompra.getDate(); 
        // 2. Realizamos la conversión de 3 pasos: Date -> Instant -> ZonedDateTime -> LocalDate
        LocalDate fechaLocalDate = fechaUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        compra.setFecha(fechaLocalDate); // Usamos la fecha convertida
        compra.setId_e(EmpleadoSeleccionado.getId_e());
        compra.setId_p(ProveedorSeleccionado.getId_p());
        // El folio_c se genera en la base de datos
        
        
        CompraDAO dao = new CompraDAO();
        boolean exito;
        try {
            exito = dao.registrarCompraTransaccion(compra, detallesCompra);
            if (exito) {
            JOptionPane.showMessageDialog(this, "Venta registrada con FOLIO: " + compra.getFolio_c(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarTodo();
        } else {
            JOptionPane.showMessageDialog(this, "Fallo al registrar la venta. Verifique los logs de error.", "Error Transaccional", JOptionPane.ERROR_MESSAGE);
        }
        } catch (SQLException ex) {
            Logger.getLogger(RegistrarCompra.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_registarcomprasMouseClicked

    private void registarcomprasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_registarcomprasKeyPressed
        // No se usa
    }//GEN-LAST:event_registarcomprasKeyPressed

    private void limpiarcamposMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_limpiarcamposMouseClicked
        limpiarTodo();
    }//GEN-LAST:event_limpiarcamposMouseClicked

    private void textcarritoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_textcarritoMouseClicked
        agregarAlCarrito();
    }//GEN-LAST:event_textcarritoMouseClicked

    private void textcarritoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textcarritoKeyPressed
        // No se usa
    }//GEN-LAST:event_textcarritoKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonclean;
    private javax.swing.JSpinner cantidadcomprada;
    private javax.swing.JPanel carrito;
    private javax.swing.JTextField codigop;
    private javax.swing.JComboBox<com.model.Empleado> empleadosregistrados;
    private com.toedter.calendar.JDateChooser fechacompra;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel limpiarcampos;
    private javax.swing.JComboBox<com.model.Proveedor> proveedoresregistrados;
    private javax.swing.JLabel registarcompras;
    private javax.swing.JPanel registrarcompra;
    private javax.swing.JTable tablecompraproducto;
    private javax.swing.JLabel textcantidad;
    private javax.swing.JLabel textcarrito;
    private javax.swing.JLabel textcodigo;
    private javax.swing.JLabel textcompra;
    private javax.swing.JLabel textdetalleproducto;
    private javax.swing.JLabel textempleado;
    private javax.swing.JLabel textfecha;
    private javax.swing.JLabel textproveedor;
    private javax.swing.JLabel texttotalventa;
    private javax.swing.JLabel totalventa;
    // End of variables declaration//GEN-END:variables
}
