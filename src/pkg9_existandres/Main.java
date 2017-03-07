package pkg9_existandres;

import java.io.*;
import org.xmldb.api.*;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;

public class Main {

    public static void main(String[] args) throws IOException, XMLDBException {
        File archivo;
        String nombreArchivo;
        byte op;
        BufferedReader lee = new BufferedReader(new InputStreamReader(System.in));
        
        Collection col = null;
        
        String driver = "org.exist.xmldb.DatabaseImpl";
        String uri = "xmldb:exist://localhost:8080/exist/xmlrpc/db";
        String usu = "admin", pass ="admin";
        
        try {
            Class cl = Class.forName(driver);
            Database db = (Database)cl.newInstance();
            DatabaseManager.registerDatabase(db);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        do {
            System.out.println("Escoger opción a realizar:"
                    + "\n[1] Crear la colección GIMNASIO"
                    + "\n[2] Subir socios_gim.xml"
                    + "\n[3] Subir actividades_gim.xml"
                    + "\n[4] Subir uso_gimnasio.xml"
                    + "\n[5] Calcular Cuota Final de Cada Socio"
                    + "\n[6] Guardar en Docuento Datos por Cada Actividad de Socio"
                    + "\n[7] Guardar en Documento Cálculo Cuotas por Cada Socio"
                    + "\n[0] Salir del Programa");
            System.out.printf(">>OPCIÓN: ");
            op = Byte.parseByte(lee.readLine());
            
            switch(op) {
                case 1:
                    col = DatabaseManager.getCollection(uri, usu, pass);
                    CollectionManagementService service = (CollectionManagementService)col.getService("CollectionManagementService", "1.0");
                    service.createCollection("GIMNASIO");
                    col = col.getChildCollection("GIMNASIO");
                    System.out.println("\n~~ Colección Creada ~~\n");
                    break;
                case 2:
                case 3:
                case 4:
                    if(op == 2) {
                        nombreArchivo = "socios_gim.xml";
                    } else if(op == 3) {
                        nombreArchivo = "actividades_gim.xml";
                    } else {
                        nombreArchivo = "uso_gimnasio.xml";
                    }
                    archivo = new File("Files/"+nombreArchivo);
                    
                    if(!archivo.canRead()) {
                        System.out.println("\n\tERROR!!");
                    } else {
                        col = col.getChildCollection("GIMNASIO");
                        Resource newResource = col.createResource(archivo.getName(), "XMLResource");
                        newResource.setContent(archivo);
                        col.storeResource(newResource);
                        System.out.println("\n~~ Archivo "+nombreArchivo+" Subido a la Colección ~~\n");
                    }
                    break;
                case 5:
                    col = col.getChildCollection("GIMNASIO");
                    
                    Metodos.verSociosCuota(col);
                    break;
                case 6:
                    col = col.getChildCollection("GIMNASIO");
                    
                    Metodos.crearXMLintermedio(col);
                    break;
                case 7:
                    col = col.getChildCollection("GIMNASIO");
                    
                    Metodos.crearXMLintermedio2(col);
                    break;
            }
        } while(op != 0);
        
    }
    
}