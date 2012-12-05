// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


// Referenced classes of package profiler.asm:
//            AnnotationWriter, Attribute, ByteVector, ClassVisitor, 
//            FieldWriter, Item, MethodWriter, Type, 
//            AnnotationVisitor, FieldVisitor, MethodVisitor

public class ClassWriter
    implements ClassVisitor
{

    public ClassWriter(boolean flag)
    {
        this(flag, false);
    }

    public ClassWriter(boolean flag, boolean flag1)
    {
        c = 1;
        d = new ByteVector();
        e = new Item[256];
        f = (int)(0.75D * (double)e.length);
        g = new Item();
        h = new Item();
        i = new Item();
        C = flag;
        checkAttributes = !flag1;
    }

    public void visit(int i1, int j1, String s1, String s2, String s3, String as[])
    {
        b = i1;
        j = j1;
        k = newClass(s1);
        if(s2 != null)
            l = newUTF8(s2);
        m = s3 != null ? newClass(s3) : 0;
        if(as != null && as.length > 0)
        {
            n = as.length;
            o = new int[n];
            for(int k1 = 0; k1 < n; k1++)
                o[k1] = newClass(as[k1]);

        }
    }

    public void visitSource(String s1, String s2)
    {
        if(s1 != null)
            p = newUTF8(s1);
        if(s2 != null)
        {
            q = new ByteVector();
            q.putUTF8(s2);
        }
    }

    public void visitOuterClass(String s1, String s2, String s3)
    {
        r = newClass(s1);
        if(s2 != null && s3 != null)
            s = newNameType(s2, s3);
    }

    public AnnotationVisitor visitAnnotation(String s1, boolean flag)
    {
        ByteVector bytevector = new ByteVector();
        bytevector.putShort(newUTF8(s1)).putShort(0);
        AnnotationWriter annotationwriter = new AnnotationWriter(this, true, bytevector, bytevector, 2);
        if(flag)
        {
            annotationwriter.g = t;
            t = annotationwriter;
        } else
        {
            annotationwriter.g = u;
            u = annotationwriter;
        }
        return annotationwriter;
    }

    public void visitAttribute(Attribute attribute)
    {
        attribute.a = v;
        v = attribute;
    }

    public void visitInnerClass(String s1, String s2, String s3, int i1)
    {
        if(x == null)
            x = new ByteVector();
        w++;
        x.putShort(s1 != null ? newClass(s1) : 0);
        x.putShort(s2 != null ? newClass(s2) : 0);
        x.putShort(s3 != null ? newUTF8(s3) : 0);
        x.putShort(i1);
    }

    public FieldVisitor visitField(int i1, String s1, String s2, String s3, Object obj)
    {
        return new FieldWriter(this, i1, s1, s2, s3, obj);
    }

    public MethodVisitor visitMethod(int i1, String s1, String s2, String s3, String as[])
    {
        return new MethodWriter(this, i1, s1, s2, s3, as, C);
    }

    public void visitEnd()
    {
    }

    public byte[] toByteArray()
    {
        int i1 = 24 + 2 * n;
        int j1 = 0;
        for(FieldWriter fieldwriter = y; fieldwriter != null; fieldwriter = fieldwriter.a)
        {
            j1++;
            i1 += fieldwriter.a();
        }

        int k1 = 0;
        for(MethodWriter methodwriter = A; methodwriter != null; methodwriter = methodwriter.a)
        {
            k1++;
            i1 += methodwriter.a();
        }

        int l1 = 0;
        if(l != 0)
        {
            l1++;
            i1 += 8;
            newUTF8("Signature");
        }
        if(p != 0)
        {
            l1++;
            i1 += 8;
            newUTF8("SourceFile");
        }
        if(q != null)
        {
            l1++;
            i1 += q.b;
            newUTF8("SourceDebugExtension");
        }
        if(r != 0)
        {
            l1++;
            i1 += 10;
            newUTF8("EnclosingMethod");
        }
        if((j & 0x20000) != 0)
        {
            l1++;
            i1 += 6;
            newUTF8("Deprecated");
        }
        if((j & 0x1000) != 0)
        {
            l1++;
            i1 += 6;
            newUTF8("Synthetic");
        }
        if(b == 48)
        {
            if((j & 0x2000) != 0)
            {
                l1++;
                i1 += 6;
                newUTF8("Annotation");
            }
            if((j & 0x4000) != 0)
            {
                l1++;
                i1 += 6;
                newUTF8("Enum");
            }
        }
        if(x != null)
        {
            l1++;
            i1 += 8 + x.b;
            newUTF8("InnerClasses");
        }
        if(t != null)
        {
            l1++;
            i1 += 8 + t.a();
            newUTF8("RuntimeVisibleAnnotations");
        }
        if(u != null)
        {
            l1++;
            i1 += 8 + u.a();
            newUTF8("RuntimeInvisibleAnnotations");
        }
        if(v != null)
        {
            l1 += v.a();
            i1 += v.a(this, null, 0, -1, -1);
        }
        i1 += d.b;
        ByteVector bytevector = new ByteVector(i1);
        bytevector.putInt(0xcafebabe).putInt(b);
        bytevector.putShort(c).putByteArray(d.a, 0, d.b);
        bytevector.putShort(j).putShort(k).putShort(m);
        bytevector.putShort(n);
        for(int i2 = 0; i2 < n; i2++)
            bytevector.putShort(o[i2]);

        bytevector.putShort(j1);
        for(FieldWriter fieldwriter1 = y; fieldwriter1 != null; fieldwriter1 = fieldwriter1.a)
            fieldwriter1.a(bytevector);

        bytevector.putShort(k1);
        for(MethodWriter methodwriter1 = A; methodwriter1 != null; methodwriter1 = methodwriter1.a)
            methodwriter1.a(bytevector);

        bytevector.putShort(l1);
        if(l != 0)
            bytevector.putShort(newUTF8("Signature")).putInt(2).putShort(l);
        if(p != 0)
            bytevector.putShort(newUTF8("SourceFile")).putInt(2).putShort(p);
        if(q != null)
        {
            int j2 = q.b;
            bytevector.putShort(newUTF8("SourceDebugExtension")).putInt(j2);
            bytevector.putByteArray(q.a, 0, j2);
        }
        if(r != 0)
        {
            bytevector.putShort(newUTF8("EnclosingMethod")).putInt(4);
            bytevector.putShort(r).putShort(s);
        }
        if((j & 0x20000) != 0)
            bytevector.putShort(newUTF8("Deprecated")).putInt(0);
        if((j & 0x1000) != 0)
            bytevector.putShort(newUTF8("Synthetic")).putInt(0);
        if(b == 48)
        {
            if((j & 0x2000) != 0)
                bytevector.putShort(newUTF8("Annotation")).putInt(0);
            if((j & 0x4000) != 0)
                bytevector.putShort(newUTF8("Enum")).putInt(0);
        }
        if(x != null)
        {
            bytevector.putShort(newUTF8("InnerClasses"));
            bytevector.putInt(x.b + 2).putShort(w);
            bytevector.putByteArray(x.a, 0, x.b);
        }
        if(t != null)
        {
            bytevector.putShort(newUTF8("RuntimeVisibleAnnotations"));
            t.a(bytevector);
        }
        if(u != null)
        {
            bytevector.putShort(newUTF8("RuntimeInvisibleAnnotations"));
            u.a(bytevector);
        }
        if(v != null)
            v.a(this, null, 0, -1, -1, bytevector);
        return bytevector.a;
    }

    Item a(Object obj)
    {
        if(obj instanceof Integer)
        {
            int i1 = ((Integer)obj).intValue();
            return a(i1);
        }
        if(obj instanceof Byte)
        {
            int j1 = ((Byte)obj).intValue();
            return a(j1);
        }
        if(obj instanceof Character)
        {
            char c1 = ((Character)obj).charValue();
            return a(c1);
        }
        if(obj instanceof Short)
        {
            int k1 = ((Short)obj).intValue();
            return a(k1);
        }
        if(obj instanceof Boolean)
        {
            int l1 = ((Boolean)obj).booleanValue() ? 1 : 0;
            return a(l1);
        }
        if(obj instanceof Float)
        {
            float f1 = ((Float)obj).floatValue();
            return a(f1);
        }
        if(obj instanceof Long)
        {
            long l2 = ((Long)obj).longValue();
            return a(l2);
        }
        if(obj instanceof Double)
        {
            double d1 = ((Double)obj).doubleValue();
            return a(d1);
        }
        if(obj instanceof String)
            return b((String)obj);
        if(obj instanceof Type)
        {
            Type type = (Type)obj;
            return a(type.getSort() != 10 ? type.getDescriptor() : type.getInternalName());
        } else
        {
            throw new IllegalArgumentException("value " + obj);
        }
    }

    public int newConst(Object obj)
    {
        return a(obj).a;
    }

    public int newUTF8(String s1)
    {
        g.a('s', s1, null, null);
        Item item = a(g);
        if(item == null)
        {
            d.putByte(1).putUTF8(s1);
            item = new Item(c++, g);
            b(item);
        }
        return item.a;
    }

    public int newClass(String s1)
    {
        return a(s1).a;
    }

    private Item a(String s1)
    {
        h.a('C', s1, null, null);
        Item item = a(h);
        if(item == null)
        {
            d.b(7, newUTF8(s1));
            item = new Item(c++, h);
            b(item);
        }
        return item;
    }

    public int newField(String s1, String s2, String s3)
    {
        i.a('G', s1, s2, s3);
        Item item = a(i);
        if(item == null)
        {
            a(9, newClass(s1), newNameType(s2, s3));
            item = new Item(c++, i);
            b(item);
        }
        return item.a;
    }

    Item a(String s1, String s2, String s3, boolean flag)
    {
        i.a(flag ? 'N' : 'M', s1, s2, s3);
        Item item = a(i);
        if(item == null)
        {
            a(flag ? 11 : 10, newClass(s1), newNameType(s2, s3));
            item = new Item(c++, i);
            b(item);
        }
        return item;
    }

    public int newMethod(String s1, String s2, String s3, boolean flag)
    {
        return a(s1, s2, s3, flag).a;
    }

    Item a(int i1)
    {
        g.a(i1);
        Item item = a(g);
        if(item == null)
        {
            d.putByte(3).putInt(i1);
            item = new Item(c++, g);
            b(item);
        }
        return item;
    }

    Item a(float f1)
    {
        g.a(f1);
        Item item = a(g);
        if(item == null)
        {
            d.putByte(4).putInt(Float.floatToIntBits(f1));
            item = new Item(c++, g);
            b(item);
        }
        return item;
    }

    Item a(long l1)
    {
        g.a(l1);
        Item item = a(g);
        if(item == null)
        {
            d.putByte(5).putLong(l1);
            item = new Item(c, g);
            b(item);
            c += 2;
        }
        return item;
    }

    Item a(double d1)
    {
        g.a(d1);
        Item item = a(g);
        if(item == null)
        {
            d.putByte(6).putLong(Double.doubleToLongBits(d1));
            item = new Item(c, g);
            b(item);
            c += 2;
        }
        return item;
    }

    private Item b(String s1)
    {
        h.a('S', s1, null, null);
        Item item = a(h);
        if(item == null)
        {
            d.b(8, newUTF8(s1));
            item = new Item(c++, h);
            b(item);
        }
        return item;
    }

    public int newNameType(String s1, String s2)
    {
        h.a('T', s1, s2, null);
        Item item = a(h);
        if(item == null)
        {
            a(12, newUTF8(s1), newUTF8(s2));
            item = new Item(c++, h);
            b(item);
        }
        return item.a;
    }

    private Item a(Item item)
    {
        int i1 = item.j;
        for(Item item1 = e[i1 % e.length]; item1 != null; item1 = item1.k)
            if(item1.j == i1 && item.a(item1))
                return item1;

        return null;
    }

    private void b(Item item)
    {
        if(c > f)
        {
            Item aitem[] = new Item[e.length * 2 + 1];
            for(int j1 = e.length - 1; j1 >= 0; j1--)
            {
                Item item2;
                for(Item item1 = e[j1]; item1 != null; item1 = item2)
                {
                    int k1 = item1.j % aitem.length;
                    item2 = item1.k;
                    item1.k = aitem[k1];
                    aitem[k1] = item1;
                }

            }

            e = aitem;
            f = (int)((double)e.length * 0.75D);
        }
        int i1 = item.j % e.length;
        item.k = e[i1];
        e[i1] = item;
    }

    private void a(int i1, int j1, int k1)
    {
        d.b(i1, j1).putShort(k1);
    }

    static byte a[];
    int b;
    private short c;
    private ByteVector d;
    private Item e[];
    private int f;
    Item g;
    Item h;
    Item i;
    private int j;
    private int k;
    private int l;
    private int m;
    private int n;
    private int o[];
    private int p;
    private ByteVector q;
    private int r;
    private int s;
    private AnnotationWriter t;
    private AnnotationWriter u;
    private Attribute v;
    private int w;
    private ByteVector x;
    FieldWriter y;
    FieldWriter z;
    MethodWriter A;
    MethodWriter B;
    private boolean C;
    boolean checkAttributes;

    static 
    {
        byte abyte0[] = new byte[220];
        String s1 = "AAAAAAAAAAAAAAAABCKLLDDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAADDDDDEEEEEEEEEEEEEEEEEEEEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMAAAAAAAAAAAAAAAAAAAAIIIIIIIIIIIIIIIIDNOAAAAAAGGGGGGGHAFBFAAFFAAQPIIJJIIIIIIIIIIIIIIIIII";
        for(int i1 = 0; i1 < abyte0.length; i1++)
            abyte0[i1] = (byte)(s1.charAt(i1) - 65);

        a = abyte0;
    }
}
