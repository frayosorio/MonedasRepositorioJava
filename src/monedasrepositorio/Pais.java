package monedasrepositorio;

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

    public void setPais(String moneda) {
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
    
    
}
