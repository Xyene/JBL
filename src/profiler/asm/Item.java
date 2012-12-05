// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


final class Item
{

    Item()
    {
    }

    Item(short word0, Item item)
    {
        a = word0;
        b = item.b;
        c = item.c;
        d = item.d;
        e = item.e;
        f = item.f;
        g = item.g;
        h = item.h;
        i = item.i;
        j = item.j;
    }

    void a(int l)
    {
        b = 'I';
        c = l;
        j = 0x7fffffff & b + l;
    }

    void a(long l)
    {
        b = 'J';
        d = l;
        j = 0x7fffffff & b + (int)l;
    }

    void a(float f1)
    {
        b = 'F';
        e = f1;
        j = 0x7fffffff & b + (int)f1;
    }

    void a(double d1)
    {
        b = 'D';
        f = d1;
        j = 0x7fffffff & b + (int)d1;
    }

    void a(char c1, String s, String s1, String s2)
    {
        b = c1;
        g = s;
        h = s1;
        i = s2;
        switch(c1)
        {
        case 67: // 'C'
        case 83: // 'S'
        case 115: // 's'
            j = 0x7fffffff & c1 + s.hashCode();
            return;

        case 84: // 'T'
            j = 0x7fffffff & c1 + s.hashCode() * s1.hashCode();
            return;
        }
        j = 0x7fffffff & c1 + s.hashCode() * s1.hashCode() * s2.hashCode();
    }

    boolean a(Item item)
    {
        if(item.b == b)
        {
            switch(b)
            {
            case 73: // 'I'
                return item.c == c;

            case 74: // 'J'
                return item.d == d;

            case 70: // 'F'
                return item.e == e;

            case 68: // 'D'
                return item.f == f;

            case 67: // 'C'
            case 83: // 'S'
            case 115: // 's'
                return item.g.equals(g);

            case 84: // 'T'
                return item.g.equals(g) && item.h.equals(h);
            }
            return item.g.equals(g) && item.h.equals(h) && item.i.equals(i);
        } else
        {
            return false;
        }
    }

    short a;
    char b;
    int c;
    long d;
    float e;
    double f;
    String g;
    String h;
    String i;
    int j;
    Item k;
}
