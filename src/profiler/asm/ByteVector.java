// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


public class ByteVector
{

    public ByteVector()
    {
        a = new byte[64];
    }

    public ByteVector(int i)
    {
        a = new byte[i];
    }

    public ByteVector putByte(int i)
    {
        int j = b;
        if(j + 1 > a.length)
            a(1);
        a[j++] = (byte)i;
        b = j;
        return this;
    }

    ByteVector a(int i, int j)
    {
        int k = b;
        if(k + 2 > a.length)
            a(2);
        byte abyte0[] = a;
        abyte0[k++] = (byte)i;
        abyte0[k++] = (byte)j;
        b = k;
        return this;
    }

    public ByteVector putShort(int i)
    {
        int j = b;
        if(j + 2 > a.length)
            a(2);
        byte abyte0[] = a;
        abyte0[j++] = (byte)(i >>> 8);
        abyte0[j++] = (byte)i;
        b = j;
        return this;
    }

    ByteVector b(int i, int j)
    {
        int k = b;
        if(k + 3 > a.length)
            a(3);
        byte abyte0[] = a;
        abyte0[k++] = (byte)i;
        abyte0[k++] = (byte)(j >>> 8);
        abyte0[k++] = (byte)j;
        b = k;
        return this;
    }

    public ByteVector putInt(int i)
    {
        int j = b;
        if(j + 4 > a.length)
            a(4);
        byte abyte0[] = a;
        abyte0[j++] = (byte)(i >>> 24);
        abyte0[j++] = (byte)(i >>> 16);
        abyte0[j++] = (byte)(i >>> 8);
        abyte0[j++] = (byte)i;
        b = j;
        return this;
    }

    public ByteVector putLong(long l)
    {
        int i = b;
        if(i + 8 > a.length)
            a(8);
        byte abyte0[] = a;
        int j = (int)(l >>> 32);
        abyte0[i++] = (byte)(j >>> 24);
        abyte0[i++] = (byte)(j >>> 16);
        abyte0[i++] = (byte)(j >>> 8);
        abyte0[i++] = (byte)j;
        j = (int)l;
        abyte0[i++] = (byte)(j >>> 24);
        abyte0[i++] = (byte)(j >>> 16);
        abyte0[i++] = (byte)(j >>> 8);
        abyte0[i++] = (byte)j;
        b = i;
        return this;
    }

    public ByteVector putUTF8(String s)
    {
        int i = s.length();
        if(b + 2 + i > a.length)
            a(2 + i);
        int j = b;
        byte abyte0[] = a;
        abyte0[j++] = (byte)(i >>> 8);
        abyte0[j++] = (byte)i;
        int k = 0;
        do
        {
            if(k >= i)
                break;
            char c = s.charAt(k);
            if(c >= '\001' && c <= '\177')
            {
                abyte0[j++] = (byte)c;
            } else
            {
                int l = k;
                for(int i1 = k; i1 < i; i1++)
                {
                    char c1 = s.charAt(i1);
                    if(c1 >= '\001' && c1 <= '\177')
                    {
                        l++;
                        continue;
                    }
                    if(c1 > '\u07FF')
                        l += 3;
                    else
                        l += 2;
                }

                abyte0[b] = (byte)(l >>> 8);
                abyte0[b + 1] = (byte)l;
                if(b + 2 + l > abyte0.length)
                {
                    b = j;
                    a(2 + l);
                    abyte0 = a;
                }
                for(int j1 = k; j1 < i; j1++)
                {
                    char c2 = s.charAt(j1);
                    if(c2 >= '\001' && c2 <= '\177')
                        abyte0[j++] = (byte)c2;
                    else
                    if(c2 > '\u07FF')
                    {
                        abyte0[j++] = (byte)(0xe0 | c2 >> 12 & 0xf);
                        abyte0[j++] = (byte)(0x80 | c2 >> 6 & 0x3f);
                        abyte0[j++] = (byte)(0x80 | c2 & 0x3f);
                    } else
                    {
                        abyte0[j++] = (byte)(0xc0 | c2 >> 6 & 0x1f);
                        abyte0[j++] = (byte)(0x80 | c2 & 0x3f);
                    }
                }

                break;
            }
            k++;
        } while(true);
        b = j;
        return this;
    }

    public ByteVector putByteArray(byte abyte0[], int i, int j)
    {
        if(b + j > a.length)
            a(j);
        if(abyte0 != null)
            System.arraycopy(abyte0, i, a, b, j);
        b += j;
        return this;
    }

    private void a(int i)
    {
        int j = 2 * a.length;
        int k = b + i;
        byte abyte0[] = new byte[j <= k ? k : j];
        System.arraycopy(a, 0, abyte0, 0, b);
        a = abyte0;
    }

    byte a[];
    int b;
}
