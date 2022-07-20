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

    //************** Métodos estaticos
    //Metodo que lista todas los paises
    public static List<Pais> obtener() throws Exception {
        List<Pais> paises = new ArrayList<>();
        try {
            BaseDatos bd = ConexionBD.obtenerBaseDatos();
            if (bd != null) {
                ResultSet rs = bd.consultar("SELECT * FROM Pais ORDER BY Pais");
                if (rs != null) {
                    rs.beforeFirst();
                    while (rs.next()) {
                        Pais  p= new Pais(Util.leerEntero(rs, "Id"),
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
            throw new Exception("Error al listar Monedas:\n [** " + ex + " **]");
        }
        return paises;
    }

}
