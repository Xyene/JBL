// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


// Referenced classes of package profiler.asm:
//            AnnotationVisitor, Attribute

public interface FieldVisitor
{

    public abstract AnnotationVisitor visitAnnotation(String s, boolean flag);

    public abstract void visitAttribute(Attribute attribute);

    public abstract void visitEnd();
}
