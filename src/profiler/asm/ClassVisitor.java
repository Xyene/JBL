// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package profiler.asm;


// Referenced classes of package profiler.asm:
//            AnnotationVisitor, Attribute, FieldVisitor, MethodVisitor

public interface ClassVisitor
{

    public abstract void visit(int i, int j, String s, String s1, String s2, String as[]);

    public abstract void visitSource(String s, String s1);

    public abstract void visitOuterClass(String s, String s1, String s2);

    public abstract AnnotationVisitor visitAnnotation(String s, boolean flag);

    public abstract void visitAttribute(Attribute attribute);

    public abstract void visitInnerClass(String s, String s1, String s2, int i);

    public abstract FieldVisitor visitField(int i, String s, String s1, String s2, Object obj);

    public abstract MethodVisitor visitMethod(int i, String s, String s1, String s2, String as[]);

    public abstract void visitEnd();
}
