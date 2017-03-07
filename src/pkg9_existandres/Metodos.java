package pkg9_existandres;

import java.io.*;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;

public class Metodos {
    
    public static void verSociosCuota(Collection col) throws XMLDBException {
        
        XPathQueryService service = (XPathQueryService)col.getService("XPathQueryService", "1.0");
        
        ResourceSet result = service.query("for $socio in /Socios/socio\n" +
            "let $uso := /USO_GIMNASIO/fila_uso[CODSOCIO=$socio/@codigo]\n" +
            "let $actividad := /Actividades/actividad[@codigo=$uso/CODACTIV]\n" +
            "let $cuota := sum($actividad/cuota_adicional) + $socio/cuota\n" +
            "order by $socio\n" +
            "return <SOCIO><NOMBRE>{data($socio/nombre)}</NOMBRE><CUOTA>{$cuota}</CUOTA></SOCIO>");
        
        ResourceIterator i = result.getIterator();
        if(!i.hasMoreResources()) {
            System.out.println("\n~~ No hay Datos ~~");
        } else {
            while(i.hasMoreResources()) {
                Resource r = i.nextResource();
                System.out.println("\n"+(String)r.getContent());
            }
            System.out.println();
        }
        
    }
 
    public static void crearXMLintermedio(Collection col) throws XMLDBException {
        XPathQueryService serviceQuery = (XPathQueryService)col.getService("XPathQueryService", "1.0");
        
        File archivo = new File("Files/gimnasio_intermedio.xml");
        
        if(!archivo.canRead()) {
            System.out.println("\n\tERROR AL LEER EL FICHERO \n");
        } else {
            Resource newResource = col.createResource(archivo.getName(), "XMLResource");
            newResource.setContent(archivo);
            col.storeResource(newResource);
            
            serviceQuery.query("update insert for $uso in /USO_GIMNASIO/fila_uso\n" +
                "let $socio := /Socios/socio[@codigo=$uso/CODSOCIO]\n" +
                "let $actividad := /Actividades/actividad[@codigo=$uso/CODACTIV]\n" +
                "order by $socio\n" +
                "return <datos>\n" +
                "<COD>{data($socio/@codigo)}</COD>\n" +
                "<NOMBRESOCIO>{data($socio/nombre)}</NOMBRESOCIO>\n" +
                "<CODACTIV>{data($actividad/@codigo)}</CODACTIV>\n" +
                "<NOMBREACTIVIDAD>{data($actividad/nombre)}</NOMBREACTIVIDAD>\n" +
                "<horas>{number($uso/HORAFINAL)-($uso/HORAINICIO)}</horas>\n" +
                "<tipoact>{data($actividad/@tipo)}</tipoact>\n" +
                "<cuota_adicional>{data($actividad/cuota_adicional)}</cuota_adicional>\n" +
                "</datos> into /Datos");
            System.out.println("\n~~ Archivo gimnasio_intermedio.xml Almacenado ~~\n");
        }
    }
    
    public static void crearXMLintermedio2(Collection col) throws XMLDBException {
        XPathQueryService serviceQuery = (XPathQueryService)col.getService("XPathQueryService", "1.0");
        
        File archivo = new File("Files/gimnasio_intermedio2.xml");
        
        if(!archivo.canRead()) {
            System.out.println("\n\tERROR AL LEER EL FICHERO \n");
        } else {
            Resource newResource = col.createResource(archivo.getName(), "XMLResource");
            newResource.setContent(archivo);
            col.storeResource(newResource);
            
            serviceQuery.query("update insert for $socio in /Socios/socio\n" +
                "let $uso := /USO_GIMNASIO/fila_uso[CODSOCIO=$socio/@codigo]\n" +
                "let $actividad := /Actividades/actividad[@codigo=$uso/CODACTIV]\n" +
                "let $cuotaSum := sum($actividad/cuota_adicional)\n" +
                "order by $socio\n" +
                "return <datos>\n" +
                "<COD>{data($socio/@codigo)}</COD>\n" +
                "<NOMBRESOCIO>{data($socio/nombre)}</NOMBRESOCIO>\n" +
                "<CUOTA_FIJA>{data($socio/cuota)}</CUOTA_FIJA>\n" +
                "<SUMA_CUOTA_ADIC>{data($cuotaSum)}</SUMA_CUOTA_ADIC>\n" +
                "<CUOTA_TOTAL>{data($cuotaSum + $socio/cuota)}</CUOTA_TOTAL>\n" +
                "</datos> into /Datos2");
            System.out.println("\n~~ Archivo gimnasio_intermedio2.xml Almacenado ~~\n");
        }
    }
    
}