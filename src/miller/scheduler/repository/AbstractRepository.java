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

    protected String NXT_ID_STATEMENT = "";
    protected String DELETE_STATEMENT = "";
    protected String DELETE_PRECHECK_STATEMENT = "";
    protected String INSERT_STATEMENT = "";
    protected String SELECT_STATEMENT = "";
    protected String UPDATE_STATEMENT = "";
    protected String FIND_BY_ID_STATEMENT = "";
    protected String FIND_BY_NAME_STATEMENT = "";

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

            try (PreparedStatement ps = connection.prepareStatement(NXT_ID_STATEMENT);
                 ResultSet rs = ps.executeQuery(NXT_ID_STATEMENT)) {

                if (rs.next()) {
                    Integer id = rs.getInt(1);
                    log.info("Nxt Id is " + id);
                    return id;
                }
            } catch (SQLException e) {
                log.severe("Error : " + e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

        return executeUpdate(INSERT_STATEMENT, entity);
    }

    public T update(T entity, String username) {
        log.info("Updating entity : " + entity.toString());

        Optional<T> savedEntity = findById(((AbstractEntity) entity).getId());

        if (savedEntity.isPresent()) {
            ((AbstractEntity) entity).setCreateDate(((AbstractEntity) savedEntity.get()).getCreateDate());
            ((AbstractEntity) entity).setCreatedBy(((AbstractEntity) savedEntity.get()).getCreatedBy());
        }

        entity = prepareEntity(entity, username);
        return executeUpdate(UPDATE_STATEMENT, entity);
    }

    public Optional<T> findById(int id) {
        return returnFirst(findBy(FIND_BY_ID_STATEMENT, id));
    }

    public T getOne(int id) {
        return findBy(FIND_BY_ID_STATEMENT, id).get(0);
    }

    public Optional<T> findOneByName(String name) {
        return returnFirst(findBy(FIND_BY_NAME_STATEMENT, name));
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
                        throw new RuntimeException("Unable to determine type of parameter.");
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
            e.printStackTrace();
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
            try (PreparedStatement ps = connection.prepareStatement(SELECT_STATEMENT);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    results.add(mapper.mapper(rs));
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement()) {


            String stmt = DELETE_STATEMENT.replace("?", String.valueOf(id));
            int i = statement.executeUpdate(stmt);

            if (i == 1) return true;
        } catch (SQLException e) {
            log.severe("Error: " + e);
        }
        return false;
    }

    public boolean deleteWillViolateReferentialIntegrity(int id) {

        boolean hasForeignKeys = false;

        if (!DELETE_PRECHECK_STATEMENT.isEmpty()) {
            log.info("Delete precheck for id : " + id);


            try (Connection connection = databaseConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(DELETE_PRECHECK_STATEMENT)) {

                ps.setInt(1, id);

                ResultSet resultSet = ps.executeQuery();

                while (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    if (count > 0) {
                        hasForeignKeys = true;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return hasForeignKeys;
    }

    public T executeUpdate(String sql, T entity) {
        log.info("Executing Statement [" + sql + "]");
        Connection connection = null;
        try {
            connection = databaseConnection.getConnection();

            PreparedStatement ps = connection.prepareStatement(sql);

            mapper.mapper(ps, entity);

            ps.executeUpdate();

            return entity;

        } catch (SQLException ex) {
            Logger.getLogger(UserRepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            databaseConnection.releaseConnection(connection);
        }

        return null;
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
