package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Product;
import org.yearup.data.ProductDao;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component // Marks this class as a Spring-managed component for dependency injection.
public class MySqlProductDao extends MySqlDaoBase implements ProductDao {

    /**
     * Constructor for dependency injection.
     * The DataSource is injected by Spring to manage database connections.
     */
    public MySqlProductDao(DataSource dataSource) {
        super(dataSource); // Call the parent class constructor to initialize the DataSource.
    }

    /**
     * Search for products using dynamic filters.
     * Supports filtering by category ID, price range, color, and name.
     * @param categoryId Category filter (nullable).
     * @param minPrice Minimum price filter (nullable).
     * @param maxPrice Maximum price filter (nullable).
     * @param color Product color filter (nullable).
     * @param name Partial name search (nullable).
     * @return List of products matching the filters.
     */
    @Override
    public List<Product> search(Integer categoryId, BigDecimal minPrice, BigDecimal maxPrice, String color, String name) {
        List<Product> products = new ArrayList<>();

        // SQL query with dynamic filtering conditions.
        String sql = """
            SELECT * FROM products
            WHERE (category_id = ? OR ? = -1)
            AND (price >= ? OR ? = -1)
            AND (price <= ? OR ? = -1)
            AND (LOWER(color) = LOWER(?) OR ? = '' OR color IS NULL)
            AND (LOWER(name) LIKE LOWER(?) OR ? = '')
            """;
        /*
        String sql = "SELECT * FROM products " +
                "WHERE (category_id = ? OR ? = -1) " +
                "  AND (price BETWEEN ? AND ? OR (? = -1 AND ? = -1)) " +
                "  AND (color = ? OR ? = '')";

*/
        // Phase Two
        // The SQL query now properly handles price ranges
        // #1 - Bug - Price and color filter are fixed!

        // Handle null values to prevent errors and set default behavior for optional filters.
        categoryId = categoryId == null ? -1 : categoryId; // Default to -1 if category ID is not provided.
        minPrice = minPrice == null ? new BigDecimal("-1") : minPrice; // Default to -1 to disable the filter.
        maxPrice = maxPrice == null ? new BigDecimal("-1") : maxPrice; // Default to -1 to disable the filter.
        color = color == null ? "" : color; // Default to an empty string if no color is provided.
        name = name == null ? "" : name; // Default to an empty string if no name is provided.

        try (Connection connection = getConnection(); // Get a database connection.
             PreparedStatement statement = connection.prepareStatement(sql)) { // Prepare the SQL query.

            // Bind parameters dynamically.
            statement.setInt(1, categoryId);
            statement.setInt(2, categoryId);
            statement.setBigDecimal(3, minPrice);
            statement.setBigDecimal(4, minPrice);
            statement.setBigDecimal(5, maxPrice);
            statement.setBigDecimal(6, maxPrice);
            statement.setString(7, color);
            statement.setString(8, color);
            statement.setString(9, "%" + name + "%");
            statement.setString(10, name);

            // Execute the query and process the results.
            try (ResultSet row = statement.executeQuery()) {
                while (row.next()) {
                    products.add(mapRow(row)); // Map each row to a Product object using mapRow.
                }
            }

        } catch (SQLException e) {
            // Log and rethrow the exception for easier debugging.
            throw new RuntimeException("Error executing search query", e);
        }

        return products; // Return the list of matching products.
    }

    /**
     * Retrieve products by category ID.
     * @param categoryId ID of the category to filter by.
     * @return List of products in the specified category.
     */
    @Override
    public List<Product> listByCategoryId(int categoryId) {
        List<Product> products = new ArrayList<>();

        // Simple SQL query to fetch products by category.
        String sql = "SELECT * FROM products WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, categoryId); // Bind the category ID.

            try (ResultSet row = statement.executeQuery()) {
                while (row.next()) {
                    products.add(mapRow(row)); // Map each row to a Product object.
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching products by category ID", e);
        }

        return products;
    }

    /**
     * Retrieve a product by its ID.
     * @param productId ID of the product to fetch.
     * @return The product with the specified ID, or null if not found.
     */
    @Override
    public Product getById(int productId) {
        String sql = "SELECT * FROM products WHERE product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId); // Bind the product ID.

            try (ResultSet row = statement.executeQuery()) {
                if (row.next()) {
                    return mapRow(row); // Return the mapped Product object.
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching product by ID", e);
        }

        return null; // Return null if no product is found.
    }

    /**
     * Insert a new product into the database.
     * @param product Product object to insert.
     * @return The newly created product, including its generated ID.
     */
    @Override
    public Product create(Product product) {
        String sql = """
            INSERT INTO products(name, price, category_id, description, color, image_url, stock, featured)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getCategoryId());
            statement.setString(4, product.getDescription());
            statement.setString(5, product.getColor());
            statement.setString(6, product.getImageUrl());
            statement.setInt(7, product.getStock());
            statement.setBoolean(8, product.isFeatured());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return getById(generatedKeys.getInt(1)); // Return the newly inserted product.
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting product", e);
        }

        return null;
    }

    /**
     * Update an existing product.
     * @param productId ID of the product to update.
     * @param product Product object with updated values.
     */
    @Override
    public void update(int productId, Product product) {
        String sql = """
            UPDATE products
            SET name = ?, price = ?, category_id = ?, description = ?, color = ?, image_url = ?, stock = ?, featured = ?
            WHERE product_id = ?
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getCategoryId());
            statement.setString(4, product.getDescription());
            statement.setString(5, product.getColor());
            statement.setString(6, product.getImageUrl());
            statement.setInt(7, product.getStock());
            statement.setBoolean(8, product.isFeatured());
            statement.setInt(9, productId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    /**
     * Delete a product by its ID.
     * @param productId ID of the product to delete.
     */
    @Override
    public void delete(int productId) {
        String sql = "DELETE FROM products WHERE product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, productId); // Bind the product ID.
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    /**
     * Map a row from the ResultSet to a Product object.
     * @param row The current row from the ResultSet.
     * @return A Product object populated with data from the row.
     */
    protected static Product mapRow(ResultSet row) throws SQLException {
        return new Product(
                row.getInt("product_id"),
                row.getString("name"),
                row.getBigDecimal("price"),
                row.getInt("category_id"),
                row.getString("description"),
                row.getString("color"),
                row.getInt("stock"),
                row.getBoolean("featured"),
                row.getString("image_url")
        );
    }
}