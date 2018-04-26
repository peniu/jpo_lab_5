import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.Timer;

public class SpringApplet extends JApplet implements MouseListener, MouseMotionListener, ActionListener {
    private SimEngine engine;
    private SimTask task;
    private Timer timer;
    //pole do sprawdzenia czy nastepuje przeciaganie kursora myszy
    private boolean drag;
    //pola do przechowywania el interfejsu graficznego
    //pola tekstowe, przycisk i etykiety
    private TextField field_masa,field_k,field_c,field_g,field_L0;
    private Button button_start;
    private Label label_masa,label_k,label_c,label_g,label_L0;
    //metody konieczne do implementacji obslugi myszy
    public void mouseClicked(MouseEvent e){
    }
    public void mousePressed(MouseEvent e){
        Vector2D mysz = new Vector2D(e.getX(),e.getY());
        Vector2D x=engine.getxM();
        double odleglosc = mysz.roznica(x).dlugosc();
        if(odleglosc<=15){
            //zatrzymanie timera
            this.timer.cancel();
            //wyzerowanie predkosci masy
            engine.reset();
            //nastepuje przeciaganie kursora
            this.drag=true;
            e.consume();
        }
    }
    public void mouseReleased(MouseEvent e){
        if(this.drag){
            //wlaczanie timera i symulacji
            this.timer = new Timer();
            this.engine = new SimEngine(20, 3, 0.3, 200, 9.81,this.engine.getxM(), this.engine.getvM(), this.engine.getZaw());
            this.task = new SimTask(engine, this, 0.1);
            this.timer.scheduleAtFixedRate(task, 100, 10);
            this.drag=false;
            e.consume();
        }
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e){
    }
    public void mouseDragged(MouseEvent e){
        if(this.drag){
            Vector2D mysz = new Vector2D(e.getX(),e.getY());
            this.engine.setxM(mysz);
            repaint();
        }
        e.consume();
    }
    public void mouseMoved(MouseEvent e){
    }
    //metoda konieczna do implementacji ActionListener
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==button_start){
            timer.cancel();
            engine.reset();
            this.timer = new Timer();
            this.engine = new SimEngine(Double.parseDouble(field_masa.getText()), Double.parseDouble(field_k.getText()), Double.parseDouble(field_c.getText()), Double.parseDouble(field_L0.getText()), Double.parseDouble(field_g.getText()),this.engine.getxM(), this.engine.getvM(), this.engine.getZaw());
            this.task = new SimTask(engine, this, 0.1);
            this.timer.scheduleAtFixedRate(task, 100, 10);
            repaint();
        }
    }
    //metody do rysowania do zad na bdb
    //rysowanie siatki wspolrzednych
    public void siatka(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.lightGray);
        for (int i = 0; i < 800; i = i + 50) {
            g2.draw(new Line2D.Double(i, 0, i, 600));
        }
        for (int i = 0; i < 600; i = i + 50) {
            g2.draw(new Line2D.Double(0, i, 800, i));
        }
    }
    //rysowanie oznaczenia utwierdzenia
    public void utwierdzenie(Graphics g, Vector2D zaw) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        g2.draw(new Line2D.Double(zaw.getWspX() - 40, zaw.getWspY(), zaw.getWspX() + 40, zaw.getWspY()));
        for (int i = (int) zaw.getWspX() - 35; i < zaw.getWspX() + 40; i = i + 10) {
            g2.draw(new Line2D.Double(i, zaw.getWspY(), i + 10, zaw.getWspY() - 10));
        }
    }
    //rysowanie sprezyny
    public void sprezyna(Graphics2D g, Vector2D zaw, Vector2D x) {
        Graphics2D g2 = (Graphics2D) g;
        Vector2D sprez = x.roznica(zaw);
        Vector2D kier_sprez = sprez.norm();
        Vector2D obrot = new Vector2D(-kier_sprez.getWspY(), kier_sprez.getWspX());
        g2.draw(new Line2D.Double(zaw.getWspX(), zaw.getWspY(), zaw.getWspX() + 25 * kier_sprez.getWspX(), zaw.getWspY() + 25 * kier_sprez.getWspY()));
        g2.draw(new Line2D.Double(x.getWspX(), x.getWspY(), x.getWspX() - 25 * kier_sprez.getWspX(), x.getWspY() - 25 * kier_sprez.getWspY()));
        int il_troj = 40;
        int obw_troj = 40;
        double h = (sprez.dlugosc() - 50) / il_troj;
        double a = (obw_troj - h) / 2;
        double y;
        if((a * a) - (h * h)/4>0){
            y = Math.sqrt(a * a - (h * h) / 4);
        }
        else{
            y=0;
        }
        Vector2D H = kier_sprez.iloczyn(h / 2);
        double x0 = zaw.getWspX() + 25 * kier_sprez.getWspX();
        double y0 = zaw.getWspY() + 25 * kier_sprez.getWspY();
        Vector2D A1 = new Vector2D();
        Vector2D A2 = new Vector2D();
        Vector2D Y1 = obrot.iloczyn(y);
        Vector2D Y2 = obrot.iloczyn(-y);
        for (int i = 0; i < il_troj; i++) {
            if (i % 2 == 0) {
                A1 = Y1.suma(H);
                A2 = Y2.suma(H);
            } else {
                A1 = Y2.suma(H);
                A2 = Y1.suma(H);
            }
            g2.draw(new Line2D.Double(x0, y0, x0 + A1.getWspX(), y0 + A1.getWspY()));
            g2.draw(new Line2D.Double(x0 + A1.getWspX(), y0 + A1.getWspY(), x0 + A1.getWspX() + A2.getWspX(), y0 + A1.getWspY() + A2.getWspY()));
            x0 = x0 + A1.getWspX() + A2.getWspX();
            y0 = y0 + A1.getWspY() + A2.getWspY();
        }
    }

    public void init() {
        this.engine = new SimEngine(20, 3, 0.3, 200, 9.81, new Vector2D(600, 300), new Vector2D(0, 0), new Vector2D(400, 50));
        this.task = new SimTask(engine, this, 0.1);
        this.timer = new Timer();
        this.timer.scheduleAtFixedRate(task, 100, 10);
        this.drag=false;
        //dodanie nasluchiwaczy myszy
        addMouseListener(this);
        addMouseMotionListener(this);

        //inicjalizacja pol elementow GUI i ustalenie ich polozenia oraz wielkosci
        setLayout(null);
        label_masa = new Label("Masa: ");
        label_k = new Label("Wsp. sprężystości:");
        label_c = new Label("Wsp. tłumienia:");
        label_g = new Label("Stała grawitacji:");
        label_L0 = new Label("Dł. swobodna sprężyny:");
        label_masa.setBounds(801,1,35,20);
        label_k.setBounds(801,21,105,20);
        label_c.setBounds(801,41,90,20);
        label_g.setBounds(801,61,90,20);
        label_L0.setBounds(801,81,135,20);

        field_masa = new TextField();
        field_k = new TextField();
        field_c = new TextField();
        field_g = new TextField();
        field_L0 = new TextField();
        field_masa.setBounds(840,1,55,20);
        field_k.setBounds(910,21,55,20);
        field_c.setBounds(895,41,55,20);
        field_g.setBounds(895,61,55,20);
        field_L0.setBounds(940,81,55,20);

        button_start = new Button("START");
        button_start.setBounds(825,120,150,30);

        //dodanie elementow GUI do appletu
        add(label_masa);
        add(label_k);
        add(label_c);
        add(label_g);
        add(label_L0);
        add(field_masa);
        add(field_k);
        add(field_c);
        add(field_g);
        add(field_L0);
        add(button_start);
        //dodanie nasluchiwacza przycisku
        button_start.addActionListener(this);
    }
    public void paint(Graphics g) {
        setSize(1000, 600);
        setBackground(Color.white);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.clearRect(0, 0, getWidth(), getHeight());
        siatka(g2);
        g2.setPaint(Color.black);
        Vector2D x = engine.getxM();
        Vector2D zaw = engine.getZaw();
        utwierdzenie(g2, zaw);
        sprezyna(g2, zaw, x);
        g2.fillOval((int) x.getWspX() - 15, (int) x.getWspY() - 15, 30, 30);
        double x0=x.getWspX();
        double y0=x.getWspY();
        //rysowanie wektorów sił i prędkości masy
        g2.setColor(Color.green);
        engine.getvM().rysowanieWektora(x0,y0,g2);
        g2.setColor(Color.red);
        engine.getFgraw().rysowanieWektora(x0,y0,g2);
        g2.setColor(Color.cyan);
        engine.getFwisk().rysowanieWektora(x0,y0,g2);
        g2.setColor(Color.magenta);
        engine.getFsprez().rysowanieWektora(x0,y0,g2);
        g2.setColor(Color.blue);
        engine.getFwyp().rysowanieWektora(x0,y0,g2);
    }
}