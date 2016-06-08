public class Kreis{
private double radius;
public Kreis(){
radius = 5.0;
}
double berechneUmfang(){
return 2.0 * 3.1415926 * radius;
}
double berechneFlaeche(){
return 3.1415926 * radius * radius;
}
}