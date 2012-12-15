// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


// Referenced classes of package profiler.asm:
//            AnnotationVisitor, ByteVector, ClassWriter, Item, 
//            Type

final class AnnotationWriter
    implements AnnotationVisitor
{

    AnnotationWriter(ClassWriter classwriter, boolean flag, ByteVector bytevector, ByteVector bytevector1, int i)
    {
        a = classwriter;
        c = flag;
        d = bytevector;
        e = bytevector1;
        f = i;
    }

    public void visit(String s, Object obj)
    {
        b++;
        if(c)
            d.putShort(a.newUTF8(s));
        if(obj instanceof String)
            d.b(115, a.newUTF8((String)obj));
        else
        if(obj instanceof Byte)
            d.b(66, a.a(((Byte)obj).byteValue()).a);
        else
        if(obj instanceof Boolean)
            d.b(90, a.a(((Boolean)obj).booleanValue() ? 1 : 0).a);
        else
        if(obj instanceof Character)
            d.b(67, a.a(((Character)obj).charValue()).a);
        else
        if(obj instanceof Short)
            d.b(83, a.a(((Short)obj).shortValue()).a);
        else
        if(obj instanceof Type)
            d.b(99, a.newUTF8(((Type)obj).getDescriptor()));
        else
        if(obj instanceof byte[])
        {
            byte abyte0[] = (byte[])obj;
            d.b(91, abyte0.length);
            for(int i = 0; i < abyte0.length; i++)
                d.b(66, a.a(abyte0[i]).a);

        } else
        if(obj instanceof boolean[])
        {
            boolean aflag[] = (boolean[])obj;
            d.b(91, aflag.length);
            for(int j = 0; j < aflag.length; j++)
                d.b(90, a.a(aflag[j] ? 1 : 0).a);

        } else
        if(obj instanceof short[])
        {
            short aword0[] = (short[])obj;
            d.b(91, aword0.length);
            for(int k = 0; k < aword0.length; k++)
                d.b(83, a.a(aword0[k]).a);

        } else
        if(obj instanceof char[])
        {
            char ac[] = (char[])obj;
            d.b(91, ac.length);
            for(int l = 0; l < ac.length; l++)
                d.b(67, a.a(ac[l]).a);

        } else
        if(obj instanceof int[])
        {
            int ai[] = (int[])obj;
            d.b(91, ai.length);
            for(int i1 = 0; i1 < ai.length; i1++)
                d.b(73, a.a(ai[i1]).a);

        } else
        if(obj instanceof long[])
        {
            long al[] = (long[])obj;
            d.b(91, al.length);
            for(int j1 = 0; j1 < al.length; j1++)
                d.b(74, a.a(al[j1]).a);

        } else
        if(obj instanceof float[])
        {
            float af[] = (float[])obj;
            d.b(91, af.length);
            for(int k1 = 0; k1 < af.length; k1++)
                d.b(70, a.a(af[k1]).a);

        } else
        if(obj instanceof double[])
        {
            double ad[] = (double[])obj;
            d.b(91, ad.length);
            for(int l1 = 0; l1 < ad.length; l1++)
                d.b(68, a.a(ad[l1]).a);

        } else
        {
            Item item = a.a(obj);
            d.b(item.b, item.a);
        }
    }

    public void visitEnum(String s, String s1, String s2)
    {
        b++;
        if(c)
            d.putShort(a.newUTF8(s));
        d.b(101, a.newUTF8(s1)).putShort(a.newUTF8(s2));
    }

    public AnnotationVisitor visitAnnotation(String s, String s1)
    {
        b++;
        if(c)
            d.putShort(a.newUTF8(s));
        d.b(64, a.newUTF8(s1)).putShort(0);
        return new AnnotationWriter(a, true, d, d, d.b - 2);
    }

    public AnnotationVisitor visitArray(String s)
    {
        b++;
        if(c)
            d.putShort(a.newUTF8(s));
        d.b(91, 0);
        return new AnnotationWriter(a, false, d, d, d.b - 2);
    }

    public void visitEnd()
    {
        if(e != null)
        {
            byte abyte0[] = e.a;
            abyte0[f] = (byte)(b >>> 8);
            abyte0[f + 1] = (byte)b;
        }
    }

    int a()
    {
        int i = 0;
        for(AnnotationWriter annotationwriter = this; annotationwriter != null; annotationwriter = annotationwriter.g)
            i += annotationwriter.d.b;

        return i;
    }

    void a(ByteVector bytevector)
    {
        int i = 0;
        int j = 2;
        AnnotationWriter annotationwriter = this;
        AnnotationWriter annotationwriter2 = null;
        for(; annotationwriter != null; annotationwriter = annotationwriter.g)
        {
            i++;
            j += annotationwriter.d.b;
            annotationwriter.visitEnd();
            annotationwriter.h = annotationwriter2;
            annotationwriter2 = annotationwriter;
        }

        bytevector.putInt(j);
        bytevector.putShort(i);
        for(AnnotationWriter annotationwriter1 = annotationwriter2; annotationwriter1 != null; annotationwriter1 = annotationwriter1.h)
            bytevector.putByteArray(annotationwriter1.d.a, 0, annotationwriter1.d.b);

    }

    static void a(AnnotationWriter aannotationwriter[], ByteVector bytevector)
    {
        int i = 1 + 2 * aannotationwriter.length;
        for(int j = 0; j < aannotationwriter.length; j++)
            i += aannotationwriter[j] != null ? aannotationwriter[j].a() : 0;

        bytevector.putInt(i).putByte(aannotationwriter.length);
        for(int k = 0; k < aannotationwriter.length; k++)
        {
            AnnotationWriter annotationwriter = aannotationwriter[k];
            AnnotationWriter annotationwriter2 = null;
            int l = 0;
            for(; annotationwriter != null; annotationwriter = annotationwriter.g)
            {
                l++;
                annotationwriter.visitEnd();
                annotationwriter.h = annotationwriter2;
                annotationwriter2 = annotationwriter;
            }

            bytevector.putShort(l);
            for(AnnotationWriter annotationwriter1 = annotationwriter2; annotationwriter1 != null; annotationwriter1 = annotationwriter1.h)
                bytevector.putByteArray(annotationwriter1.d.a, 0, annotationwriter1.d.b);

        }

    }

    private final ClassWriter a;
    private int b;
    private final boolean c;
    private final ByteVector d;
    private final ByteVector e;
    private final int f;
    AnnotationWriter g;
    AnnotationWriter h;
}
