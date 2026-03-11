/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package LogicaDelJuego;

import InterfazGrafica.VistaJugador;
import PartesLogicas.ParametrosJuego;
import PartesLogicas.ParametrosJuegoDAO;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author fernan
 */
public class VistaDePartida extends javax.swing.JFrame {
    private String nombreJugador;
    private int idJugador;
    private Thread hiloGenerador;
    private int nivelActual = 1;
    private int puntos = 0;
    private int pedidosEntregados = 0;
    private boolean turnoActivo = true;
    private final int MAX_PEDIDOS = 20;
    private java.util.Random random = new java.util.Random();
    private int idSucursal;
    private ParametrosJuego parametros;
    /**
     * Creates new form VistaDePartida
     */
    public VistaDePartida(int idJugador,String nombreJugador, int idSucursal) {
        initComponents();
        this.nombreJugador = nombreJugador;
        this.idSucursal = idSucursal;
        this.idJugador = idJugador;
        
        labelNombreJugador.setText(nombreJugador);
        labelNivel.setText("Nivel: "+nivelActual);
        labelPuntos.setText("Puntos: "+puntos);
        
        ParametrosJuegoDAO dao = new ParametrosJuegoDAO();
        parametros = dao.obtenerParametros(idSucursal);
        
        configurarTabla();
        configurarBarra();
        iniciarGeneradorPedidos();
        iniciarTiempoDeTurno(120);
    }

    private void iniciarGeneradorPedidos() {
        hiloGenerador = new Thread(()->{
            while(turnoActivo){
                try {
                    Thread.sleep(parametros.getTiempoGeneracionPedidos() * 1000);
                } catch (InterruptedException ex) {
                    break;
                }
                javax.swing.SwingUtilities.invokeLater(()->{
                    if(turnoActivo && jTable1.getRowCount() < MAX_PEDIDOS){
                        generarPedido();
                    }
                });
            }
        });
        hiloGenerador.start();
    }

    private void generarPedido() {
        PartesLogicas.ProductoDAO dao = new PartesLogicas.ProductoDAO();
        java.util.List<PartesLogicas.Producto> productosActivos =
                dao.obtenerProductosActivosPorSucursal(idSucursal);
        
        if(productosActivos.isEmpty()){
            return;
        }
        
        PartesLogicas.Producto productoAleatorio =
                productosActivos.get(random.nextInt(productosActivos.size()));
        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        int tiempoBase = obtenerTiempoSegunNivel();
        int tiempoInicial = tiempoBase;
        
        modelo.addRow(new Object[]{
            productoAleatorio.getNombre(),
            "RECIBIDA",
            tiempoInicial
        });
        int nuevaFila = modelo.getRowCount()-1;
        iniciarTiempoFila(nuevaFila);
    }

