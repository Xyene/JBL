// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;

import java.io.IOException;
import java.io.InputStream;

// Referenced classes of package profiler.asm:
//            AnnotationVisitor, Attribute, ClassVisitor, ClassWriter, 
//            FieldVisitor, Label, MethodVisitor, Type

public class ClassReader
{

    public ClassReader(byte abyte0[])
    {
        this(abyte0, 0, abyte0.length);
    }

    public ClassReader(byte abyte0[], int i, int j)
    {
        b = abyte0;
        a = new int[readUnsignedShort(i + 8)];
        c = new String[a.length];
        int k = 0;
        int l = i + 10;
        for(int i1 = 1; i1 < a.length; i1++)
        {
            a[i1] = l + 1;
            byte byte0 = abyte0[l];
            int j1;
            switch(byte0)
            {
            case 3: // '\003'
            case 4: // '\004'
            case 9: // '\t'
            case 10: // '\n'
            case 11: // '\013'
            case 12: // '\f'
                j1 = 5;
                break;

            case 5: // '\005'
            case 6: // '\006'
                j1 = 9;
                i1++;
                break;

            case 1: // '\001'
                j1 = 3 + readUnsignedShort(l + 1);
                if(j1 > k)
                    k = j1;
                break;

            case 2: // '\002'
            case 7: // '\007'
            case 8: // '\b'
            default:
                j1 = 3;
                break;
            }
            l += j1;
        }

        d = k;
        e = l;
    }

    public ClassReader(InputStream inputstream)
        throws IOException
    {
        this(a(inputstream));
    }

    public ClassReader(String s)
        throws IOException
    {
        this(ClassLoader.getSystemResourceAsStream(s.replace('.', '/') + ".class"));
    }

    private static byte[] a(InputStream inputstream)
        throws IOException
    {
        if(inputstream == null)
            throw new IOException("Class not found");
        byte abyte0[] = new byte[inputstream.available()];
        int i = 0;
        do
        {
            do
            {
                int j = inputstream.read(abyte0, i, abyte0.length - i);
                if(j == -1)
                {
                    if(i < abyte0.length)
                    {
                        byte abyte1[] = new byte[i];
                        System.arraycopy(abyte0, 0, abyte1, 0, i);
                        abyte0 = abyte1;
                    }
                    return abyte0;
                }
                i += j;
            } while(i != abyte0.length);
            byte abyte2[] = new byte[abyte0.length + 1000];
            System.arraycopy(abyte0, 0, abyte2, 0, i);
            abyte0 = abyte2;
        } while(true);
    }

    public void accept(ClassVisitor classvisitor, boolean flag)
    {
        accept(classvisitor, new Attribute[0], flag);
    }

