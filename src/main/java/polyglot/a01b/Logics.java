package polyglot.a01b;

import polyglot.CellInGame;

public interface Logics {

    void hit(int x, int y);
    void flagCell(int x, int y);
    CellInGame getCell(int x, int y);
    boolean won();
    boolean lost();

}
