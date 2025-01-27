package LAB_B.Database;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class Tools {

    // Aggiungere tipi se necessario
    public static PreparedStatement setParametri(PreparedStatement query, Object... params) {
        try {
            int i = 0;
            for (var parametro : params) {
                if(parametro == null){
                    throw new Exception("parametro nullo");
                }
                else if (parametro instanceof String) {
                    query.setString(++i, (String) parametro);
                } else if (parametro instanceof Double) {
                    query.setDouble(++i, (Double) parametro);
                } else if (parametro instanceof Long) {
                    query.setLong(++i, (Long) parametro);
                } else if (parametro instanceof Date) {
                    query.setDate(++i, (Date) parametro);
                } else if (parametro instanceof BigDecimal) {
                    query.setBigDecimal(++i, (BigDecimal) parametro);
                } else if (parametro instanceof Integer) {
                    query.setInt(++i, (Integer) parametro);
                } else if (parametro instanceof BigInteger) {
                    BigInteger b = (BigInteger)parametro;
                    query.setLong(++i, b.longValue());
                } else if (parametro instanceof Date) {
                     query.setDate(++i,(Date) parametro);
                }else if (parametro instanceof ArrayList<?>) {
                    for(var t:((ArrayList)parametro)){
                        if(t.getClass() == String.class){
                            query.setString(++i, (String) t);
                        }else if(t.getClass() == Integer.class){
                            query.setInt(++i, (Integer) t);
                        }
                    }
                } else {
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