    public void accept(ClassVisitor classvisitor, Attribute aattribute[], boolean flag)
    {
        byte abyte0[] = b;
        char ac[] = new char[d];
        int k9 = 0;
        int j10 = 0;
        Attribute attribute8 = null;
        int i5 = e;
        int l8 = readUnsignedShort(i5);
        String s = readClass(i5 + 2, ac);
        int j5 = a[readUnsignedShort(i5 + 4)];
        String s15 = j5 != 0 ? readUTF8(j5, ac) : null;
        String as[] = new String[readUnsignedShort(i5 + 6)];
        int j6 = 0;
        i5 += 8;
        for(int i = 0; i < as.length; i++)
        {
            as[i] = readClass(i5, ac);
            i5 += 2;
        }

        j5 = i5;
        int j = readUnsignedShort(j5);
        j5 += 2;
        for(; j > 0; j--)
        {
            int k = readUnsignedShort(j5 + 6);
            j5 += 8;
            for(; k > 0; k--)
                j5 += 6 + readInt(j5 + 2);

        }

        j = readUnsignedShort(j5);
        j5 += 2;
        for(; j > 0; j--)
        {
            int l = readUnsignedShort(j5 + 6);
            j5 += 8;
            for(; l > 0; l--)
                j5 += 6 + readInt(j5 + 2);

        }

        String s12 = null;
        String s16 = null;
        String s17 = null;
        String s18 = null;
        String s19 = null;
        String s20 = null;
        j = readUnsignedShort(j5);
        j5 += 2;
        for(; j > 0; j--)
        {
            String s8 = readUTF8(j5, ac);
            if(s8.equals("SourceFile"))
                s16 = readUTF8(j5 + 6, ac);
            else
            if(s8.equals("Deprecated"))
                l8 |= 0x20000;
            else
            if(s8.equals("Synthetic"))
                l8 |= 0x1000;
            else
            if(s8.equals("Annotation"))
                l8 |= 0x2000;
            else
            if(s8.equals("Enum"))
                l8 |= 0x4000;
            else
            if(s8.equals("InnerClasses"))
                j6 = j5 + 6;
            else
            if(s8.equals("Signature"))
                s12 = readUTF8(j5 + 6, ac);
            else
            if(s8.equals("SourceDebugExtension"))
            {
                int i11 = readInt(j5 + 2);
                s17 = a(j5 + 6, i11, new char[i11]);
            } else
            if(s8.equals("EnclosingMethod"))
            {
                s18 = readClass(j5 + 6, ac);
                int j11 = readUnsignedShort(j5 + 8);
                if(j11 != 0)
                {
                    s19 = readUTF8(a[j11], ac);
                    s20 = readUTF8(a[j11] + 2, ac);
                }
            } else
            if(s8.equals("RuntimeVisibleAnnotations"))
                k9 = j5 + 6;
            else
            if(s8.equals("RuntimeInvisibleAnnotations"))
            {
                j10 = j5 + 6;
            } else
            {
                Attribute attribute = a(aattribute, s8, j5 + 6, readInt(j5 + 2), ac, -1, null);
                if(attribute != null)
                {
                    attribute.a = attribute8;
                    attribute8 = attribute;
                }
            }
            j5 += 6 + readInt(j5 + 2);
        }

        classvisitor.visit(readInt(4), l8, s, s12, s15, as);
        if(s16 != null || s17 != null)
            classvisitor.visitSource(s16, s17);
        if(s18 != null)
            classvisitor.visitOuterClass(s18, s19, s20);
        for(j = 1; j >= 0; j--)
        {
            int k5 = j != 0 ? k9 : j10;
            if(k5 == 0)
                continue;
            int i1 = readUnsignedShort(k5);
            k5 += 2;
            for(; i1 > 0; i1--)
            {
                String s3 = readUTF8(k5, ac);
                k5 += 2;
                k5 = a(k5, ac, classvisitor.visitAnnotation(s3, j != 0));
            }

        }

        Attribute attribute1;
        for(; attribute8 != null; attribute8 = attribute1)
        {
            attribute1 = attribute8.a;
            attribute8.a = null;
            classvisitor.visitAttribute(attribute8);
        }

        if(j6 != 0)
        {
            j = readUnsignedShort(j6);
            j6 += 2;
            for(; j > 0; j--)
            {
                classvisitor.visitInnerClass(readUnsignedShort(j6) != 0 ? readClass(j6, ac) : null, readUnsignedShort(j6 + 2) != 0 ? readClass(j6 + 2, ac) : null, readUnsignedShort(j6 + 4) != 0 ? readUTF8(j6 + 4, ac) : null, readUnsignedShort(j6 + 6));
                j6 += 8;
            }

        }
        j = readUnsignedShort(i5);
        i5 += 2;
        for(; j > 0; j--)
        {
            int i9 = readUnsignedShort(i5);
            String s1 = readUTF8(i5 + 2, ac);
            String s4 = readUTF8(i5 + 4, ac);
            int k11 = 0;
            String s13 = null;
            int l9 = 0;
            int k10 = 0;
            Attribute attribute9 = null;
            int j1 = readUnsignedShort(i5 + 6);
            i5 += 8;
            for(; j1 > 0; j1--)
            {
                String s9 = readUTF8(i5, ac);
                if(s9.equals("ConstantValue"))
                    k11 = readUnsignedShort(i5 + 6);
                else
                if(s9.equals("Synthetic"))
                    i9 |= 0x1000;
                else
                if(s9.equals("Deprecated"))
                    i9 |= 0x20000;
                else
                if(s9.equals("Enum"))
                    i9 |= 0x4000;
                else
                if(s9.equals("Signature"))
                    s13 = readUTF8(i5 + 6, ac);
                else
                if(s9.equals("RuntimeVisibleAnnotations"))
                    l9 = i5 + 6;
                else
                if(s9.equals("RuntimeInvisibleAnnotations"))
                {
                    k10 = i5 + 6;
                } else
                {
                    Attribute attribute2 = a(aattribute, s9, i5 + 6, readInt(i5 + 2), ac, -1, null);
                    if(attribute2 != null)
                    {
                        attribute2.a = attribute9;
                        attribute9 = attribute2;
                    }
                }
                i5 += 6 + readInt(i5 + 2);
            }

            Object obj = k11 != 0 ? readConst(k11, ac) : null;
            FieldVisitor fieldvisitor = classvisitor.visitField(i9, s1, s4, s13, obj);
            if(fieldvisitor == null)
                continue;
            for(int k1 = 1; k1 >= 0; k1--)
            {
                int l5 = k1 != 0 ? l9 : k10;
                if(l5 == 0)
                    continue;
                int j3 = readUnsignedShort(l5);
                l5 += 2;
                for(; j3 > 0; j3--)
                {
                    String s5 = readUTF8(l5, ac);
                    l5 += 2;
                    l5 = a(l5, ac, fieldvisitor.visitAnnotation(s5, k1 != 0));
                }

            }

            Attribute attribute3;
            for(; attribute9 != null; attribute9 = attribute3)
            {
                attribute3 = attribute9.a;
                attribute9.a = null;
                fieldvisitor.visitAttribute(attribute9);
            }

            fieldvisitor.visitEnd();
        }

        j = readUnsignedShort(i5);
        i5 += 2;
        for(; j > 0; j--)
        {
            int j9 = readUnsignedShort(i5);
            String s2 = readUTF8(i5 + 2, ac);
            String s6 = readUTF8(i5 + 4, ac);
            String s14 = null;
            int i10 = 0;
            int l10 = 0;
            int l11 = 0;
            int i12 = 0;
            int j12 = 0;
            Attribute attribute10 = null;
            int i6 = 0;
            int k6 = 0;
            int l1 = readUnsignedShort(i5 + 6);
            i5 += 8;
            for(; l1 > 0; l1--)
            {
                String s10 = readUTF8(i5, ac);
                i5 += 2;
                int k12 = readInt(i5);
                i5 += 4;
                if(s10.equals("Code"))
                    i6 = i5;
                else
                if(s10.equals("Exceptions"))
                    k6 = i5;
                else
                if(s10.equals("Synthetic"))
                    j9 |= 0x1000;
                else
                if(s10.equals("Varargs"))
                    j9 |= 0x80;
                else
                if(s10.equals("Bridge"))
                    j9 |= 0x40;
                else
                if(s10.equals("Deprecated"))
                    j9 |= 0x20000;
                else
                if(s10.equals("Signature"))
                    s14 = readUTF8(i5, ac);
                else
                if(s10.equals("AnnotationDefault"))
                    l11 = i5;
                else
                if(s10.equals("RuntimeVisibleAnnotations"))
                    i10 = i5;
                else
                if(s10.equals("RuntimeInvisibleAnnotations"))
                    l10 = i5;
                else
                if(s10.equals("RuntimeVisibleParameterAnnotations"))
                    i12 = i5;
                else
                if(s10.equals("RuntimeInvisibleParameterAnnotations"))
                {
                    j12 = i5;
                } else
                {
                    Attribute attribute4 = a(aattribute, s10, i5, k12, ac, -1, null);
                    if(attribute4 != null)
                    {
                        attribute4.a = attribute10;
                        attribute10 = attribute4;
                    }
                }
                i5 += k12;
            }

            String as1[];
            if(k6 == 0)
            {
                as1 = null;
            } else
            {
                as1 = new String[readUnsignedShort(k6)];
                k6 += 2;
                for(int i2 = 0; i2 < as1.length; i2++)
                {
                    as1[i2] = readClass(k6, ac);
                    k6 += 2;
                }

            }
            MethodVisitor methodvisitor = classvisitor.visitMethod(j9, s2, s6, s14, as1);
            if(methodvisitor != null)
            {
                if(l11 != 0)
                {
                    AnnotationVisitor annotationvisitor = methodvisitor.visitAnnotationDefault();
                    a(l11, ac, ((String) (null)), annotationvisitor);
                    annotationvisitor.visitEnd();
                }
                for(int j2 = 1; j2 >= 0; j2--)
                {
                    int l6 = j2 != 0 ? i10 : l10;
                    if(l6 == 0)
                        continue;
                    int k3 = readUnsignedShort(l6);
                    l6 += 2;
                    for(; k3 > 0; k3--)
                    {
                        String s7 = readUTF8(l6, ac);
                        l6 += 2;
                        l6 = a(l6, ac, methodvisitor.visitAnnotation(s7, j2 != 0));
                    }

                }

                if(i12 != 0)
                    a(i12, ac, true, methodvisitor);
                if(j12 != 0)
                    a(j12, ac, false, methodvisitor);
                Attribute attribute5;
                for(; attribute10 != null; attribute10 = attribute5)
                {
                    attribute5 = attribute10.a;
                    attribute10.a = null;
                    methodvisitor.visitAttribute(attribute10);
                }

            }
            if(methodvisitor != null && i6 != 0)
            {
                int l12 = readUnsignedShort(i6);
                int i13 = readUnsignedShort(i6 + 2);
                int j13 = readInt(i6 + 4);
                int k13 = i6 += 8;
                int l13 = i6 + j13;
                Label alabel[] = new Label[j13 + 1];
                do
                {
                    if(i6 >= l13)
                        break;
                    int l16 = abyte0[i6] & 0xff;
                    switch(ClassWriter.a[l16])
                    {
                    case 0: // '\0'
                    case 4: // '\004'
                        i6++;
                        break;

                    case 8: // '\b'
                        int i14 = (i6 - k13) + readShort(i6 + 1);
                        if(alabel[i14] == null)
                            alabel[i14] = new Label();
                        i6 += 3;
                        break;

                    case 9: // '\t'
                        int j14 = (i6 - k13) + readInt(i6 + 1);
                        if(alabel[j14] == null)
                            alabel[j14] = new Label();
                        i6 += 5;
                        break;

                    case 16: // '\020'
                        int i17 = abyte0[i6 + 1] & 0xff;
                        if(i17 == 132)
                            i6 += 6;
                        else
                            i6 += 4;
                        break;

                    case 13: // '\r'
                        int i7 = i6 - k13;
                        i6 = (i6 + 4) - (i7 & 3);
                        int k14 = i7 + readInt(i6);
                        i6 += 4;
                        if(alabel[k14] == null)
                            alabel[k14] = new Label();
                        int k2 = readInt(i6);
                        i6 += 4;
                        k2 = (readInt(i6) - k2) + 1;
                        i6 += 4;
                        while(k2 > 0) 
                        {
                            int l14 = i7 + readInt(i6);
                            i6 += 4;
                            if(alabel[l14] == null)
                                alabel[l14] = new Label();
                            k2--;
                        }
                        break;

                    case 14: // '\016'
                        int j7 = i6 - k13;
                        i6 = (i6 + 4) - (j7 & 3);
                        int i15 = j7 + readInt(i6);
                        i6 += 4;
                        if(alabel[i15] == null)
                            alabel[i15] = new Label();
                        int l2 = readInt(i6);
                        i6 += 4;
                        while(l2 > 0) 
                        {
                            i6 += 4;
                            int j15 = j7 + readInt(i6);
                            i6 += 4;
                            if(alabel[j15] == null)
                                alabel[j15] = new Label();
                            l2--;
                        }
                        break;

                    case 1: // '\001'
                    case 3: // '\003'
                    case 10: // '\n'
                        i6 += 2;
                        break;

                    case 2: // '\002'
                    case 5: // '\005'
                    case 6: // '\006'
                    case 11: // '\013'
                    case 12: // '\f'
                        i6 += 3;
                        break;

                    case 7: // '\007'
                        i6 += 5;
                        break;

                    case 15: // '\017'
                    default:
                        i6 += 4;
                        break;
                    }
                } while(true);
                int i3 = readUnsignedShort(i6);
                i6 += 2;
                for(; i3 > 0; i3--)
                {
                    int k15 = readUnsignedShort(i6);
                    if(alabel[k15] == null)
                        alabel[k15] = new Label();
                    k15 = readUnsignedShort(i6 + 2);
                    if(alabel[k15] == null)
                        alabel[k15] = new Label();
                    k15 = readUnsignedShort(i6 + 4);
                    if(alabel[k15] == null)
                        alabel[k15] = new Label();
                    i6 += 8;
                }

                int j17 = 0;
                int k17 = 0;
                Attribute attribute11 = null;
                i3 = readUnsignedShort(i6);
                i6 += 2;
                for(; i3 > 0; i3--)
                {
                    String s11 = readUTF8(i6, ac);
                    if(s11.equals("LocalVariableTable"))
                    {
                        if(!flag)
                        {
                            j17 = i6 + 6;
                            int l3 = readUnsignedShort(i6 + 6);
                            int k7 = i6 + 8;
                            for(; l3 > 0; l3--)
                            {
                                int l15 = readUnsignedShort(k7);
                                if(alabel[l15] == null)
                                    alabel[l15] = new Label();
                                l15 += readUnsignedShort(k7 + 2);
                                if(alabel[l15] == null)
                                    alabel[l15] = new Label();
                                k7 += 10;
                            }

                        }
                    } else
                    if(s11.equals("LocalVariableTypeTable"))
                        k17 = i6 + 6;
                    else
                    if(s11.equals("LineNumberTable"))
                    {
                        if(!flag)
                        {
                            int i4 = readUnsignedShort(i6 + 6);
                            int l7 = i6 + 8;
                            for(; i4 > 0; i4--)
                            {
                                int i16 = readUnsignedShort(l7);
                                if(alabel[i16] == null)
                                    alabel[i16] = new Label();
                                alabel[i16].k = readUnsignedShort(l7 + 2);
                                l7 += 4;
                            }

                        }
                    } else
                    {
                        for(int j4 = 0; j4 < aattribute.length; j4++)
                        {
                            if(!aattribute[j4].type.equals(s11))
                                continue;
                            Attribute attribute6 = aattribute[j4].read(this, i6 + 6, readInt(i6 + 2), ac, k13 - 8, alabel);
                            if(attribute6 != null)
                            {
                                attribute6.a = attribute11;
                                attribute11 = attribute6;
                            }
                        }

                    }
                    i6 += 6 + readInt(i6 + 2);
                }

                methodvisitor.visitCode();
                i6 = k13;
                do
                {
                    if(i6 >= l13)
                        break;
                    int i8 = i6 - k13;
                    Label label = alabel[i8];
                    if(label != null)
                    {
                        methodvisitor.visitLabel(label);
                        if(!flag && label.k > 0)
                            methodvisitor.visitLineNumber(label.k, label);
                    }
                    int l17 = abyte0[i6] & 0xff;
                    switch(ClassWriter.a[l17])
                    {
                    case 0: // '\0'
                        methodvisitor.visitInsn(l17);
                        i6++;
                        break;

                    case 4: // '\004'
                        if(l17 > 54)
                        {
                            l17 -= 59;
                            methodvisitor.visitVarInsn(54 + (l17 >> 2), l17 & 3);
                        } else
                        {
                            l17 -= 26;
                            methodvisitor.visitVarInsn(21 + (l17 >> 2), l17 & 3);
                        }
                        i6++;
                        break;

                    case 8: // '\b'
                        methodvisitor.visitJumpInsn(l17, alabel[i8 + readShort(i6 + 1)]);
                        i6 += 3;
                        break;

                    case 9: // '\t'
                        methodvisitor.visitJumpInsn(l17 - 33, alabel[i8 + readInt(i6 + 1)]);
                        i6 += 5;
                        break;

                    case 16: // '\020'
                        l17 = abyte0[i6 + 1] & 0xff;
                        if(l17 == 132)
                        {
                            methodvisitor.visitIincInsn(readUnsignedShort(i6 + 2), readShort(i6 + 4));
                            i6 += 6;
                        } else
                        {
                            methodvisitor.visitVarInsn(l17, readUnsignedShort(i6 + 2));
                            i6 += 4;
                        }
                        break;

                    case 13: // '\r'
                        i6 = (i6 + 4) - (i8 & 3);
                        int j16 = i8 + readInt(i6);
                        i6 += 4;
                        int i18 = readInt(i6);
                        i6 += 4;
                        int k18 = readInt(i6);
                        i6 += 4;
                        Label alabel1[] = new Label[(k18 - i18) + 1];
                        for(i3 = 0; i3 < alabel1.length; i3++)
                        {
                            alabel1[i3] = alabel[i8 + readInt(i6)];
                            i6 += 4;
                        }

                        methodvisitor.visitTableSwitchInsn(i18, k18, alabel[j16], alabel1);
                        break;

                    case 14: // '\016'
                        i6 = (i6 + 4) - (i8 & 3);
                        int k16 = i8 + readInt(i6);
                        i6 += 4;
                        i3 = readInt(i6);
                        i6 += 4;
                        int ai1[] = new int[i3];
                        Label alabel2[] = new Label[i3];
                        for(i3 = 0; i3 < ai1.length; i3++)
                        {
                            ai1[i3] = readInt(i6);
                            i6 += 4;
                            alabel2[i3] = alabel[i8 + readInt(i6)];
                            i6 += 4;
                        }

                        methodvisitor.visitLookupSwitchInsn(alabel[k16], ai1, alabel2);
                        break;

                    case 3: // '\003'
                        methodvisitor.visitVarInsn(l17, abyte0[i6 + 1] & 0xff);
                        i6 += 2;
                        break;

                    case 1: // '\001'
                        methodvisitor.visitIntInsn(l17, abyte0[i6 + 1]);
                        i6 += 2;
                        break;

                    case 2: // '\002'
                        methodvisitor.visitIntInsn(l17, readShort(i6 + 1));
                        i6 += 3;
                        break;

                    case 10: // '\n'
                        methodvisitor.visitLdcInsn(readConst(abyte0[i6 + 1] & 0xff, ac));
                        i6 += 2;
                        break;

                    case 11: // '\013'
                        methodvisitor.visitLdcInsn(readConst(readUnsignedShort(i6 + 1), ac));
                        i6 += 3;
                        break;

                    case 6: // '\006'
                    case 7: // '\007'
                        int l19 = a[readUnsignedShort(i6 + 1)];
                        String s22 = readClass(l19, ac);
                        l19 = a[readUnsignedShort(l19 + 2)];
                        String s23 = readUTF8(l19, ac);
                        String s24 = readUTF8(l19 + 2, ac);
                        if(l17 < 182)
                            methodvisitor.visitFieldInsn(l17, s22, s23, s24);
                        else
                            methodvisitor.visitMethodInsn(l17, s22, s23, s24);
                        if(l17 == 185)
                            i6 += 5;
                        else
                            i6 += 3;
                        break;

                    case 5: // '\005'
                        methodvisitor.visitTypeInsn(l17, readClass(i6 + 1, ac));
                        i6 += 3;
                        break;

                    case 12: // '\f'
                        methodvisitor.visitIincInsn(abyte0[i6 + 1] & 0xff, abyte0[i6 + 2]);
                        i6 += 3;
                        break;

                    case 15: // '\017'
                    default:
                        methodvisitor.visitMultiANewArrayInsn(readClass(i6 + 1, ac), abyte0[i6 + 3] & 0xff);
                        i6 += 4;
                        break;
                    }
                } while(true);
                Label label1 = alabel[l13 - k13];
                if(label1 != null)
                    methodvisitor.visitLabel(label1);
                i3 = readUnsignedShort(i6);
                i6 += 2;
                for(; i3 > 0; i3--)
                {
                    Label label2 = alabel[readUnsignedShort(i6)];
                    Label label3 = alabel[readUnsignedShort(i6 + 2)];
                    Label label4 = alabel[readUnsignedShort(i6 + 4)];
                    int i19 = readUnsignedShort(i6 + 6);
                    if(i19 == 0)
                        methodvisitor.visitTryCatchBlock(label2, label3, label4, null);
                    else
                        methodvisitor.visitTryCatchBlock(label2, label3, label4, readUTF8(a[i19], ac));
                    i6 += 8;
                }

                if(!flag && j17 != 0)
                {
                    int ai[] = null;
                    if(k17 != 0)
                    {
                        int j8 = k17;
                        int k4 = readUnsignedShort(j8) * 3;
                        j8 += 2;
                        ai = new int[k4];
                        while(k4 > 0) 
                        {
                            ai[--k4] = j8 + 6;
                            ai[--k4] = readUnsignedShort(j8 + 8);
                            ai[--k4] = readUnsignedShort(j8);
                            j8 += 10;
                        }
                    }
                    int k8 = j17;
                    int l4 = readUnsignedShort(k8);
                    k8 += 2;
                    for(; l4 > 0; l4--)
                    {
                        int j18 = readUnsignedShort(k8);
                        int l18 = readUnsignedShort(k8 + 2);
                        int j19 = readUnsignedShort(k8 + 8);
                        String s21 = null;
                        if(ai != null)
                        {
                            int k19 = 0;
                            do
                            {
                                if(k19 >= ai.length)
                                    break;
                                if(ai[k19] == j18 && ai[k19 + 1] == j19)
                                {
                                    s21 = readUTF8(ai[k19 + 2], ac);
                                    break;
                                }
                                k19 += 3;
                            } while(true);
                        }
                        methodvisitor.visitLocalVariable(readUTF8(k8 + 4, ac), readUTF8(k8 + 6, ac), s21, alabel[j18], alabel[j18 + l18], j19);
                        k8 += 10;
                    }

                }
                Attribute attribute7;
                for(; attribute11 != null; attribute11 = attribute7)
                {
                    attribute7 = attribute11.a;
                    attribute11.a = null;
                    methodvisitor.visitAttribute(attribute11);
                }

                methodvisitor.visitMaxs(l12, i13);
            }
            if(methodvisitor != null)
                methodvisitor.visitEnd();
        }

        classvisitor.visitEnd();
    }

