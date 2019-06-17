package miller.scheduler.repository;

import miller.scheduler.domain.AbstractEntity;
import miller.scheduler.repository.mapper.Mapper;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractRepository<T> {
    private final Logger log = Logger.getLogger(AbstractRepository.class.getName());

    String nxtIdStatement = "";
    String deleteStatement = "";
    String deletePrecheckStatement = "?";
    String insertStatement = "";
    String selectStatement = "";
    String updateStatement = "";
    String findByIdStatement = "";
    String findByNameStatement = "";

    protected Mapper<T> mapper;

    private final DatabaseConnection databaseConnection;

    public AbstractRepository(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    private int getNxtPkId() {
        log.info("Getting Nxt Id");
        Connection connection = null;
        try {
            connection = databaseConnection.getConnection();

            try (PreparedStatement ps = connection.prepareStatement(nxtIdStatement);
                 ResultSet rs = ps.executeQuery(nxtIdStatement)) {

                if (rs.next()) {
                    Integer id = rs.getInt(1);
                    log.info("Nxt Id is " + id);
                    return id;
                }
            } catch (SQLException e) {
                Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (SQLException e) {
            Logger.getLogger(AbstractRepository.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            databaseConnection.releaseConnection(connection);
        }

        log.info("Nxt id not found.");
        return -1;
    }


    public T insert(T entity, String username) {
        log.info("Inserting " + entity.toString());

        entity = prepareEntity(entity, username);

        if (((AbstractEntity) entity).getId() == null) {
            ((AbstractEntity) entity).setId(getNxtPkId());
            log.info("Setting entity id");
        }

        return executeUpdate(insertStatement, entity);
    }

    public T update(T entity, String username) {
        log.info("Updating entity : " + entity.toString());

        Optional<T> savedEntity = findById(((AbstractEntity) entity).getId());

        if (savedEntity.isPresent()) {
            ((AbstractEntity) entity).setCreateDate(((AbstractEntity) savedEntity.get()).getCreateDate());
            ((AbstractEntity) entity).setCreatedBy(((AbstractEntity) savedEntity.get()).getCreatedBy());
        }

        entity = prepareEntity(entity, username);
        return executeUpdate(updateStatement, entity);
    }

    public Optional<T> findById(int id) {
        return returnFirst(findBy(findByIdStatement, id));
    }

    public T getOne(int id) {
        return findBy(findByIdStatement, id).get(0);
    }

    public Optional<T> findOneByName(String name) {
        return returnFirst(findBy(findByNameStatement, name));
    }

    private Optional<T> returnFirst(List<T> entities) {
        Optional<T> result;
        if (entities.isEmpty()) {
            result = Optional.empty();
        } else {
            result = Optional.of(entities.get(0));
        }
        return result;
    }

    public List<T> findBy(String sql, Object... params) {
        List<T> results = new ArrayList<>();
        Connection connection = null;

        try {
            connection = databaseConnection.getConnection();

            try (PreparedStatement ps = connection.prepareStatement(sql)) {


                for (int i = 0; i < params.length; i++) {
                    if (params[i] instanceof String) {
                        ps.setString(i + 1, (String) params[i]);
                    } else if (params[i] instanceof Integer) {
                        ps.setInt(i + 1, (Integer) params[i]);
                    } else {
                        ps.setObject(i + 1, params[i]);
                    }
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        results.add(mapper.mapper(rs));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException e) {
            Logger.getLogger(AbstractRepository.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            databaseConnection.releaseConnection(connection);
        }

        return results;
    }

    public List<T> findAll() {
        log.info("Getting all entities.");

        List<T> results = new ArrayList<>();
        Connection connection = null;
        try {
            connection = databaseConnection.getConnection();

            try (PreparedStatement ps = connection.prepareStatement(selectStatement);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    results.add(mapper.mapper(rs));
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException e) {
            Logger.getLogger(AbstractRepository.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            databaseConnection.releaseConnection(connection);
        }
        return results;
    }

    public boolean delete(int id) {
        log.info("Deleting entity id : " + id);

        if (deleteWillViolateReferentialIntegrity(id)) {
            return false;
        }

        Connection connection = null;

        try {
            connection = databaseConnection.getConnection();

            try (Statement statement = connection.createStatement()) {


                String stmt = deleteStatement.replace("?", String.valueOf(id));
                int i = statement.executeUpdate(stmt);

                if (i == 1) return true;
            } catch (SQLException e) {
                log.severe("Error: " + e);
            }
        } catch (SQLException e) {
            Logger.getLogger(AbstractRepository.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            databaseConnection.releaseConnection(connection);
        }
        return false;
    }

    public boolean deleteWillViolateReferentialIntegrity(int id) {

        boolean hasForeignKeys = false;

        if (!deletePrecheckStatement.isEmpty()) {
            log.info("Delete precheck for id : " + id);

            Connection connection = null;

            try {
                connection = databaseConnection.getConnection();

                try (PreparedStatement ps = connection.prepareStatement(deletePrecheckStatement)) {

                    ps.setInt(1, id);

                    try (ResultSet resultSet = ps.executeQuery()) {

                        while (resultSet.next()) {
                            int count = resultSet.getInt(1);
                            if (count > 0) {
                                hasForeignKeys = true;
                            }
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (SQLException e) {
                Logger.getLogger(AbstractRepository.class.getName()).log(Level.SEVERE, null, e);
            } finally {
                databaseConnection.releaseConnection(connection);
            }
        }
        return hasForeignKeys;
    }

    public T executeUpdate(String sql, T entity) {
        log.info("Executing Statement [" + sql + "]");

        Connection connection = null;

        try {
            connection = databaseConnection.getConnection();

            if (runStatement(sql, entity, connection)) return entity;
        } catch (SQLException e) {
            Logger.getLogger(AbstractRepository.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            databaseConnection.releaseConnection(connection);
        }
        return null;
    }

    private boolean runStatement(String sql, T entity, Connection connection) {
        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            mapper.mapper(ps, entity);

            ps.executeUpdate();

            return true;

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public T saveOrUpdate(T entity, String username) {
        log.info("Save or Update: " + entity);

        if (((AbstractEntity) entity).getId() != null) {
            entity = update(entity, username);
        } else {
            entity = insert(entity, username);
        }
        return entity;
    }

    public T prepareEntity(T entity, String username) {

        if (((AbstractEntity) entity).getId() == null) {
            ((AbstractEntity) entity).setCreateDate(Instant.now());
            ((AbstractEntity) entity).setCreatedBy(username);
        }

        ((AbstractEntity) entity).setLastUpdate(Instant.now());
        ((AbstractEntity) entity).setLastUpdatedBy(username);
        return entity;
    }
}
