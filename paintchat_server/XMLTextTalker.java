package paintchat_server;

import java.io.IOException;
import paintchat.MgText;
import paintchat.Res;
import paintchat.debug.DebugListener;
import syi.util.ByteInputStream;
import syi.util.Vector2;

// Referenced classes of package paintchat_server:
//            XMLTalker, TextTalkerListener, TextServer

public class XMLTextTalker extends XMLTalker
    implements TextTalkerListener
{

    private Vector2 send_text;
    private Vector2 send_update;
    private ByteInputStream input;
    private TextServer server;
    private DebugListener debug;
    private MgText mgName;
    private boolean isOnlyUserList;
    private boolean isGuest;
    private boolean isKill;
    private int countSpeak;

    public XMLTextTalker(TextServer textserver, DebugListener debuglistener)
    {
        send_text = new Vector2();
        send_update = new Vector2();
        input = new ByteInputStream();
        isOnlyUserList = false;
        isGuest = false;
        isKill = false;
        countSpeak = 0;
        server = textserver;
        debug = debuglistener;
    }

    protected void mInit()
        throws IOException
    {
        Res res = getStatus();
        mgName = new MgText(0, (byte)1, getStatus().get("name"));
        isOnlyUserList = res.getBool("user_list_only", false);
        isGuest = res.getBool("guest", false);
        server.addTalker(this);
    }

    protected void mDestroy()
    {
    }

    protected void mRead(String s, String s1, Res res)
        throws IOException
    {
        if(mgName == null)
        {
            return;
        }
        byte byte0 = (byte)strToHint(s);
        MgText mgtext = new MgText();
        mgtext.setData(mgName.ID, byte0, toEscape(s1));
        switch(byte0)
        {
        default:
            if(isGuest)
            {
                String s2 = server.getPassword();
                if(s2 == null || s2.length() <= 0 || !getStatus().get("password").equals(s2))
                {
                    break;
                }
            }
            server.addText(this, mgtext);
            break;

        case 102: // 'f'
            try
            {
                if(s1.indexOf("get:infomation") >= 0)
                {
                    send(server.getInfomation());
                    break;
                }
                int i = res.getInt("id", 0);
                if(i > 0)
                {
                    server.doAdmin(s1, i);
                }
            }
            catch(RuntimeException runtimeexception)
            {
                debug.log(runtimeexception);
            }
            break;

        case 2: // '\002'
            server.removeTalker(this);
            break;
        }
    }

    protected void mWrite()
        throws IOException
    {
        if(send_update != null)
        {
            int i = send_update.size();
            if(i > 0)
            {
                StringBuffer stringbuffer = new StringBuffer();
                for(int l = 0; l < i; l++)
                {
                    MgText mgtext = (MgText)send_update.get(l);
                    String s = hintToString(mgtext.head);
                    stringbuffer.append('<');
                    stringbuffer.append(s);
                    stringbuffer.append(" name=\"");
                    stringbuffer.append(toEscape(mgtext.getUserName()));
                    stringbuffer.append("\">");
                    stringbuffer.append(toEscape(mgtext.toString()));
                    stringbuffer.append("</");
                    stringbuffer.append(s);
                    stringbuffer.append('>');
                }

                write("update", stringbuffer.toString());
                send_update.removeAll();
            }
            send_update = null;
        }
        int j = send_text.size();
        if(j <= 0)
        {
            return;
        }
        for(int k = 0; k < j; k++)
        {
            MgText mgtext1 = (MgText)send_text.get(k);
            write(hintToString(mgtext1.head), toEscape(mgtext1.toString()), "id=\"" + mgtext1.ID + "\" name=\"" + toEscape(mgtext1.getUserName()) + "\"");
        }

        send_text.remove(j);
    }

    public void send(MgText mgtext)
    {
        if(!isValidate())
        {
            return;
        }
        if(isOnlyUserList && mgtext.head != 1 && mgtext.head != 2)
        {
            return;
        } else
        {
            send_text.add(mgtext);
            return;
        }
    }

    public MgText getHandleName()
    {
        return mgName;
    }

    public void sendUpdate(Vector2 vector2)
    {
        vector2.copy(send_text);
    }

    public boolean isGuest()
    {
        return isGuest;
    }

    private String toEscape(String s)
    {
        if(s.indexOf('<') >= 0 || s.indexOf('>') >= 0 || s.indexOf('&') >= 0 || s.indexOf('"') >= 0 || s.indexOf('"') >= 0)
        {
            StringBuffer stringbuffer = new StringBuffer();
            for(int i = 0; i < s.length(); i++)
            {
                char c = s.charAt(i);
                switch(c)
                {
                case 60: // '<'
                    stringbuffer.append("&lt;");
                    break;

                case 62: // '>'
                    stringbuffer.append("&gt;");
                    break;

                case 38: // '&'
                    stringbuffer.append("&amp;");
                    break;

                case 34: // '"'
                    stringbuffer.append("&qout;");
                    break;

                default:
                    stringbuffer.append(c);
                    break;
                }
            }

            return stringbuffer.toString();
        } else
        {
            return s;
        }
    }

    private boolean equalsPassword()
    {
        String s = server.getPassword();
        return s == null || s.length() <= 0 || !getStatus().get("password").equals(s);
    }

    public void kill()
    {
        isKill = true;
    }

    public int getSpeakCount()
    {
        return countSpeak;
    }
}
