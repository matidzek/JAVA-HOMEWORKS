package zad1;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;


public class Database {

    private final String url;
    private final TravelData travelData;

    private static final String[] LOCALES = {"pl_PL", "en_GB", "de_DE"};
    private static final String[] DATE_PATTERNS = {"yyyy-MM-dd", "d MMMM yyyy", "EEEE, d MMMM yyyy"};

    public Database(String url, TravelData travelData) {
        this.url = url;
        this.travelData = travelData;
    }

    public void create() {
        try (Connection con = DriverManager.getConnection(url);
             Statement st = con.createStatement()) {

            try { st.executeUpdate("DROP TABLE OFFERS"); }
            catch (SQLException ignore) { /* tabela jeszcze nie istnieje */ }

            st.executeUpdate(
                    "CREATE TABLE OFFERS (" +
                            "  ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY," +
                            "  SRCLOC   VARCHAR(20)," +
                            "  COUNTRY  VARCHAR(120)," +
                            "  DEP      VARCHAR(20)," +
                            "  RET      VARCHAR(20)," +
                            "  PLACE    VARCHAR(60)," +
                            "  PRICE    VARCHAR(40)," +
                            "  CURRENCY VARCHAR(10)," +
                            "  PRIMARY KEY(ID))");

            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO OFFERS (SRCLOC,COUNTRY,DEP,RET,PLACE,PRICE,CURRENCY)" +
                            " VALUES (?,?,?,?,?,?,?)")) {
                for (String[] o : travelData.getOffers()) {
                    for (int i = 0; i < 7; i++) ps.setString(i + 1, o[i]);
                    ps.executeUpdate();
                }
            }
            System.out.println("Baza danych utworzona, wstawiono ofert: " + travelData.getOffers().size());
        } catch (SQLException exc) {
            throw new RuntimeException("Blad bazy danych: " + exc.getMessage(), exc);
        }
    }

    private List<String[]> loadRawOffers() {
        List<String[]> rows = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(url);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT SRCLOC,COUNTRY,DEP,RET,PLACE,PRICE,CURRENCY FROM OFFERS ORDER BY ID")) {
            while (rs.next()) {
                String[] r = new String[7];
                for (int i = 0; i < 7; i++) r[i] = rs.getString(i + 1);
                rows.add(r);
            }
        } catch (SQLException exc) {
            throw new RuntimeException("Blad odczytu z bazy: " + exc.getMessage(), exc);
        }
        return rows;
    }

    public void showGui() {
        final List<String[]> raw = loadRawOffers();
        SwingUtilities.invokeLater(() -> buildGui(raw));
    }

    private void buildGui(List<String[]> raw) {
        final JComboBox<String> locBox = new JComboBox<>(LOCALES);
        final JComboBox<String> fmtBox = new JComboBox<>(DATE_PATTERNS);
        final DefaultTableModel model = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        final JTable table = new JTable(model);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.setRowHeight(24);

        Runnable refresh = () -> fillModel(model, raw,
                (String) locBox.getSelectedItem(), (String) fmtBox.getSelectedItem());
        locBox.addActionListener(e -> refresh.run());
        fmtBox.addActionListener(e -> refresh.run());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Język / region:")); top.add(locBox);
        top.add(new JLabel("   Format daty:")); top.add(fmtBox);

        JFrame f = new JFrame();
        f.setLayout(new BorderLayout());
        f.add(top, BorderLayout.NORTH);
        f.add(new JScrollPane(table), BorderLayout.CENTER);
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        refresh.run();
        f.setTitle(title((String) locBox.getSelectedItem()));
        f.setSize(720, 320);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void fillModel(DefaultTableModel model, List<String[]> raw, String loc, String pattern) {
        model.setColumnIdentifiers(headers(loc));
        model.setRowCount(0);
        for (String[] r : raw) {
            model.addRow(travelData.localizeFields(r, loc, pattern));
        }
    }

    private static String[] headers(String loc) {
        String lang = TravelData.toLocale(loc).getLanguage();
        if (lang.equals("pl")) return new String[]{"Kraj", "Wyjazd", "Powrót", "Miejsce", "Cena", "Waluta"};
        if (lang.equals("de")) return new String[]{"Land", "Abreise", "Rückkehr", "Ort", "Preis", "Währung"};
        return new String[]{"Country", "Departure", "Return", "Place", "Price", "Currency"};
    }

    private static String title(String loc) {
        String lang = TravelData.toLocale(loc).getLanguage();
        if (lang.equals("pl")) return "Oferty wycieczek";
        if (lang.equals("de")) return "Reiseangebote";
        return "Travel offers";
    }
}