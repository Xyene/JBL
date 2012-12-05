// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;

import java.lang.reflect.Method;

public class Type
{

    private Type(int i)
    {
        a = i;
        d = 1;
    }

    private Type(int i, char ac[], int j, int k)
    {
        a = i;
        b = ac;
        c = j;
        d = k;
    }

    public static Type getType(String s)
    {
        return a(s.toCharArray(), 0);
    }

    public static Type getType(Class class1)
    {
        if(class1.isPrimitive())
        {
            if(class1 == Integer.TYPE)
                return INT_TYPE;
            if(class1 == Void.TYPE)
                return VOID_TYPE;
            if(class1 == Boolean.TYPE)
                return BOOLEAN_TYPE;
            if(class1 == Byte.TYPE)
                return BYTE_TYPE;
            if(class1 == Character.TYPE)
                return CHAR_TYPE;
            if(class1 == Short.TYPE)
                return SHORT_TYPE;
            if(class1 == Double.TYPE)
                return DOUBLE_TYPE;
            if(class1 == Float.TYPE)
                return FLOAT_TYPE;
            else
                return LONG_TYPE;
        } else
        {
            return getType(getDescriptor(class1));
        }
    }

    public static Type[] getArgumentTypes(String s)
    {
        char ac[] = s.toCharArray();
        int i = 1;
        int j = 0;
        do
        {
            char c1 = ac[i++];
            if(c1 == ')')
                break;
            if(c1 == 'L')
            {
                while(ac[i++] != ';') ;
                j++;
            } else
            if(c1 != '[')
                j++;
        } while(true);
        Type atype[] = new Type[j];
        i = 1;
        for(int k = 0; ac[i] != ')'; k++)
        {
            atype[k] = a(ac, i);
            i += atype[k].d;
        }

        return atype;
    }

    public static Type[] getArgumentTypes(Method method)
    {
        Class aclass[] = method.getParameterTypes();
        Type atype[] = new Type[aclass.length];
        for(int i = aclass.length - 1; i >= 0; i--)
            atype[i] = getType(aclass[i]);

        return atype;
    }

    public static Type getReturnType(String s)
    {
        char ac[] = s.toCharArray();
        return a(ac, s.indexOf(')') + 1);
    }

    public static Type getReturnType(Method method)
    {
        return getType(method.getReturnType());
    }

    private static Type a(char ac[], int i)
    {
        int k;
        switch(ac[i])
        {
        case 86: // 'V'
            return VOID_TYPE;

        case 90: // 'Z'
            return BOOLEAN_TYPE;

        case 67: // 'C'
            return CHAR_TYPE;

        case 66: // 'B'
            return BYTE_TYPE;

        case 83: // 'S'
            return SHORT_TYPE;

        case 73: // 'I'
            return INT_TYPE;

        case 70: // 'F'
            return FLOAT_TYPE;

        case 74: // 'J'
            return LONG_TYPE;

        case 68: // 'D'
            return DOUBLE_TYPE;

        case 91: // '['
            int j;
            for(j = 1; ac[i + j] == '['; j++);
            if(ac[i + j] == 'L')
                for(j++; ac[i + j] != ';'; j++);
            return new Type(9, ac, i, j + 1);

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
        case 87: // 'W'
        case 88: // 'X'
        case 89: // 'Y'
        default:
            k = 1;
            break;
        }
        for(; ac[i + k] != ';'; k++);
        return new Type(10, ac, i, k + 1);
    }

    public int getSort()
    {
        return a;
    }

    public int getDimensions()
    {
        int i;
        for(i = 1; b[c + i] == '['; i++);
        return i;
    }

    public Type getElementType()
    {
        return a(b, c + getDimensions());
    }

    public String getClassName()
    {
        switch(a)
        {
        case 0: // '\0'
            return "void";

        case 1: // '\001'
            return "boolean";

        case 2: // '\002'
            return "char";

        case 3: // '\003'
            return "byte";

        case 4: // '\004'
            return "short";

        case 5: // '\005'
            return "int";

        case 6: // '\006'
            return "float";

        case 7: // '\007'
            return "long";

        case 8: // '\b'
            return "double";

        case 9: // '\t'
            StringBuffer stringbuffer = new StringBuffer(getElementType().getClassName());
            for(int i = getDimensions(); i > 0; i--)
                stringbuffer.append("[]");

            return stringbuffer.toString();
        }
        return (new String(b, c + 1, d - 2)).replace('/', '.');
    }

    public String getInternalName()
    {
        return new String(b, c + 1, d - 2);
    }

    public String getDescriptor()
    {
        StringBuffer stringbuffer = new StringBuffer();
        a(stringbuffer);
        return stringbuffer.toString();
    }

    public static String getMethodDescriptor(Type type, Type atype[])
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append('(');
        for(int i = 0; i < atype.length; i++)
            atype[i].a(stringbuffer);

