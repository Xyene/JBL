// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


// Referenced classes of package profiler.asm:
//            ByteVector, ClassWriter, Label, ClassReader

public class Attribute
{

    protected Attribute(String s)
    {
        type = s;
    }

    public boolean isUnknown()
    {
        return true;
    }

    public boolean isCodeAttribute()
    {
        return false;
    }

    protected Label[] getLabels()
    {
        return null;
    }

    protected Attribute read(ClassReader classreader, int i, int j, char ac[], int k, Label alabel[])
    {
        return new Attribute(type);
    }

    protected ByteVector write(ClassWriter classwriter, byte abyte0[], int i, int j, int k)
    {
        return new ByteVector();
    }

    final int a()
    {
        int i = 0;
        for(Attribute attribute = this; attribute != null; attribute = attribute.a)
            if(!attribute.isUnknown())
                i++;

        return i;
    }

    final int a(ClassWriter classwriter, byte abyte0[], int i, int j, int k)
    {
        int l = 0;
        for(Attribute attribute = this; attribute != null; attribute = attribute.a)
        {
            classwriter.newUTF8(attribute.type);
            l += attribute.write(classwriter, abyte0, i, j, k).b + 6;
        }

        return l;
    }

    final void a(ClassWriter classwriter, byte abyte0[], int i, int j, int k, ByteVector bytevector)
    {
        for(Attribute attribute = this; attribute != null; attribute = attribute.a)
            if(attribute.isUnknown())
            {
                if(classwriter.checkAttributes)
                    throw new IllegalArgumentException("Unknown attribute type: " + attribute.type);
            } else
            {
                ByteVector bytevector1 = attribute.write(classwriter, abyte0, i, j, k);
                bytevector.putShort(classwriter.newUTF8(attribute.type)).putInt(bytevector1.b);
                bytevector.putByteArray(bytevector1.a, 0, bytevector1.b);
            }

    }

    public final String type;
    Attribute a;
}
