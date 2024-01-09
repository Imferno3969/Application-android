class Point {
    private double x,y;
    public Point (double x,double y) {
        this.x=x;
        this.y=y;
    }
    public void deplacer (double pasx,double pasy) {
        x+=pasx;
        y+=pasy;
    }
    public void afficher () {
        System.out.println("Point de coordonnées " + x + " et " + y);
    }
    public double valeurdex () {
        return x;
    }
    public double valeurdey () {
        return y;
    }
}

class Cercle extends Point{
    private double rayon;
    public Cercle(double rayon, double x, double y){
        super(x,y);
        this.rayon=rayon;
    }
    public void modifCentre(double dx, double dy){
        super.deplacer(dx,dy);
    }
    public void modifRayon(double r){
        this.rayon=r;
    }
    public Point retourneCentre(){
        return new Point(super.valeurdex(), super.valeurdey());
    }
    @Override
    public void afficher () {
        System.out.println("Cercle de coordonnées " +super.valeurdex() + "et" + super.valeurdey());
        System.out.println("et de rayon" + rayon);
    }
}

class Prog {
    public static void main(String[] args){
        Point a=new Point (1.0,2.0);
        Cercle c=new Cercle(5.2, 1, 2);
        a.afficher();
        c.afficher();
        Point d=c.retourneCentre();
        d.afficher();
    }

}