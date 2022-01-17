package com.mycompany.crudhinterface;

import java.net.URL;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javax.swing.JFrame;
import org.hibernate.Session;
import models.Pedidos;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.swing.JRViewer;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

/*
 ** FXML Controller class
 ** @author Ridouan Abdellah Tieb
 */
public class PrimaryController implements Initializable {

    @FXML
    private TableView<Pedidos> tablaPedidos;
    @FXML
    private TableColumn<Pedidos, Integer> numeroPedidos;
    @FXML
    private TableColumn<Pedidos, String> usuarioPedidos;
    @FXML
    private TableColumn<Pedidos, String> fechaPedidos;
    @FXML
    private TableColumn<Pedidos, String> cicloPedidos;
    @FXML
    private TableColumn<Pedidos, String> estadoPedidos;
    @FXML
    private Label total;

    /*
    Creo la conversion de la fecha de java.sql.Date a java.util.Date
     */
    java.util.Date utilDate = new java.util.Date();
    long lnMilisegundos = utilDate.getTime();
    java.sql.Date sqlDate = new java.sql.Date(lnMilisegundos);

    /*
    Abro la sesión.
     */
    Session s = HibernateUtil.getSessionFactory().openSession();
    
    /*
    Creo el Observable.
    */
    ObservableList<Pedidos> lista = FXCollections.observableArrayList();
    @FXML
    private Label titulo;
    @FXML
    private Label textoPedidos;
    @FXML
    private Label pedidosPendientes;
    @FXML
    private Button carta;
    @FXML
    private Button comandasHoy;
    @FXML
    private Button comandasFecha;
    @FXML
    private Button exportar;
    
    /*        
     * Inicializo la clase controladora. 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                       
                        listarPedidos();
                        contarPedidos();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 5000);
        
        /*
        Enlazo las variables del diseño con las columnas de la tabla. Además doy
        formato a la fecha para que sea mas legible. 
         */
        numeroPedidos.setCellValueFactory(new PropertyValueFactory<>("id"));
        cicloPedidos.setCellValueFactory(new PropertyValueFactory<>("ciclo"));
        usuarioPedidos.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        estadoPedidos.setCellValueFactory(new PropertyValueFactory<>("estado"));
        fechaPedidos.setCellValueFactory(
                (var row) -> {
                    DateFormat formatoCorto = DateFormat.getDateInstance(DateFormat.SHORT);
                    String fechaCorta = formatoCorto.format(row.getValue().getFecha());
                    return new SimpleStringProperty(fechaCorta);
                }
        );

        listarPedidos();

        contarPedidos();

    }

    /*
    Creo un método para listar los pedidos.
     */
    private void listarPedidos() {
        
        lista.clear();
        
        /*
        Declaro una variable tipo Query en ella crearé las consulta.
         */
        Query hoy = s.createQuery("FROM Pedidos");

        /*
        Modifico los parámetros de la query.
         */
        //hoy.setParameter("fecha", sqlDate);

        /*
        Añado al observale las variables correspondientes a las columnas.
         */
        lista.addAll(hoy.list());

        /*
        Añado los items a la variable que corresponde a la tabla en Scene Builder.
         */
        tablaPedidos.setItems(lista);

        /*
        Imprimo la lista por consola como debuger.
         */
        System.out.println(hoy.list());
        
        
    }

    /*
    Creo un método para contar los pedidos pendientes. 
     */
    private void contarPedidos() {

        /*
        Creo la Query que me lista los pedidos.
         */
        Query qa = s.createQuery("FROM Pedidos WHERE estado= :estado AND fecha= :fecha");
        
        /*
        Meto los parámetros del estado para que liste los pendientes.
        */
        String pendientes="PENDIENTE";
        qa.setParameter("estado", pendientes);
        qa.setParameter("fecha", sqlDate);
        /*
        Cambio el texto, ejecuto la query y le añado una cadena vacía para que
        pase por un String. 
         */
        total.setText(qa.list().size() + "");
    }

    /*
    Creo el evento al clicar en un pedido para cambiar el estado del pedido. 
     */
    @FXML
    private void recoger(MouseEvent event) {
        /*
        Creo la query que me permite actualizar el pedido como recogido. 
         */
        Query qc = s.createQuery("UPDATE Pedidos SET estado =:estado WHERE id =:id");

        /*
        Creo una variable tipo Integer en la que guardo la selección del ratón
        en una fila determinada.
         */
        Integer recogiendo = tablaPedidos.getSelectionModel().getSelectedItem().getId();

        /*
        La imprimo como debuger.
         */
        System.out.println(recogiendo);

        /*
        Introduzco los parámetros en la Query.
         */
        String recogido="RECOGIDO";
        qc.setParameter("id", recogiendo);
        qc.setParameter("estado", recogido);

        /*
        Hago la transacción.
         */
        Transaction t = s.beginTransaction();

        /*
        Ejecuto la query. 
         */
        qc.executeUpdate();

        /*
        Lo subo haciendo commit.
         */
        t.commit();

        /*
        Listo los pedidos después del proceso de actualización. 
         */
//        listarPedidos();

        /*
        Cuento los pedidos después del proceso de actualización.
         */
//        contarPedidos();

    }

    @FXML
    private void mostrarCarta(ActionEvent event){
            String archivo = "Carta.jrxml";
         
        try {
            var parameters = new HashMap();
            parameters.put("Carta", "Listado de productos");
            
            JasperReport informe = JasperCompileManager.compileReport(archivo);
            JasperPrint impresion = JasperFillManager.fillReport(informe, parameters, Conexion.getConexion());
            
            JRViewer visor = new JRViewer(impresion);
            
            JFrame ventanaInforme = new JFrame("Informe");
            ventanaInforme.getContentPane().add(visor);
            ventanaInforme.pack();
            ventanaInforme.setVisible(true);
            
            JRPdfExporter exportador = new JRPdfExporter();
            exportador.setExporterInput( new SimpleExporterInput(impresion) );
            exportador.setExporterOutput( new SimpleOutputStreamExporterOutput("Carta.pdf") );
            
            var configuracion = new SimplePdfExporterConfiguration();
            exportador.setConfiguration(configuracion);
            
            exportador.exportReport();
            
                    
        } catch (JRException ex) {
            System.out.println(ex);
        }
    }

    @FXML
    private void mostrarComanda(ActionEvent event) {
            String archivo = "Pedidos.jrxml";
         
        try {
            var parameters = new HashMap();
            parameters.put("Pedidos", "Listado de pedidos");
            
            JasperReport informe = JasperCompileManager.compileReport(archivo);
            JasperPrint impresion = JasperFillManager.fillReport(informe, parameters, Conexion.getConexion());
            
            JRViewer visor = new JRViewer(impresion);
            
            JFrame ventanaInforme = new JFrame("Informe");
            ventanaInforme.getContentPane().add(visor);
            ventanaInforme.pack();
            ventanaInforme.setVisible(true);
            
            JRPdfExporter exportador = new JRPdfExporter();
            exportador.setExporterInput( new SimpleExporterInput(impresion) );
            exportador.setExporterOutput( new SimpleOutputStreamExporterOutput("Pedidos.pdf") );
            
            var configuracion = new SimplePdfExporterConfiguration();
            exportador.setConfiguration(configuracion);
            
            exportador.exportReport();
            
                    
        } catch (JRException ex) {
            System.out.println(ex);
        }
    }
}