    private void iniciarTiempoFila(int fila) {
        Thread hilo = new Thread(() -> {
            while (turnoActivo) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    break;
                }
                javax.swing.SwingUtilities.invokeLater(() -> {
                    //verificacion de que la fila exista
                    if (fila >= jTable1.getRowCount()) {
                        return;
                    }
                    Object tiempoObj = jTable1.getValueAt(fila, 2);
                    Object estadoObj = jTable1.getValueAt(fila, 1);

                    if (tiempoObj == null || estadoObj == null) {
                        return;
                    }
                    int tiempoActual = (int) tiempoObj;
                    String estado = estadoObj.toString();
                    if (estado.equals("ENTREGADA")
                            || estado.equals("CANCELADA")
                            || estado.equals("NO_ENTREGADO")) {
                        return;
                    }
                    if (tiempoActual > 0) {
                        jTable1.setValueAt(tiempoActual - 1, fila, 2);
                    } else {
                        jTable1.setValueAt("NO_ENTREGADO", fila, 1);
                        jTable1.setValueAt(0, fila, 2);
                        puntos -=50;
                        if(puntos<0)puntos =0;
                        actualizarPuntos();
                        return;
                    }
                });
            }
        });
        hilo.start();
    }
    
    private int obtenerTiempoSegunNivel(){
        int base;
        
        switch(nivelActual){
            case 1:
                base = parametros.getTiempoNivel1();
                break;
            case 2:
                base = parametros.getTiempoNivel2();
                break;
            case 3:
                base = parametros.getTiempoNivel3();
                break;
            default:
                base = parametros.getTiempoNivel1();
        }
        int variacion = random.nextInt(21) - 10;
        return base + variacion;
    }
    
    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Pedido", "Estado", "Tiempo"}
        ) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) {
                    return Integer.class;
                }
                return Object.class;
            }
        };

        jTable1.setModel(modelo);
    }

    class ProgressBarRenderer extends JProgressBar implements TableCellRenderer {

        public ProgressBarRenderer() {
            setMinimum(0);
            setMaximum(100);
            setStringPainted(false);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            int tiempo = (Integer) value;
            setValue(tiempo);

            if (tiempo > 60) {
                setForeground(Color.GREEN);
            } else if (tiempo > 30) {
                setForeground(Color.ORANGE);
            } else {
                setForeground(Color.RED);
            }

            return this;
        }
    }

    private void configurarBarra() {
        jTable1.getColumn("Tiempo")
                .setCellRenderer(new ProgressBarRenderer());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        labelNombreJugador = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        labelTiempoDeTurno = new javax.swing.JLabel();
        labelNivel = new javax.swing.JLabel();
        labelPuntos = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnCambiarDeEstado = new javax.swing.JButton();
        btnCancelarPedido = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Ubuntu Sans Mono", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Jugador: ");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 110, -1));

        labelNombreJugador.setFont(new java.awt.Font("Ubuntu Sans Mono", 3, 18)); // NOI18N
        labelNombreJugador.setText("jLabel2");
        jPanel1.add(labelNombreJugador, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 50, 220, 30));

        jLabel3.setFont(new java.awt.Font("Ubuntu Sans Mono", 3, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Tiempo De Turno: ");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 50, -1, -1));

        labelTiempoDeTurno.setFont(new java.awt.Font("Ubuntu Sans Mono", 3, 18)); // NOI18N
        labelTiempoDeTurno.setText("jLabel4");
        jPanel1.add(labelTiempoDeTurno, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 40, 170, 30));

        labelNivel.setText("jLabel2");
        jPanel1.add(labelNivel, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 50, 110, 30));
        jPanel1.add(labelPuntos, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 40, 120, 30));

        jPanel2.setBackground(new java.awt.Color(153, 153, 153));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Pedido", "Estado", "Tiempo"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCambiarDeEstado.setBackground(new java.awt.Color(204, 204, 204));
        btnCambiarDeEstado.setFont(new java.awt.Font("Ubuntu Sans Mono", 3, 24)); // NOI18N
        btnCambiarDeEstado.setForeground(new java.awt.Color(0, 0, 0));
        btnCambiarDeEstado.setText("Avanzar estado");
        btnCambiarDeEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarDeEstadoActionPerformed(evt);
            }
        });
        jPanel3.add(btnCambiarDeEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 60, 230, -1));

        btnCancelarPedido.setBackground(new java.awt.Color(204, 204, 204));
        btnCancelarPedido.setFont(new java.awt.Font("Ubuntu Sans Mono", 3, 24)); // NOI18N
        btnCancelarPedido.setForeground(new java.awt.Color(0, 0, 0));
        btnCancelarPedido.setText("Cancelar pedido");
        btnCancelarPedido.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarPedidoActionPerformed(evt);
            }
        });
        jPanel3.add(btnCancelarPedido, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 60, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 1098, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(339, 339, 339))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 12, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCambiarDeEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarDeEstadoActionPerformed
        aplicarEventoAlBoton();
        int fila = jTable1.getSelectedRow();

        if (fila == -1) {
            return; // no hay selección
        }

        String estadoActual = (String) jTable1.getValueAt(fila, 1);

        switch (estadoActual) {

            case "RECIBIDA":
                jTable1.setValueAt("PREPARANDO", fila, 1);
                break;
            case "PREPARANDO":
                jTable1.setValueAt("EN_HORNO", fila, 1);
                break;
            case "EN_HORNO":
                jTable1.setValueAt("ENTREGADA", fila, 1);
                int tiempoRestante = (int) jTable1.getValueAt(fila, 2);
                jTable1.setValueAt(0, fila, 2);
                puntos+=100;
                if(tiempoRestante > 40){
                    puntos+=50;
                }
                actualizarPuntos();
                pedidosEntregados++;
                verificarSubidaDeNivel();
                break;
            case "ENTREGADA":
            case "NO_ENTREGADO":
            case "CANCELADA":
                // no hacer nada
                break;
        }
    }//GEN-LAST:event_btnCambiarDeEstadoActionPerformed
    private void actualizarPuntos(){
        labelPuntos.setText("Puntos: " + puntos);
    }
    private void verificarSubidaDeNivel(){
        if(nivelActual == 1 && pedidosEntregados >=10){
            subirNivel();
        }else if(nivelActual == 2 && pedidosEntregados >=15){
            subirNivel();
        }
    }
    
    private void subirNivel(){
        nivelActual++;
        labelNivel.setText("Nivel: " + nivelActual);
        JOptionPane.showMessageDialog(this, "Subiste de nivel, ahora tu nivel es "+nivelActual+
                " ahora los pedidos tendran menos tiempo");
        puntos+=200;
        actualizarPuntos();
    }
    private void btnCancelarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarPedidoActionPerformed
        int fila = jTable1.getSelectedRow();

        if (fila == -1) {
            return; //No hay seleccion
        }

        String estadoActual = (String) jTable1.getValueAt(fila, 1);

        // 
        if (!estadoActual.equals("RECIBIDA")
            && !estadoActual.equals("PREPARANDO")) {
            return;
        }

        // Cambiar estado
        jTable1.setValueAt("CANCELADA", fila, 1);
        jTable1.setValueAt(0, fila, 2);
        
        puntos -=20;
        if(puntos<0)puntos =0;
        actualizarPuntos();
        
    }//GEN-LAST:event_btnCancelarPedidoActionPerformed
   
    private void aplicarEventoAlBoton(){
        int tiempoRandom = random.nextInt(2000);
        btnCambiarDeEstado.setEnabled(false);
        
         new Thread(() -> {
        try {
            Thread.sleep(tiempoRandom);
        } catch (InterruptedException e) {
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            if(turnoActivo){
                btnCambiarDeEstado.setEnabled(true);
            }
        });
        }).start();
    }
    
    private void iniciarTiempoDeTurno(int segundosTotal){
       Thread hilo = new Thread(()->{
          for(int i = segundosTotal; i >=0; i--){
              int minutos = i/60;
              int segundos = i % 60;
              
              String tiempoFormato = String.format("%02d:%02d", minutos,segundos);
              javax.swing.SwingUtilities.invokeLater(()->{
                  labelTiempoDeTurno.setText(tiempoFormato);
              });
              
              if(i==0){
                  finalizarTurno();
                  break;
              }
              try {
                  Thread.sleep(1000);
              } catch (InterruptedException ex) {
                  return;
              }
          } 
       });
       hilo.start();
    }
    private void finalizarTurno(){
        turnoActivo = false;
        if(hiloGenerador !=null){
            hiloGenerador.interrupt();
        }
        
        btnCambiarDeEstado.setEnabled(false);
        btnCancelarPedido.setEnabled(false);
        javax.swing.SwingUtilities.invokeLater(()->{
            marcarPedidosNoEntregados();
            guardarPartida();
            mostrarEstadisticas();
        });
    }
    private void mostrarEstadisticas(){
        int entregados =0;
        int cancelados =0;
        int noEntregados =0;
        
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            String estado = jTable1.getValueAt(i, 1).toString();
            switch (estado){
                case "ENTREGADA":
                    entregados++;
                    break;
                case "CANCELADA":
                    cancelados++;
                    break;
                case "NO_ENTREGADO":
                    noEntregados++;
                    break;
            }
        }
        String mensaje = "RESULTADOS DEL TURNO \n\n"
                +"Pedidos Entregados: "+entregados +"\n"
                +"Pedidos Cancelados: "+cancelados +"\n"
                +"Pedidos No Entregados: "+noEntregados;
        JOptionPane.showMessageDialog(this, mensaje);
        volverAVistaJugador();
    }
    
    private void volverAVistaJugador(){
        VistaJugador vista = new VistaJugador(idJugador,nombreJugador, idSucursal);
        vista.setVisible(true);
        this.dispose();
    }
    private void marcarPedidosNoEntregados(){
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            String estado = jTable1.getValueAt(i, 1).toString();
            
            if(!estado.equals("ENTREGADA")
                && !estado.equals("CANCELADA")
                && !estado.equals("NO_ENTREGADO")){
                jTable1.setValueAt("NO_ENTREGADO", i, 1);
                jTable1.setValueAt(0, i, 2);
                 puntos-=50;
            }
            actualizarPuntos();
        }
    }
    private void guardarPartida(){
        int entregados = 0;
        int cancelados = 0;
        int noEntregados = 0;
        
        for (int i = 0; i < jTable1.getRowCount(); i++) {
            String estado = jTable1.getValueAt(i, 1).toString();
            
            switch(estado){
                case "ENTREGADA":
                    entregados++;
                    break;
                case "CANCELADA":
                    cancelados++;
                    break;
                case "NO_ENTREGADO":
                    noEntregados++;
                    break;
            }
            
        }
        
        PartesLogicas.PartidaDAO partidaDAO = new PartesLogicas.PartidaDAO();
        partidaDAO.guardarPartida(
                idJugador,
                idSucursal,
                puntos,
                nivelActual,
                entregados,
                cancelados,
                noEntregados);

        PartesLogicas.JugadorDAO jugadorDAO = new PartesLogicas.JugadorDAO();
        jugadorDAO.actualizarJugador(idJugador, puntos, nivelActual);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCambiarDeEstado;
    private javax.swing.JButton btnCancelarPedido;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel labelNivel;
    private javax.swing.JLabel labelNombreJugador;
    private javax.swing.JLabel labelPuntos;
    private javax.swing.JLabel labelTiempoDeTurno;
    // End of variables declaration//GEN-END:variables
}
