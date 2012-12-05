// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


// Referenced classes of package profiler.asm:
//            AnnotationWriter, Attribute, ByteVector, ClassWriter, 
//            FieldVisitor, Item, AnnotationVisitor

final class FieldWriter
    implements FieldVisitor
{

    protected FieldWriter(ClassWriter classwriter, int k, String s, String s1, String s2, Object obj)
    {
        if(classwriter.y == null)
            classwriter.y = this;
        else
            classwriter.z.a = this;
        classwriter.z = this;
        b = classwriter;
        c = k;
        d = classwriter.newUTF8(s);
        e = classwriter.newUTF8(s1);
        if(s2 != null)
            f = classwriter.newUTF8(s2);
        if(obj != null)
            g = classwriter.a(obj).a;
    }

    public AnnotationVisitor visitAnnotation(String s, boolean flag)
    {
        ByteVector bytevector = new ByteVector();
        bytevector.putShort(b.newUTF8(s)).putShort(0);
        AnnotationWriter annotationwriter = new AnnotationWriter(b, true, bytevector, bytevector, 2);
        if(flag)
        {
            annotationwriter.g = h;
            h = annotationwriter;
        } else
        {
            annotationwriter.g = i;
            i = annotationwriter;
        }
        return annotationwriter;
    }

    public void visitAttribute(Attribute attribute)
    {
        attribute.a = j;
        j = attribute;
    }

    public void visitEnd()
    {
    }

    int a()
    {
        int k = 8;
        if(g != 0)
        {
            b.newUTF8("ConstantValue");
            k += 8;
        }
        if((c & 0x1000) != 0)
        {
            b.newUTF8("Synthetic");
            k += 6;
        }
        if((c & 0x20000) != 0)
        {
            b.newUTF8("Deprecated");
            k += 6;
        }
        if(b.b == 48 && (c & 0x4000) != 0)
        {
            b.newUTF8("Enum");
            k += 6;
        }
        if(f != 0)
        {
            b.newUTF8("Signature");
            k += 8;
        }
        if(h != null)
        {
            b.newUTF8("RuntimeVisibleAnnotations");
            k += 8 + h.a();
        }
        if(i != null)
        {
            b.newUTF8("RuntimeInvisibleAnnotations");
            k += 8 + i.a();
        }
        if(j != null)
            k += j.a(b, null, 0, -1, -1);
        return k;
    }

    void a(ByteVector bytevector)
    {
        bytevector.putShort(c).putShort(d).putShort(e);
        int k = 0;
        if(g != 0)
            k++;
        if((c & 0x1000) != 0)
            k++;
        if((c & 0x20000) != 0)
            k++;
        if(b.b == 48 && (c & 0x4000) != 0)
            k++;
        if(f != 0)
            k++;
        if(h != null)
            k++;
        if(i != null)
            k++;
        if(j != null)
            k += j.a();
        bytevector.putShort(k);
        if(g != 0)
        {
            bytevector.putShort(b.newUTF8("ConstantValue"));
            bytevector.putInt(2).putShort(g);
        }
        if((c & 0x1000) != 0)
            bytevector.putShort(b.newUTF8("Synthetic")).putInt(0);
        if((c & 0x20000) != 0)
            bytevector.putShort(b.newUTF8("Deprecated")).putInt(0);
        if(b.b == 48 && (c & 0x4000) != 0)
            bytevector.putShort(b.newUTF8("Enum")).putInt(0);
        if(f != 0)
        {
            bytevector.putShort(b.newUTF8("Signature"));
            bytevector.putInt(2).putShort(f);
        }
        if(h != null)
        {
            bytevector.putShort(b.newUTF8("RuntimeVisibleAnnotations"));
            h.a(bytevector);
        }
        if(i != null)
        {
            bytevector.putShort(b.newUTF8("RuntimeInvisibleAnnotations"));
            i.a(bytevector);
        }
        if(j != null)
            j.a(b, null, 0, -1, -1, bytevector);
    }

    FieldWriter a;
    private ClassWriter b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private AnnotationWriter h;
    private AnnotationWriter i;
    private Attribute j;
}