    private void a(int i, char ac[], boolean flag, MethodVisitor methodvisitor)
    {
        int j = b[i++] & 0xff;
        for(int k = 0; k < j; k++)
        {
            int l = readUnsignedShort(i);
            i += 2;
            for(; l > 0; l--)
            {
                String s = readUTF8(i, ac);
                i += 2;
                AnnotationVisitor annotationvisitor = methodvisitor.visitParameterAnnotation(k, s, flag);
                i = a(i, ac, annotationvisitor);
            }

        }

    }

    private int a(int i, char ac[], AnnotationVisitor annotationvisitor)
    {
        int j = readUnsignedShort(i);
        i += 2;
        for(; j > 0; j--)
        {
            String s = readUTF8(i, ac);
            i += 2;
            i = a(i, ac, s, annotationvisitor);
        }

        annotationvisitor.visitEnd();
        return i;
    }

    private int a(int i, char ac[], String s, AnnotationVisitor annotationvisitor)
    {
label0:
        switch(readByte(i++))
        {
        case 65: // 'A'
        case 69: // 'E'
        case 71: // 'G'
        case 72: // 'H'
        case 75: // 'K'
        case 76: // 'L'
        case 77: // 'M'
        case 78: // 'N'
        case 79: // 'O'
        case 80: // 'P'
        case 81: // 'Q'
        case 82: // 'R'
        case 84: // 'T'
        case 85: // 'U'
        case 86: // 'V'
        case 87: // 'W'
        case 88: // 'X'
        case 89: // 'Y'
        case 92: // '\\'
        case 93: // ']'
        case 94: // '^'
        case 95: // '_'
        case 96: // '`'
        case 97: // 'a'
        case 98: // 'b'
        case 100: // 'd'
        case 102: // 'f'
        case 103: // 'g'
        case 104: // 'h'
        case 105: // 'i'
        case 106: // 'j'
        case 107: // 'k'
        case 108: // 'l'
        case 109: // 'm'
        case 110: // 'n'
        case 111: // 'o'
        case 112: // 'p'
        case 113: // 'q'
        case 114: // 'r'
        default:
            break;

        case 68: // 'D'
        case 70: // 'F'
        case 73: // 'I'
        case 74: // 'J'
            annotationvisitor.visit(s, readConst(readUnsignedShort(i), ac));
            i += 2;
            break;

        case 66: // 'B'
            annotationvisitor.visit(s, new Byte((byte)readInt(a[readUnsignedShort(i)])));
            i += 2;
            break;

        case 90: // 'Z'
            annotationvisitor.visit(s, readInt(a[readUnsignedShort(i)]) != 0 ? ((Object) (Boolean.TRUE)) : ((Object) (Boolean.FALSE)));
            i += 2;
            break;

        case 83: // 'S'
            annotationvisitor.visit(s, new Short((short)readInt(a[readUnsignedShort(i)])));
            i += 2;
            break;

        case 67: // 'C'
            annotationvisitor.visit(s, new Character((char)readInt(a[readUnsignedShort(i)])));
            i += 2;
            break;

        case 115: // 's'
            annotationvisitor.visit(s, readUTF8(i, ac));
            i += 2;
            break;

        case 101: // 'e'
            annotationvisitor.visitEnum(s, readUTF8(i, ac), readUTF8(i + 2, ac));
            i += 4;
            break;

        case 99: // 'c'
            annotationvisitor.visit(s, Type.getType(readUTF8(i, ac)));
            i += 2;
            break;

        case 64: // '@'
            String s1 = readUTF8(i, ac);
            i += 2;
            i = a(i, ac, annotationvisitor.visitAnnotation(s, s1));
            break;

        case 91: // '['
            int k2 = readUnsignedShort(i);
            i += 2;
            switch(readByte(i++))
            {
            case 66: // 'B'
                byte abyte0[] = new byte[k2];
                for(int j = 0; j < k2; j++)
                {
                    abyte0[j] = (byte)readInt(a[readUnsignedShort(i)]);
                    i += 3;
                }

                annotationvisitor.visit(s, abyte0);
                i--;
                break label0;

            case 90: // 'Z'
                boolean aflag[] = new boolean[k2];
                for(int k = 0; k < k2; k++)
                {
                    aflag[k] = readInt(a[readUnsignedShort(i)]) != 0;
                    i += 3;
                }

                annotationvisitor.visit(s, aflag);
                i--;
                break label0;

            case 83: // 'S'
                short aword0[] = new short[k2];
                for(int l = 0; l < k2; l++)
                {
                    aword0[l] = (short)readInt(a[readUnsignedShort(i)]);
                    i += 3;
                }

                annotationvisitor.visit(s, aword0);
                i--;
                break label0;

            case 67: // 'C'
                char ac1[] = new char[k2];
                for(int i1 = 0; i1 < k2; i1++)
                {
                    ac1[i1] = (char)readInt(a[readUnsignedShort(i)]);
                    i += 3;
                }

                annotationvisitor.visit(s, ac1);
                i--;
                break label0;

            case 73: // 'I'
                int ai[] = new int[k2];
                for(int j1 = 0; j1 < k2; j1++)
                {
                    ai[j1] = readInt(a[readUnsignedShort(i)]);
                    i += 3;
                }

                annotationvisitor.visit(s, ai);
                i--;
                break label0;

            case 74: // 'J'
                long al[] = new long[k2];
                for(int k1 = 0; k1 < k2; k1++)
                {
                    al[k1] = readLong(a[readUnsignedShort(i)]);
                    i += 3;
                }

                annotationvisitor.visit(s, al);
                i--;
                break label0;

            case 70: // 'F'
                float af[] = new float[k2];
                for(int l1 = 0; l1 < k2; l1++)
                {
                    af[l1] = Float.intBitsToFloat(readInt(a[readUnsignedShort(i)]));
                    i += 3;
                }

                annotationvisitor.visit(s, af);
                i--;
                break label0;

            case 68: // 'D'
                double ad[] = new double[k2];
                for(int i2 = 0; i2 < k2; i2++)
                {
                    ad[i2] = Double.longBitsToDouble(readLong(a[readUnsignedShort(i)]));
                    i += 3;
                }

                annotationvisitor.visit(s, ad);
                i--;
                break label0;

            case 69: // 'E'
            case 71: // 'G'
            case 72: // 'H'
            case 75: // 'K'
            case 76: // 'L'
            case 77: // 'M'
            case 78: // 'N'
            case 79: // 'O'
            case 80: // 'P'
            case 81: // 'Q'
            case 82: // 'R'
            case 84: // 'T'
            case 85: // 'U'
            case 86: // 'V'
            case 87: // 'W'
            case 88: // 'X'
            case 89: // 'Y'
            default:
                i--;
                AnnotationVisitor annotationvisitor1 = annotationvisitor.visitArray(s);
                for(int j2 = k2; j2 > 0; j2--)
                    i = a(i, ac, ((String) (null)), annotationvisitor1);

                annotationvisitor1.visitEnd();
                break;
            }
            break;
        }
        return i;
    }

