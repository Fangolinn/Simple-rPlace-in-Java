package pl.wit.kowalcj1.java_place;

public interface FrontendAPI {
    void setConnectionStatus(String status);

    void updateCell(CellInfo cellInfo);
}
