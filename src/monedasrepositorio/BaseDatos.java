package monedasrepositorio;

import java.sql.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;

public class BaseDatos {

    private Connection cn;
    private String jdbc_url;
    private String jdbc_driver;
    private String usuario;
    private String clave;

    public void asignarCredenciales(String servidor, String baseDatos, String usuario, String clave) {
        this.jdbc_url = "jdbc:mysql://" + servidor + ":3306/" + baseDatos;
        this.usuario = usuario;
        this.clave = clave;
        this.jdbc_driver = "com.mysql.cj.jdbc.Driver";
    }
    
    public boolean conectar() throws Exception {
        if (this.jdbc_url.length() > 0) {
            try {
                if (cn == null || cn.isClosed()) {
                    Class.forName(this.jdbc_driver);
                    this.cn = DriverManager.getConnection(this.jdbc_url, this.usuario, this.clave);
                }
                return true;
            } catch (SQLException e) {
                throw new Exception("Error al conectar la base de datos:\n" + e);
            } catch (Exception e) {
                throw new Exception("Error:\n" + e);
            }
        }
        return false;
    }

    public void cerrar() throws Exception {
        try {
            this.cn.close();
        } catch (SQLException e) {
            throw new Exception("Error al cerrar la base de datos:\n" + e);
        }
    }

    public ResultSet consultar(String strConsulta) throws Exception {
        try {
            if (this.conectar()) {
                Statement sSQL = this.cn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                boolean tuvoRegistros = sSQL.execute(strConsulta);
                if (tuvoRegistros) {
                    return sSQL.getResultSet();
                }
                return null;
            }
            return null;
        } catch (SQLException e) {
            throw new Exception("Error al realizar consulta:\n" + e);
        } catch (Exception e) {
            throw new Exception("Error:\n" + e);
        }
    }

    public boolean actualizar(String strConsulta) throws Exception {
        try {
            if (this.conectar()) {
                Statement sSQL = this.cn.createStatement();
                sSQL.executeUpdate(strConsulta);
                sSQL = null;
                this.cn.close();
                return true;
            }
            return false;
        } catch (SQLException e) {
            throw new Exception("Error al actualizar la base de datos:" + e);
        } catch (Exception e) {
            throw new Exception("Error:\n" + e);
        }
    }

    public List llenarCombobox(JComboBox combo, String strSQL) throws Exception {
        try {
            List id = new ArrayList<>();
            combo.removeAllItems();
            ResultSet rs = this.consultar(strSQL);
            if (rs != null) {
                while (rs.next()) {
                    id.add(rs.getInt(1));
                    combo.addItem(rs.getString(2));
                }
                rs.close();
                this.cn.close();
            }
            return id;
        } catch (SQLException e) {
            throw new Exception("Error al acceder la consulta:" + e);
        } catch (Exception e) {
            throw new Exception("Error:" + e);
        }
    }

    public List llenarLista(JList lista, String strSQL) throws Exception {
        try {
            List id = new ArrayList<>();
            DefaultListModel dato = (DefaultListModel) lista.getModel();
            dato.removeAllElements();
            ResultSet rs = this.consultar(strSQL);
            if (rs != null) {
                while (rs.next()) {
                    id.add(rs.getInt(1));
                    dato.addElement(rs.getString(2));
                }
                rs.close();
                this.cn.close();
            }
            lista = dato != null ? new JList(dato) : new JList();
            return id;
        } catch (SQLException e) {
            throw new Exception("Error al llenar la lista:\n" + e);
        } catch (Exception e) {
            throw new Exception("Error:\n" + e);
        }
    }

    public List llenarArbol(DefaultMutableTreeNode nodoP, String strSQL) throws Exception {
        try {
            List id = new ArrayList<>();
            nodoP.removeAllChildren();
            ResultSet rs = this.consultar(strSQL);
            if (rs != null) {
                while (rs.next()) {
                    id.add(rs.getInt(1));
                    nodoP.add(new DefaultMutableTreeNode(rs.getString(2)));
                }
                rs.close();
                this.cn.close();
            }
            return id;
        } catch (SQLException e) {
            throw new Exception("Error al acceder la consulta:\n" + e);
        } catch (Exception e) {
            throw new Exception("Error:\n" + e);
        }
    }

    public List llenarTabla(JTable t, String strSQL) throws Exception {
        try {
            List id = new ArrayList<>();
            DefaultTableModel dtm;
            String[] titulos;
            ResultSet rs = this.consultar(strSQL);
            if (rs != null) {
                ResultSetMetaData rsmd = rs.getMetaData();
                int tColumnas = rsmd.getColumnCount();
                titulos = new String[tColumnas - 1];
                String[] tipos = new String[tColumnas - 1];
                for (int i = 1; i < tColumnas; i++) {
                    titulos[i - 1] = rsmd.getColumnName(i + 1);
                    tipos[i - 1] = rsmd.getColumnClassName(i + 1);
                }
                rs.last();
                int tFilas = rs.getRow();
                dtm = new DefaultTableModel(titulos, tFilas);
                t.setModel(dtm);
                int fila = -1;
                rs.beforeFirst();
                while (rs.next()) {
                    id.add(rs.getInt(1));
                    ++fila;
                    int i2 = 1;
                    while (i2 < tColumnas) {
                        if (rs.getString((String) titulos[i2 - 1]) != null) {
                            if (tipos[i2 - 1].endsWith("Timestamp")) {
                                t.getModel().setValueAt(rs.getDate((String) titulos[i2 - 1]).toString(), fila, i2 - 1);
                            } else if (tipos[i2 - 1].endsWith("Boolean")) {
                                t.getModel().setValueAt(rs.getObject((String) titulos[i2 - 1]), fila, i2 - 1);
                            } else {
                                t.getModel().setValueAt(rs.getString((String) titulos[i2 - 1]), fila, i2 - 1);
                            }
                        } else {
                            t.getModel().setValueAt("", fila, i2 - 1);
                        }
                        ++i2;
                    }
                }
                rs.close();
                this.cn.close();
            } else {
                titulos = new String[]{"No hubo resultados"};
                dtm = new DefaultTableModel(titulos, 1);
                t.setModel(dtm);
            }
            return id;
        } catch (SQLException e) {
            throw new Exception("Error al acceder la consulta:" + e);
        } catch (Exception e) {
            throw new Exception("Error al llenar tabla:" + e);
        }
    }

    public int obtenerId(String strConsulta) throws Exception {
        try {
            ResultSet rs = this.consultar(strConsulta);
            if (rs != null) {
                rs.next();
                return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            throw new Exception("Error al acceder la consulta:" + e);
        } catch (Exception e) {
            throw new Exception("Error al llenar tabla:" + e);
        }
    }

    //Metodo para actualizar un campo imagen desde un vector de octetos
    public boolean actualizarBytes(String tabla, String campo, String condicion, byte[] bImagen) throws Exception {
        try {
            if (bImagen != null && conectar()) {
                //Definir la cadena de consulta
                String strConsulta = "UPDATE " + tabla
                        + " SET " + campo + "=?";
                if (!condicion.equals("")) {
                    strConsulta += " WHERE " + condicion;
                }
                PreparedStatement ps = cn.prepareStatement(strConsulta);
                //Asignar los octetos cargados desde la imagen
                ps.setBytes(1, bImagen);

                ps.executeUpdate();
                ps = null;
                this.cn.close();
            }
            return true;

        } catch (SQLException e) {
            throw new Exception("Error al ejecutar la consulta:\n" + e);
        } catch (Exception e) {
            throw new Exception("Error al actualizar bytes en la base de datos:\n" + e);
        }
    }

}
