package org.yearup.data.mysql;

import org.springframework.http.ResponseEntity;
import org.yearup.models.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Override
    @Override
    public ResponseEntity<Void> delete(int categoryId) {
        String sql = """
            DELETE FROM categories
            WHERE category_id = ?;
            """;

        try (Connection connection = getConnection();
             PreparedStatement query = connection.prepareStatement(sql)) {

            query.setInt(1, categoryId);

            query.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting category with ID " + categoryId, e);
        }
    }

private Category mapRow(ResultSet row) throws SQLException
{
    int categoryId = row.getInt("category_id");
    String name = row.getString("name");
    String description = row.getString("description");

    Category category = new Category()
    {{
        setCategoryId(categoryId);
        setName(name);
        setDescription(description);
    }};
    return category;
}

