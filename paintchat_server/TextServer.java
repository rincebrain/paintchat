package paintchat_server;

import java.io.*;
import java.net.InetAddress;
import paintchat.Config;
import paintchat.MgText;
import paintchat.debug.DebugListener;
import syi.util.PProperties;
import syi.util.Vector2;

// Referenced classes of package paintchat_server:
//            TextTalkerListener, XMLTalker, ChatLogOutputStream, Server

public class TextServer
{

    private boolean isLive;
    private TextTalkerListener talkers[];
    private int countTalker;
    private XMLTalker xmlTalkers[];
    private int countXMLTalker;
    private ChatLogOutputStream outLog;
    private DebugListener debug;
    private Config config;
    private String strPassword;
    private Vector2 mgtexts;
    private boolean isCash;
    private int iMaxCash;
    public Vector2 vKillIP;
    private int iUniqueLast;
    private Server server;

    public TextServer()
    {
        isLive = true;
        talkers = new TextTalkerListener[10];
        countTalker = 0;
        xmlTalkers = new XMLTalker[10];
        countXMLTalker = 0;
        mgtexts = new Vector2();
        isCash = true;
        vKillIP = new Vector2();
        iUniqueLast = 1;
    }

    public void mInit(Server server1, Config config1, DebugListener debuglistener)
    {
        if(!isLive)
        {
            return;
        }
        server = server1;
        config = config1;
        debug = debuglistener;
        strPassword = config1.getString("Admin_Password", null);
        boolean flag = config1.getBool("Server_Log_Text", false);
        isCash = config1.getBool("Server_Cash_Text", true);
        File file = new File(config1.getString("Server_Log_Text_Dir", "save_server"));
        outLog = new ChatLogOutputStream(file, debuglistener, flag);
        iMaxCash = Math.max(config1.getInt("Server_Cash_Text_Size", 128), 5);
        if(config1.getBool("Server_Load_Text", false))
        {
            outLog.loadLog(mgtexts, iMaxCash, true);
        }
    }

    public synchronized void mStop()
    {
        if(!isLive)
        {
            return;
        }
        isLive = false;
        for(int i = 0; i < countTalker; i++)
        {
            TextTalkerListener texttalkerlistener = talkers[i];
            if(texttalkerlistener != null)
            {
                texttalkerlistener.mStop();
            }
        }

        if(outLog != null)
        {
            outLog.close();
        }
        if(mgtexts.size() > 0)
        {
            try
            {
                BufferedWriter bufferedwriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outLog.getTmpFile()), "UTF8"));
                int j = mgtexts.size();
                for(int k = 0; k < j; k++)
                {
                    MgText mgtext = (MgText)mgtexts.get(k);
                    switch(mgtext.head)
                    {
                    case 1: // '\001'
                        bufferedwriter.write("Login name=" + mgtext.toString());
                        break;

                    case 2: // '\002'
                        bufferedwriter.write("Logout name=" + mgtext.getUserName());
                        break;

                    default:
                        bufferedwriter.write(mgtext.getUserName() + '>' + mgtext.toString());
                        break;
                    }
                    bufferedwriter.newLine();
                }

