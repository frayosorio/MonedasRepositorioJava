package monedasrepositorio;

import java.io.*;
import java.lang.reflect.*;
import java.sql.ResultSet;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;

public abstract class Util {



    public static BufferedReader cargarArchivo(String nombre) {
        BufferedReader contenido = null;
        try {
            FileReader archivo = new FileReader(nombre);
            contenido = new BufferedReader(archivo);
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return contenido;
    }

    public static String[][] llenarMatriz(ResultSet rs, String[] campos) {
        String[][] datos = null;
        int f = 0;
        try {
            rs.beforeFirst();
            while (rs.next()) {
                f++;
            }
            datos = new String[f][campos.length];
            rs.beforeFirst();
            f = 0;
            while (rs.next()) {
                for (int c = 0; c < campos.length; c++) {
                    datos[f][c] = Util.leerTexto(rs, campos[c]);
                }
                f++;
            }
        } catch (Exception ex) {

        }
        return datos;
    }

    public static int[] llenarVector(ResultSet rs, String campo) {
        int[] vector = null;
        int f = 0;
        try {
            rs.beforeFirst();
            while (rs.next()) {
                f++;
            }
            vector = new int[f];
            rs.beforeFirst();
            f = 0;
            while (rs.next()) {
                vector[f] = Util.leerEntero(rs, campo);
                f++;
            }
        } catch (Exception ex) {

        }
        return vector;
    }

    public static int ubicarEnLista(List lista, Object valor) {
        int posicion = -1;
        int i = 0;
        while (i < lista.size() && posicion == -1) {
            Object datoLista = Array.get(lista, i);
            if (valor.toString().equals(datoLista.toString())) {
                posicion = i;
                continue;
            }
            ++i;
        }
        return posicion;
    }

    public static DefaultMutableTreeNode crearArbol(JTree tr) {
        DefaultMutableTreeNode nr = new DefaultMutableTreeNode();
        DefaultTreeModel dtm = new DefaultTreeModel(nr);
        tr.setModel(dtm);
        return nr;
    }

    public static void asignarListaTabla(JComboBox combo, JTable t, int c) {
        TableColumn tc = t.getColumnModel().getColumn(c);
        tc.setCellEditor(new DefaultCellEditor(combo));
    }

    public static String obtenerTextoFecha(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        return String.valueOf(String.format("%04d", c.get(1))) + "/" + String.format("%02d", c.get(2) + 1) + "/" + String.format("%02d", c.get(5));
    }

    public static String obtenerFrase(String texto) {
        texto = String.valueOf(texto.substring(0, 1).toUpperCase()) + texto.substring(1).toLowerCase();
        return texto.trim();
    }

    public static String obtenerTextoFechaExportar(Date fecha) {
        if (fecha != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(fecha);
            return String.valueOf(String.format("%02d", c.get(5))) + "/" + String.format("%02d", c.get(2) + 1) + "/" + Integer.toString(c.get(1)) + " " + String.format("%02d", c.get(11)) + ":" + String.format("%02d", c.get(12));
        }
        return "";
    }

    public static String leerTexto(ResultSet rs, String campo) {
        try {
            return rs.getString(campo);
        } catch (Exception ex) {
            return "";
        }
    }

    public static int leerEntero(ResultSet rs, String campo) {
        try {
            return rs.getInt(campo);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static double leerReal(ResultSet rs, String campo) {
        try {
            return rs.getDouble(campo);
        } catch (Exception ex) {
            return 0;
        }
    }

    public static Date leerFecha(ResultSet rs, String campo) {
        try {
            return rs.getDate(campo);
        } catch (Exception ex) {
            return new Date();
        }
    }

    public static int obtenerEntero(String valor) {

        try {
            return Integer.parseInt(valor);
        } catch(Exception ex)  {
            return 0;
        }
    }

    public static long obtenerEnteroLargo(String valor) {
        try {
            return Long.parseLong(valor);
        } catch(Exception ex)  {
            return 0;
        }
    }

    public static double obtenerReal(String valor) {
        try {
            return Double.parseDouble(valor);
        } catch(Exception ex)  {
            return 0;
        }
    }

}
