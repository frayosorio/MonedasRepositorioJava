package monedasrepositorio;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Moneda {

    private int id;
    private String moneda;
    private String sigla;
    private String simbolo;
    private String emisor;

    public Moneda(int id,
            String moneda,
            String sigla,
            String simbolo,
            String emisor
    ) {
        this.id = id;
        this.moneda = moneda;
        this.sigla = sigla;
        this.simbolo = simbolo;
        this.emisor = emisor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public byte[] getImagen() throws Exception {
        try {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            ResultSet rs = bd.consultar("SELECT Imagen FROM Moneda WHERE Id=" + id);
            if (rs.next()) {
                return rs.getBytes("Imagen");
            }
        } catch (Exception ex) {
            throw new Exception("Error al consultar Imagen:\n [** " + ex + " **]");
        }
        return null;
    }//getImagen

    //************** Métodos estaticos
    //Metodo que lista todas las monedas
    public static List<Moneda> obtener() throws Exception {
        List<Moneda> monedas = new ArrayList<>();
        try {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            if (bd != null) {
                ResultSet rs = bd.consultar("SELECT * FROM Moneda ORDER BY Moneda");
                if (rs != null) {
                    rs.beforeFirst();
                    while (rs.next()) {
                        Moneda m = new Moneda(Util.leerEntero(rs, "Id"),
                                Util.leerTexto(rs, "Moneda"),
                                Util.leerTexto(rs, "Sigla"),
                                Util.leerTexto(rs, "Simbolo"),
                                Util.leerTexto(rs, "Emisor")
                        );
                        monedas.add(m);
                    }
                }
            } else {
                throw new Exception("No se ha conectado a la base de datos");
            }
        } catch (Exception ex) {
            throw new Exception("Error al listar Monedas:\n [** " + ex + " **]");
        }
        return monedas;
    }

    //Metodo que devuelve un objeto moneda con base en la clave primaria
    public static Moneda obtener(int id) throws Exception {
        try {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            if (bd != null) {
                ResultSet rs = bd.consultar("SELECT * FROM Moneda WHERE Id=" + id);
                if (rs != null) {
                    rs.beforeFirst();
                    rs.next();
                    return new Moneda(Util.leerEntero(rs, "Id"),
                            Util.leerTexto(rs, "Moneda"),
                            Util.leerTexto(rs, "Sigla"),
                            Util.leerTexto(rs, "Simbolo"),
                            Util.leerTexto(rs, "Emisor")
                    );
                }

            } else {
                throw new Exception("No se ha conectado a la base de datos");
            }
        } catch (Exception ex) {
            throw new Exception("Error al obtener una Moneda:\n [** " + ex + " **]");
        }

        return null;
    }

    //Metodo que busca monedas que cumplan un criterio de búsqueda
    public static List<Moneda> buscar(int DatoBusqueda, String Dato) throws Exception {
        List<Moneda> monedas = new ArrayList<>();
        try {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            String strSQL = "CALL spBuscarMonedas('" + Dato
                    + "','" + DatoBusqueda + "')";
            ResultSet rs = bd.consultar(strSQL);
            if (rs != null) {
                rs.beforeFirst();
                while (rs.next()) {
                    monedas.add(new Moneda(Util.leerEntero(rs, "Id"),
                            Util.leerTexto(rs, "Moneda"),
                            Util.leerTexto(rs, "Sigla"),
                            Util.leerTexto(rs, "Simbolo"),
                            Util.leerTexto(rs, "Emisor")
                    ));
                }
            }
        } catch (Exception ex) {
            throw new Exception("Error buscando Monedaes:\n [** " + ex + " **]");
        }
        return monedas;
    }//Buscar

    //Método para Guardar un Moneda
    public static boolean guardar(Moneda m) throws Exception {
        //Son válidos todos los datos?
        if (!m.getMoneda().isEmpty()
                && !m.getSigla().isEmpty()) {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            //Construir cadena de consulta
            String strSQL = "CALL spActualizarMoneda('"
                    + m.getId() + "', '"
                    + m.getMoneda() + "', '"
                    + m.getSigla() + "', '"
                    + m.getSimbolo() + "', '"
                    + m.getEmisor() + "')";
            try {
                //Ejecutar la consulta
                return bd.actualizar(strSQL);
            } catch (Exception ex) {
                throw new Exception("Error al actualizar Moneda:\n [** " + ex + " **]");
            }
        }
        return false;
    }//Guardar

    //Método para Eliminar un Moneda
    public static boolean eliminar(Moneda m) throws Exception {
        try {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            String strSQL = "DELETE FROM Moneda"
                    + " WHERE Id='" + m.getId() + "'";
            //Ejecutar la consulta
            return bd.actualizar(strSQL);
        } catch (Exception ex) {
            throw new Exception("Error al eliminar Moneda:\n [** " + ex + " **]");
        }
    }//Eliminar

    public static int obtenerIndice(List<Moneda> monedas, int Id) {
        for (int i = 0; i < monedas.size(); i++) {
            if (monedas.get(i).getId() == Id) {
                return i;
            }
        }
        return -1;
    }//obtenerIndice

}
