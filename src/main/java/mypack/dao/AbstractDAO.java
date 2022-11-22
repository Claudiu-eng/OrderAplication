package mypack.dao;

import mypack.connection.ConnectionFactory;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.beans.PropertyDescriptor;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractDAO<T> {
    protected static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private String createSelectAllQuerry() {
        StringBuilder st = new StringBuilder();
        st.append("SELECT * FROM ");
        st.append(type.getSimpleName());
        return st.toString();
    }

    private String createDeleteQuerry() {
        StringBuilder st = new StringBuilder();
        st.append("DELETE FROM ");
        st.append(type.getSimpleName());
        st.append(" WHERE ");
        String a = type.getDeclaredFields()[0].getName();
        st.append(a);
        st.append(" = ? ");
        return st.toString();
    }

    private String createUpdateQuerry(){
        StringBuilder st = new StringBuilder();
        st.append("UPDATE ");
        st.append(type.getSimpleName()+" set ");
        for (Field a : type.getDeclaredFields()) {
            a.setAccessible(true);
            if (!a.getName().startsWith(type.getDeclaredFields()[0].getName())) {
                st.append(a.getName() + " = ? , ");
            }
        }
        st.delete(st.length()-2,st.length());
        st.append(" WHERE ");
        st.append(type.getDeclaredFields()[0].getName());
        st.append(" = ? ;");
        return st.toString();
    }

    private String createInsertQuerry() {
        StringBuilder st = new StringBuilder();
        st.append("Insert INTO ");
        st.append(type.getSimpleName());
        st.append(" ( ");
        int nr = 0;
        for (Field a : type.getDeclaredFields()) {
            a.setAccessible(true);
            if (!a.getName().startsWith(type.getDeclaredFields()[0].getName())) {
                st.append(a.getName() + " , ");
                nr++;
            }
        }
        st.delete(st.length() - 2, st.length());
        st.append(")");
        st.append(" VALUES ");
        st.append(" ( ");
        while (nr > 0) {
            st.append(" ?, ");
            nr--;
        }
        st.delete(st.length() - 2, st.length());
        st.append(" )");
        return st.toString();
    }

    public void insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String queryFinal = createInsertQuerry();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(queryFinal);
            int nr = 1;
            for (Field a : t.getClass().getDeclaredFields()) {
                a.setAccessible(true);
                Object v=a.get(t);
                if (!a.getName().equals(t.getClass().getDeclaredFields()[0].getName()))
                    if (v.getClass().equals(String.class))
                        statement.setString(nr++, (String) v);
                    else if(v.getClass().equals(Integer.class))
                        statement.setInt(nr++, (Integer) v);
                    else statement.setFloat(nr++, (Float) v);
            }
            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:INSERT " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public void delete(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String queryFinal = createDeleteQuerry();
        Field v = t.getClass().getDeclaredFields()[0];
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(queryFinal);
            v.setAccessible(true);
            Object a = v.get(t);
            statement.setInt(1, (int) a);
            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:DELETE " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public void update(T t){
        Connection connection = null;
        PreparedStatement statement = null;
        String queryFinal = createUpdateQuerry();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(queryFinal);
            int nr=1;
            for (Field a : t.getClass().getDeclaredFields()) {
                a.setAccessible(true);
                Object v=a.get(t);
                if (!a.getName().equals(t.getClass().getDeclaredFields()[0].getName()))
                    if (v.getClass().equals(String.class))
                        statement.setString(nr++, (String) v);
                    else if(v.getClass().equals(Integer.class))
                        statement.setInt(nr++, (Integer) v);
                    else statement.setFloat(nr++, (Float) v);
            }
            Field a=t.getClass().getDeclaredFields()[0];
            a.setAccessible(true);
            Object v=a.get(t);
            /*
            * in caz ca primary key este int sau string
            * */
            if (v.getClass().equals(String.class))
                statement.setString(nr++, (String) v);
            else if(v.getClass().equals(Integer.class))
                statement.setInt(nr++, (Integer) v);

            statement.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:UPDATE " + e.getMessage());
        } finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public ArrayList<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectAllQuerry();
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO:findAll " + e.getMessage());
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return null;
    }

    private ArrayList<T> createObjects(ResultSet resultSet) {
        ArrayList<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                ctor.setAccessible(true);
                T instance = (T) ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

}
