package java-example.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Some useful object conversion methods.
 */
public class ObjectUtils {
    private ObjectUtils() {
    }

    /**
     * Convert an object to a Map<String, Object>
     * It's useful when convert an object as a parameter to a map before passing it
     * as SQL parameters.
     * <pre>
     *     @Autowired NamedParameterJdbcTemplate jdbcTemplate;
     *     NewProduct newProduct = new Product("Product 01", 100);
     *     String sql = "INSERT INTO product(pname, price) VALUES(:name, :price);
     *     Map<String, Object> namedParameters = ObjectUtils.objectToMap(newProduct);
     *     int rowAffected = jdbcTemplate.update(sql, namedParameters);
     * </pre>
     * @param object
     * @return a Map with key is field's name and value is field's value.
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object object) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field: fields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(object));
        }

        return map;
    }

    /**
     * Convert an object to an array Object[] that contains the values of object's fields;
     * It's useful when convert an object as a parameter to an array before passing it
     * as SQL parameters.
     * <pre>
     *     @Autowired JdbcTemplate jdbcTemplate;
     *     NewProduct newProduct = new Product("Product 01", 100);
     *     String sql = "INSERT INTO product(pname, price) VALUES(?, ?);
     *     Object[] params = ObjectUtils.objectToArray(newProduct);
     *     int rowAffected = jdbcTemplate.update(sql, params);
     * </pre>
     * @param object
     * @return an array Object[] that contains the values of object's fields.
     * @throws IllegalAccessException
     */
    public static Object[] objectToArray(Object object) throws IllegalAccessException {
        Field[] fields = object.getClass().getDeclaredFields();

        Object[] objects = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            objects[i] = field.get(object);
        }
        return objects;
    }
}
