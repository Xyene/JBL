// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


public interface AnnotationVisitor
{

    public abstract void visit(String s, Object obj);

    public abstract void visitEnum(String s, String s1, String s2);

    public abstract AnnotationVisitor visitAnnotation(String s, String s1);

    public abstract AnnotationVisitor visitArray(String s);

    public abstract void visitEnd();
}
