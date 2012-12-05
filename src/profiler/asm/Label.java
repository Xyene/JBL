// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


// Referenced classes of package profiler.asm:
//            ByteVector, Edge, MethodWriter

public class Label
{

    public Label()
    {
    }

    public int getOffset()
    {
        if(!a)
            throw new IllegalStateException("Label offset position has not been resolved yet");
        else
            return b;
    }

    void a(MethodWriter methodwriter, ByteVector bytevector, int l, boolean flag)
    {
        if(a)
        {
            if(flag)
                bytevector.putInt(b - l);
            else
                bytevector.putShort(b - l);
        } else
        if(flag)
        {
            a(-1 - l, bytevector.b);
            bytevector.putInt(-1);
        } else
        {
            a(l, bytevector.b);
            bytevector.putShort(-1);
        }
    }

    private void a(int l, int i1)
    {
        if(e == null)
            e = new int[6];
        if(d >= e.length)
        {
            int ai[] = new int[e.length + 6];
            System.arraycopy(e, 0, ai, 0, e.length);
            e = ai;
        }
        e[d++] = l;
        e[d++] = i1;
    }

    boolean a(MethodWriter methodwriter, int l, byte abyte0[])
    {
        boolean flag = false;
        a = true;
        b = l;
        for(int i1 = 0; i1 < d;)
        {
            int j1 = e[i1++];
            int k1 = e[i1++];
            if(j1 >= 0)
            {
                int l1 = l - j1;
                if(l1 < -32768 || l1 > 32767)
                {
                    int j2 = abyte0[k1 - 1] & 0xff;
                    if(j2 <= 168)
                        abyte0[k1 - 1] = (byte)(j2 + 49);
                    else
                        abyte0[k1 - 1] = (byte)(j2 + 20);
                    flag = true;
                }
                abyte0[k1++] = (byte)(l1 >>> 8);
                abyte0[k1] = (byte)l1;
            } else
            {
                int i2 = l + j1 + 1;
                abyte0[k1++] = (byte)(i2 >>> 24);
                abyte0[k1++] = (byte)(i2 >>> 16);
                abyte0[k1++] = (byte)(i2 >>> 8);
                abyte0[k1] = (byte)i2;
            }
        }

        return flag;
    }

    public String toString()
    {
        return "L" + System.identityHashCode(this);
    }

    int k;
    boolean a;
    int b;
    boolean c;
    private int d;
    private int e[];
    int f;
    int g;
    Edge h;
    Label i;
    boolean j;
}
