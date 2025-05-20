package task11;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ConsoleHelperFX {
    public static List<String> getAllTables(Connection conn) {
        List<String> tables = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT table_name FROM information_schema.tables " +
                             "WHERE table_schema = 'public' AND table_type = 'BASE TABLE'")) {
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
        } catch (Exception e) {
            tables.add("Ошибка: " + e.getMessage());
        }
        return tables;
    }

    public static boolean tableExists(Connection conn, String tableName) {
        try (ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null)) {
            return rs.next();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean hasColumns(Connection conn, String tableName, String[] expectedColumns) {
        try (ResultSet rs = conn.getMetaData().getColumns(null, null, tableName, null)) {
            HashSet<String> actualColumns = new HashSet<>();
            while (rs.next()) {
                actualColumns.add(rs.getString("COLUMN_NAME").toLowerCase());
            }
            for (String col : expectedColumns) {
                if (!actualColumns.contains(col.toLowerCase())) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}