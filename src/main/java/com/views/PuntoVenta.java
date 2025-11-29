package com.views;

import com.DAO.ClienteDAO;
import com.DAO.ProductoDAO;
import com.model.Cliente;
import com.model.DetalleVentaTemporal;
import com.model.Producto;
import com.model.Usuario;
import java.awt.List;
import java.util.*;
import javax.swing.JOptionPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

public class PuntoVenta extends javax.swing.JPanel {

    private Usuario usuarioActual;
    private Producto productoActual;
    private DefaultTableModel modeloCarrito; 
    private double totalVentaAcumulado = 0.0; 
    private java.util.List<DetalleVentaTemporal> detallesVenta;
    
    public PuntoVenta(Usuario user) {
        this.usuarioActual = user;
        initComponents();
        casillavendido.setEnabled(false);
        
        modeloCarrito = (DefaultTableModel) tabladecarrito.getModel();
        detallesVenta = new ArrayList<>();
        
        modeloCarrito.setColumnIdentifiers(new Object[]{"Producto", "Cantidad", "Precio Unitario", "Subtotal"});
        cargarClientes();
    }

    private void buscarProducto() {
        String codigoStr = busquedafolio.getText().trim();
        int codigo;

        if (codigoStr.isEmpty() || codigoStr.equals("Codigo de producto")) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el código del producto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            limpiarCamposProducto();
            return;
        }

        try {
            codigo = Integer.parseInt(codigoStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El código debe ser un número entero (ej: 5000).", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
            limpiarCamposProducto();
            return;
        }
        try {
            ProductoDAO dao = new ProductoDAO();
            Producto productoEncontrado = dao.buscarProductoPorCodigo(codigo); 

            if (productoEncontrado != null) {
                this.productoActual = productoEncontrado;
                nombreproducto.setText(productoEncontrado.getDescripcion());
                categoriaproducto.setText(productoEncontrado.getCategoria());

                String preciovFormateado = String.format("%.2f", productoEncontrado.getPrecio_v());
                productopreciov.setText(preciovFormateado); 
                
                String preciocFormateado = String.format("%.2f", productoEncontrado.getPrecio_c());
                productoprecioc.setText(preciocFormateado); 

                configurarLimiteCantidad(productoEncontrado.getExitencia());
                
                int stock = productoEncontrado.getExitencia();
                boolean hayStock = stock > 0;
                // Se marca si hay stock, se desmarca si es 0
                casillavendido.setSelected(hayStock); 
                casillavendido.setEnabled(false); 
                configurarLimiteCantidad(stock);
    
            } else {
                JOptionPane.showMessageDialog(this, "Código no válido. Producto no encontrado en el inventario.", "Búsqueda Fallida", JOptionPane.ERROR_MESSAGE);
                limpiarCamposProducto();
            }

        } catch(Exception e) { 
            JOptionPane.showMessageDialog(this, 
                "Error del sistema al consultar la base de datos. Contacte a soporte.\nDetalle: " + e.getMessage(), 
                "Error de Consulta", 
                JOptionPane.ERROR_MESSAGE);
            limpiarCamposProducto();
        }
    }

    // Para resetear los campos
    private void limpiarCamposProducto() {
        nombreproducto.setText("-----");
        categoriaproducto.setText("-----");
        productopreciov.setText("-----"); 
        productoprecioc.setText("-----");
        casillavendido.setEnabled(false); 
    }
    
