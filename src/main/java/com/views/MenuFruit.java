package com.views;

import com.conexion.Conexion;
import java.awt.Color;
import java.awt.Image;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;


public class MenuFruit extends javax.swing.JFrame {
    
    private java.awt.CardLayout cardLayout;
    private String nombreUsuario;
    private String rolFuncional;

    public MenuFruit(String usuarioLogin) {
        String grupoFuncional = obtenerGrupoFuncional(usuarioLogin);
        this.nombreUsuario = usuarioLogin;    // Guarda "usuario"
        this.rolFuncional = grupoFuncional;  // Guarda "login"
        
        initComponents();
        mostrarFechaActual(); 
        SetImageLabel(logo,"src/main/resources/images/newlogo.png");
        textwelcome.setText("Bienvenido, " + this.nombreUsuario);
        rol.setText("Usted entro como: " + grupoFuncional);
        configurarPermisos();
      
        cardLayout = (java.awt.CardLayout) panelcontenedor.getLayout();
        PuntoVenta pnlVentas = new PuntoVenta(); 
        InventarioProducto pnlProducto = new InventarioProducto();
        HistorialCompras pnlCompras = new HistorialCompras();
        RegistrarCompra pnlregistrarc = new RegistrarCompra();
        PestanaCliente pnlcliente = new PestanaCliente();
        panelcontenedor.add(pnlVentas, "Ventas");
        panelcontenedor.add(pnlProducto, "Producto");
        panelcontenedor.add(pnlCompras, "Compras");
        panelcontenedor.add(pnlregistrarc, "RegistrarCompra");
        panelcontenedor.add(pnlcliente, "Cliente");
    }
    
