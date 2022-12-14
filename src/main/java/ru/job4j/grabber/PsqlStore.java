package ru.job4j.grabber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class PsqlStore implements Store, AutoCloseable {

    private static final Logger LOG = LogManager.getLogManager().getLogger(PsqlStore.class.getName());

    private final Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("driver-class-name"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    @Override
    public void save(Post post) {
        try (PreparedStatement preparedStatement  =
                cnn.prepareStatement("INSERT INTO post(name,text,link,created) VALUES (?,?,?,?)"
                        + "on conflict (link) do nothing")) {
            preparedStatement.setString(1, post.getTitle());
            preparedStatement.setString(2, post.getDescription());
            preparedStatement.setString(3, post.getLink());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            preparedStatement.execute();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    post.setId(resultSet.getInt("id"));
                }
            }
        } catch (SQLException e) {
            LOG.info(e.toString());
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> rsl = new ArrayList<>();
        try (Statement st = cnn.createStatement()) {
            ResultSet resultSet = st.executeQuery("SELECT * FROM post");
            while (resultSet.next()) {
                rsl.add(createNewPostFromBD(resultSet));
            }
        } catch (SQLException e) {
            LOG.info(String.valueOf(e));
        }
        return rsl;
    }


        @Override
    public Post findById(int id) {
        Post rsl = null;
        try (PreparedStatement preparedStatement = cnn.prepareStatement("SELECT * FROM post WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                rsl = createNewPostFromBD(resultSet);
            }
        } catch (SQLException e) {
            LOG.info(String.valueOf((e)));
        }
        return rsl;
    }

            @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public Post createNewPostFromBD(ResultSet rsl) throws SQLException {
        return new Post(rsl.getInt("id"), rsl.getString("name"), rsl.getString("link"),
                rsl.getString("text"), rsl.getTimestamp("created").toLocalDateTime());
    }
}

