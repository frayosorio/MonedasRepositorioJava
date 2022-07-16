package monedasrepositorio;

public class ConexionBD
{
    private static BaseDatos bd;

    public static BaseDatos obtenerBaseDatos()
    {
        return bd; 
    }

    public static Boolean establecer() throws Exception
    {
        try
        {
            //Instanciar objeto para consultas
            if (bd == null)
            {
                bd = new BaseDatos();

                /*Definir cadena de conexion:
                * Servidor, Base de Datos y Credenciales de seguridad */
                bd.asignarCredenciales("localhost",
                                        "Monedas",
                                        "root",
                                        "Fray.1969");

                return bd.conectar();
            }
            else
                return true;
        }
        catch (Exception ex)
        {
            throw new Exception("Error al acceder la base de datos MONEDAS\n" + ex);
        }
    }//Conectar

}