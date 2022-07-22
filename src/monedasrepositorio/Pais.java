package monedasrepositorio;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Pais {

    private int id;
    private String pais;
    private String codigoAlfa2;
    private String codigoAlfa3;
    private Moneda moneda;

    public Pais(int id,
            String nombre,
            String codigoAlfa2,
            String codigoAlfa3,
            int idMoneda) {
        this.id = id;
        this.pais = nombre;
        this.codigoAlfa2 = codigoAlfa2;
        this.codigoAlfa3 = codigoAlfa3;
        try {
            this.moneda = Moneda.obtener(idMoneda);
        } catch (Exception ex) {
            this.moneda = null;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCodigoAlfa2() {
        return codigoAlfa2;
    }

    public void setCodigoAlfa2(String codigoAlfa2) {
        this.codigoAlfa2 = codigoAlfa2;
    }

    public String getCodigoAlfa3() {
        return codigoAlfa3;
    }

    public void setCodigoAlfa3(String codigoAlfa3) {
        this.codigoAlfa3 = codigoAlfa3;
    }

    public void setMoneda(Moneda moneda) {
        this.moneda = moneda;
    }

    public Moneda getMoneda() {
        return moneda;
    }

    public byte[] getMapa() throws Exception {
        try {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            ResultSet rs = bd.consultar("SELECT Mapa FROM Pais WHERE Id=" + id);
            if (rs.next()) {
                return rs.getBytes("Mapa");
            }
        } catch (Exception ex) {
            throw new Exception("Error al consultar Mapa:\n [** " + ex + " **]");
        }
        return null;
    }//getMapa

    public byte[] getBandera() throws Exception {
        try {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            ResultSet rs = bd.consultar("SELECT Bandera FROM Pais WHERE Id=" + id);
            if (rs.next()) {
                return rs.getBytes("Bandera");
            }
        } catch (Exception ex) {
            throw new Exception("Error al consultar Bandera:\n [** " + ex + " **]");
        }
        return null;
    }//getBandera

    //************** Métodos estaticos
    //Metodo que lista todos los paises
    public static List<Pais> obtener() throws Exception {
        List<Pais> paises = new ArrayList<>();
        try {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            if (bd != null) {
                ResultSet rs = bd.consultar("SELECT * FROM Pais ORDER BY Pais");
                if (rs != null) {
                    rs.beforeFirst();
                    while (rs.next()) {
                        Pais p = new Pais(Util.leerEntero(rs, "Id"),
                                Util.leerTexto(rs, "Pais"),
                                Util.leerTexto(rs, "CodigoAlfa2"),
                                Util.leerTexto(rs, "CodigoAlfa3"),
                                Util.leerEntero(rs, "IdMoneda")
                        );
                        paises.add(p);
                    }
                }
            } else {
                throw new Exception("No se ha conectado a la base de datos");
            }
        } catch (Exception ex) {
            throw new Exception("Error al listar Paises:\n [** " + ex + " **]");
        }
        return paises;
    }//Obtener

    //Metodo que obtiene un registro
    public static Pais obtener(int Id) throws Exception {
        try {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            ResultSet rs = bd.consultar("SELECT * FROM Pais WHERE Id=" + Id);
            if (rs != null) {
                rs.next();
                return new Pais(Util.leerEntero(rs, "Id"),
                        Util.leerTexto(rs, "Pais"),
                        Util.leerTexto(rs, "CodigoAlfa2"),
                        Util.leerTexto(rs, "CodigoAlfa3"),
                        Util.leerEntero(rs, "IdMoneda")
                );
            } else {
                return null;
            }
        } catch (Exception ex) {
            throw new Exception("Error al consultar País:\n [** " + ex + " **]");
        }
    }//Obtener

    //Metodo que busca paises que cumplan un criterio de búsqueda
    public static List<Pais> buscar(int DatoBusqueda, String Dato) throws Exception {
        List<Pais> paises = new ArrayList<>();
        try {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            String strSQL = "CALL spBuscarPaises('" + Dato
                    + "','" + DatoBusqueda + "')";
            ResultSet rs = bd.consultar(strSQL);
            if (rs != null) {
                rs.beforeFirst();
                while (rs.next()) {
                    paises.add(new Pais(Util.leerEntero(rs, "Id"),
                            Util.leerTexto(rs, "Pais"),
                            Util.leerTexto(rs, "CodigoAlfa2"),
                            Util.leerTexto(rs, "CodigoAlfa3"),
                            Util.leerEntero(rs, "IdMoneda")
                    ));
                }
            }
        } catch (Exception ex) {
            throw new Exception("Error buscando Paises:\n [** " + ex + " **]");
        }
        return paises;
    }//Buscar

    //Método para Guardar un Pais
    public static boolean guardar(Pais p) throws Exception {
        //Son válidos todos los datos?
        if (!p.getPais().isEmpty()) {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            //Construir cadena de consulta
            String strSQL = "CALL spActualizarPais('"
                    + p.getId() + "', '"
                    + p.getPais() + "', '"
                    + p.getCodigoAlfa2() + "', '"
                    + p.getCodigoAlfa3() + "', '"
                    + p.getMoneda().getId() + "')";
            try {
                //Ejecutar la consulta
                return bd.actualizar(strSQL);
            } catch (Exception ex) {
                throw new Exception("Error al actualizar País:\n [** " + ex + " **]");
            }
        }
        return false;
    }//Guardar

    //Método para Eliminar un Pais
    public static boolean eliminar(Pais p) throws Exception {
        try {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            String strSQL = "DELETE FROM Pais"
                    + " WHERE Id='" + p.getId() + "'";
            //Ejecutar la consulta
            return bd.actualizar(strSQL);
        } catch (Exception ex) {
            throw new Exception("Error al eliminar País:\n [** " + ex + " **]");
        }
    }//Eliminar

    //Obtener la clave primaria basado en datos de una clave alterna
    public static int obtenerId(String Nombre) throws Exception {
        try {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            //Ejecutar la consulta
            return bd.obtenerId("EXEC spBuscarPais '" + Nombre + "'");
        } catch (Exception ex) {
            throw new Exception("Error al obtener clave primaria de País:\n [** " + ex + " **]");
        }
    }//ObtenerId

    public static int obtenerIndice(List<Pais> paises, int Id) {
        for (int i = 0; i < paises.size(); i++) {
            if (paises.get(i).getId() == Id) {
                return i;
            }
        }
        return -1;
    }//obtenerIndice

}