    private Attribute a(Attribute aattribute[], String s, int i, int j, char ac[], int k, Label alabel[])
    {
        for(int l = 0; l < aattribute.length; l++)
            if(aattribute[l].type.equals(s))
                return aattribute[l].read(this, i, j, ac, k, alabel);

        return new Attribute(s);
    }

    public int getItem(int i)
    {
        return a[i];
    }

    public int readByte(int i)
    {
        return b[i] & 0xff;
    }

    public int readUnsignedShort(int i)
    {
        byte abyte0[] = b;
        return (abyte0[i] & 0xff) << 8 | abyte0[i + 1] & 0xff;
    }

    public short readShort(int i)
    {
        byte abyte0[] = b;
        return (short)((abyte0[i] & 0xff) << 8 | abyte0[i + 1] & 0xff);
    }

    public int readInt(int i)
    {
        byte abyte0[] = b;
        return (abyte0[i] & 0xff) << 24 | (abyte0[i + 1] & 0xff) << 16 | (abyte0[i + 2] & 0xff) << 8 | abyte0[i + 3] & 0xff;
    }

    public long readLong(int i)
    {
        long l = readInt(i);
        long l1 = (long)readInt(i + 4) & 0xffffffffL;
        return l << 32 | l1;
    }

    public String readUTF8(int i, char ac[])
    {
        int j = readUnsignedShort(i);
        String s = c[j];
        if(s != null)
        {
            return s;
        } else
        {
            i = a[j];
            return c[j] = a(i + 2, readUnsignedShort(i), ac);
        }
    }

