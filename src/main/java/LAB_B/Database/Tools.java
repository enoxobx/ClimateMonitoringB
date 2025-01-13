package LAB_B.Database;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;

public class Tools {

    //Aggiungere tipi se necessario
    public static PreparedStatement setParametri(PreparedStatement query, Object... params){
        try {
            int  i = 0;
        for (var parametro:params){
            if(parametro instanceof String){
                query.setString(++i,(String)parametro);
            }
            else if(parametro instanceof Double){
                query.setDouble(++i,(Double)parametro);
            }
            else if(parametro instanceof Long){
                query.setLong(++i,(Long)parametro);
            }
            else if(parametro instanceof Date){
                query.setDate(++i,(Date)parametro);
            }
            else if(parametro instanceof BigDecimal){
                query.setBigDecimal(++i,(BigDecimal)parametro);
            }
            else if(parametro instanceof Integer){
                query.setInt(++i,(Integer)parametro);
            }
            else{
                throw new Exception("tipo non ancora implementato");
            }
        }
        return query;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
        
    }
}
