package org.joe.gestion.model.sqlpersistencia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import oracle.sql.BLOB;
import java.util.Properties;
import oracle.jdbc.OracleConnection;
import org.apache.commons.codec.digest.DigestUtils;
import org.joe.gestion.model.data.Category;
import org.joe.gestion.model.data.Player;
import org.joe.gestion.model.data.Season;
import org.joe.gestion.model.data.Team;
import org.joe.gestion.model.persistence.EquipDataInterface;
import org.joe.gestion.model.persistence.EquipDataInterfaceException;

/**
 *
 * @author jonah
 */
public class EquipDataImplementationSQL implements EquipDataInterface {

    Connection con;
    PreparedStatement userPreparedStatement;
    PreparedStatement uservalidation;
    PreparedStatement restorepassword;
    PreparedStatement getplaybyid;
    PreparedStatement addnewplayps;
    PreparedStatement deletplayer;
    PreparedStatement editplayer;
    PreparedStatement newteam;
    PreparedStatement teambyCat;
    PreparedStatement teamPlyers;
    PreparedStatement delteam;
    PreparedStatement newSeasonDate;
    PreparedStatement teamseason;
    PreparedStatement getteammemcount;
    PreparedStatement removeteam;
    PreparedStatement delfromplyteam;
    PreparedStatement addplyteam;
    PreparedStatement rmveplayers;
    PreparedStatement rmveteamswithPlayers;
    PreparedStatement checkplayer;
    PreparedStatement checkplayerteam;
    PreparedStatement playerteamget;
    PreparedStatement filterplayerspstmt;
    PreparedStatement getteambynamepstmt;
    PreparedStatement seasonTeamstmnt;
    PreparedStatement getseasoncatteamsStatement;
    PreparedStatement getCategorypstmt;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public EquipDataImplementationSQL() {
        this("config.properties");
    }

    public EquipDataImplementationSQL(String configurationfile) {
        if (configurationfile == null || configurationfile.equals("")) {
            throw new EquipDataInterfaceException("The Configuration File Name Does Not Exists Or Is Null");
        }

        /* boolean answer = connectDatasource(configurationfile);
        if (answer) {
            System.out.println("Connection Established");
        }*/
    }

    public boolean connectDatasource(String filename) {

        Properties propfile = new Properties();

        try {
            propfile.load(new FileInputStream(filename));

        } catch (FileNotFoundException ex) {
            throw new EquipDataInterfaceException("The properties file " + filename + " wasn't found", ex.getCause());
        } catch (IOException ex) {
            throw new EquipDataInterfaceException("Error loading the provided properties file", ex.getCause());
        }

        String url = propfile.getProperty("url");
        String password = propfile.getProperty("password");
        String user = propfile.getProperty("username");

        if (url.isBlank()) {
            throw new EquipDataInterfaceException("Estas pasando un url vacio o nul");
        }
        if (user.isBlank()) {
            throw new EquipDataInterfaceException("Estas pasando un usuario vacio o nul");
        }
        if (password.isBlank()) {
            throw new EquipDataInterfaceException("Estas pasando una contraseña vacio o nul");
        }

        try {
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Connection Established");

            //  con.setAutoCommit(false);
        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Unable To Establish Connection To The Defined Database " + ex.getMessage(), ex.getCause());
        }

        return true;
    }

    public boolean disconnectDatasource() {

        if (con != null) {
            try {

                con.close();
                System.out.println("Connection Closed");
            } catch (SQLException ex) {

                throw new EquipDataInterfaceException("Datasource Wasn't Able To close" + ex.getMessage(), ex.getCause());

            } finally {
                con = null;
            }
        }

        return true;
    }

    private Player getPlayer(ResultSet rs) {

        try {
            if (rs.next()) {
                Integer playerid = rs.getInt("id");

                String name = rs.getString("name");
                if (rs.wasNull()) {
                    name = "";
                }

                String surname = rs.getString("surname");
                if (rs.wasNull()) {
                    surname = "";
                }

                String legalId = rs.getString("legal_id");
                if (rs.wasNull()) {
                    legalId = "";
                }
                String sexe = rs.getString("sex");
                if (rs.wasNull()) {
                    sexe = "";
                }
                Date datebirth = rs.getDate("birth_year");
                if (rs.wasNull()) {
                    datebirth = null;
                }

                String iban = rs.getString("iban");
                if (rs.wasNull()) {
                    iban = "";
                }
                String direccion = rs.getString("direcion");
                if (direccion == null) {
                    direccion = "";
                }
                String codigopostal = rs.getString("codigo_postal");
                if (codigopostal == null) {
                    codigopostal = "";
                }
                String localidad = rs.getString("localidad");
                if (localidad == null) {
                    localidad = "";
                }
                String provincia = rs.getString("provincia");
                if (provincia == null) {
                    provincia = "";
                }
                String pais = rs.getString("pais");
                if (pais == null) {
                    pais = "";
                }

                Integer medicalfin = rs.getInt("medical_rev_fin");
                if (rs.wasNull()) {
                    medicalfin = null;
                }

                Blob photo = rs.getBlob("photo");
                if (rs.wasNull()) {
                    photo = null;
                }

                Player player
                        = new Player(name, surname, sexe, datebirth, legalId, iban, direccion, codigopostal, localidad, provincia, pais, photo, medicalfin);
                player.setId(playerid);
                return player;

            } else {
                throw new EquipDataInterfaceException("Query returned with no player");
            }

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Unable To get Player " + ex.getMessage(), ex.getCause());
        } finally {
            closeResultSet(rs);
        }
    }

