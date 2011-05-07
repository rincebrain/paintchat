package paintchat_client;

import paintchat.M;

public abstract interface IMi
{
  public abstract void changeSize();

  public abstract void scroll(boolean paramBoolean, int paramInt1, int paramInt2);

  public abstract void send(M paramM);

  public abstract void setARGB(int paramInt);

  public abstract void setLineSize(int paramInt);

  public abstract void undo(boolean paramBoolean);
}

/* Location:           /home/rich/paintchat/paintchat/reveng/
 * Qualified Name:     paintchat_client.IMi
 * JD-Core Version:    0.6.0
 */