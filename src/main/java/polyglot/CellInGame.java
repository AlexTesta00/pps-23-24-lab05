package polyglot;

public class CellInGame{
    private final boolean isReveald;
    private final boolean isFlagged;
    private final boolean isMine;
    private final int minesNear;

    public CellInGame(boolean isReveald, boolean isFlagged, boolean isMine, int minesNear){
        this.isReveald = isReveald;
        this.isFlagged = isFlagged;
        this.isMine = isMine;
        this.minesNear = minesNear;
    }

    public boolean isReveald(){
        return this.isReveald;
    }

    public boolean isFlagged(){
        return this.isFlagged;
    }

    public boolean isMine(){
        return this.isMine;
    }

    public int minesNear(){
        return this.minesNear;
    }

}