    private List<Player> getPlayers(ResultSet rs) {

        List<Player> players = new ArrayList<>();

        try {
            while (rs.next()) {
                Integer playerid = rs.getInt("id");

                String name = rs.getString("name");
                if (rs.wasNull()) {
                    name = "";
                }

                String surname = rs.getString("surname");
                if (rs.wasNull()) {
                    surname = "";
                }

                String legalId = rs.getString("legal_id");
                if (rs.wasNull()) {
                    legalId = "";
                }
                String sexe = rs.getString("sex");
                if (rs.wasNull()) {
                    sexe = "";
                }
                Date datebirth = rs.getDate("birth_year");
                if (rs.wasNull()) {
                    datebirth = null;
                }

                String iban = rs.getString("iban");
                if (rs.wasNull()) {
                    iban = "";
                }

                String direccion = rs.getString("direcion");
                if (direccion == null) {
                    direccion = "";
                }
                String codigopostal = rs.getString("codigo_postal");
                if (codigopostal == null) {
                    codigopostal = "";
                }
                String localidad = rs.getString("localidad");
                if (localidad == null) {
                    localidad = "";
                }
                String provincia = rs.getString("provincia");
                if (provincia == null) {
                    provincia = "";
                }
                String pais = rs.getString("pais");
                if (pais == null) {
                    pais = "";
                }

                Integer medicalfin = rs.getInt("medical_rev_fin");
                if (rs.wasNull()) {
                    medicalfin = null;
                }

                Blob photo = rs.getBlob("photo");
                if (rs.wasNull()) {
                    photo = null;
                }

                Player play = new Player(name, surname, sexe, datebirth, legalId, iban, direccion, codigopostal, localidad, provincia, pais, photo, medicalfin);
                play.setId(playerid);
                players.add(play);

            }
        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Unable To get Players " + ex.getMessage(), ex.getCause());
        } finally {
            closeResultSet(rs);
        }
        return players;
    }

    private List<Player> getPlayersconCat(ResultSet rs) {

        List<Player> players = new ArrayList<>();

        try {
            while (rs.next()) {
                Integer playerid = rs.getInt("id");

                String name = rs.getString("name");
                if (rs.wasNull()) {
                    name = "";
                }

                String surname = rs.getString("surname");
                if (rs.wasNull()) {
                    surname = "";
                }

                String legalId = rs.getString("legal_id");
                if (rs.wasNull()) {
                    legalId = "";
                }
                String sexe = rs.getString("sex");
                if (rs.wasNull()) {
                    sexe = "";
                }
                Date datebirth = rs.getDate("birth_year");
                if (rs.wasNull()) {
                    datebirth = null;
                }

                String iban = rs.getString("iban");
                if (rs.wasNull()) {
                    iban = "";
                }

                String direccion = rs.getString("direcion");
                if (direccion == null) {
                    direccion = "";
                }
                String codigopostal = rs.getString("codigo_postal");
                if (codigopostal == null) {
                    codigopostal = "";
                }
                String localidad = rs.getString("localidad");
                if (localidad == null) {
                    localidad = "";
                }
                String provincia = rs.getString("provincia");
                if (provincia == null) {
                    provincia = "";
                }
                String pais = rs.getString("pais");
                if (pais == null) {
                    pais = "";
                }

                Integer medicalfin = rs.getInt("medical_rev_fin");
                if (rs.wasNull()) {
                    medicalfin = null;
                }

                Blob photo = rs.getBlob("photo");
                if (rs.wasNull()) {
                    photo = null;
                }

                String category = rs.getString("category_name");
                if (rs.wasNull()) {
                    category = "";
                }

                String titularidad = rs.getString("tit_con");
                if (rs.wasNull()) {
                    titularidad = "";
                }
                Player play = new Player(name, surname, sexe, datebirth, legalId, iban, direccion, codigopostal, localidad, provincia, pais, photo, medicalfin);
                play.setCategory(category);
                play.setId(playerid);
                play.setTitularidad(titularidad);
                players.add(play);

            }
        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Unable To get Players. " + ex.getMessage(), ex.getCause());
        } finally {
            closeResultSet(rs);
        }
        if (players.isEmpty()) {
            throw new EquipDataInterfaceException("Player List Is Null. Check Season");
        } else {
            return players;
        }

    }

    private List<Team> getTeams(ResultSet rs) {
        List<Team> teams = new ArrayList<>();
        try {
            while (rs.next()) {
                Integer teamId = rs.getInt("ID");
                String teamName = rs.getString("NAME");
                String teamType = rs.getString("TEAM_TYPE");
                String teamCatName = rs.getString("CATEGORY_NAME");
                Date teamSeason = rs.getDate("SEASON_YEAR");

                teamName = teamName == null ? "" : teamName;
                teamType = teamType == null ? "" : teamType;
                teamCatName = teamCatName == null ? "" : teamCatName;

                Team nteam = new Team(teamName, teamSeason, teamCatName, teamType);
                nteam.setId(teamId);
                teams.add(nteam);
            }

            if (teams.isEmpty()) {

                throw new EquipDataInterfaceException("Ningun equipo encontrado");
            }

        } catch (SQLException e) {

            throw new EquipDataInterfaceException("Unable to get teams." + e.getMessage(), e.getCause());
        } finally {
            closeResultSet(rs);
        }
        return teams;
    }