        stringbuffer.append(')');
        type.a(stringbuffer);
        return stringbuffer.toString();
    }

    private void a(StringBuffer stringbuffer)
    {
        switch(a)
        {
        case 0: // '\0'
            stringbuffer.append('V');
            return;

        case 1: // '\001'
            stringbuffer.append('Z');
            return;

        case 2: // '\002'
            stringbuffer.append('C');
            return;

        case 3: // '\003'
            stringbuffer.append('B');
            return;

        case 4: // '\004'
            stringbuffer.append('S');
            return;

        case 5: // '\005'
            stringbuffer.append('I');
            return;

        case 6: // '\006'
            stringbuffer.append('F');
            return;

        case 7: // '\007'
            stringbuffer.append('J');
            return;

        case 8: // '\b'
            stringbuffer.append('D');
            return;
        }
        stringbuffer.append(b, c, d);
    }

    public static String getInternalName(Class class1)
    {
        return class1.getName().replace('.', '/');
    }

    public static String getDescriptor(Class class1)
    {
        StringBuffer stringbuffer = new StringBuffer();
        a(stringbuffer, class1);
        return stringbuffer.toString();
    }

    public static String getMethodDescriptor(Method method)
    {
        Class aclass[] = method.getParameterTypes();
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append('(');
        for(int i = 0; i < aclass.length; i++)
            a(stringbuffer, aclass[i]);

        stringbuffer.append(')');
        a(stringbuffer, method.getReturnType());
        return stringbuffer.toString();
    }

    private static void a(StringBuffer stringbuffer, Class class1)
    {
        Class class2 = class1;
        do
        {
            if(class2.isPrimitive())
            {
                char c1;
                if(class2 == Integer.TYPE)
                    c1 = 'I';
                else
                if(class2 == Void.TYPE)
                    c1 = 'V';
                else
                if(class2 == Boolean.TYPE)
                    c1 = 'Z';
                else
                if(class2 == Byte.TYPE)
                    c1 = 'B';
                else
                if(class2 == Character.TYPE)
                    c1 = 'C';
                else
                if(class2 == Short.TYPE)
                    c1 = 'S';
                else
                if(class2 == Double.TYPE)
                    c1 = 'D';
                else
                if(class2 == Float.TYPE)
                    c1 = 'F';
                else
                    c1 = 'J';
                stringbuffer.append(c1);
                return;
            }
            if(!class2.isArray())
                break;
            stringbuffer.append('[');
            class2 = class2.getComponentType();
        } while(true);
        stringbuffer.append('L');
        String s = class2.getName();
        int i = s.length();
        for(int j = 0; j < i; j++)
        {
            char c2 = s.charAt(j);
            stringbuffer.append(c2 != '.' ? c2 : '/');
        }

        stringbuffer.append(';');
    }

    public int getSize()
    {
        return a != 7 && a != 8 ? 1 : 2;
    }

    public int getOpcode(int i)
    {
        if(i == 46 || i == 79)
        {
            switch(a)
            {
            case 1: // '\001'
            case 3: // '\003'
                return i + 5;

            case 2: // '\002'
                return i + 6;

            case 4: // '\004'
                return i + 7;

            case 5: // '\005'
                return i;

            case 6: // '\006'
                return i + 2;

            case 7: // '\007'
                return i + 1;

            case 8: // '\b'
                return i + 3;
            }
            return i + 4;
        }
        switch(a)
        {
        case 0: // '\0'
            return i + 5;

        case 1: // '\001'
        case 2: // '\002'
        case 3: // '\003'
        case 4: // '\004'
        case 5: // '\005'
            return i;

        case 6: // '\006'
            return i + 2;

        case 7: // '\007'
            return i + 1;

        case 8: // '\b'
            return i + 3;
        }
        return i + 4;
    }

    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        if(obj == null || !(obj instanceof Type))
            return false;
        Type type = (Type)obj;
        if(a != type.a)
            return false;
        if(a == 10 || a == 9)
        {
            if(d != type.d)
                return false;
            int i = c;
            int j = type.c;
            for(int k = i + d; i < k;)
            {
                if(b[i] != type.b[j])
                    return false;
                i++;
                j++;
            }

        }
        return true;
    }

    public int hashCode()
    {
        int i = 13 * a;
        if(a == 10 || a == 9)
        {
            int j = c;
            for(int k = j + d; j < k; j++)
                i = 17 * (i + b[j]);

        }
        return i;
    }

    public String toString()
    {
        return getDescriptor();
    }

    public static final int VOID = 0;
    public static final int BOOLEAN = 1;
    public static final int CHAR = 2;
    public static final int BYTE = 3;
    public static final int SHORT = 4;
    public static final int INT = 5;
    public static final int FLOAT = 6;
    public static final int LONG = 7;
    public static final int DOUBLE = 8;
    public static final int ARRAY = 9;
    public static final int OBJECT = 10;
    public static final Type VOID_TYPE = new Type(0);
    public static final Type BOOLEAN_TYPE = new Type(1);
    public static final Type CHAR_TYPE = new Type(2);
    public static final Type BYTE_TYPE = new Type(3);
    public static final Type SHORT_TYPE = new Type(4);
    public static final Type INT_TYPE = new Type(5);
    public static final Type FLOAT_TYPE = new Type(6);
    public static final Type LONG_TYPE = new Type(7);
    public static final Type DOUBLE_TYPE = new Type(8);
    private final int a;
    private char b[];
    private int c;
    private int d;

}
