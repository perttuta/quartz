package fi.perttuta.quartz.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Repository
public class QuartzNodeDao {
  @Autowired
  private DataSource dataSource;
  @Value("${build.revision}")
  private String revision;

  public boolean isPrimary() {
    String sql = "INSERT INTO build_revision (revision) VALUES (?);";
    try(Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      try (PreparedStatement ps = connection.prepareStatement(sql)) {
        connection.setAutoCommit(false);
        ps.setString(1, revision);
        ps.executeUpdate();
        connection.commit();
        // insert was successful, so the caller is primary node
        return true;
      }
    } catch (Exception e) {
      // Should be handled better in real life, but handling ignored in demo.
      // Most likely insert failed, because entry exists already, so caller is not primary node.
      return false;
    }
  }
}
