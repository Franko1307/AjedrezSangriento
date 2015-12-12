package celda;

import escenario.Escenario;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import sprite.PiezaDeAjedrez;

/**
 * Clase Celda
 * Esta clase extiende de JLabel y funciona como una etiqueta que permite almacenar cholos (piezas de ajedez)
 * en ella. Añade métodos para la falicitación de lo antes mencionado.
 * @author Fran
 */
public class Celda extends JLabel{
    
    private boolean ocupado = false;
    private final Point ubicacionMatriz;
    private final int ancho = 115;
    private final int altura = 80;
    private PiezaDeAjedrez pieza;
    private String imagenActual;
    private final Escenario escenario;
    
    /**
     * Este constructor liga el escenario con la Celda, asigna la ubicación en la matriz
     * Y pone el tamanio por default, además de las posiciones del texto.
     * @param Point ubicacionMatriz
     * @param Escenario escenario
     */
    public Celda (Point ubicacionMatriz, Escenario escenario)
    {
        this.escenario = escenario;
        this.ubicacionMatriz = ubicacionMatriz;
        setPreferredSize(new Dimension(115,80));
        setVerticalTextPosition(JLabel.TOP);
        setHorizontalTextPosition(JLabel.CENTER);     
    }
    /**
     * Este método retorna la pieza de ajedrez actual arriba de ella.
     * @return PiezaDeAjedrez
     */
    public PiezaDeAjedrez getPiezaDeAjedrez(){
        return pieza;
    }
    /**
     * Este método asigna a la celda una PiezaDeAjedrez
     * @param PiezaDeAjedrez pieza
     */
    public void setPiezaDeAjedrez(PiezaDeAjedrez pieza){
        this.pieza = pieza;
    }
    /**
     * Este método asigna el estado (ocupado/vacío[true,false]) de la etiqueta.
     * @param boolean estado
     */
    public void setEstado(boolean estado){
        ocupado = estado;
    }
    /**
     * Este método retorna el estado actual de la etiqueta.
     * @return boolean
     */
    public boolean getEstado(){
        return ocupado;
    }
    /**
     * Este método retorna la ubicación en el arreglo de etiquetas donde ésta se encuentra localizada
     * @return Point
     */
    public Point getUbicacionMatriz(){
        return ubicacionMatriz;
    }
    /**
     * Este método asigna el Icono actual de la etiqueta
     * @param String imagen
     */
    public void setImagen(String imagen){
        imagenActual = imagen;
        ImageIcon iconLogo = new ImageIcon(this.getClass().getResource("/image/"+imagen));
        this.setIcon(iconLogo);
        escenario.repaint();
    }
    /**
     * Este método remueve el ícono actual de la etiqueta
     */
    public void removeImagen(){
        imagenActual = null;
        setIcon(null);
        escenario.repaint();
    }  
}