    private Team getTeam(ResultSet rs) {
        Team nteam = null;
        try {

            while (rs.next()) {

                Integer teamid = rs.getInt("id");
                if (rs.wasNull()) {
                    teamid = null;
                }

                String teamname = rs.getString("name");
                if (rs.wasNull()) {
                    teamname = "";
                }
                String teamType = rs.getString("team_type");
                if (rs.wasNull()) {
                    teamType = "";
                }
                String teamCatName = rs.getString("category_name");
                if (rs.wasNull()) {
                    teamCatName = "";
                }
                Date teamSeason = rs.getDate("season_year");
                if (rs.wasNull()) {
                    teamSeason = null;
                }

                nteam = new Team(teamname, teamSeason, teamCatName, teamType);
                nteam.setId(teamid);
                return nteam;

            }

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Unable To get Team. " + ex.getMessage(), ex.getCause());
        } finally {
            closeResultSet(rs);
        }
        if (nteam == null) {
            throw new EquipDataInterfaceException("Unable To get Team. Check Name || Season");
        } else {
            return nteam;
        }

    }

    @Override
    public boolean createUser(String username, String name, String password) {

        String hashedPassword = DigestUtils.sha1Hex(password);
        String query = "INSERT INTO USERMG (USERNAME,NAME, PASSWORD) VALUES (?, ?,?)";

        try {
            userPreparedStatement = con.prepareStatement(query);
            userPreparedStatement.setString(1, username);
            userPreparedStatement.setString(2, name);
            userPreparedStatement.setString(3, hashedPassword);

            userPreparedStatement.executeUpdate();

            return true;

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Failed To Insert New User. " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(userPreparedStatement);
        }

    }

    private void closePreparedStatement(PreparedStatement ps) {

        if (ps != null) {
            try {
                ps.close();

                System.out.println("PreparedStatement Tancada");
            } catch (SQLException ex) {
                throw new EquipDataInterfaceException("Fallo en tancar preparedStatment:  \tINFO: -> " + ex.getMessage()
                        + "\tCAUSE: -> " + ex.getCause());
            }
        } else {
            System.out.println("PreparedStatement ya está cerrado o nunca fue creado.");
        }

    }

