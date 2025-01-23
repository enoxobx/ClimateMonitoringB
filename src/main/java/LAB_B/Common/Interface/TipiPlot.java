package LAB_B.Common.Interface;

public enum TipiPlot{
    WIND(0,"WIND"),
    HUMIDITY(1,"HUMIDITY"),
    PRESSURE(2,"PRESSURE"),
    TEMPERATURE(3,"TEMPERATURE"),
    PRECIPITATION(4,"PRECIPITATION"),
    GALCIER_ALTITUDE(5,"GALCIER_ALTITUDE"),
    GLACIER_MASS(6,"GLACIER_MASS");

    private final int codice;
    private final String nome;
    
    TipiPlot(int codice, String  name){
        this.codice = codice;
        this.nome=name;
    }
    public int getCodice(){
        return codice;
    }
    public String getName(){
        return nome;
    }

}
