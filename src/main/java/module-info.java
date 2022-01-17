module com.mycompany.crudhinterface {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.base;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires java.persistence;
    requires jasperreports; 
   

    opens com.mycompany.crudhinterface to javafx.fxml, javafx.swing, java.sql;
    opens models;
    exports com.mycompany.crudhinterface;
    
}