    private void closeStatement(Statement s) {
        if (s != null) {
            try {
                s.close();

                System.out.println("Statement Tancada");
            } catch (SQLException ex) {
                throw new EquipDataInterfaceException("Fallo en tancar Statment:  \tINFO: -> " + ex.getMessage()
                        + "\tCAUSE: -> " + ex.getCause());
            }
        } else {
            System.out.println("Statement ya está cerrado o nunca fue creado.");
        }
    }

    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                System.out.println("ResultSet cerrado.");
            } catch (SQLException ex) {

                throw new EquipDataInterfaceException("Error al cerrar ResultSet: INFO -> " + ex.getMessage()
                        + " CAUSE -> " + ex.getCause(), ex);
            }
        } else {
            System.out.println("ResultSet ya está cerrado o nunca fue creado.");
        }
    }

    @Override
    public boolean validateUser(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new EquipDataInterfaceException("The username or password entered is null or empty");
        }

        String hashedPassword = DigestUtils.sha1Hex(password);

        String query = "SELECT username, password FROM usermg WHERE username = ?";
        ResultSet rs = null;
        try {
            uservalidation = con.prepareStatement(query);
            uservalidation.setString(1, username);
            rs = uservalidation.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                if (hashedPassword.equals(storedPassword)) {
                    return true;
                } else {
                    throw new EquipDataInterfaceException("Invalid Password");
                }
            } else {
                throw new EquipDataInterfaceException("Invalid Username");
            }
        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Database error during validation." + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(uservalidation);
            closeResultSet(rs);
        }
    }

    @Override
    public void restorePassword(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new EquipDataInterfaceException("The username is null or empty");
        }

        String hashedpasString = DigestUtils.sha1Hex(password);

        String query = "UPDATE USERMG SET PASSWORD = ? WHERE USERNAME = ?";

        try {
            restorepassword = con.prepareStatement(query);
            restorepassword.setString(1, hashedpasString);
            restorepassword.setString(2, username);

            int result = restorepassword.executeUpdate();

            if (result > 0) {
                System.out.println("Password Restored Successfully");
            } else {

                throw new EquipDataInterfaceException("Username not found. Password restore failed.");
            }

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Couldn't restore password. Please try again later. " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(restorepassword);
        }
    }

    @Override
    public Player getPlayerByLegalId(String legalId) {

        if (legalId == null || legalId.isEmpty()) {
            throw new EquipDataInterfaceException("The legal ID provided is null or empty");
        }

        String query = "SELECT * FROM player WHERE legal_id = ?";

        try {
            getplaybyid = con.prepareStatement(query);
            getplaybyid.setString(1, legalId);

            ResultSet rs = getplaybyid.executeQuery();

            return getPlayer(rs);

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Unable to retrieve player by legal ID. " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(getplaybyid);
        }

    }

    @Override
    public void addNewPlayer(Player player) {
        if (player == null) {
            throw new EquipDataInterfaceException("The Player Is Null");
        }

        String newplayerQuery = "Insert into Player (name,surname,sex,birth_year,"
                + "legal_id,iban,direcion,codigo_postal,localidad,provincia,pais,photo,medical_rev_fin) values "
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            addnewplayps = con.prepareStatement(newplayerQuery);

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Unable to create prepared statement for new player " + ex.getMessage(), ex.getCause());
        }

        try {
            addnewplayps.setString(1, player.getName());
            addnewplayps.setString(2, player.getSurname());
            addnewplayps.setString(3, String.valueOf(player.getSex()));

            java.util.Date utilDate = player.getBirth_year();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            addnewplayps.setDate(4, sqlDate);

            addnewplayps.setString(5, player.getLegal_id());
            addnewplayps.setString(6, player.getIban());
            addnewplayps.setString(7, player.getDireccion());
            addnewplayps.setString(8, player.getCodigo_postal());
            addnewplayps.setString(9, player.getLocalidad());
            addnewplayps.setString(10, player.getProvincia());
            addnewplayps.setString(11, player.getPais());

            Blob imageBlob = player.getImage();
            if (imageBlob != null) {
                byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());

                OracleConnection oracleCon = (OracleConnection) con;
                BLOB oracleBlob = BLOB.createTemporary(oracleCon, true, BLOB.DURATION_SESSION);
                oracleBlob.setBytes(1, imageBytes);
                addnewplayps.setBlob(12, oracleBlob);
            }

            addnewplayps.setInt(13, player.getMedical_rev_fin());

            int result = addnewplayps.executeUpdate();
            if (result == 1) {
                System.out.println("Player Added Successfully");

            }
        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Unable To Insert New Player " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(addnewplayps);
        }

    }

    @Override
    public void eliminarJugador(String legalID) {
        if (legalID == null) {
            throw new EquipDataInterfaceException("The ID Field Can't Be Null");
        }

        String query = "DELETE FROM PLAYER WHERE LEGAL_ID = ?";

        try {
            deletplayer = con.prepareStatement(query);
            deletplayer.setString(1, legalID);
            int results = deletplayer.executeUpdate();

            if (results > 0) {
                System.out.println("Player with legal ID " + legalID + " has been successfully deleted.");
            } else {
                throw new EquipDataInterfaceException("No player found with legal ID " + legalID);
            }

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Somthing Went Wrong Trying To Create The Delete Statement " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(deletplayer);
        }

    }

    @Override
    public void editarJugador(Player player) {
        String legalID = player.getLegal_id();
        if (legalID == null) {
            throw new EquipDataInterfaceException("The ID Field Can't Be Null");
        }

        String query = "UPDATE PLAYER "
                + "SET NAME = ?, SURNAME = ?, SEX = ?, BIRTH_YEAR = ?, LEGAL_ID = ?,"
                + "IBAN = ?, DIRECION = ?, CODIGO_POSTAL = ?, LOCALIDAD = ?, PROVINCIA = ?,"
                + "PAIS = ?,PHOTO = ?, MEDICAL_REV_FIN = ? WHERE LEGAL_ID = ?";

        try {
            editplayer = con.prepareStatement(query);
            editplayer.setString(1, player.getName());
            editplayer.setString(2, player.getSurname());
            editplayer.setString(3, String.valueOf(player.getSex()));

            java.util.Date utilDate = player.getBirth_year();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            editplayer.setDate(4, sqlDate);

            editplayer.setString(5, player.getLegal_id());
            editplayer.setString(6, player.getIban());
            editplayer.setString(7, player.getDireccion());
            editplayer.setString(8, player.getCodigo_postal());
            editplayer.setString(9, player.getLocalidad());
            editplayer.setString(10, player.getProvincia());
            editplayer.setString(11, player.getPais());

            Blob imageBlob = player.getImage();
            if (imageBlob != null) {
                byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());

                OracleConnection oracleCon = (OracleConnection) con;
                BLOB oracleBlob = BLOB.createTemporary(oracleCon, true, BLOB.DURATION_SESSION);
                oracleBlob.setBytes(1, imageBytes);
                editplayer.setBlob(12, oracleBlob);
            }

            editplayer.setInt(13, player.getMedical_rev_fin());
            editplayer.setString(14, legalID);

            int result = editplayer.executeUpdate();
            if (result == 1) {
                System.out.println("Player Updated Successfully");

            } else {
                System.out.println("Could not update player");
            }
        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Error Trying Update Player " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(editplayer);
        }
    }

    @Override
    public List<Category> getCategorys() {
        List<Category> categories = new ArrayList<>();
        ResultSet rs = null;
        try {
            String query = "Select * from category";

            Statement stm = con.createStatement();
            rs = stm.executeQuery(query);

            while (rs.next()) {

                Integer catId = rs.getInt("id");

                String catname = rs.getString("name");
                if (catname == null) {
                    catname = "";
                }

                Integer catminage = rs.getInt("min_age");
                if (catminage == 0) {
                    catminage = null;
                }

                Integer catmaxage = rs.getInt("max_age");
                if (catmaxage == 0) {
                    catmaxage = null;
                }

                Category cat = new Category(catname, catminage.intValue(), catmaxage.intValue());
                cat.setId(catId);
                categories.add(cat);
            }

            return categories;

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Couldn't Retrieve The Categories " + ex.getMessage(), ex.getCause());
        } finally {
            closeResultSet(rs);
        }
    }

    @Override
    public List<Team> getTeamsByCategory(String cat) {

        if (cat.equals("") || cat == null) {
            throw new EquipDataInterfaceException("Category Can't Be Null or Empty");
        }
        String query = "SELECT * FROM Team WHERE UPPER(CATEGORY_NAME) = UPPER(?)";

        try {
            teambyCat = con.prepareStatement(query);
            teambyCat.setString(1, cat);

            ResultSet rs = teambyCat.executeQuery();

            return getTeams(rs);

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Error Trying To Retrieve The Teams By Categories " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(teambyCat);
        }

    }

    @Override
    public List<Team> getSeasonCategoryTeam(String category, Date date) {
        if (category.isBlank() || category == null) {
            throw new EquipDataInterfaceException("Category Can't Be Null or Empty");
        }

        if (date == null) {
            throw new EquipDataInterfaceException("Date Can't Be Null or Empty");
        }

        String query = "SELECT * FROM TEAM WHERE UPPER(CATEGORY_NAME) = UPPER(?) AND SEASON_YEAR = ?";
        java.util.Date utildate = date;
        java.sql.Date sqldate = new java.sql.Date(utildate.getTime());

        try {
            getseasoncatteamsStatement = con.prepareStatement(query);
            getseasoncatteamsStatement.setString(1, category);
            getseasoncatteamsStatement.setDate(2, sqldate);

            ResultSet rs = getseasoncatteamsStatement.executeQuery();
            return getTeams(rs);

        } catch (SQLException ex) {
            throw new EquipDataInterfaceException("Error retornando equipos de categoria " + category + " : " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(getseasoncatteamsStatement);
        }

    }

    @Override
    public Category getCategory(String category) {
        if (category.isBlank() || category == null) {
            throw new EquipDataInterfaceException("Category Can't Be Null or Empty");
        }

        String query = " select * from category where name = ?";
        Category category1 = null;
        ResultSet rs = null;
        try {
            getCategorypstmt = con.prepareStatement(query);
            getCategorypstmt.setString(1, category);
            rs = getCategorypstmt.executeQuery();

            if (rs.next()) {
                Integer id = rs.getInt("id");
                String name = rs.getString("name");
                Integer minage = rs.getInt("min_age");
                Integer maxage = rs.getInt("max_age");

                category1 = new Category(name, minage, maxage);
                category1.setId(id);
            }

        } catch (SQLException ex) {
            throw new EquipDataInterfaceException("Error Getting Category " + category + " : " + ex.getMessage(), ex.getCause());
        } finally {
            closeResultSet(rs);
            closePreparedStatement(getCategorypstmt);
        }

        return category1;
    }

    @Override
    public int getTeamMemCount(String team) {
        int count = 0;

        if (team.equals("") || team == null) {
            throw new EquipDataInterfaceException("Team Can't Be Null or Empty");
        }

        String query = "SELECT COUNT(*) AS PLAYERCOUNT FROM PLAYER P"
                + " JOIN PLAYERTEAM PT ON P.ID = PT.PLAYER"
                + " JOIN TEAM T ON PT.TEAM = T.ID"
                + " WHERE T.NAME = ?";

        try {
            getteammemcount = con.prepareStatement(query);
            getteammemcount.setString(1, team);

            ResultSet rs = getteammemcount.executeQuery();
            if (rs.next()) {
                count = rs.getInt("PLAYERCOUNT");
            } else {
                throw new EquipDataInterfaceException("Query Returned With Null");
            }
        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Error Trying To Retrieve COUNT of players of " + team + " " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(getteammemcount);
        }

        return count;
    }

    public List<Team> getTeamsBySeason(Date season) {
        if (season == null) {
            throw new EquipDataInterfaceException("Season Can't Be Null or Empty");
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(season);
        int year = calendar.get(Calendar.YEAR);

        String query = "SELECT * "
                + "FROM team "
                + "WHERE EXTRACT(YEAR FROM season_year) = ?";

        ResultSet rs = null;

        try {
            teamseason = con.prepareStatement(query);
            teamseason.setInt(1, year);

            rs = teamseason.executeQuery();
            return getTeams(rs);

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Error Getting Teams By Season " + season + "  " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(teamseason);
        }
    }

    public List<Team> getAllTeams() {
        String query = "Select * from team order by season_year asc, id asc";
        ResultSet rs = null;
        Statement stm = null;
        try {
            stm = con.createStatement();
            rs = stm.executeQuery(query);

            return getTeams(rs);

        } catch (SQLException ex) {
            throw new EquipDataInterfaceException("Error All Teams Teams " + ex.getMessage(), ex.getCause());
        } finally {
            closeStatement(stm);
        }

    }

    @Override
    public Team getTeamByName(String name, java.sql.Date date) {

        if (date == null) {
            throw new EquipDataInterfaceException("No Puedes Pasar Fecha Vacio O Nulo");
        }
        Team team = null;
        if (name.isBlank() || name == null) {
            throw new EquipDataInterfaceException("No Puedes Pasar Nombre Vacio O Nulo");
        }

        String query = "Select * from team where name = ? and season_year = ?";

        try {
            getteambynamepstmt = con.prepareStatement(query);
            getteambynamepstmt.setString(1, name);
            getteambynamepstmt.setDate(2, date);
            ResultSet rs = getteambynamepstmt.executeQuery();

            team = getTeam(rs);

        } catch (SQLException ex) {
            throw new EquipDataInterfaceException(ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(getteambynamepstmt);
        }

        return team;
    }

    @Override
    public void addNewTeam(String name, String teamtype,
            String cat_name, Date seasondate
    ) {
        if (name == null || name.isBlank() || cat_name == null || seasondate == null) {
            throw new EquipDataInterfaceException("Invalid Input For Creating A new Team");
        }

        String query = "Insert into team(name,team_type,category_name,season_year)"
                + " values (?,?,?,?)";

        try {
            newteam = con.prepareStatement(query);
            newteam.setString(1, name);
            newteam.setString(2, teamtype);
            newteam.setString(3, cat_name);

            java.util.Date utildate = seasondate;
            java.sql.Date sqldate = new java.sql.Date(utildate.getTime());
            newteam.setDate(4, sqldate);

            int result = newteam.executeUpdate();
            if (result == 1) {
                System.out.println("Team Added Successfully");
            } else {
                System.out.println("Failed To Add  Team");
            }

        } catch (SQLException ex) {
            throw new EquipDataInterfaceException("Error Making A New Team ", ex.getCause());
        } finally {
            closePreparedStatement(newteam);
        }

    }

    @Override
    public List<Player> getTeamPlayers(String teamName, java.sql.Date seasonyear
    ) {
        if (teamName == null) {
            throw new EquipDataInterfaceException("team name Can't Be Null or Empty");
        }
        String query = "SELECT p.id,p.name,p.surname,p.sex,p.birth_year,p.legal_id,p.iban,p.direcion,p.codigo_postal,p.localidad,p.provincia,p.pais,photo,p.medical_rev_fin"
                + " ,t.category_name,pt.tit_con "
                + "FROM PLAYERTEAM pt "
                + "JOIN PLAYER p ON pt.player = p.id "
                + "JOIN TEAM t ON pt.team = t.id "
                + "WHERE t.name = ? and pt.season = ?";

        try {
            teamPlyers = con.prepareStatement(query);
            teamPlyers.setString(1, teamName);
            teamPlyers.setDate(2, seasonyear);
            ResultSet rs = teamPlyers.executeQuery();

            return getPlayersconCat(rs);

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Error Getting Players Of Team " + teamName + "  " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(teamPlyers);
        }

    }

    @Override
    public void addPlayerToTeam(Player player, Team team,
            boolean titularidad
    ) {
        if (player == null || team == null) {
            throw new EquipDataInterfaceException("Player | Team  Can't Be Null or Empty");
        }
        String tit = null;

        if (titularidad) {
            tit = "T";
        } else {
            tit = "C";
        }
        String query = "Insert into playerteam (player,team,tit_con,season) values (?,?,?,?)";

        java.util.Date utildate = team.getSeason_year();
        java.sql.Date sqldate = new java.sql.Date(utildate.getTime());

        try {
            addplyteam = con.prepareStatement(query);
            addplyteam.setInt(1, player.getId());
            addplyteam.setInt(2, team.getId());
            addplyteam.setString(3, tit);
            addplyteam.setDate(4, sqldate);

            int ans = addplyteam.executeUpdate();
            if (ans == 1) {
                System.out.println("Player Added Successfuly To Team");
            } else if (ans != 1) {
                System.out.println("Could Not Add Player To Team");
            }

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Error Adding Player To Team " + team.getName() + " " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(addplyteam);
        }
    }

    @Override
    public void deletePlayerFromTeam(Player player, Integer team
    ) {
        if (player == null) {
            throw new EquipDataInterfaceException("Player Can't Be Null or Empty");
        }

        String query = "delete from playerteam where player = ? and team = ?";

        try {
            delfromplyteam = con.prepareStatement(query);
            delfromplyteam.setInt(1, player.getId());
            delfromplyteam.setInt(2, team.intValue());

            int ans = delfromplyteam.executeUpdate();
            if (ans == 1) {
                System.out.println("Player deleted from team successfully");
            } else if (ans == 0) {
                System.out.println("Can't Delete player From Specified Team");
            }

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Error Deleting Player Of Team " + team + " " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(delfromplyteam);
        }

    }

    @Override
    public void deleteTeam(String name
    ) {
        if (name == null) {
            throw new EquipDataInterfaceException("The Name Field Can't Be Null");
        }

        String query = "DELETE FROM TEAM WHERE NAME = ?";

        try {
            delteam = con.prepareStatement(query);
            delteam.setString(1, name);
            int results = delteam.executeUpdate();

            if (results > 0) {
                System.out.println("Team with name " + name + " has been successfully deleted.");
            } else {
                throw new EquipDataInterfaceException("No player found with legal ID " + name);
            }

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Somthing Went Wrong Trying To Create The Delete Statement " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(delteam);
        }

    }

    @Override
    public void addNewSeason(String season_n, Date date
    ) {

        if (season_n == null || season_n.isEmpty()) {
            throw new EquipDataInterfaceException("Season name can't be null or empty.");
        }
        if (date == null) {
            throw new EquipDataInterfaceException("Season date can't be null.");
        }

        String query = "INSERT INTO SEASON (season_name,season_year) VALUES (?,?)";

        try {
            java.util.Date utildate = date;
            java.sql.Date sqldate = new java.sql.Date(utildate.getTime());

            newSeasonDate = con.prepareStatement(query);
            newSeasonDate.setString(1, season_n);
            newSeasonDate.setDate(2, sqldate);

            newSeasonDate.executeUpdate();
            System.out.println("New season " + season_n + " added successfully with date " + date);
        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Error adding new season " + season_n + " " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(newSeasonDate);
        }
    }

    @Override
    public void removeTeamFromSeason(String t_name
    ) {
        if (t_name == null || t_name.isEmpty()) {
            throw new EquipDataInterfaceException("Team name can't be null or empty.");
        }

        String query = "Delete from team where name = ?";

        try {

            removeteam = con.prepareCall(query);
            removeteam.setString(1, t_name);
            ResultSet rs = removeteam.executeQuery();
            if (rs.next()) {
                System.out.println("Team " + t_name + " Removed Successfuly");
            } else {
                System.out.println("Team " + t_name + " Wasn't Removed");
            }

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Error removing team " + t_name + "  " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(removeteam);
        }
    }

    @Override
    public void removeTeamWithPlayers(Team team
    ) {
        if (team == null) {
            throw new EquipDataInterfaceException("Team Can't be Null");
        }

        boolean ans = removePlayers(team);
        if (ans) {
            String query = "Delete from team where name = ?";
            try {

                rmveteamswithPlayers = con.prepareCall(query);
                rmveteamswithPlayers.setString(1, team.getName());
                int rs = rmveteamswithPlayers.executeUpdate();
                if (rs == 1) {
                    System.out.println("Team " + team.getName() + " Removed Successfuly");
                } else {
                    System.out.println("Team " + team.getName() + " Wasn't Removed");
                }

            } catch (SQLException ex) {

                throw new EquipDataInterfaceException("Error removing team " + team.getName() + " " + ex.getMessage(), ex.getCause());
            } finally {
                closePreparedStatement(rmveteamswithPlayers);
            }

        } else {
            System.out.println("Could Not Delete Team With Players");
        }

    }

    private boolean removePlayers(Team team) {
        if (team == null) {
            throw new EquipDataInterfaceException("Team Can't be Null");
        }
        boolean status = false;
        String query = "Delete from playerteam where team = ?";

        try {
            rmveplayers = con.prepareStatement(query);
            rmveplayers.setInt(1, team.getId());
            int ans = rmveplayers.executeUpdate();

            if (ans >= 0) {
                status = true;
                System.out.println("Correctly Removed All Players From Team");
            }

        } catch (SQLException ex) {

            throw new EquipDataInterfaceException("Error removing players from team " + team.getName() + " " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(rmveplayers);
        }
        return status;
    }

    @Override
    public List<Player> getPlayers() {
        String query = "select * from player";
        Statement stm = null;
        try {
            stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);

            return getPlayers(rs);

        } catch (SQLException ex) {
            throw new EquipDataInterfaceException("Unable To Create Statement " + ex.getMessage(), ex.getCause());
        } finally {
            closeStatement(stm);
        }

    }

    public List<Season> getSeasons() {
        String query = "Select * from season order by season_year DESC";
        List<Season> seasons = new ArrayList<>();
        Statement stm = null;
        try {
            stm = con.createStatement();
            ResultSet rs = stm.executeQuery(query);

            while (rs.next()) {
                String seasonname = rs.getString("season_name");
                Date seasondate = rs.getDate("season_year");
                String seasontxtform = sdf.format(seasondate);

                if (seasonname == null) {
                    seasonname = seasontxtform;
                }
                Season season = new Season(seasonname, seasondate);
                seasons.add(season);
            }

        } catch (SQLException ex) {
            throw new EquipDataInterfaceException("Unable To Create Statement " + ex.getMessage(), ex.getCause());
        } finally {
            closeStatement(stm);
        }
        return seasons;
    }

    @Override
    public boolean checkPlayerBelongsToTeam(String legalID) {
        boolean ans = false;
        if (legalID == null || legalID.isBlank()) {
            throw new EquipDataInterfaceException("Player Id Can't Be Null Or Empty");
        }

        String query = "SELECT P.NAME FROM PLAYER P"
                + " JOIN PLAYERTEAM PT ON P.ID = PT.PLAYER WHERE P.LEGAL_ID = ?";

        try {
            checkplayer = con.prepareStatement(query);
            checkplayer.setString(1, legalID);
            ResultSet rs = checkplayer.executeQuery();

            if (rs.next()) {
                ans = true;

            }

        } catch (SQLException ex) {
            throw new EquipDataInterfaceException("Could Not Verify If Player Belongs To A Team " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(checkplayer);
        }

        return ans;
    }

    @Override
    public Integer checkPlayerTeam(String legalID) {
        Integer ans = null;
        if (legalID == null || legalID.isBlank()) {
            throw new EquipDataInterfaceException("Player Id Can't Be Null Or Empty");
        }

        String query = "SELECT T.ID FROM TEAM T JOIN PLAYERTEAM PT ON T.ID = PT.TEAM"
                + " JOIN PLAYER P ON P.ID = PT.PLAYER WHERE P.LEGAL_ID = ?";

        try {
            checkplayerteam = con.prepareStatement(query);
            checkplayerteam.setString(1, legalID);
            ResultSet rs = checkplayerteam.executeQuery();

            while (rs.next()) {
                ans = rs.getInt(1);
                if (rs.wasNull()) {
                    ans = null;
                }
            }

        } catch (SQLException ex) {
            throw new EquipDataInterfaceException("Can't Get Player Team " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(checkplayerteam);
        }

        return ans;
    }

    @Override
    public Team getPlayerTeam(String legalID) {
        if (legalID == null || legalID.isBlank()) {
            throw new EquipDataInterfaceException("Player Id Can't Be Null Or Empty");
        }

        String query = "SELECT t.id, t.name, t.team_type, t.category_name, t.season_year "
                + "FROM TEAM t "
                + "JOIN PLAYERTEAM pt ON t.ID = pt.TEAM "
                + "JOIN PLAYER p ON p.ID = pt.PLAYER "
                + "WHERE p.LEGAL_ID = ?";
        Team team = null;
        try {
            playerteamget = con.prepareStatement(query);
            playerteamget.setString(1, legalID);
            ResultSet rs = playerteamget.executeQuery();

            team = getTeam(rs);
        } catch (SQLException ex) {
            throw new EquipDataInterfaceException("Can't Get Player Team " + ex.getMessage(), ex.getCause());
        } finally {
            closePreparedStatement(playerteamget);
        }
        return team;
    }

    @Override
    public List<Player> playerFilterSearch(String cognom, String legalid, Date birthdate, String categoria, String order) {
        List<Player> players = new ArrayList<>();
        StringBuilder query = new StringBuilder(
                "SELECT P.LEGAL_ID, P.NAME, P.SURNAME, P.BIRTH_YEAR, P.SEX, T.CATEGORY_NAME, P.LOCALIDAD, P.MEDICAL_REV_FIN "
                + "FROM PLAYERTEAM PT "
                + "JOIN PLAYER P ON P.ID = PT.PLAYER "
                + "JOIN TEAM T ON T.ID = PT.TEAM WHERE 1=1 ");

        if (cognom != null) {
            query.append("AND P.SURNAME = ? ");
        }
        if (legalid != null) {
            query.append("AND P.LEGAL_ID = ? ");
        }
        if (birthdate != null) {
            query.append("AND P.BIRTH_YEAR = ? ");
        }
        if (categoria != null) {
            query.append("AND T.CATEGORY_NAME = ? ");
        }

        if (order != null && !order.isEmpty()) {
            if (!order.matches("^[a-zA-Z_]+$")) {
                throw new IllegalArgumentException("Invalid order column: " + order);
            }
            query.append("ORDER BY P.").append(order);
        }

        try {
            filterplayerspstmt = con.prepareStatement(query.toString());

            int paramIndex = 1;
            if (cognom != null) {
                filterplayerspstmt.setString(paramIndex++, cognom);
            }
            if (legalid != null) {
                filterplayerspstmt.setString(paramIndex++, legalid);
            }
            if (birthdate != null) {
                filterplayerspstmt.setDate(paramIndex++, new java.sql.Date(birthdate.getTime()));
            }
            if (categoria != null) {
                filterplayerspstmt.setString(paramIndex++, categoria);
            }

            try (ResultSet rs = filterplayerspstmt.executeQuery()) {
                while (rs.next()) {
                    Player player = new Player();
                    player.setLegal_id(rs.getString("legal_id"));
                    player.setName(rs.getString("name"));
                    player.setSurname(rs.getString("surname"));
                    player.setBirth_year(rs.getDate("birth_year"));
                    player.setSex(rs.getString("sex"));
                    player.setCategory(rs.getString("category_name"));
                    player.setLocalidad(rs.getString("localidad"));
                    player.setMedical_rev_fin(rs.getInt("medical_rev_fin"));

                    players.add(player);
                }
            }
        } catch (SQLException ex) {
            throw new EquipDataInterfaceException("Unable to retrieve players: " + ex.getMessage(), ex);
        } finally {
            closePreparedStatement(filterplayerspstmt);
        }

        return players;
    }

    private static int calculateAge(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }

        LocalDate birthLocalDate;
        if (birthDate instanceof java.sql.Date) {
            birthLocalDate = ((java.sql.Date) birthDate).toLocalDate();
        } else {
            birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        LocalDate referenceDate = LocalDate.of(2024, 9, 1);

        return Period.between(birthLocalDate, referenceDate).getYears();
    }
}