    public void SetImageLabel(JLabel labelname, String root){
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(labelname.getWidth() ,labelname.getHeight(), Image.SCALE_DEFAULT));
        labelname.setIcon(icon);
        this.repaint();
    }
    
    private String obtenerGrupoFuncional(String usuario) {
        // Definimos los grupos (roles) que vamos a verificar en PostgreSQL
        String[] grupos = {"administrador_sistema", "supervisor", "vendedor"}; 

        // Consulta SQL para verificar si el usuario es miembro de un rol
        String sql = "SELECT 1 FROM pg_auth_members m JOIN pg_roles g ON m.roleid = g.oid "
                   + "JOIN pg_roles u ON m.member = u.oid "
                   + "WHERE u.rolname = ? AND g.rolname = ?";

        //Todo el try/catch debe centrarse en la conexión de Administrador
        try (Connection adminConn = Conexion.getInstance().getConnection()) {

            try (PreparedStatement ps = adminConn.prepareStatement(sql)) {

                for (String grupo : grupos) {
                    ps.setString(1, usuario); // Usuario que inició sesión
                    ps.setString(2, grupo);   // Rol de grupo a verificar (administrador_sistema, supervisor, etc.)

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            // Se encontró el grupo, retornamos el nombre simplificado para el switch.
                            if (grupo.equals("administrador_sistema")) {
                                return "Administrador";
                            } else if (grupo.equals("supervisor")) {
                                return "Supervisor";
                            } else if (grupo.equals("vendedor")) { // Tu rol de grupo "vendedor"
                                return "Vendedor";
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            // Solo capturará errores de la consulta misma (no de autenticación)
            System.err.println("Error al obtener grupos funcionales para " + usuario + ": " + e.getMessage());
            return "Default"; 
        }
    
        return "Default"; 
    }
    
    private void configurarPermisos() {
        String rol = this.rolFuncional;

        puntoventatext.setVisible(false);
        productotext.setVisible(false);
        historialcomprastext.setVisible(false);
        clientestext.setVisible(false);
        reportexx.setVisible(false);
        empleadotext.setVisible(false);

        switch (rol) {
            case "Administrador":
                puntoventatext.setVisible(true);
                productotext.setVisible(true);
                historialcomprastext.setVisible(true);
                clientestext.setVisible(true);
                reportexx.setVisible(true);
                empleadotext.setVisible(true);
                break;

            case "Supervisor":
                puntoventatext.setVisible(true);
                productotext.setVisible(true);
                historialcomprastext.setVisible(true);
                clientestext.setVisible(true);
                reportexx.setVisible(true);
                break;

            default:
                button1.setVisible(true);
                break;
        }
    }
    
    private void mostrarFechaActual() {
        // Obtenemos la fecha de hoy
        Date fechaHoy = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy");
        String fechaFormateada = formato.format(fechaHoy);
        fechaactual.setText("Hoy es " + fechaFormateada);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gb = new javax.swing.JPanel();
        rol = new javax.swing.JLabel();
        textwelcome = new javax.swing.JLabel();
        loginout = new javax.swing.JPanel();
        loginouttext = new javax.swing.JLabel();
        encabezado = new javax.swing.JPanel();
        fechaactual = new javax.swing.JLabel();
        mainbar = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        button1 = new javax.swing.JPanel();
        puntoventatext = new javax.swing.JLabel();
        button2 = new javax.swing.JPanel();
        productotext = new javax.swing.JLabel();
        button3 = new javax.swing.JPanel();
        historialcomprastext = new javax.swing.JLabel();
        button4 = new javax.swing.JPanel();
        clientestext = new javax.swing.JLabel();
        button5 = new javax.swing.JPanel();
        reportexx = new javax.swing.JLabel();
        button6 = new javax.swing.JPanel();
        empleadotext = new javax.swing.JLabel();
        button7 = new javax.swing.JPanel();
        registrarcompra = new javax.swing.JLabel();
        panelcontenedor = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        gb.setBackground(new java.awt.Color(255, 255, 255));
        gb.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rol.setToolTipText("");
        gb.add(rol, new org.netbeans.lib.awtextra.AbsoluteConstraints(175, 15, 300, 20));

        textwelcome.setFont(new java.awt.Font("PT Sans", 0, 28)); // NOI18N
        textwelcome.setText("Bienvenido, Ricardo Valencia");
        gb.add(textwelcome, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 40, 360, 50));

        loginout.setBackground(new java.awt.Color(191, 255, 141));

        loginouttext.setFont(new java.awt.Font("PT Sans", 1, 18)); // NOI18N
        loginouttext.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        loginouttext.setText("Cerrar Sesión");
        loginouttext.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        loginouttext.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        loginouttext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                loginouttextMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout loginoutLayout = new javax.swing.GroupLayout(loginout);
        loginout.setLayout(loginoutLayout);
        loginoutLayout.setHorizontalGroup(
            loginoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loginouttext, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        );
        loginoutLayout.setVerticalGroup(
            loginoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(loginouttext, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        gb.add(loginout, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 60, 120, 40));

        encabezado.setBackground(new java.awt.Color(254, 222, 95));

        fechaactual.setText("Hoy es lunes 7 de diciembre de 2025");

        javax.swing.GroupLayout encabezadoLayout = new javax.swing.GroupLayout(encabezado);
        encabezado.setLayout(encabezadoLayout);
        encabezadoLayout.setHorizontalGroup(
            encabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(encabezadoLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(fechaactual)
                .addContainerGap(510, Short.MAX_VALUE))
        );
        encabezadoLayout.setVerticalGroup(
            encabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, encabezadoLayout.createSequentialGroup()
                .addContainerGap(112, Short.MAX_VALUE)
                .addComponent(fechaactual)
                .addContainerGap())
        );

        gb.add(encabezado, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 750, 135));

        mainbar.setBackground(new java.awt.Color(254, 222, 95));
        mainbar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/newlogo.png"))); // NOI18N
        mainbar.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 10, 120, 120));

        button1.setBackground(new java.awt.Color(254, 222, 95));

        puntoventatext.setBackground(new java.awt.Color(124, 123, 242));
        puntoventatext.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        puntoventatext.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        puntoventatext.setText("Punto de venta");
        puntoventatext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                puntoventatextMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                puntoventatextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                puntoventatextMouseExited(evt);
            }
        });

        javax.swing.GroupLayout button1Layout = new javax.swing.GroupLayout(button1);
        button1.setLayout(button1Layout);
        button1Layout.setHorizontalGroup(
            button1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(puntoventatext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        button1Layout.setVerticalGroup(
            button1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(puntoventatext, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        mainbar.add(button1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 160, 40));

        button2.setBackground(new java.awt.Color(254, 222, 95));

        productotext.setBackground(new java.awt.Color(124, 123, 242));
        productotext.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        productotext.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productotext.setText("Producto");
        productotext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                productotextMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                productotextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                productotextMouseExited(evt);
            }
        });

        javax.swing.GroupLayout button2Layout = new javax.swing.GroupLayout(button2);
        button2.setLayout(button2Layout);
        button2Layout.setHorizontalGroup(
            button2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productotext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        button2Layout.setVerticalGroup(
            button2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(productotext, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        mainbar.add(button2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 190, 160, 40));

        button3.setBackground(new java.awt.Color(254, 222, 95));

        historialcomprastext.setBackground(new java.awt.Color(124, 123, 242));
        historialcomprastext.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        historialcomprastext.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        historialcomprastext.setText("Historial Compras");
        historialcomprastext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                historialcomprastextMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                historialcomprastextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                historialcomprastextMouseExited(evt);
            }
        });

        javax.swing.GroupLayout button3Layout = new javax.swing.GroupLayout(button3);
        button3.setLayout(button3Layout);
        button3Layout.setHorizontalGroup(
            button3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(historialcomprastext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        button3Layout.setVerticalGroup(
            button3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(historialcomprastext, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        mainbar.add(button3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 160, 40));

        button4.setBackground(new java.awt.Color(254, 222, 95));

        clientestext.setBackground(new java.awt.Color(124, 123, 242));
        clientestext.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        clientestext.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clientestext.setText("Clientes");
        clientestext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clientestextMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                clientestextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                clientestextMouseExited(evt);
            }
        });

        javax.swing.GroupLayout button4Layout = new javax.swing.GroupLayout(button4);
        button4.setLayout(button4Layout);
        button4Layout.setHorizontalGroup(
            button4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientestext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        button4Layout.setVerticalGroup(
            button4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(clientestext, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        mainbar.add(button4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 160, 40));

        button5.setBackground(new java.awt.Color(254, 222, 95));

        reportexx.setBackground(new java.awt.Color(124, 123, 242));
        reportexx.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        reportexx.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        reportexx.setText("Reportes");
        reportexx.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                reportexxMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                reportexxMouseExited(evt);
            }
        });

        javax.swing.GroupLayout button5Layout = new javax.swing.GroupLayout(button5);
        button5.setLayout(button5Layout);
        button5Layout.setHorizontalGroup(
            button5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(reportexx, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        button5Layout.setVerticalGroup(
            button5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(reportexx, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        mainbar.add(button5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 390, 160, 40));

        button6.setBackground(new java.awt.Color(254, 222, 95));

        empleadotext.setBackground(new java.awt.Color(124, 123, 242));
        empleadotext.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        empleadotext.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        empleadotext.setText("Empleados");
        empleadotext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                empleadotextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                empleadotextMouseExited(evt);
            }
        });

        javax.swing.GroupLayout button6Layout = new javax.swing.GroupLayout(button6);
        button6.setLayout(button6Layout);
        button6Layout.setHorizontalGroup(
            button6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(empleadotext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        button6Layout.setVerticalGroup(
            button6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(empleadotext, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        mainbar.add(button6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 440, 160, 40));

        button7.setBackground(new java.awt.Color(254, 222, 95));

        registrarcompra.setBackground(new java.awt.Color(124, 123, 242));
        registrarcompra.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        registrarcompra.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        registrarcompra.setText("Compra");
        registrarcompra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                registrarcompraMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registrarcompraMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registrarcompraMouseExited(evt);
            }
        });

        javax.swing.GroupLayout button7Layout = new javax.swing.GroupLayout(button7);
        button7.setLayout(button7Layout);
        button7Layout.setHorizontalGroup(
            button7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(registrarcompra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
        );
        button7Layout.setVerticalGroup(
            button7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(registrarcompra, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        mainbar.add(button7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 290, 160, 40));

        gb.add(mainbar, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 160, 600));

        panelcontenedor.setBackground(new java.awt.Color(252, 249, 235));
        panelcontenedor.setLayout(new java.awt.CardLayout());
        gb.add(panelcontenedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 155, 700, 425));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(gb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void loginouttextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_loginouttextMouseClicked
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
    
    if (confirm == JOptionPane.YES_OPTION) {
        Login login = new Login();
        login.setVisible(true);
        this.dispose();
    }
    }//GEN-LAST:event_loginouttextMouseClicked

    private void puntoventatextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_puntoventatextMouseEntered
        button1.setBackground(new Color(124, 123, 174));
    }//GEN-LAST:event_puntoventatextMouseEntered

    private void puntoventatextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_puntoventatextMouseExited
        button1.setBackground(new Color(254, 222, 95));
    }//GEN-LAST:event_puntoventatextMouseExited

    private void productotextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productotextMouseEntered
        button2.setBackground(new Color(124, 123, 174));
    }//GEN-LAST:event_productotextMouseEntered

    private void productotextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productotextMouseExited
        button2.setBackground(new Color(254, 222, 95));
    }//GEN-LAST:event_productotextMouseExited

    private void historialcomprastextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_historialcomprastextMouseEntered
        button3.setBackground(new Color(124, 123, 174));
    }//GEN-LAST:event_historialcomprastextMouseEntered

    private void historialcomprastextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_historialcomprastextMouseExited
        button3.setBackground(new Color(254, 222, 95));
    }//GEN-LAST:event_historialcomprastextMouseExited

    private void clientestextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientestextMouseEntered
        button4.setBackground(new Color(124, 123, 174));
    }//GEN-LAST:event_clientestextMouseEntered

    private void clientestextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientestextMouseExited
        button4.setBackground(new Color(254, 222, 95));
    }//GEN-LAST:event_clientestextMouseExited

    private void reportexxMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportexxMouseEntered
        button5.setBackground(new Color(124, 123, 174));
    }//GEN-LAST:event_reportexxMouseEntered

    private void reportexxMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reportexxMouseExited
        button5.setBackground(new Color(254, 222, 95));
    }//GEN-LAST:event_reportexxMouseExited

    private void empleadotextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empleadotextMouseEntered
        button6.setBackground(new Color(124, 123, 174));
    }//GEN-LAST:event_empleadotextMouseEntered

    private void empleadotextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_empleadotextMouseExited
        button6.setBackground(new Color(254, 222, 95));
    }//GEN-LAST:event_empleadotextMouseExited

    private void puntoventatextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_puntoventatextMouseClicked
        cardLayout.show(panelcontenedor, "Ventas");
        resetButtonColors();
        button1.setBackground(new Color(124, 123, 174));
    }//GEN-LAST:event_puntoventatextMouseClicked

    private void productotextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productotextMouseClicked
        cardLayout.show(panelcontenedor, "Producto");
        resetButtonColors();
        button2.setBackground(new Color(124, 123, 174));
    }//GEN-LAST:event_productotextMouseClicked

    private void historialcomprastextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_historialcomprastextMouseClicked
        cardLayout.show(panelcontenedor, "Compras");
        resetButtonColors();
        button3.setBackground(new Color(124, 123, 174));
    }//GEN-LAST:event_historialcomprastextMouseClicked

    private void registrarcompraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registrarcompraMouseClicked
        cardLayout.show(panelcontenedor, "RegistrarCompra");
        resetButtonColors();
        button7.setBackground(new Color(124, 123, 174));
    }//GEN-LAST:event_registrarcompraMouseClicked

    private void registrarcompraMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registrarcompraMouseEntered
        button7.setBackground(new Color(124, 123, 174));
    }//GEN-LAST:event_registrarcompraMouseEntered

    private void registrarcompraMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_registrarcompraMouseExited
        button7.setBackground(new Color(254, 222, 95));
    }//GEN-LAST:event_registrarcompraMouseExited

    private void clientestextMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clientestextMouseClicked
        cardLayout.show(panelcontenedor, "Cliente");
        resetButtonColors();
        button4.setBackground(new Color(124, 123, 174));
    }//GEN-LAST:event_clientestextMouseClicked

    private void resetButtonColors() {
    Color defaultColor = new Color(254, 222, 95); // Amarillo original
        button1.setBackground(defaultColor);
        button2.setBackground(defaultColor);
        button3.setBackground(defaultColor);
        button4.setBackground(defaultColor);
        button5.setBackground(defaultColor);
        button6.setBackground(defaultColor);
    }
    
    /**
     * @param args the command line arguments
    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuFruit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuFruit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuFruit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuFruit.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuFruit(Usuario user).setVisible(true);
            }
        });
    } */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel button1;
    private javax.swing.JPanel button2;
    private javax.swing.JPanel button3;
    private javax.swing.JPanel button4;
    private javax.swing.JPanel button5;
    private javax.swing.JPanel button6;
    private javax.swing.JPanel button7;
    private javax.swing.JLabel clientestext;
    private javax.swing.JLabel empleadotext;
    private javax.swing.JPanel encabezado;
    private javax.swing.JLabel fechaactual;
    private javax.swing.JPanel gb;
    private javax.swing.JLabel historialcomprastext;
    private javax.swing.JPanel loginout;
    private javax.swing.JLabel loginouttext;
    private javax.swing.JLabel logo;
    private javax.swing.JPanel mainbar;
    private javax.swing.JPanel panelcontenedor;
    private javax.swing.JLabel productotext;
    private javax.swing.JLabel puntoventatext;
    private javax.swing.JLabel registrarcompra;
    private javax.swing.JLabel reportexx;
    private javax.swing.JLabel rol;
    private javax.swing.JLabel textwelcome;
    // End of variables declaration//GEN-END:variables
}