    private void configurarLimiteCantidad(int maxStock) {
        int valorInicial = (maxStock > 0) ? 1 : 0;
        int valorMaximo = (maxStock > 0) ? maxStock : 0;

        SpinnerNumberModel model = new SpinnerNumberModel(
            valorInicial,     // Valor actual
            0,                // Mínimo: No se puede vender menos de 0
            valorMaximo,      // Máximo: El límite de existencia
            1                 // Paso: Incrementa de 1 en 1
        );
        
        cantidadvendida.setModel(model);
        cantidadvendida.setEnabled(maxStock > 0);

        if (maxStock == 0) {
            JOptionPane.showMessageDialog(this, "¡Advertencia! El producto seleccionado no tiene existencia.", "Sin Stock", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void agregarAlCarrito() {
        if (productoActual == null) {
            JOptionPane.showMessageDialog(this, "Debe buscar y cargar un producto primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }


        int cantidad = (Integer) cantidadvendida.getValue();
    
        if (cantidad <= 0 || cantidad > productoActual.getExitencia()) {
            JOptionPane.showMessageDialog(this, "Cantidad no válida o excede la existencia (" + productoActual.getExitencia() + ").", "Error de Stock", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Cálculos
        double precioUnitario = productoActual.getPrecio_v();
        double subtotal = precioUnitario * cantidad;
    
  
        Object[] fila = new Object[]{
            productoActual.getDescripcion(), // Necesitas el ID real del producto para la DB
            cantidad,
            String.format("%.2f", precioUnitario),
            String.format("%.2f", subtotal)
        };
        modeloCarrito.addRow(fila);
    
  
    
        detallesVenta.add(new DetalleVentaTemporal(productoActual.getCodigo(), productoActual.getDescripcion(), cantidad, precioUnitario, subtotal));
    
   
        actualizarTotalVenta(subtotal);
    
    
        limpiarCamposProducto();
        productoActual = null;
    }

    private void actualizarTotalVenta(double subtotalNuevo) {
        totalVentaAcumulado += subtotalNuevo;
        totalventa.setText(String.format("%.2f", totalVentaAcumulado));
    }
    
    private void cargarClientes() {
        try {
            ClienteDAO dao = new ClienteDAO();
            java.util.List<Cliente> clientes = dao.listarClientes();

            clienteregistrados.removeAllItems(); 
            for (Cliente cliente : clientes) {
                clienteregistrados.addItem(cliente.toString());
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la lista de clientes: " + e.getMessage(), "Error de Carga", JOptionPane.ERROR_MESSAGE);
        }
    }

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        busquedatext = new javax.swing.JLabel();
        busquedafolio = new javax.swing.JTextField();
        separadorbusque = new javax.swing.JSeparator();
        nombreproducto = new javax.swing.JLabel();
        txtnombre = new javax.swing.JLabel();
        txtcategoria = new javax.swing.JLabel();
        categoriaproducto = new javax.swing.JLabel();
        txtcantidad = new javax.swing.JLabel();
        productopreciov = new javax.swing.JLabel();
        casillavendido = new javax.swing.JCheckBox();
        cantidadvendida = new javax.swing.JSpinner();
        txtprecioventa = new javax.swing.JLabel();
        separadorcarrito = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabladecarrito = new javax.swing.JTable();
        txttotalventa = new javax.swing.JLabel();
        totalventa = new javax.swing.JLabel();
        clienteregistrados = new javax.swing.JComboBox<>();
        txtcliente = new javax.swing.JLabel();
        panelfinalizarventa = new javax.swing.JPanel();
        finalizarventa = new javax.swing.JLabel();
        pnlcarrito = new javax.swing.JPanel();
        agregaralcarrito = new javax.swing.JLabel();
        pnlbuscar = new javax.swing.JPanel();
        buscar = new javax.swing.JLabel();
        txtpreciocompra = new javax.swing.JLabel();
        productoprecioc = new javax.swing.JLabel();

        setBackground(new java.awt.Color(252, 249, 235));
        setPreferredSize(new java.awt.Dimension(300, 300));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        busquedatext.setFont(new java.awt.Font("PT Sans", 1, 16)); // NOI18N
        busquedatext.setText("Busqueda:");
        add(busquedatext, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 10, 75, 25));

        busquedafolio.setForeground(new java.awt.Color(204, 204, 204));
        busquedafolio.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        busquedafolio.setText("Codigo de producto");
        busquedafolio.setBorder(null);
        busquedafolio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                busquedafolioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                busquedafolioFocusLost(evt);
            }
        });
        add(busquedafolio, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 150, 25));

        separadorbusque.setBackground(new java.awt.Color(0, 0, 0));
        add(separadorbusque, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 35, 150, -1));

        nombreproducto.setFont(new java.awt.Font("PT Sans", 0, 16)); // NOI18N
        nombreproducto.setText("-----");
        add(nombreproducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 60, -1, -1));

        txtnombre.setText("Nombre:");
        add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, -1, -1));

        txtcategoria.setText("Categoria:");
        add(txtcategoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, -1, -1));

        categoriaproducto.setFont(new java.awt.Font("PT Sans", 0, 16)); // NOI18N
        categoriaproducto.setText("-----");
        add(categoriaproducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 90, -1, -1));

        txtcantidad.setText("Cantidad:");
        add(txtcantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, -1, 20));

        productopreciov.setFont(new java.awt.Font("PT Sans", 0, 16)); // NOI18N
        productopreciov.setText("-----");
        add(productopreciov, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 120, -1, -1));

        casillavendido.setText("Puede ser vendido");
        add(casillavendido, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, 150, 20));

        cantidadvendida.setToolTipText("");
        add(cantidadvendida, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 60, -1, 20));

        txtprecioventa.setText("Precio Venta:");
        add(txtprecioventa, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        separadorcarrito.setForeground(new java.awt.Color(0, 0, 0));
        separadorcarrito.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        add(separadorcarrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 510, 5));

        tabladecarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Producto", "Cantidad", "Precio Unitario", "Subtotal"
            }
        ));
        jScrollPane1.setViewportView(tabladecarrito);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, 490, 100));

        txttotalventa.setText("Total Venta:");
        add(txttotalventa, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 300, -1, -1));

        totalventa.setText("0.0");
        add(totalventa, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, -1, -1));

        add(clienteregistrados, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 300, 150, 20));

        txtcliente.setText("Cliente:");
        add(txtcliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 300, -1, 20));

        panelfinalizarventa.setBackground(new java.awt.Color(124, 123, 174));

        finalizarventa.setBackground(new java.awt.Color(124, 123, 174));
        finalizarventa.setFont(new java.awt.Font("PT Sans", 1, 16)); // NOI18N
        finalizarventa.setForeground(new java.awt.Color(255, 255, 255));
        finalizarventa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        finalizarventa.setText("Finalizar Venta");

        javax.swing.GroupLayout panelfinalizarventaLayout = new javax.swing.GroupLayout(panelfinalizarventa);
        panelfinalizarventa.setLayout(panelfinalizarventaLayout);
        panelfinalizarventaLayout.setHorizontalGroup(
            panelfinalizarventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(finalizarventa, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        panelfinalizarventaLayout.setVerticalGroup(
            panelfinalizarventaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(finalizarventa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        add(panelfinalizarventa, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 325, 160, 25));

        pnlcarrito.setBackground(new java.awt.Color(124, 123, 174));

        agregaralcarrito.setBackground(new java.awt.Color(124, 123, 174));
        agregaralcarrito.setFont(new java.awt.Font("PT Sans", 1, 16)); // NOI18N
        agregaralcarrito.setForeground(new java.awt.Color(255, 255, 255));
        agregaralcarrito.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        agregaralcarrito.setText("Agregar al carrito");
        agregaralcarrito.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregaralcarritoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlcarritoLayout = new javax.swing.GroupLayout(pnlcarrito);
        pnlcarrito.setLayout(pnlcarritoLayout);
        pnlcarritoLayout.setHorizontalGroup(
            pnlcarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(agregaralcarrito, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        pnlcarritoLayout.setVerticalGroup(
            pnlcarritoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(agregaralcarrito, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        add(pnlcarrito, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 140, 160, 25));

        pnlbuscar.setBackground(new java.awt.Color(124, 123, 174));

        buscar.setBackground(new java.awt.Color(124, 123, 174));
        buscar.setFont(new java.awt.Font("PT Sans", 1, 16)); // NOI18N
        buscar.setForeground(new java.awt.Color(255, 255, 255));
        buscar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        buscar.setText("Buscar");
        buscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buscarMouseClicked(evt);
            }
        });
        buscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                buscarKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlbuscarLayout = new javax.swing.GroupLayout(pnlbuscar);
        pnlbuscar.setLayout(pnlbuscarLayout);
        pnlbuscarLayout.setHorizontalGroup(
            pnlbuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlbuscarLayout.setVerticalGroup(
            pnlbuscarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buscar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
        );

        add(pnlbuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 10, 100, 25));

        txtpreciocompra.setText("Precio Compra:");
        add(txtpreciocompra, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, -1, -1));

        productoprecioc.setFont(new java.awt.Font("PT Sans", 0, 16)); // NOI18N
        productoprecioc.setText("-----");
        add(productoprecioc, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 150, -1, -1));
    }// </editor-fold>//GEN-END:initComponents

    private void buscarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buscarMouseClicked
        buscarProducto();
    }//GEN-LAST:event_buscarMouseClicked

    private void buscarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarKeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
        buscarProducto();
        }
    }//GEN-LAST:event_buscarKeyPressed

    private void busquedafolioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_busquedafolioFocusGained
        if (busquedafolio.getText().equals("Codigo de producto")) {
            busquedafolio.setText("");
            busquedafolio.setForeground(java.awt.Color.BLACK);
        }
    }//GEN-LAST:event_busquedafolioFocusGained

    private void busquedafolioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_busquedafolioFocusLost
        if (busquedafolio.getText().isEmpty()) {
            busquedafolio.setText("Codigo de producto");
            busquedafolio.setForeground(new java.awt.Color(204, 204, 204));
        }
    }//GEN-LAST:event_busquedafolioFocusLost

    private void agregaralcarritoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agregaralcarritoMouseClicked
        agregarAlCarrito();
    }//GEN-LAST:event_agregaralcarritoMouseClicked

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel agregaralcarrito;
    private javax.swing.JLabel buscar;
    private javax.swing.JTextField busquedafolio;
    private javax.swing.JLabel busquedatext;
    private javax.swing.JSpinner cantidadvendida;
    private javax.swing.JCheckBox casillavendido;
    private javax.swing.JLabel categoriaproducto;
    private javax.swing.JComboBox<String> clienteregistrados;
    private javax.swing.JLabel finalizarventa;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nombreproducto;
    private javax.swing.JPanel panelfinalizarventa;
    private javax.swing.JPanel pnlbuscar;
    private javax.swing.JPanel pnlcarrito;
    private javax.swing.JLabel productoprecioc;
    private javax.swing.JLabel productopreciov;
    private javax.swing.JSeparator separadorbusque;
    private javax.swing.JSeparator separadorcarrito;
    private javax.swing.JTable tabladecarrito;
    private javax.swing.JLabel totalventa;
    private javax.swing.JLabel txtcantidad;
    private javax.swing.JLabel txtcategoria;
    private javax.swing.JLabel txtcliente;
    private javax.swing.JLabel txtnombre;
    private javax.swing.JLabel txtpreciocompra;
    private javax.swing.JLabel txtprecioventa;
    private javax.swing.JLabel txttotalventa;
    // End of variables declaration//GEN-END:variables
}
