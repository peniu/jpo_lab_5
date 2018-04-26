import java.awt.*;
import java.awt.geom.Line2D;

public class Vector2D {
    private double wspX, wspY;
    public double getWspX() {
        return wspX;
    }
    public double getWspY() {
        return wspY;
    }
    public void setWspX(double wspX) {
        this.wspX = wspX;
    }
    public void setWspY(double wspY) {
        this.wspY = wspY;
    }
    //konstruktor domyslny
    public Vector2D() {
        setWspX(0);
        setWspY(0);
    }
    //konstruktor z parametrami
    public Vector2D(double x, double y) {
        setWspX(x);
        setWspY(y);
    }
    //metoda zwracajaca sume wektorow dla ktorego zostala wywolana i podanego jako parametr
    public Vector2D suma(Vector2D x) {
        double a = getWspX() + x.getWspX();
        double b = getWspY() + x.getWspY();
        return new Vector2D(a, b);
    }
    //metoda zwracajaca roznice wektorow dla ktorego zostala wywolana i podanego jako parametr
    public Vector2D roznica(Vector2D x) {
        double a = getWspX() - x.getWspX();
        double b = getWspY() - x.getWspY();
        return new Vector2D(a, b);
    }
    //metoda zwracajaca wektor dla ktorego zostala wywolana pomnozony przez stala k
    public Vector2D iloczyn(double k) {
        double a = k * getWspX();
        double b = k * getWspY();
        return new Vector2D(a, b);
    }
    //metoda zwracajaca dlugosc wektora dla ktorego zostala wywolana
    public double dlugosc(){
        return Math.sqrt(getWspX()*getWspX()+getWspY()*getWspY());
    }
    //metoda zwracajaca wektor, bedacy znormalizowanym wektorem dla ktorego zostala wywolana
    public Vector2D norm(){
        double h=dlugosc();
        return iloczyn(1/h);
    }
    public void info(){
        System.out.println("x= "+getWspX());
        System.out.println("y= "+getWspY());
    }
    //rysowanie wektorow - do zadania na bdb
    public void rysowanieWektora(double x,double y,Graphics g) {
        //x, y - miejsce poczatku wektora (w ukl wspolrzednych)
        Graphics2D g2 = (Graphics2D) g;
        //rysowanie linii wektora
        g2.draw(new Line2D.Double(x,y,x+getWspX(),y+getWspY()));
        //rysowanie grota wektora
        Vector2D normalny = norm();
        double a = x + getWspX() - 6 * normalny.getWspX();
        double b = y + getWspY() - 6 * normalny.getWspY();
        Vector2D obrot = new Vector2D();
        //obracam wektor normalny o 90 stopni
        obrot.setWspX(-normalny.getWspY());
        obrot.setWspY(normalny.getWspX());
        //przygotowuje wspolrzedne trojkata sluzacego jako grot
        int x1 = (int) Math.round(a + 4 * obrot.getWspX());
        int x2 = (int) Math.round(a - 4 * obrot.getWspX());
        int y1 = (int) Math.round(b + 4 * obrot.getWspY());
        int y2 = (int) Math.round(b - 4 * obrot.getWspY());
        int x3 = (int) Math.round(x + getWspX());
        int y3 = (int) Math.round(y + getWspY());
        int xpoints[]={x1,x2,x3};
        int ypoints[]={y1,y2,y3};
        g2.fillPolygon(xpoints, ypoints, 3);
    }
    public static void main (String[] args){
    }
}