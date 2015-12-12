package sprite;

import celda.Celda;
import escenario.Escenario;
import java.awt.Point;
import java.util.Random;
import panel_de_control.PanelDeControl;

/**
 * Esta clase simula a una Pieza de Ajedrez en un escenario, donde ésta se mueve al azar
 * por un tablero de ajedrez, ataca a las piezas enemigas y respeta a sus aliados.
 * @author Fran
 */
public class PiezaDeAjedrez extends Thread{
    
    private Point posicion;
    private int prioridad = 10;
    private final String pieza;
    private final int danio;
    private Celda celdaActual;
    private final Escenario escenario;
    public boolean estoyEnPelea;
    private final String equipo;
    private final int[] posicionesy;
    private final PanelDeControl panelDeControl;
    Random random = new Random();
    
    /**
     * Este constructor asigna todos los atributos necesarios a la clase
     * Liga con el panel de control, asigna daño, asiga el equipo entre otras cosas.
     * @param pieza
     * @param celdaActual
     * @param escenario
     * @param panelDeControl
     */
    public PiezaDeAjedrez(String pieza, Celda celdaActual, Escenario escenario, PanelDeControl panelDeControl)
    {
        this.panelDeControl = panelDeControl;
        danio = setDanio(pieza);
        equipo = setEquipo(pieza);
        this.posicionesy = new int[]{-1, 1};
        this.pieza = pieza;
        this.celdaActual = celdaActual;
        this.escenario = escenario;
        this.celdaActual.setPiezaDeAjedrez(this);
    }
    
    /**
     * Este método retorna el danio que la Pieza de Ajedrez puede causar.
     * @return int danio
     */
    public int getDanio(){
        return danio;
    }

    /**
     * Este método asigna la celda actual
     * @param Celda celdaActual
     */
    public void setCeldaActual(Celda celdaActual){
        this.celdaActual = celdaActual;
    }
    
    /**
     * Este método asigna la Prioridad de la pieza de ajedrez
     * @param int prioridad
     */
    public void setPrioridad(int prioridad){
        this.prioridad = prioridad;
    }
    
    /**
     * Este método retorna la prioridad actual de la pieza de ajedrez
     * @return
     */
    public int getPrioridad(){
        return prioridad;
    }
    
    /**
     * Este método retorna el equipo (blanco,negro) de la pieza de ajedrez.
     * @return
     */
    public String getEquipo(){
        return equipo;
    }
    
    private int setDanio(String pieza) {
        if (pieza.contains("Peon"))   return 1;
        if (pieza.contains("Alfil"))  return 2;
        if (pieza.contains("Caballo"))return 2;
        if (pieza.contains("Torre"))  return 3;
        if (pieza.contains("Rey"))    return 4;
        if (pieza.contains("Reina"))  return 5;
        return 1;  
    }
    private String setEquipo(String pieza) {
        if (pieza.contains("Blanc")) return "blanco";
        if (pieza.contains("Negr"))  return "negro";
        return null;
    }
    @Override
    public synchronized void run(){  
        celdaActual.setText("VIDA: " + prioridad);
        celdaActual.setImagen(pieza);
        actualizaEtiqueta();
        while(prioridad > 2){
            try {
                wait(500);
            } catch (InterruptedException ex) {
                System.out.println("ERROR: NO SE PUDO DORMIR");
            }
            buscarCeldaNueva();
        }
        volverActualizarEtiqueta();
        try {
            celdaActual.setText("MUERTO");
            wait(300);
            celdaActual.setText("");
            celdaActual.setPiezaDeAjedrez(null);
            celdaActual.setEstado(false);
            celdaActual.setIcon(null); 
            this.finalize();
        } catch (Throwable ex) {
            System.out.println("ERROR: NO SE PUDO FINALIZAR");
        }   
    }
    private void buscarCeldaNueva() {
        
        int y;
        int x = (int) (Math.random()*3 -1);
        
        if (x == 0) y = posicionesy[random.nextInt(this.posicionesy.length)];
        else y = (int) (Math.random()*3 -1);
        
        x += celdaActual.getUbicacionMatriz().x;
        y += celdaActual.getUbicacionMatriz().y;
        if (x > 7) x = 0;
        if (y > 7) y = 0;
        if (x < 0) x = 7;
        if (y < 0) y = 7;
      
        Celda celda = escenario.getMatrix().get(x).get(y);
              
        if (celda.getEstado()){
            PiezaDeAjedrez piezaEnemiga = celda.getPiezaDeAjedrez();
            if (piezaEnemiga == null) System.out.println("Pelea cancelada por huida de la pieza");
            else if (!equipo.equals(piezaEnemiga.getEquipo()))
                    iniciarPelea(piezaEnemiga);
        }
        celdaActual.setEstado(false);
        celdaActual.setPiezaDeAjedrez(null);
        celdaActual.setIcon(null);
        celdaActual.setText("");
        cambiarCelda(celda);
   
    }
    private void iniciarPelea(PiezaDeAjedrez piezaEnemiga) {
        
        int danioEnemigo = piezaEnemiga.getDanio();
        
        if (danio > danioEnemigo){
            piezaEnemiga.setPrioridad(piezaEnemiga.getPrioridad() - danio);
        }else if (danio < danioEnemigo){
            this.prioridad -= danioEnemigo;
        }else{
            this.prioridad -= danioEnemigo;
            piezaEnemiga.setPrioridad(piezaEnemiga.getPrioridad()-danio);
        }
        
        if (prioridad > 1)
            setPriority(prioridad);
        
        if (piezaEnemiga.getPrioridad() > 2)
            piezaEnemiga.setPriority(piezaEnemiga.getPrioridad() - 1);
    }

    private synchronized void cambiarCelda(Celda celda) {   
        while(celda.getEstado()){
            try {
                wait(1);
            } catch (InterruptedException ex) {
                System.out.println("ERROR: NO SE PUDO DORMIR");
            }
        }
        if (!celda.getEstado() && celda.getPiezaDeAjedrez() == null){
            celda.setEstado(true);
            celda.setPiezaDeAjedrez(this);
            celda.setImagen(pieza);
            celda.setText("VIDA: " + prioridad);
            setCeldaActual(celda);
        }       
    }   
    private void actualizaEtiqueta() {
        if (pieza.contains("Blanc"))
            panelDeControl.jLabelVivosBlancos.setText((Integer.parseInt(panelDeControl.jLabelVivosBlancos.getText()) + 1) + "");
        else panelDeControl.jLabelVivosNegros.setText((Integer.parseInt(panelDeControl.jLabelVivosNegros.getText()) + 1) + "");
    }

    private void volverActualizarEtiqueta() {
        if (pieza.contains("Blanc")){
            panelDeControl.jLabelVivosBlancos.setText((Integer.parseInt(panelDeControl.jLabelVivosBlancos.getText()) - 1) + "");
            panelDeControl.jLabelMuertosBlancos.setText((Integer.parseInt(panelDeControl.jLabelMuertosBlancos.getText())+1) + "");
        }
        else{
            panelDeControl.jLabelVivosNegros.setText((Integer.parseInt(panelDeControl.jLabelVivosNegros.getText()) - 1) + "");
            panelDeControl.jLabelMuertosNegros.setText((Integer.parseInt(panelDeControl.jLabelMuertosNegros.getText()) + 1) + "");   
        }   
    }   
}