                bufferedwriter.flush();
                bufferedwriter.close();
            }
            catch(IOException _ex) { }
        }
    }

    public synchronized void addTalker(TextTalkerListener texttalkerlistener)
    {
        if(!isLive)
        {
            return;
        }
        MgText mgtext = texttalkerlistener.getHandleName();
        String s = mgtext.toString();
        if(s.length() <= 0 || !texttalkerlistener.isValidate())
        {
            texttalkerlistener.mStop();
            return;
        }
        mgtext.ID = getUniqueID();
        String s1 = getUniqueName(s);
        if(s != s1)
        {
            mgtext.setData(mgtext.ID, mgtext.head, getUniqueName(s));
            s = mgtext.toString();
            mgtext.setUserName(s);
        }
        mgtext.toBin(false);
        int i = countTalker;
        for(int j = 0; j < i; j++)
        {
            if(!talkers[j].isGuest())
            {
                texttalkerlistener.send(talkers[j].getHandleName());
            }
        }

        if(isCash)
        {
            texttalkerlistener.sendUpdate(mgtexts);
        }
        if(!texttalkerlistener.isGuest())
        {
            addText(texttalkerlistener, new MgText(mgtext.ID, (byte)1, s));
        }
        if(countTalker + 1 >= talkers.length)
        {
            TextTalkerListener atexttalkerlistener[] = new TextTalkerListener[talkers.length + 1];
            System.arraycopy(talkers, 0, atexttalkerlistener, 0, talkers.length);
            talkers = atexttalkerlistener;
        }
        talkers[countTalker] = texttalkerlistener;
        countTalker++;
    }

    public void doAdmin(String s, int i)
    {
        if(s.indexOf("kill") >= 0)
        {
            killTalker(i);
        } else
        {
            TextTalkerListener texttalkerlistener = getTalker(i);
            texttalkerlistener.send(new MgText(i, (byte)102, s));
        }
    }

    public synchronized void removeTalker(TextTalkerListener texttalkerlistener)
    {
        if(!isLive)
        {
            return;
        }
        boolean flag = texttalkerlistener.isGuest();
        texttalkerlistener.mStop();
        MgText mgtext = new MgText();
        mgtext.setData(texttalkerlistener.getHandleName());
        mgtext.head = 2;
        mgtext.toBin(false);
        int i = countTalker;
        for(int j = 0; j < i; j++)
        {
            if(talkers[j] == texttalkerlistener)
            {
                talkers[j] = null;
                if(j + 1 < i)
                {
                    System.arraycopy(talkers, j + 1, talkers, j, i - (j + 1));
                    talkers[i - 1] = null;
                }
                countTalker--;
                i--;
                j--;
            } else
            if(!flag)
            {
                talkers[j].send(mgtext);
            }
        }

    }

    public synchronized void killTalker(int i)
    {
        TextTalkerListener texttalkerlistener = getTalker(i);
        if(texttalkerlistener == null)
        {
            return;
        } else
        {
            MgText mgtext = texttalkerlistener.getHandleName();
            vKillIP.add(texttalkerlistener.getAddress());
            texttalkerlistener.kill();
            addText(texttalkerlistener, new MgText(0, (byte)6, "kill done name=+" + mgtext.toString() + " address=" + texttalkerlistener.getAddress()));
            return;
        }
    }

    public synchronized void addText(TextTalkerListener texttalkerlistener, MgText mgtext)
    {
        if(!isLive)
        {
            return;
        }
        for(int i = 0; i < countTalker; i++)
        {
            TextTalkerListener texttalkerlistener1 = talkers[i];
            if(texttalkerlistener1 != texttalkerlistener && texttalkerlistener1 != null)
            {
                if(texttalkerlistener1.isValidate())
                {
                    texttalkerlistener1.send(mgtext);
                } else
                {
                    removeTalker(texttalkerlistener1);
                    i--;
                }
            }
        }

        if(isCash)
        {
            mgtexts.add(mgtext);
            if(mgtexts.size() >= iMaxCash)
            {
                mgtexts.remove(5);
            }
        }
        if(outLog != null)
        {
            outLog.write(mgtext);
        }
    }

    public int getUserCount()
    {
        return countTalker;
    }

    public void clearKillIP()
    {
        vKillIP.removeAll();
    }

    public synchronized void clearDeadTalker()
    {
        for(int i = 0; i < countTalker; i++)
        {
            TextTalkerListener texttalkerlistener = talkers[i];
            if(texttalkerlistener == null || !texttalkerlistener.isValidate())
            {
                if(texttalkerlistener != null)
                {
                    removeTalker(texttalkerlistener);
                } else
                {
                    if(i < countTalker - 1)
                    {
                        System.arraycopy(talkers, i + 1, talkers, i, countTalker - (i + 1));
                    }
                    countTalker--;
                }
                i = -1;
            }
        }

    }

    public synchronized TextTalkerListener getTalker(InetAddress inetaddress)
    {
        if(inetaddress == null)
        {
            return null;
        }
        for(int i = 0; i < countTalker; i++)
        {
            TextTalkerListener texttalkerlistener = talkers[i];
            if(texttalkerlistener != null && texttalkerlistener.getAddress().equals(inetaddress))
            {
                return texttalkerlistener;
            }
        }

        return null;
    }

    public synchronized TextTalkerListener getTalker(int i)
    {
        if(i <= 0)
        {
            return null;
        }
        for(int j = 0; j < countTalker; j++)
        {
            TextTalkerListener texttalkerlistener = talkers[j];
            if(texttalkerlistener != null && texttalkerlistener.getHandleName().ID == i)
            {
                return texttalkerlistener;
            }
        }

        return null;
    }

    public synchronized TextTalkerListener getTalkerAt(int i)
    {
        if(i < 0 || i >= countTalker)
        {
            return null;
        } else
        {
            return talkers[i];
        }
    }

    private int getUniqueID()
    {
        int i = iUniqueLast;
        TextTalkerListener texttalkerlistener;
        for(int j = 0; j < countTalker;)
        {
            if((texttalkerlistener = talkers[j++]) != null && texttalkerlistener.getHandleName().ID == i)
            {
                if(++i >= 65535)
                {
                    i = 1;
                }
                j = 0;
            }
        }

        iUniqueLast = i < 65535 ? i + 1 : 1;
        return i;
    }

    private String getUniqueName(String s)
    {
        int i = 2;
        String s1 = s;
        TextTalkerListener texttalkerlistener;
        for(int j = 0; j < countTalker;)
        {
            if((texttalkerlistener = talkers[j++]) != null && s1.equals(texttalkerlistener.getHandleName().toString()))
            {
                s1 = s + i;
                i++;
                j = 0;
            }
        }

        return s1;
    }

    public String getPassword()
    {
        return strPassword;
    }

    public synchronized void getUserListXML(StringBuffer stringbuffer)
    {
        int i = countTalker;
        for(int j = 0; j < i; j++)
        {
            TextTalkerListener texttalkerlistener = talkers[j];
            if(texttalkerlistener != null && !texttalkerlistener.isGuest())
            {
                MgText mgtext = texttalkerlistener.getHandleName();
                String s = mgtext.toString();
                if(s != null && s.length() > 0)
                {
                    stringbuffer.append("<in id=\"" + mgtext.ID + "\">" + s + "</in>");
                }
            }
        }

    }

    public synchronized MgText getInfomation()
    {
        return server.getInfomation();
    }

    public void writeLog(String s)
    {
        if(outLog != null)
        {
            outLog.write(s);
        }
    }
}
