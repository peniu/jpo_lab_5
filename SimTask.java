import java.util.TimerTask;

public class SimTask extends TimerTask {
    private SimEngine engine;
    private SpringApplet applet;
    private double time;
    //konstruktor
    public SimTask(SimEngine eng,SpringApplet app,double t){
        this.engine=eng;
        this.applet=app;
        this.time=t;
    }
    //metoda uruchamiajaca symulacje i odswiezajaca powierzchnie appletu
    public void run(){
        engine.sim(time);
        applet.repaint();
    }
    public static void main(String[] args){
    }
}