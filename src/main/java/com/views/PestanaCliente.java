package com.views;

import com.DAO.ClienteDAO;
import com.model.Cliente;
import java.awt.Color;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class PestanaCliente extends javax.swing.JPanel {

     private DefaultTableModel modeloclientes;
     private ClienteDAO clientedao;
     private Cliente cliente;
     
    public PestanaCliente() {
        initComponents();
        clientedao = new ClienteDAO();
        configurarTabla();
        cargarClientes();
        campoidcliente.setEnabled(false);
    }
    
    public void configurarTabla() {
        String[] columnNames = {"ID", "RFC", "TIPO", "Nombre/Razon Social", "Telefono", "Domicilio"};
    
        this.modeloclientes = new DefaultTableModel(columnNames, 0) {
            // Hacemos que la tabla no sea editable directamente
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaclientes.setModel(this.modeloclientes);
        tablaclientes.getTableHeader().setResizingAllowed(true);

        tablaclientes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaclientes.getColumnModel().getColumn(1).setPreferredWidth(120);
        tablaclientes.getColumnModel().getColumn(2).setPreferredWidth(50);
        tablaclientes.getColumnModel().getColumn(3).setPreferredWidth(180);
        tablaclientes.getColumnModel().getColumn(4).setPreferredWidth(100); 
        tablaclientes.getColumnModel().getColumn(5).setPreferredWidth(160); 
    }
    
     public void cargarClientes() {
        // 1. Limpiar filas anteriores
        modeloclientes.setRowCount(0); 
        try {
            // 2. Obtener la lista de clientes activos
            List<Cliente> clientes = clientedao.listarTodosActivos();
            // 3. Iterar sobre la lista y añadir cada cliente como una fila
            for (Cliente cliente : clientes) {
                // Definimos el arreglo de 6 elementos para las 6 columnas
                Object[] rowData = new Object[6];
                // Columna 0: ID
                rowData[0] = cliente.getId_c();
                // Columna 1: RFC
                rowData[1] = cliente.getRfc();
                // Columna 2: TIPO (Fisica o Moral)
                rowData[2] = cliente.getTipo();
                // Columna 3: NOMBRE / RAZÓN SOCIAL (Usamos el campo correcto según el tipo)
                if ("Fisica".equals(cliente.getTipo())) {
                    // Si es Física, usamos el nombre (que en listarTodosActivos trae solo el primer nombre)
                    // NOTA: Para listarTodosActivos, ClientDAO usa COALESCE para traer el nombre completo.
                    // Aquí asumiremos que el DAO es capaz de devolver un nombre útil para la tabla.
                    rowData[3] = cliente.getNombre(); // Si el DAO trae solo el nombre
                    // Si el DAO concatenó el nombre y apellidos en el campo 'nombre_completo'
                    // rowData[3] = cliente.getNombre() + " " + cliente.getApellido_p(); 
                } else {
                    // Si es Moral, usamos la Razón Social
                    rowData[3] = cliente.getRazonSocial();
                }
                // Columna 4: TELÉFONO
                rowData[4] = cliente.getTelefono(); 
                // Columna 5: DOMICILIO
                rowData[5] = cliente.getDomicilio(); 

                modeloclientes.addRow(rowData);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar la lista de clientes: " + e.getMessage(), 
                "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
     
     public void buscarclienteid() {
        // 1. Limpiar filas anteriores
        modeloclientes.setRowCount(0); 
        try {
            // 2. Obtener la lista de clientes activos
            int id_cliente = clientedao.buscarIdPorIdentificador(busquedanombre.getText());
            cliente = clientedao.obtenerClienteCompletoPorId(id_cliente);
            // 3. Iterar sobre la el cliente como una fila
                // Definimos el arreglo de 6 elementos para las 6 columnas
                Object[] rowData = new Object[6];
                // Columna 0: ID
                rowData[0] = cliente.getId_c();
                // Columna 1: RFC
                rowData[1] = cliente.getRfc();
                // Columna 2: TIPO (Fisica o Moral)
                rowData[2] = cliente.getTipo();
                // Columna 3: NOMBRE / RAZÓN SOCIAL (Usamos el campo correcto según el tipo)
                if ("Fisica".equals(cliente.getTipo())) {
                    // Si es Física, usamos el nombre (que en listarTodosActivos trae solo el primer nombre)
                    // NOTA: Para listarTodosActivos, ClientDAO usa COALESCE para traer el nombre completo.
                    // Aquí asumiremos que el DAO es capaz de devolver un nombre útil para la tabla.
                    rowData[3] = cliente.getNombre(); // Si el DAO trae solo el nombre
                    // Si el DAO concatenó el nombre y apellidos en el campo 'nombre_completo'
                    // rowData[3] = cliente.getNombre() + " " + cliente.getApellido_p(); 
                } else {
                    // Si es Moral, usamos la Razón Social
                    rowData[3] = cliente.getRazonSocial();
                }
                // Columna 4: TELÉFONO
                rowData[4] = cliente.getTelefono(); 
                // Columna 5: DOMICILIO
                rowData[5] = cliente.getDomicilio(); 

                modeloclientes.addRow(rowData);  
                
                camporfc.setText(cliente.getRfc());
                campoidcliente.setText(""+cliente.getId_c());
                if ("Fisica".equals(cliente.getTipo())) {
                    tipocliente.setSelectedIndex(1);
                    camponombre.setText(cliente.getNombre());
                } else {
                     tipocliente.setSelectedIndex(2);
                     camponombre.setText(cliente.getRazonSocial());
                    
                }
                campodomicilio.setText(cliente.getDomicilio());
                campotelefono.setText(cliente.getTelefono());
                if(cliente.isEstado()){
                    campoestado.setSelectedIndex(1);
                }else{
                    campoestado.setSelectedIndex(2);
                }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error al cargar la lista de clientes: " + e.getMessage(), 
                "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tablaclientes = new javax.swing.JTable();
        pnlupdate = new javax.swing.JPanel();
        buttonupdate = new javax.swing.JLabel();
        pnlsearch = new javax.swing.JPanel();
        buttonsearch = new javax.swing.JLabel();
        pnlborrar = new javax.swing.JPanel();
        buttondelete = new javax.swing.JLabel();
        textrfc = new javax.swing.JLabel();
        camporfc = new javax.swing.JTextField();
        textidcliente = new javax.swing.JLabel();
        texttipoc = new javax.swing.JLabel();
        textnombre = new javax.swing.JLabel();
        textdomicilio = new javax.swing.JLabel();
        texttelefono = new javax.swing.JLabel();
        tipocliente = new javax.swing.JComboBox<>();
        campoidcliente = new javax.swing.JTextField();
        campodomicilio = new javax.swing.JTextField();
        camponombre = new javax.swing.JTextField();
        campotelefono = new javax.swing.JTextField();
        textbusqueda = new javax.swing.JLabel();
        busquedanombre = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        campoestado = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(252, 249, 235));
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Clientes", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Helvetica Neue", 1, 13))); // NOI18N
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tablaclientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablaclientes);

        add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 660, 180));

        pnlupdate.setBackground(new java.awt.Color(124, 123, 174));

        buttonupdate.setBackground(new java.awt.Color(124, 123, 174));
        buttonupdate.setFont(new java.awt.Font("PT Sans", 1, 16)); // NOI18N
        buttonupdate.setForeground(new java.awt.Color(255, 255, 255));
        buttonupdate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        buttonupdate.setText("Actualizar Datos");
        buttonupdate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonupdateMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlupdateLayout = new javax.swing.GroupLayout(pnlupdate);
        pnlupdate.setLayout(pnlupdateLayout);
        pnlupdateLayout.setHorizontalGroup(
            pnlupdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonupdate, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        pnlupdateLayout.setVerticalGroup(
            pnlupdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonupdate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        add(pnlupdate, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 220, 160, 30));

        pnlsearch.setBackground(new java.awt.Color(124, 123, 174));

        buttonsearch.setBackground(new java.awt.Color(124, 123, 174));
        buttonsearch.setFont(new java.awt.Font("PT Sans", 1, 16)); // NOI18N
        buttonsearch.setForeground(new java.awt.Color(255, 255, 255));
        buttonsearch.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        buttonsearch.setText("Buscar");
        buttonsearch.setToolTipText("");
        buttonsearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttonsearchMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlsearchLayout = new javax.swing.GroupLayout(pnlsearch);
        pnlsearch.setLayout(pnlsearchLayout);
        pnlsearchLayout.setHorizontalGroup(
            pnlsearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonsearch, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        pnlsearchLayout.setVerticalGroup(
            pnlsearchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonsearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        add(pnlsearch, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 220, 160, 30));

        pnlborrar.setBackground(new java.awt.Color(124, 123, 174));

        buttondelete.setBackground(new java.awt.Color(124, 123, 174));
        buttondelete.setFont(new java.awt.Font("PT Sans", 1, 16)); // NOI18N
        buttondelete.setForeground(new java.awt.Color(255, 255, 255));
        buttondelete.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        buttondelete.setText("Borrar");
        buttondelete.setToolTipText("");
        buttondelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                buttondeleteMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlborrarLayout = new javax.swing.GroupLayout(pnlborrar);
        pnlborrar.setLayout(pnlborrarLayout);
        pnlborrarLayout.setHorizontalGroup(
            pnlborrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttondelete, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        pnlborrarLayout.setVerticalGroup(
            pnlborrarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttondelete, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        add(pnlborrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(485, 220, 160, 30));

        textrfc.setText("RFC:");
        add(textrfc, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, -1, 25));

        camporfc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        camporfc.setBorder(null);
        add(camporfc, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, 170, 25));

        textidcliente.setText("ID Cliente:");
        add(textidcliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, 25));

        texttipoc.setText("Tipo de Cliente:");
        add(texttipoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, -1, 25));

        textnombre.setText("Nombre/Razón Social:");
        add(textnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 310, -1, 25));

        textdomicilio.setText("Domicilio:");
        add(textdomicilio, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 270, -1, 25));

        texttelefono.setText("Teléfono:");
        add(texttelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 350, -1, 25));

        tipocliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Persona Fisica", "Persona Moral" }));
        tipocliente.setBorder(null);
        add(tipocliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 390, 150, 25));

        campoidcliente.setBorder(null);
        add(campoidcliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 350, 130, 25));

        campodomicilio.setBorder(null);
        add(campodomicilio, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 270, 200, 25));

        camponombre.setBorder(null);
        add(camponombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 310, 200, 25));

        campotelefono.setBorder(null);
        add(campotelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 350, 150, 25));

        textbusqueda.setText("BUSQUEDA:");
        add(textbusqueda, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, -1, 25));

        busquedanombre.setForeground(new java.awt.Color(153, 153, 153));
        busquedanombre.setText("Escribir RFC o Nombre");
        busquedanombre.setBorder(null);
        busquedanombre.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                busquedanombreMouseClicked(evt);
            }
        });
        add(busquedanombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 270, 150, 25));

        jLabel1.setText("Estado:");
        add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 390, -1, 25));

        campoestado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Activo", "Desactivo" }));
        add(campoestado, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 390, 150, 25));
    }// </editor-fold>//GEN-END:initComponents

    private void buttonupdateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonupdateMouseClicked
        try {
            cliente.setRfc(camporfc.getText());
            cliente.setTipo(tipocliente.getSelectedItem().toString());
            cliente.setDomicilio(campodomicilio.getText());
            cliente.setTelefono(campotelefono.getText());
            
            if ("Fisica".equals(cliente.getTipo())) {
                    cliente.setNombre(camponombre.getText());
                    cliente.setRazonSocial(null);
                } else {
                    cliente.setRazonSocial(camponombre.getText());
                    cliente.setNombre(null);
                }
            
            // 3. Llamar al método del DAO para ejecutar la actualización
            if (clientedao.actualizarDatosCliente(cliente)) {
                javax.swing.JOptionPane.showMessageDialog(null, "Cliente actualizado con éxito.", "Actualización", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                configurarTabla();
                cargarClientes();
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "No se pudo actualizar el cliente.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
             Logger.getLogger(PestanaCliente.class.getName()).log(Level.SEVERE, null, ex);
             javax.swing.JOptionPane.showMessageDialog(null, "Error al actualizar la base de datos: " + ex.getMessage(), "Error SQL", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_buttonupdateMouseClicked

    private void buttonsearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttonsearchMouseClicked
        buscarclienteid();
    }//GEN-LAST:event_buttonsearchMouseClicked

    private void buttondeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_buttondeleteMouseClicked
         try {
             if(clientedao.eliminarCliente(cliente.getId_c())){
                 javax.swing.JOptionPane.showMessageDialog(null, "Cliente Eliminado con éxito.", "Actualización", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    configurarTabla();
                    cargarClientes();
             }else{
                 javax.swing.JOptionPane.showMessageDialog(null, "No se pudo eliminar el cliente.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
         } catch (SQLException ex) {
             Logger.getLogger(PestanaCliente.class.getName()).log(Level.SEVERE, null, ex);
         }
    }//GEN-LAST:event_buttondeleteMouseClicked

    private void busquedanombreMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_busquedanombreMouseClicked
        busquedanombre.setText("");
        busquedanombre.setForeground(Color.BLACK);
    }//GEN-LAST:event_busquedanombreMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField busquedanombre;
    private javax.swing.JLabel buttondelete;
    private javax.swing.JLabel buttonsearch;
    private javax.swing.JLabel buttonupdate;
    private javax.swing.JTextField campodomicilio;
    private javax.swing.JComboBox<String> campoestado;
    private javax.swing.JTextField campoidcliente;
    private javax.swing.JTextField camponombre;
    private javax.swing.JTextField camporfc;
    private javax.swing.JTextField campotelefono;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlborrar;
    private javax.swing.JPanel pnlsearch;
    private javax.swing.JPanel pnlupdate;
    private javax.swing.JTable tablaclientes;
    private javax.swing.JLabel textbusqueda;
    private javax.swing.JLabel textdomicilio;
    private javax.swing.JLabel textidcliente;
    private javax.swing.JLabel textnombre;
    private javax.swing.JLabel textrfc;
    private javax.swing.JLabel texttelefono;
    private javax.swing.JLabel texttipoc;
    private javax.swing.JComboBox<String> tipocliente;
    // End of variables declaration//GEN-END:variables
}
