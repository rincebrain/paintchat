package paintchat_client;

import paintchat.M;

public interface IMi
{

    public abstract void changeSize();

    public abstract void scroll(boolean flag, int i, int j);

    public abstract void send(M m);

    public abstract void setARGB(int i);

    public abstract void setLineSize(int i);

    public abstract void undo(boolean flag);
}
