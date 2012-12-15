// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


// Referenced classes of package profiler.asm:
//            AnnotationWriter, Attribute, ByteVector, ClassWriter, 
//            Edge, Item, Label, MethodVisitor, 
//            Type, AnnotationVisitor

class MethodWriter
    implements MethodVisitor
{

    MethodWriter(ClassWriter classwriter, int i1, String s1, String s2, String s3, String as[], boolean flag)
    {
        p = new ByteVector();
        if(classwriter.A == null)
            classwriter.A = this;
        else
            classwriter.B.a = this;
        classwriter.B = this;
        b = classwriter;
        c = i1;
        d = classwriter.newUTF8(s1);
        e = classwriter.newUTF8(s2);
        f = s2;
        if(s3 != null)
            g = classwriter.newUTF8(s3);
        if(as != null && as.length > 0)
        {
            h = as.length;
            i = new int[h];
            for(int j1 = 0; j1 < h; j1++)
                i[j1] = classwriter.newClass(as[j1]);

        }
        C = flag;
        if(flag)
        {
            int k1 = a(s2) >> 2;
            if((i1 & 8) != 0)
                k1--;
            r = k1;
            F = new Label();
            F.j = true;
            G = F;
        }
    }

    public AnnotationVisitor visitAnnotationDefault()
    {
        j = new ByteVector();
        return new AnnotationWriter(b, false, j, null, 0);
    }

    public AnnotationVisitor visitAnnotation(String s1, boolean flag)
    {
        ByteVector bytevector = new ByteVector();
        bytevector.putShort(b.newUTF8(s1)).putShort(0);
        AnnotationWriter annotationwriter = new AnnotationWriter(b, true, bytevector, bytevector, 2);
        if(flag)
        {
            annotationwriter.g = k;
            k = annotationwriter;
        } else
        {
            annotationwriter.g = l;
            l = annotationwriter;
        }
        return annotationwriter;
    }

    public AnnotationVisitor visitParameterAnnotation(int i1, String s1, boolean flag)
    {
        ByteVector bytevector = new ByteVector();
        bytevector.putShort(b.newUTF8(s1)).putShort(0);
        AnnotationWriter annotationwriter = new AnnotationWriter(b, true, bytevector, bytevector, 2);
        if(flag)
        {
            if(m == null)
                m = new AnnotationWriter[Type.getArgumentTypes(f).length];
            annotationwriter.g = m[i1];
            m[i1] = annotationwriter;
        } else
        {
            if(n == null)
                n = new AnnotationWriter[Type.getArgumentTypes(f).length];
            annotationwriter.g = n[i1];
            n[i1] = annotationwriter;
        }
        return annotationwriter;
    }

    public void visitAttribute(Attribute attribute)
    {
        if(attribute.isCodeAttribute())
        {
            attribute.a = A;
            A = attribute;
        } else
        {
            attribute.a = o;
            o = attribute;
        }
    }

    public void visitCode()
    {
    }

    public void visitInsn(int i1)
    {
        if(C)
        {
            int j1 = D + H[i1];
            if(j1 > E)
                E = j1;
            D = j1;
            if((i1 >= 172 && i1 <= 177 || i1 == 191) && F != null)
            {
                F.g = E;
                F = null;
            }
        }
        p.putByte(i1);
    }

    public void visitIntInsn(int i1, int j1)
    {
        if(C && i1 != 188)
        {
            int k1 = D + 1;
            if(k1 > E)
                E = k1;
            D = k1;
        }
        if(i1 == 17)
            p.b(i1, j1);
        else
            p.a(i1, j1);
    }

    public void visitVarInsn(int i1, int j1)
    {
        if(C)
        {
            if(i1 == 169)
            {
                if(F != null)
                {
                    F.g = E;
                    F = null;
                }
            } else
            {
                int k1 = D + H[i1];
                if(k1 > E)
                    E = k1;
                D = k1;
            }
            int l1;
            if(i1 == 22 || i1 == 24 || i1 == 55 || i1 == 57)
                l1 = j1 + 2;
            else
                l1 = j1 + 1;
            if(l1 > r)
                r = l1;
        }
        if(j1 < 4 && i1 != 169)
        {
            int i2;
            if(i1 < 54)
                i2 = 26 + (i1 - 21 << 2) + j1;
            else
                i2 = 59 + (i1 - 54 << 2) + j1;
            p.putByte(i2);
        } else
        if(j1 >= 256)
            p.putByte(196).b(i1, j1);
        else
            p.a(i1, j1);
    }

    public void visitTypeInsn(int i1, String s1)
    {
        if(C && i1 == 187)
        {
            int j1 = D + 1;
            if(j1 > E)
                E = j1;
            D = j1;
        }
        p.b(i1, b.newClass(s1));
    }

    public void visitFieldInsn(int i1, String s1, String s2, String s3)
    {
        if(C)
        {
            char c1 = s3.charAt(0);
            int j1;
            switch(i1)
            {
            case 178: 
                j1 = D + (c1 != 'D' && c1 != 'J' ? 1 : 2);
                break;

            case 179: 
                j1 = D + (c1 != 'D' && c1 != 'J' ? -1 : -2);
                break;

            case 180: 
                j1 = D + (c1 != 'D' && c1 != 'J' ? 0 : 1);
                break;

            default:
                j1 = D + (c1 != 'D' && c1 != 'J' ? -2 : -3);
                break;
            }
            if(j1 > E)
                E = j1;
            D = j1;
        }
        p.b(i1, b.newField(s1, s2, s3));
    }

    public void visitMethodInsn(int i1, String s1, String s2, String s3)
    {
        boolean flag = i1 == 185;
        Item item = b.a(s1, s2, s3, flag);
        int j1 = item.c;
        if(C)
        {
            if(j1 == 0)
            {
                j1 = a(s3);
                item.c = j1;
            }
            int k1;
            if(i1 == 184)
                k1 = (D - (j1 >> 2)) + (j1 & 3) + 1;
            else
                k1 = (D - (j1 >> 2)) + (j1 & 3);
            if(k1 > E)
                E = k1;
            D = k1;
        }
        if(flag)
        {
            if(!C && j1 == 0)
            {
                j1 = a(s3);
                item.c = j1;
            }
            p.b(185, item.a).a(j1 >> 2, 0);
        } else
        {
            p.b(i1, item.a);
        }
    }

    public void visitJumpInsn(int i1, Label label)
    {
        if(C)
            if(i1 == 167)
            {
                if(F != null)
                {
                    F.g = E;
                    a(D, label);
                    F = null;
                }
            } else
            if(i1 == 168)
            {
                if(F != null)
                    a(D + 1, label);
            } else
            {
                D += H[i1];
                if(F != null)
                    a(D, label);
            }
        if(label.a && label.b - p.b < -32768)
        {
            if(i1 == 167)
                p.putByte(200);
            else
            if(i1 == 168)
            {
                p.putByte(201);
            } else
            {
                p.putByte(i1 > 166 ? i1 ^ 1 : (i1 + 1 ^ 1) - 1);
                p.putShort(8);
                p.putByte(200);
            }
            label.a(this, p, p.b - 1, true);
        } else
        {
            p.putByte(i1);
            label.a(this, p, p.b - 1, false);
        }
    }

    public void visitLabel(Label label)
    {
        if(C)
        {
            if(F != null)
            {
                F.g = E;
                a(D, label);
            }
            F = label;
            D = 0;
            E = 0;
        }
        B |= label.a(this, p.b, p.a);
    }

    public void visitLdcInsn(Object obj)
    {
        Item item = b.a(obj);
        if(C)
        {
            int i1;
            if(item.b == 'J' || item.b == 'D')
                i1 = D + 2;
            else
                i1 = D + 1;
            if(i1 > E)
                E = i1;
            D = i1;
        }
        short word0 = item.a;
        if(item.b == 'J' || item.b == 'D')
            p.b(20, word0);
        else
        if(word0 >= 256)
            p.b(19, word0);
        else
            p.a(18, word0);
    }

    public void visitIincInsn(int i1, int j1)
    {
        if(C)
        {
            int k1 = i1 + 1;
            if(k1 > r)
                r = k1;
        }
        if(i1 > 255 || j1 > 127 || j1 < -128)
            p.putByte(196).b(132, i1).putShort(j1);
        else
            p.putByte(132).a(i1, j1);
    }

    public void visitTableSwitchInsn(int i1, int j1, Label label, Label alabel[])
    {
        if(C)
        {
            D--;
            if(F != null)
            {
                F.g = E;
                a(D, label);
                for(int k1 = 0; k1 < alabel.length; k1++)
                    a(D, alabel[k1]);

                F = null;
            }
        }
        int l1 = p.b;
        p.putByte(170);
        while(p.b % 4 != 0) 
            p.putByte(0);
        label.a(this, p, l1, true);
        p.putInt(i1).putInt(j1);
        for(int i2 = 0; i2 < alabel.length; i2++)
            alabel[i2].a(this, p, l1, true);

    }

    public void visitLookupSwitchInsn(Label label, int ai[], Label alabel[])
    {
        if(C)
        {
            D--;
            if(F != null)
            {
                F.g = E;
                a(D, label);
                for(int i1 = 0; i1 < alabel.length; i1++)
                    a(D, alabel[i1]);

                F = null;
            }
        }
        int j1 = p.b;
        p.putByte(171);
        while(p.b % 4 != 0) 
            p.putByte(0);
        label.a(this, p, j1, true);
        p.putInt(alabel.length);
        for(int k1 = 0; k1 < alabel.length; k1++)
        {
            p.putInt(ai[k1]);
            alabel[k1].a(this, p, j1, true);
        }

    }

    public void visitMultiANewArrayInsn(String s1, int i1)
    {
        if(C)
            D += 1 - i1;
        p.b(197, b.newClass(s1)).putByte(i1);
    }

    public void visitTryCatchBlock(Label label, Label label1, Label label2, String s1)
    {
        if(C && !label2.j)
        {
            label2.f = 1;
            label2.j = true;
            label2.i = G;
            G = label2;
        }
        s++;
        if(t == null)
            t = new ByteVector();
        t.putShort(label.b);
        t.putShort(label1.b);
        t.putShort(label2.b);
        t.putShort(s1 == null ? 0 : b.newClass(s1));
    }

    public void visitLocalVariable(String s1, String s2, String s3, Label label, Label label1, int i1)
    {
        if(s3 != null)
        {
            if(x == null)
                x = new ByteVector();
            w++;
            x.putShort(label.b).putShort(label1.b - label.b).putShort(b.newUTF8(s1)).putShort(b.newUTF8(s3)).putShort(i1);
        }
        if(v == null)
            v = new ByteVector();
        u++;
        v.putShort(label.b).putShort(label1.b - label.b).putShort(b.newUTF8(s1)).putShort(b.newUTF8(s2)).putShort(i1);
    }

    public void visitLineNumber(int i1, Label label)
    {
        if(z == null)
            z = new ByteVector();
        y++;
        z.putShort(label.b);
        z.putShort(i1);
    }

    public void visitMaxs(int i1, int j1)
    {
        if(C)
        {
            int k1 = 0;
            for(Label label = G; label != null;)
            {
                Label label1 = label;
                label = label.i;
                int l1 = label1.f;
                int i2 = l1 + label1.g;
                if(i2 > k1)
                    k1 = i2;
                Edge edge = label1.h;
                while(edge != null) 
                {
                    Label label2 = edge.b;
                    if(!label2.j)
                    {
                        label2.f = l1 + edge.a;
                        label2.j = true;
                        label2.i = label;
                        label = label2;
                    }
                    edge = edge.c;
                }
            }

            q = k1;
        } else
        {
            q = i1;
            r = j1;
        }
    }

    public void visitEnd()
    {
    }

    private static int a(String s1)
    {
        int i1 = 1;
        int j1 = 1;
        do
        {
            char c1 = s1.charAt(j1++);
            if(c1 == ')')
            {
                c1 = s1.charAt(j1);
                return i1 << 2 | (c1 != 'V' ? c1 != 'D' && c1 != 'J' ? 1 : 2 : 0);
            }
            if(c1 == 'L')
            {
                while(s1.charAt(j1++) != ';') ;
                i1++;
            } else
            if(c1 == '[')
            {
                while((c1 = s1.charAt(j1)) == '[') 
                    j1++;
                if(c1 == 'D' || c1 == 'J')
                    i1--;
            } else
            if(c1 == 'D' || c1 == 'J')
                i1 += 2;
            else
                i1++;
        } while(true);
    }

    private void a(int i1, Label label)
    {
        Edge edge = new Edge();
        edge.a = i1;
        edge.b = label;
        edge.c = F.h;
        F.h = edge;
    }

    final int a()
    {
        if(B)
            a(new int[0], new int[0], 0);
        int i1 = 8;
        if(p.b > 0)
        {
            b.newUTF8("Code");
            i1 += 18 + p.b + 8 * s;
            if(v != null)
            {
                b.newUTF8("LocalVariableTable");
                i1 += 8 + v.b;
            }
            if(x != null)
            {
                b.newUTF8("LocalVariableTypeTable");
                i1 += 8 + x.b;
            }
            if(z != null)
            {
                b.newUTF8("LineNumberTable");
                i1 += 8 + z.b;
            }
            if(A != null)
                i1 += A.a(b, p.a, p.b, q, r);
        }
        if(h > 0)
        {
            b.newUTF8("Exceptions");
            i1 += 8 + 2 * h;
        }
        if((c & 0x1000) != 0)
        {
            b.newUTF8("Synthetic");
            i1 += 6;
        }
        if((c & 0x20000) != 0)
        {
            b.newUTF8("Deprecated");
            i1 += 6;
        }
        if(b.b == 48)
        {
            if((c & 0x80) != 0)
            {
                b.newUTF8("Varargs");
                i1 += 6;
            }
            if((c & 0x40) != 0)
            {
                b.newUTF8("Bridge");
                i1 += 6;
            }
        }
        if(g != 0)
        {
            b.newUTF8("Signature");
            i1 += 8;
        }
        if(j != null)
        {
            b.newUTF8("AnnotationDefault");
            i1 += 6 + j.b;
        }
        if(k != null)
        {
            b.newUTF8("RuntimeVisibleAnnotations");
            i1 += 8 + k.a();
        }
        if(l != null)
        {
            b.newUTF8("RuntimeInvisibleAnnotations");
            i1 += 8 + l.a();
        }
        if(m != null)
        {
            b.newUTF8("RuntimeVisibleParameterAnnotations");
            i1 += 7 + 2 * m.length;
            for(int j1 = m.length - 1; j1 >= 0; j1--)
                i1 += m[j1] != null ? m[j1].a() : 0;

        }
        if(n != null)
        {
            b.newUTF8("RuntimeInvisibleParameterAnnotations");
            i1 += 7 + 2 * n.length;
            for(int k1 = n.length - 1; k1 >= 0; k1--)
                i1 += n[k1] != null ? n[k1].a() : 0;

        }
        if(o != null)
            i1 += o.a(b, null, 0, -1, -1);
        return i1;
    }

    final void a(ByteVector bytevector)
    {
        bytevector.putShort(c).putShort(d).putShort(e);
        int i1 = 0;
        if(p.b > 0)
            i1++;
        if(h > 0)
            i1++;
        if((c & 0x1000) != 0)
            i1++;
        if((c & 0x20000) != 0)
            i1++;
        if(b.b == 48)
        {
            if((c & 0x80) != 0)
                i1++;
            if((c & 0x40) != 0)
                i1++;
        }
        if(g != 0)
            i1++;
        if(j != null)
            i1++;
        if(k != null)
            i1++;
        if(l != null)
            i1++;
        if(m != null)
            i1++;
        if(n != null)
            i1++;
        if(o != null)
            i1 += o.a();
        bytevector.putShort(i1);
        if(p.b > 0)
        {
            int k1 = 12 + p.b + 8 * s;
            if(v != null)
                k1 += 8 + v.b;
            if(x != null)
                k1 += 8 + x.b;
            if(z != null)
                k1 += 8 + z.b;
            if(A != null)
                k1 += A.a(b, p.a, p.b, q, r);
            bytevector.putShort(b.newUTF8("Code")).putInt(k1);
            bytevector.putShort(q).putShort(r);
            bytevector.putInt(p.b).putByteArray(p.a, 0, p.b);
            bytevector.putShort(s);
            if(s > 0)
                bytevector.putByteArray(t.a, 0, t.b);
            int j1 = 0;
            if(v != null)
                j1++;
            if(x != null)
                j1++;
            if(z != null)
                j1++;
            if(A != null)
                j1 += A.a();
            bytevector.putShort(j1);
            if(v != null)
            {
                bytevector.putShort(b.newUTF8("LocalVariableTable"));
                bytevector.putInt(v.b + 2).putShort(u);
                bytevector.putByteArray(v.a, 0, v.b);
            }
            if(x != null)
            {
                bytevector.putShort(b.newUTF8("LocalVariableTypeTable"));
                bytevector.putInt(x.b + 2).putShort(w);
                bytevector.putByteArray(x.a, 0, x.b);
            }
            if(z != null)
            {
                bytevector.putShort(b.newUTF8("LineNumberTable"));
                bytevector.putInt(z.b + 2).putShort(y);
                bytevector.putByteArray(z.a, 0, z.b);
            }
            if(A != null)
                A.a(b, p.a, p.b, r, q, bytevector);
        }
        if(h > 0)
        {
            bytevector.putShort(b.newUTF8("Exceptions")).putInt(2 * h + 2);
            bytevector.putShort(h);
            for(int l1 = 0; l1 < h; l1++)
                bytevector.putShort(i[l1]);

        }
        if((c & 0x1000) != 0)
            bytevector.putShort(b.newUTF8("Synthetic")).putInt(0);
        if((c & 0x20000) != 0)
            bytevector.putShort(b.newUTF8("Deprecated")).putInt(0);
        if(b.b == 48)
        {
            if((c & 0x80) != 0)
                bytevector.putShort(b.newUTF8("Varargs")).putInt(0);
            if((c & 0x40) != 0)
                bytevector.putShort(b.newUTF8("Bridge")).putInt(0);
        }
        if(g != 0)
            bytevector.putShort(b.newUTF8("Signature")).putInt(2).putShort(g);
        if(j != null)
        {
            bytevector.putShort(b.newUTF8("AnnotationDefault"));
            bytevector.putInt(j.b);
            bytevector.putByteArray(j.a, 0, j.b);
        }
        if(k != null)
        {
            bytevector.putShort(b.newUTF8("RuntimeVisibleAnnotations"));
            k.a(bytevector);
        }
        if(l != null)
        {
            bytevector.putShort(b.newUTF8("RuntimeInvisibleAnnotations"));
            l.a(bytevector);
        }
        if(m != null)
        {
            bytevector.putShort(b.newUTF8("RuntimeVisibleParameterAnnotations"));
            AnnotationWriter.a(m, bytevector);
        }
        if(n != null)
        {
            bytevector.putShort(b.newUTF8("RuntimeInvisibleParameterAnnotations"));
            AnnotationWriter.a(n, bytevector);
        }
        if(o != null)
            o.a(b, null, 0, -1, -1, bytevector);
    }

    private int[] a(int ai[], int ai1[], int i1)
    {
        byte abyte0[] = p.a;
        int ai2[] = new int[i1];
        int ai3[] = new int[i1];
        System.arraycopy(ai, 0, ai2, 0, i1);
        System.arraycopy(ai1, 0, ai3, 0, i1);
        boolean aflag[] = new boolean[p.b];
        int l8 = 3;
        do
        {
            if(l8 == 3)
                l8 = 2;
            int j1 = 0;
            do
            {
                if(j1 >= abyte0.length)
                    break;
                int i9 = abyte0[j1] & 0xff;
                int k9 = 0;
                switch(ClassWriter.a[i9])
                {
                case 0: // '\0'
                case 4: // '\004'
                    j1++;
                    break;

                case 8: // '\b'
                    int i3;
                    if(i9 > 201)
                    {
                        i9 = i9 >= 218 ? i9 - 20 : i9 - 49;
                        i3 = j1 + c(abyte0, j1 + 1);
                    } else
                    {
                        i3 = j1 + b(abyte0, j1 + 1);
                    }
                    int j6 = a(ai2, ai3, j1, i3);
                    if((j6 < -32768 || j6 > 32767) && !aflag[j1])
                    {
                        if(i9 == 167 || i9 == 168)
                            k9 = 2;
                        else
                            k9 = 5;
                        aflag[j1] = true;
                    }
                    j1 += 3;
                    break;

                case 9: // '\t'
                    j1 += 5;
                    break;

                case 13: // '\r'
                    if(l8 == 1)
                    {
                        int k6 = a(ai2, ai3, 0, j1);
                        k9 = -(k6 & 3);
                    } else
                    if(!aflag[j1])
                    {
                        k9 = j1 & 3;
                        aflag[j1] = true;
                    }
                    j1 = (j1 + 4) - (j1 & 3);
                    j1 += 4 * ((a(abyte0, j1 + 8) - a(abyte0, j1 + 4)) + 1) + 12;
                    break;

                case 14: // '\016'
                    if(l8 == 1)
                    {
                        int l6 = a(ai2, ai3, 0, j1);
                        k9 = -(l6 & 3);
                    } else
                    if(!aflag[j1])
                    {
                        k9 = j1 & 3;
                        aflag[j1] = true;
                    }
                    j1 = (j1 + 4) - (j1 & 3);
                    j1 += 8 * a(abyte0, j1 + 4) + 8;
                    break;

                case 16: // '\020'
                    int j9 = abyte0[j1 + 1] & 0xff;
                    if(j9 == 132)
                        j1 += 6;
                    else
                        j1 += 4;
                    break;

                case 1: // '\001'
                case 3: // '\003'
                case 10: // '\n'
                    j1 += 2;
                    break;

                case 2: // '\002'
                case 5: // '\005'
                case 6: // '\006'
                case 11: // '\013'
                case 12: // '\f'
                    j1 += 3;
                    break;

                case 7: // '\007'
                    j1 += 5;
                    break;

                case 15: // '\017'
                default:
                    j1 += 4;
                    break;
                }
                if(k9 != 0)
                {
                    int ai4[] = new int[ai2.length + 1];
                    int ai5[] = new int[ai3.length + 1];
                    System.arraycopy(ai2, 0, ai4, 0, ai2.length);
                    System.arraycopy(ai3, 0, ai5, 0, ai3.length);
                    ai4[ai2.length] = j1;
                    ai5[ai3.length] = k9;
                    ai2 = ai4;
                    ai3 = ai5;
                    if(k9 > 0)
                        l8 = 3;
                }
            } while(true);
            if(l8 < 3)
                l8--;
        } while(l8 != 0);
        ByteVector bytevector = new ByteVector(p.b);
        int k1 = 0;
        do
        {
            if(k1 >= p.b)
                break;
            for(int i5 = ai2.length - 1; i5 >= 0; i5--)
            {
                if(ai2[i5] != k1 || i5 >= i1)
                    continue;
                if(ai1[i5] > 0)
                    bytevector.putByteArray(null, 0, ai1[i5]);
                else
                    bytevector.b += ai1[i5];
                ai[i5] = bytevector.b;
            }

            int l9 = abyte0[k1] & 0xff;
            switch(ClassWriter.a[l9])
            {
            case 0: // '\0'
            case 4: // '\004'
                bytevector.putByte(l9);
                k1++;
                break;

            case 8: // '\b'
                int j3;
                if(l9 > 201)
                {
                    l9 = l9 >= 218 ? l9 - 20 : l9 - 49;
                    j3 = k1 + c(abyte0, k1 + 1);
                } else
                {
                    j3 = k1 + b(abyte0, k1 + 1);
                }
                int i7 = a(ai2, ai3, k1, j3);
                if(aflag[k1])
                {
                    if(l9 == 167)
                        bytevector.putByte(200);
                    else
                    if(l9 == 168)
                    {
                        bytevector.putByte(201);
                    } else
                    {
                        bytevector.putByte(l9 > 166 ? l9 ^ 1 : (l9 + 1 ^ 1) - 1);
                        bytevector.putShort(8);
                        bytevector.putByte(200);
                        i7 -= 3;
                    }
                    bytevector.putInt(i7);
                } else
                {
                    bytevector.putByte(l9);
                    bytevector.putShort(i7);
                }
                k1 += 3;
                break;

            case 9: // '\t'
                int k3 = k1 + a(abyte0, k1 + 1);
                int j7 = a(ai2, ai3, k1, k3);
                bytevector.putByte(l9);
                bytevector.putInt(j7);
                k1 += 5;
                break;

            case 13: // '\r'
                int k2 = k1;
                k1 = (k1 + 4) - (k2 & 3);
                bytevector.putByte(170);
                while(bytevector.b % 4 != 0) 
                    bytevector.putByte(0);
                int l3 = k2 + a(abyte0, k1);
                k1 += 4;
                int k7 = a(ai2, ai3, k2, l3);
                bytevector.putInt(k7);
                int l5 = a(abyte0, k1);
                k1 += 4;
                bytevector.putInt(l5);
                l5 = (a(abyte0, k1) - l5) + 1;
                k1 += 4;
                bytevector.putInt(a(abyte0, k1 - 4));
                while(l5 > 0) 
                {
                    int i4 = k2 + a(abyte0, k1);
                    k1 += 4;
                    int l7 = a(ai2, ai3, k2, i4);
                    bytevector.putInt(l7);
                    l5--;
                }
                break;

            case 14: // '\016'
                int l2 = k1;
                k1 = (k1 + 4) - (l2 & 3);
                bytevector.putByte(171);
                while(bytevector.b % 4 != 0) 
                    bytevector.putByte(0);
                int j4 = l2 + a(abyte0, k1);
                k1 += 4;
                int i8 = a(ai2, ai3, l2, j4);
                bytevector.putInt(i8);
                int i6 = a(abyte0, k1);
                k1 += 4;
                bytevector.putInt(i6);
                while(i6 > 0) 
                {
                    bytevector.putInt(a(abyte0, k1));
                    k1 += 4;
                    int k4 = l2 + a(abyte0, k1);
                    k1 += 4;
                    int j8 = a(ai2, ai3, l2, k4);
                    bytevector.putInt(j8);
                    i6--;
                }
                break;

            case 16: // '\020'
                int i10 = abyte0[k1 + 1] & 0xff;
                if(i10 == 132)
                {
                    bytevector.putByteArray(abyte0, k1, 6);
                    k1 += 6;
                } else
                {
                    bytevector.putByteArray(abyte0, k1, 4);
                    k1 += 4;
                }
                break;

            case 1: // '\001'
            case 3: // '\003'
            case 10: // '\n'
                bytevector.putByteArray(abyte0, k1, 2);
                k1 += 2;
                break;

            case 2: // '\002'
            case 5: // '\005'
            case 6: // '\006'
            case 11: // '\013'
            case 12: // '\f'
                bytevector.putByteArray(abyte0, k1, 3);
                k1 += 3;
                break;

            case 7: // '\007'
                bytevector.putByteArray(abyte0, k1, 5);
                k1 += 5;
                break;

            case 15: // '\017'
            default:
                bytevector.putByteArray(abyte0, k1, 4);
                k1 += 4;
                break;
            }
        } while(true);
        if(t != null)
        {
            byte abyte1[] = t.a;
            for(int l1 = 0; l1 < t.b; l1 += 8)
            {
                a(abyte1, l1, a(ai2, ai3, 0, c(abyte1, l1)));
                a(abyte1, l1 + 2, a(ai2, ai3, 0, c(abyte1, l1 + 2)));
                a(abyte1, l1 + 4, a(ai2, ai3, 0, c(abyte1, l1 + 4)));
            }

        }
        for(int j5 = 0; j5 < 2; j5++)
        {
            ByteVector bytevector1 = j5 != 0 ? x : v;
            if(bytevector1 == null)
                continue;
            byte abyte2[] = bytevector1.a;
            for(int i2 = 0; i2 < bytevector1.b; i2 += 10)
            {
                int l4 = c(abyte2, i2);
                int k8 = a(ai2, ai3, 0, l4);
                a(abyte2, i2, k8);
                l4 += c(abyte2, i2 + 2);
                k8 = a(ai2, ai3, 0, l4) - k8;
                a(abyte2, i2 + 2, k8);
            }

        }

        if(z != null)
        {
            byte abyte3[] = z.a;
            for(int j2 = 0; j2 < z.b; j2 += 4)
                a(abyte3, j2, a(ai2, ai3, 0, c(abyte3, j2)));

        }
        do
        {
            if(A == null)
                break;
            Label alabel[] = A.getLabels();
            if(alabel != null)
            {
                int k5 = alabel.length - 1;
                while(k5 >= 0) 
                {
                    if(!alabel[k5].c)
                    {
                        alabel[k5].b = a(ai2, ai3, 0, alabel[k5].b);
                        alabel[k5].c = true;
                    }
                    k5--;
                }
            }
        } while(true);
        p = bytevector;
        return ai;
    }

    static int c(byte abyte0[], int i1)
    {
        return (abyte0[i1] & 0xff) << 8 | abyte0[i1 + 1] & 0xff;
    }

    static short b(byte abyte0[], int i1)
    {
        return (short)((abyte0[i1] & 0xff) << 8 | abyte0[i1 + 1] & 0xff);
    }

    static int a(byte abyte0[], int i1)
    {
        return (abyte0[i1] & 0xff) << 24 | (abyte0[i1 + 1] & 0xff) << 16 | (abyte0[i1 + 2] & 0xff) << 8 | abyte0[i1 + 3] & 0xff;
    }

    static void a(byte abyte0[], int i1, int j1)
    {
        abyte0[i1] = (byte)(j1 >>> 8);
        abyte0[i1 + 1] = (byte)j1;
    }

    static int a(int ai[], int ai1[], int i1, int j1)
    {
        int k1 = j1 - i1;
        for(int l1 = 0; l1 < ai.length; l1++)
        {
            if(i1 < ai[l1] && ai[l1] <= j1)
            {
                k1 += ai1[l1];
                continue;
            }
            if(j1 < ai[l1] && ai[l1] <= i1)
                k1 -= ai1[l1];
        }

        return k1;
    }

    MethodWriter a;
    private ClassWriter b;
    private int c;
    private int d;
    private int e;
    private String f;
    private int g;
    private int h;
    private int i[];
    private ByteVector j;
    private AnnotationWriter k;
    private AnnotationWriter l;
    private AnnotationWriter m[];
    private AnnotationWriter n[];
    private Attribute o;
    private ByteVector p;
    private int q;
    private int r;
    private int s;
    private ByteVector t;
    private int u;
    private ByteVector v;
    private int w;
    private ByteVector x;
    private int y;
    private ByteVector z;
    private Attribute A;
    private boolean B;
    private final boolean C;
    private int D;
    private int E;
    private Label F;
    private Label G;
    private static final int H[];

    static 
    {
        int ai[] = new int[202];
        String s1 = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
        for(int i1 = 0; i1 < ai.length; i1++)
            ai[i1] = s1.charAt(i1) - 69;

        H = ai;
    }
}
