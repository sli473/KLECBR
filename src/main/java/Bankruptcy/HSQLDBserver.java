package Bankruptcy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import jcolibri.util.FileIO;
import org.apache.commons.logging.LogFactory;
import org.hsqldb.Server;
import org.hsqldb.util.SqlFile;

public class HSQLDBserver {
    static boolean initialized = false;
    private static Server server;

    public HSQLDBserver() {
    }

    public static void init() {
        if (!initialized) {
            LogFactory.getLog(HSQLDBserver.class).info("Creating data base ...");
            server = new Server();
            server.setDatabaseName(0, "bankruptcy");
            server.setDatabasePath(0, "mem:bankruptcy;sql.enforce_strict_size=true");
            server.setDatabaseName(1, "bankruptcy-ext");
            server.setDatabasePath(1, "mem:bankruptcy-ext;sql.enforce_strict_size=true");
            server.setLogWriter((PrintWriter)null);
            server.setErrWriter((PrintWriter)null);
            server.setSilent(true);
            server.start();
            initialized = true;

            try {
                Class.forName("org.hsqldb.jdbcDriver");
                PrintStream out = new PrintStream(new ByteArrayOutputStream());
                Connection conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/bankruptcy", "sa", "");
                SqlFile file = new SqlFile(new File(FileIO.findFile("src/main/java/Bankruptcy/bankruptcy.sql").getFile()), false, new HashMap());
                file.execute(conn, out, out, true);
                Connection connExt = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/bankruptcy-ext", "sa", "");
                SqlFile fileExt = new SqlFile(new File(FileIO.findFile("src/main/java/Bankruptcy/bankruptcy-ext.sql").getFile()), false, new HashMap());
                fileExt.execute(connExt, out, out, true);
                LogFactory.getLog(HSQLDBserver.class).info("Data base generation finished");
            } catch (Exception var5) {
                LogFactory.getLog(HSQLDBserver.class).error(var5);
            }

        }
    }

    public static void shutDown() {
        if (initialized) {
            server.stop();
            initialized = false;
        }

    }

    public static void main(String[] args) {
        init();
        shutDown();
        System.exit(0);
    }
}