    private String a(int i, int j, char ac[])
    {
        int k = i + j;
        byte abyte0[] = b;
        int l = 0;
        do
        {
            if(i >= k)
                break;
            int i1 = abyte0[i++] & 0xff;
            switch(i1 >> 4)
            {
            case 0: // '\0'
            case 1: // '\001'
            case 2: // '\002'
            case 3: // '\003'
            case 4: // '\004'
            case 5: // '\005'
            case 6: // '\006'
            case 7: // '\007'
                ac[l++] = (char)i1;
                break;

            case 12: // '\f'
            case 13: // '\r'
                byte byte0 = abyte0[i++];
                ac[l++] = (char)((i1 & 0x1f) << 6 | byte0 & 0x3f);
                break;

            case 8: // '\b'
            case 9: // '\t'
            case 10: // '\n'
            case 11: // '\013'
            default:
                byte byte1 = abyte0[i++];
                byte byte2 = abyte0[i++];
                ac[l++] = (char)((i1 & 0xf) << 12 | (byte1 & 0x3f) << 6 | byte2 & 0x3f);
                break;
            }
        } while(true);
        return new String(ac, 0, l);
    }

    public String readClass(int i, char ac[])
    {
        return readUTF8(a[readUnsignedShort(i)], ac);
    }

    public Object readConst(int i, char ac[])
    {
        int j = a[i];
        switch(b[j - 1])
        {
        case 3: // '\003'
            return new Integer(readInt(j));

        case 4: // '\004'
            return new Float(Float.intBitsToFloat(readInt(j)));

        case 5: // '\005'
            return new Long(readLong(j));

        case 6: // '\006'
            return new Double(Double.longBitsToDouble(readLong(j)));

        case 7: // '\007'
            String s = readUTF8(j, ac);
            return Type.getType(s.charAt(0) != '[' ? "L" + s + ";" : s);
        }
        return readUTF8(j, ac);
    }

    public final byte b[];
    private int a[];
    private String c[];
    private int d;
    private int e;
}
