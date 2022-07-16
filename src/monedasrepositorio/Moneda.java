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
}
